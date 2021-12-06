package com.bancointer.controller;

import com.bancointer.exception.CriptografiaException;
import com.bancointer.exception.EntidadeNaoEncontradaException;
import com.bancointer.exception.ValorInvalidException;
import com.bancointer.service.DigitoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/digito")
public class DigitoUnicoController
{
    private DigitoService digitoService;


    @Autowired
    public DigitoUnicoController(DigitoService digitoService)
    {
        this.digitoService = digitoService;
    }


    @ApiOperation(value = "Realiza o calculo dos digitos", notes = "Caso o id do usuário seja passado, todos os cálculos executados são associados ao usuário desse id")
    @GetMapping
    public Integer calculaDigitoUnico(
        @RequestParam(required = false) Long usuarioId,
        @RequestParam String valor,
        @RequestParam(required = false) Integer repeticoes) throws ValorInvalidException,
                                                                                       EntidadeNaoEncontradaException,
                                                                                       CriptografiaException
    {
        return digitoService.calculaDigitoUnico(usuarioId, valor, repeticoes);
    }

}
