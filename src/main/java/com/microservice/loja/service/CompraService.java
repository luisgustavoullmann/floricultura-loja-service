package com.microservice.loja.service;

import com.microservice.loja.client.FornecedorClient;
import com.microservice.loja.controller.dto.CompraDto;
import com.microservice.loja.controller.dto.InfoFornecedorDto;
import com.microservice.loja.controller.dto.InfoPedidoDto;
import com.microservice.loja.model.Compra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Luis Gustavo Ullmann on 26/06/2020
 */
@Service
public class CompraService {

    //RestTemplate
    //@Autowired
    //private RestTemplate restTemplate;

    //Não é necessário implementar - Discovery Client para ver quais instancias estão disponíveis no Eureka
    //@Autowired //DiscoveryClient do SpringFramework.Cloud
    //private DiscoveryClient eurekaClient;


    //Feign - Não esqueça de habilitar o @EnableFeignClients no main
    @Autowired
    private FornecedorClient fornecedorClient;

    public Compra realizaCompra(CompraDto compra) {//Metodo retorna os dados de Compra de um Cliente/Loja

        //Implementado com Feign
        InfoFornecedorDto info = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado()); //Envio endereco Loja e retorna o Estado do Fornecedor

        //Info do pedido do usuário/loja - Realiza um POST no fornecedor com os dados do pedido e pegando (InfoPedidoDto)
        InfoPedidoDto pedido = fornecedorClient.realizaPedido(compra.getItens());//Post, quais itens Loja quer, retorna idPedido e tempo de preparo

        System.out.println(info.getEndereco()); //recebendo o Estado do Fornecedor

        //Elaborando dados de uma compra - para quando compra for feita/post na loja, gera um pedido/post no fornecedor
        Compra compraSalva = new Compra(); // pedido é uma nova compra
        compraSalva.setPedidoId(pedido.getId()); //Pegando o InfoPedidoDto e passando para Compra - Dto vem do fornecedor
        compraSalva.setTempoDePreparo(pedido.getTempoDePreparo()); //Pegando o InfoPedidoDto e passando para Compra
        compraSalva.setEnderecoDestino(compra.getEndereco().toString()); //Endereco vem do Post do pedido do cliente na Loja
        return compraSalva;




        //Implementado com RestTemplate
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
}
