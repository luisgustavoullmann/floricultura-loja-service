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
    // Será um POST na loja, cliente passa seu endereço para termos os fornecedores da região
    // e quais produtos quer do fornecedor

    private List<ItemDaCompraDto> itens;

    private EnderecoDto endereco; //Endereço do cliente que fará um POST na loja
}
