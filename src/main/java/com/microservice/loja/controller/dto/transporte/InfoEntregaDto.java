package com.microservice.loja.controller.dto.transporte;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Created by Luis Gustavo Ullmann on 29/06/2020
 */
@Getter
@Setter
public class InfoEntregaDto {
    private Long pedidoId;
    private LocalDate dataParaEntrega;
    private String enderecoOrigem;
    private String enderecoDestino;
}
