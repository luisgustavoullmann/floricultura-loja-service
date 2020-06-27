package com.microservice.loja.controller;

import com.microservice.loja.controller.dto.CompraDto;
import com.microservice.loja.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Luis Gustavo Ullmann on 26/06/2020
 */
@RestController
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @PostMapping
    public void realizaCompra(@RequestBody CompraDto compra){//Loja postando a requisição para o fornecedor
        compraService.realizaCompra(compra);
    }
}
