package com.microservice.loja.controller.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by Luis Gustavo Ullmann on 26/06/2020
 */
@Data
public class CompraDto {

    private List<ItemDaCompraDto> itens;

    private EnderecoDto endereco;
}
