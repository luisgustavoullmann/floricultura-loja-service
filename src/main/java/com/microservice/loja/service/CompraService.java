package com.microservice.loja.service;

import com.microservice.loja.client.FornecedorClient;
import com.microservice.loja.client.TransportadorClient;
import com.microservice.loja.controller.dto.CompraDto;
import com.microservice.loja.controller.dto.fornecedor.InfoFornecedorDto;
import com.microservice.loja.controller.dto.fornecedor.InfoPedidoDto;
import com.microservice.loja.controller.dto.transporte.InfoEntregaDto;
import com.microservice.loja.controller.dto.transporte.VoucherDto;
import com.microservice.loja.model.Compra;
import com.microservice.loja.model.CompraState;
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
            threadPoolKey = "realizaCompraThreadPool")
    //treadsPoolKey = BulkHead Patter, cria um poll de treads separadas por requisição
    public Compra realizaCompra(CompraDto compra) {//Metodo retorna os dados de Compra de um Cliente/Loja

        //Tratamento de erro
        //Em caso de erro na transação com fornecedor e transporte o cliente precisa ter duas opções:
        //1 - Cancelar
        //2 - Tentar realizar a mesma compra novamente
        //Por isso, primeira coisa que vamos fazer é salvar a compra
        Compra compraSalva = new Compra(); // pedido é uma nova compra
        compraSalva.setState(CompraState.RECEBIDO); //gerando um estado para compra
        compraRepository.save(compraSalva); //Salvando no db
        compra.setCompraId(compraSalva.getId());//Alterando o compraId no DTO para caso dê fallback

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
        compraSalva.setState(CompraState.PEDIDO_REALIZADO); //gerando um estado para compra
        compraRepository.save(compraSalva); //Salvando no db

        //Transporte
        InfoEntregaDto entregaDto = new InfoEntregaDto(); //criando um Dto que vai receber os dados da entrega
        //Passando as informações do pedido para o transporte
        entregaDto.setPedidoId(pedido.getId()); //vem do fornecedor
        entregaDto.setDataParaEntrega(LocalDate.now().plusDays(pedido.getTempoDePreparo())); //tempo de preparo pelo fornecedor, a partir da data de hoje
        entregaDto.setEnderecoOrigem(info.getEndereco()); //endereco do fornecedor
        entregaDto.setEnderecoDestino(compra.getEndereco().toString()); //endereço do cliente final que realiza a compra

        //if(1==1)throw new RuntimeException(); //Lançando um exception para gerar uma falha

        //Feign do Transporte
        VoucherDto voucher = transportadorClient.reservaEntrega(entregaDto);
        compraSalva.setState(CompraState.ENTREGA_RESERVADA_REALIZADA); //gerando um estado para compra
        compraRepository.save(compraSalva); //Salvando no db

        //Após o POST do pedido no fornecedor, essas dados retornam para loja - Postman: http://localhost:8081/compra
        //Retorno do POST é pelo Feign FornecedorCLient (acima)

        compraSalva.setPedidoId(pedido.getId()); //Pegando o InfoPedidoDto e passando para Compra - Dto vem do fornecedor
        compraSalva.setTempoDePreparo(pedido.getTempoDePreparo()); //Pegando o InfoPedidoDto e passando para Compra
        compraSalva.setEnderecoDestino(compra.getEndereco().toString()); //Endereco vem do Post do pedido do cliente na Loja

        //Feign Transporte com dados do voucher para a compra
        compraSalva.setDataParaEntrega(voucher.getPrevisaoParaEntrega());
        compraSalva.setVoucher(voucher.getNumero());
        compraSalva.setState(CompraState.TRANSPORTE_GERADO); //gerando um estado para compra
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
        if (compra.getCompraId() != null) {
            //Já tendo uma compra geranda(ID dela), antes de dar fallback em algum ponto da transação
            // retorna o estado da compra que estava antes de dar fallback
            return compraRepository.findById(compra.getCompraId()).get();
        }
        //Crie uma resposta padrão para o Fallback
        Compra compraFallback = new Compra();
        compraFallback.setEnderecoDestino(compra.getEndereco().getEstado());
        return compraFallback;
    }

    // Esse método pode ser chamado em caso de fallback e o cliente queira reprocessar a compra

//    public Compra reprocessaCompra(Long id){
//        return null;
//    }

    // Esse método pode ser chamado em caso de fallback e o cliente queira cancelar a compra
    // com base no id e estado atual do pedido
//    public Compra cancelaCompra(Long id){
//
//    }

}
