package com.microservice.loja.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * Created by Luis Gustavo Ullmann on 27/06/2020
 */
@Getter
@Setter
@Entity
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pedidoId; //ID do pedido gerado pelo fornecedor
    private Integer tempoDePreparo;
    private String enderecoDestino;

    //Voucher que irá preencher
    private LocalDate dataParaEntrega;
    private Long voucher;
}
