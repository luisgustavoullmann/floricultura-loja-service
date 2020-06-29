package com.microservice.loja.client;

import com.microservice.loja.controller.dto.transporte.InfoEntregaDto;
import com.microservice.loja.controller.dto.transporte.VoucherDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by Luis Gustavo Ullmann on 29/06/2020
 */
@FeignClient("transporte")
public interface TransportadorClient {

    @PostMapping("/entrega")
    public VoucherDto reservaEntrega(InfoEntregaDto pedidoDto); //Dtos do Transporte para loja
}
