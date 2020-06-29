package com.microservice.loja.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Luis Gustavo Ullmann on 26/06/2020
 */
@Getter
@Setter
public class ItemDaCompraDto {
    //POST na Loja pelo cliente - ID = produto do fornecedor, Quantidade = quantos o cliente quer
    //esse POST na loja é passado para o fornecedor
    private long id;
    private int quantidade;
}
