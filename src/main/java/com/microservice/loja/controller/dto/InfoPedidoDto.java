package com.microservice.loja.controller.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Luis Gustavo Ullmann on 27/06/2020
 */
@Getter
@Setter
public class InfoPedidoDto { //Após o POST, esses são os dados que retornam para Loja
    private Long id;
    private Integer tempoDePreparo;
}
