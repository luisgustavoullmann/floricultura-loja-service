package com.microservice.loja.service;

import com.microservice.loja.client.FornecedorClient;
import com.microservice.loja.controller.dto.CompraDto;
import com.microservice.loja.controller.dto.InfoFornecedorDto;
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

    //Feign - Não esqueça de habilitar o @EnableFeignClients no main
    @Autowired
    private FornecedorClient fornecedorClient;


    //RestTemplate
    //@Autowired
    //private RestTemplate restTemplate;

    //Não é necessário implementar - Discovery Client para ver quais instancias estão disponíveis no Eureka
    //@Autowired //DiscoveryClient do SpringFramework.Cloud
    //private DiscoveryClient eurekaClient;

    public void realizaCompra(CompraDto compra) {

        //Implementado com Feign
        InfoFornecedorDto info = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado()); //passando o Estado do Cliente da Loja
        System.out.println(info.getEndereco()); //recebendo o Estado do Fornecedor

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
