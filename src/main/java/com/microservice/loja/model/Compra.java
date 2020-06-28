package com.microservice.loja.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Luis Gustavo Ullmann on 27/06/2020
 */
@Getter
@Setter
public class Compra {
    private Long pedidoId;
    private Integer tempoDePreparo;
    private String enderecoDestino;
}
