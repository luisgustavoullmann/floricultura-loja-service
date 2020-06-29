package com.microservice.loja.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by Luis Gustavo Ullmann on 27/06/2020
 */
@Getter
@Setter
@Entity
public class Compra {
    //No nosso caso, essa transação se resume em fazer um pedido no Fornecedor e criar um voucher no Transportador

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //Id compra

    //Estado da compra - Tratamento de erro
    @Enumerated(EnumType.STRING)
    private CompraState state;

    //Fornecedor
    private Long pedidoId; //ID do pedido gerado pelo fornecedor
    private Integer tempoDePreparo;
    private String enderecoDestino;

    //Voucher que irá preencher
    private LocalDate dataParaEntrega;
    private Long voucher;
}
