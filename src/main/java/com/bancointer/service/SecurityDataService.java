package com.bancointer.service;

import com.bancointer.exception.CriptografiaException;
import com.bancointer.exception.EntidadeNaoEncontradaException;

public interface SecurityDataService
{
    String getChavePublicaUsuario(Long usuarioId) throws EntidadeNaoEncontradaException;

    String resetChavesUsuario(Long usuarioId) throws EntidadeNaoEncontradaException, CriptografiaException;

}
