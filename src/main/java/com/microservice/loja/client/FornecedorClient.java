package com.microservice.loja.client;

import com.microservice.loja.controller.dto.fornecedor.InfoFornecedorDto;
import com.microservice.loja.controller.dto.fornecedor.InfoPedidoDto;
import com.microservice.loja.controller.dto.ItemDaCompraDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Created by Luis Gustavo Ullmann on 27/06/2020
 */
@FeignClient("fornecedor") //Id da aplicação que quer acessar
public interface FornecedorClient {//pom spring-cloud-starter-feing e no main @EnableFeignClients
    //implementar os metodos do Controller que quer acessar

    //Assinatura do método Get, porém alteramos o retorno para o DTO
    @GetMapping("/info/{estado}")
    //Passar na anotação toda a URL RequestMapping+GetMapping
    InfoFornecedorDto getInfoPorEstado(@PathVariable("estado") String estado);

    @PostMapping("/pedido")
        //fazendo um POST de um pedido no fornecedor
    InfoPedidoDto realizaPedido(List<ItemDaCompraDto> itens);
}
