package com.microservice.loja.repositories;

import com.microservice.loja.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Luis Gustavo Ullmann on 28/06/2020
 */
public interface CompraRepository extends JpaRepository<Compra, Long> {
}
