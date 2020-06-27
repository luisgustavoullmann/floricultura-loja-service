package com.microservice.loja.controller.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by Luis Gustavo Ullmann on 26/06/2020
 */
@Getter
@Setter
public class CompraDto {

    private List<ItemDaCompraDto> itens;

    private EnderecoDto endereco;
}
