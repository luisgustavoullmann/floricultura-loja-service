package com.microservice.loja.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by Luis Gustavo Ullmann on 26/06/2020
 */
@Getter
@Setter
@ToString
public class EnderecoDto {
    //DTO com que passa o Endereço na loja atraves de um POST e retorna quais fornecedores de região
    private String rua;
    private int numero;
    private String estado;
}
