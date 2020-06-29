package com.microservice.loja.controller;

import com.microservice.loja.controller.dto.CompraDto;
import com.microservice.loja.model.Compra;
import com.microservice.loja.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Luis Gustavo Ullmann on 26/06/2020
 */
@RestController
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private CompraService compraService;


    @GetMapping("/{id}") //Retorna os dados de uma compra do cliente
    public Compra getCompraById(@PathVariable("id") Long id) {
        return compraService.getCompraByID(id);
    }

    @PostMapping //Loja postando a requisição para o fornecedor
    public Compra realizaCompra(@RequestBody CompraDto compra) {
        //Feign não repassa o token para os outros microserviços, então temos que implementar
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return compraService.realizaCompra(compra);
    }
}
