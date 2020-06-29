package com.microservice.loja.controller.dto.fornecedor;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Luis Gustavo Ullmann on 27/06/2020
 */
@Getter
@Setter
public class InfoPedidoDto { //Após o POST, esses são os dados que retornam para Loja
    private Long id; //pedidoId que terá um POST no fornecedor
    private Integer tempoDePreparo;
}
