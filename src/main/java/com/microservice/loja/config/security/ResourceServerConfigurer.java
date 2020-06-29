package com.microservice.loja.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Created by Luis Gustavo Ullmann on 29/06/2020
 */
@Configuration
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {
    //Classe que verifica o token, usuário logado ou não, para responder as requests

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //Para toda requisição, o usuário precisa estar autenticado
//        http.authorizeRequests()
//                .anyRequest()
//                .authenticated();

        //Apenas a requisição POST para /pedido é que o usuário deve estar logado
        //requisição para /produto irá funcionar se a necessidade de login, já que pedido é o único que pede o token
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/compra")
                .hasRole("USER"); //precisa ter esse papel para ter autorização e fazer o pedido
    }
}
