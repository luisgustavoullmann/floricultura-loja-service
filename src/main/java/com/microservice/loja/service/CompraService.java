package com.microservice.loja.service;

import com.microservice.loja.controller.dto.CompraDto;
import com.microservice.loja.controller.dto.InfoFornecedorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Luis Gustavo Ullmann on 26/06/2020
 */
@Service
public class CompraService {

    @Autowired
    private RestTemplate restTemplate;

    public void realizaCompra(CompraDto compra) {


        RestTemplate client = new RestTemplate();
        //null depois do GET, pois não envia informação nenhuma
        //loja POST pedido, para fornecedor
        ResponseEntity<InfoFornecedorDto> exchage =
                restTemplate.exchange("http://fornecedor/info/" + compra.getEndereco().getEstado(),
                HttpMethod.GET, null, InfoFornecedorDto.class);

        System.out.println(exchage.getBody().getEndereco());
    }
}
