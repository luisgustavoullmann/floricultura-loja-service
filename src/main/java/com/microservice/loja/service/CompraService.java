package com.microservice.loja.service;

import com.microservice.loja.client.FornecedorClient;
import com.microservice.loja.client.TransportadorClient;
import com.microservice.loja.controller.dto.CompraDto;
import com.microservice.loja.controller.dto.fornecedor.InfoFornecedorDto;
import com.microservice.loja.controller.dto.fornecedor.InfoPedidoDto;
import com.microservice.loja.controller.dto.transporte.InfoEntregaDto;
import com.microservice.loja.controller.dto.transporte.VoucherDto;
import com.microservice.loja.model.Compra;
import com.microservice.loja.repositories.CompraRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Created by Luis Gustavo Ullmann on 26/06/2020
 */
@Slf4j
@Service
public class CompraService {

    //RestTemplate
    //@Autowired
    //private RestTemplate restTemplate;

    //Não é necessário implementar - Discovery Client para ver quais instancias estão disponíveis no Eureka
    //@Autowired //DiscoveryClient do SpringFramework.Cloud
    //private DiscoveryClient eurekaClient;

    //Trabalhando com logs - pois quando temos muitos microservices, fica mais fácil de entender o andamento dos logs
//    private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);

    //Feign - Não esqueça de habilitar o @EnableFeignClients no main
    @Autowired
    private FornecedorClient fornecedorClient;

    @Autowired
    private TransportadorClient transportadorClient;

    @Autowired
    private CompraRepository compraRepository;

    @HystrixCommand(fallbackMethod = "realizaCompraFallBack",
            threadPoolKey = "realizaCompraThreadPool") //treadsPoolKey = BulkHead Patter, cria um poll de treads separadas por requisição
    public Compra realizaCompra(CompraDto compra) {//Metodo retorna os dados de Compra de um Cliente/Loja

        final String estado = compra.getEndereco().getEstado();

        log.info("Buscando informações do fornecedor de {}, para o cliente da loja de {}", estado, estado);
        //Implementado com Feign
        //Envio endereco Loja e retorna o Estado do Fornecedor
        InfoFornecedorDto info = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado());

        log.info("realizando um pedido");
        //Info do pedido do usuário/loja - Realiza um POST no fornecedor com os dados do pedido e pegando (InfoPedidoDto)
        //Feign Fornecedor
        //POST, quais itens Loja quer, retorna idPedido e tempo de preparo
        InfoPedidoDto pedido = fornecedorClient.realizaPedido(compra.getItens());

        //Transporte
        InfoEntregaDto entregaDto = new InfoEntregaDto(); //criando um Dto que vai receber os dados da entrega
        //Passando as informações do pedido para o transporte
        entregaDto.setPedidoId(pedido.getId()); //vem do fornecedor
        entregaDto.setDataParaEntrega(LocalDate.now().plusDays(pedido.getTempoDePreparo())); //tempo de preparo pelo fornecedor, a partir da data de hoje
        entregaDto.setEnderecoOrigem(info.getEndereco()); //endereco do fornecedor
        entregaDto.setEnderecoDestino(compra.getEndereco().toString()); //endereço do cliente final que realiza a compra

        //Feign do Transporte
        VoucherDto voucher = transportadorClient.reservaEntrega(entregaDto);

        //Após o POST do pedido no fornecedor, essas dados retornam para loja - Postman: http://localhost:8081/compra
        //Retorno do POST é pelo Feign FornecedorCLient (acima)
        Compra compraSalva = new Compra(); // pedido é uma nova compra
        compraSalva.setPedidoId(pedido.getId()); //Pegando o InfoPedidoDto e passando para Compra - Dto vem do fornecedor
        compraSalva.setTempoDePreparo(pedido.getTempoDePreparo()); //Pegando o InfoPedidoDto e passando para Compra
        compraSalva.setEnderecoDestino(compra.getEndereco().toString()); //Endereco vem do Post do pedido do cliente na Loja

        //Feign Transporte com dados do voucher para a compra
        compraSalva.setDataParaEntrega(voucher.getPrevisaoParaEntrega());
        compraSalva.setVoucher(voucher.getNumero());

        //Salvando no db
        compraRepository.save(compraSalva);

        return compraSalva;


        //Implementado com RestTemplate - funciona igual ao Feign
        //null depois do GET, pois não envia informação nenhuma
        //loja POST pedido, para fornecedor do endereco do cliente
//        ResponseEntity<InfoFornecedorDto> exchage =
//                restTemplate.exchange("http://fornecedor/info/"+compra.getEndereco().getEstado(), //endereco do cliente
//                HttpMethod.GET, null, InfoFornecedorDto.class);
//
//        //imprime o localhost de todos os fornecedores disponíveis
//        eurekaClient.getInstances("fornecedor").stream()
//                .forEach(fornecedor -> {
//                    System.out.println("localhost:"+fornecedor.getPort());
//                });

        //System.out.println(exchage.getBody().getEndereco());
    }

    @HystrixCommand(threadPoolKey = "getCompraByIDThreadPool")
    public Compra getCompraByID(Long id) {
        return compraRepository.findById(id).orElse(new Compra()); //orElse - se não tiver o id determinado, retorna uma compra vazia
    }


    //Fallback - dá fallback toda vez que há um timeout no método pelo Hystrix - Apache JMeter, programa para testar o Hystrix se quiser
    public Compra realizaCompraFallBack(CompraDto compra) {
        //Crie uma resposta padrão para o Fallback
        Compra compraFallback = new Compra();
        compraFallback.setEnderecoDestino(compra.getEndereco().getEstado());
        return compraFallback;
    }


}
