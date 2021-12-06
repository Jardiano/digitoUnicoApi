package com.bancointer.controller;

import com.bancointer.exception.CriptografiaException;
import com.bancointer.exception.EntidadeNaoEncontradaException;
import com.bancointer.service.SecurityDataService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/auth")
public class SecurityDataController
{
    private SecurityDataService dataService;

    @Autowired
    public SecurityDataController(SecurityDataService dataService)
    {
        this.dataService = dataService;
    }

    @ApiOperation(value = "Recupera a chave publica do usuário")
    @GetMapping("/{usuarioId}")
    @ResponseStatus(code = HttpStatus.OK)
    public String getChavePublicaUsuario(@PathVariable Long usuarioId) throws EntidadeNaoEncontradaException
    {
        return dataService.getChavePublicaUsuario(usuarioId);
    }

    @ApiOperation(value = "Gera novas chaves publica e privada utilizando o id do usuário")
    @PostMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    public String resetChavesUsuario(@PathVariable Long usuarioId, @RequestHeader("Authorization") String key) throws EntidadeNaoEncontradaException, CriptografiaException
    {
        return dataService.resetChavesUsuario(usuarioId);
    }
}
