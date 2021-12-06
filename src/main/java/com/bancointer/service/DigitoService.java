package com.bancointer.service;

import com.bancointer.exception.CriptografiaException;
import com.bancointer.exception.EntidadeNaoEncontradaException;
import com.bancointer.exception.ValorInvalidException;

public interface DigitoService
{
    Integer calculaDigitoUnico(Long usuarioId,String valor) throws EntidadeNaoEncontradaException, CriptografiaException, ValorInvalidException;

    Integer calculaDigitoUnico(Long usuarioId,String valor, Integer repeticoes) throws ValorInvalidException, EntidadeNaoEncontradaException, CriptografiaException;
}