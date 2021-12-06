package com.bancointer.service;

import com.bancointer.datatransferobject.UsuarioDTO;
import com.bancointer.exception.CriptografiaException;
import com.bancointer.exception.EntidadeNaoEncontradaException;
import com.bancointer.exception.ViolacaoConstraintException;
import java.security.PublicKey;
import java.util.List;

public interface UsuarioService
{
    List<UsuarioDTO> buscarTodosUsuarios();

    UsuarioDTO buscarUsuarioPorId(Long id) throws CriptografiaException, EntidadeNaoEncontradaException;

    UsuarioDTO buscarUsuarioPorId(Long id, String key) throws EntidadeNaoEncontradaException, CriptografiaException;

    String adicionarUsuario(UsuarioDTO usuarioDTO, String key) throws ViolacaoConstraintException,CriptografiaException;

    void deletarUsuario(Long driverId) throws EntidadeNaoEncontradaException;

    String atualizarUsuario(UsuarioDTO usuarioDTO) throws EntidadeNaoEncontradaException, CriptografiaException;

    public PublicKey getChavePublicaUsuario(Long usuarioId) throws EntidadeNaoEncontradaException;

}
