package com.microservice.loja.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Luis Gustavo Ullmann on 26/06/2020
 */
@Getter
@Setter
public class EnderecoDto {

    private String rua;
    private int numero;
    private String estado;
}
