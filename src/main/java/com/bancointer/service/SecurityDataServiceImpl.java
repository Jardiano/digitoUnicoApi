package com.bancointer.service;

import com.bancointer.controller.mapper.UsuarioMapper;
import com.bancointer.dataaccessobject.UsuarioRepository;
import com.bancointer.datatransferobject.UsuarioDTO;
import com.bancointer.domainobject.Usuario;
import com.bancointer.exception.CriptografiaException;
import com.bancointer.exception.EntidadeNaoEncontradaException;
import com.bancointer.security.CifragemRSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityDataServiceImpl implements SecurityDataService
{
    private UsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;


    @Autowired
    public SecurityDataServiceImpl(UsuarioRepository usuarioRepository, UsuarioService usuarioService)
    {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public String getChavePublicaUsuario(Long usuarioId) throws EntidadeNaoEncontradaException
    {
        Usuario key = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Não foi possível encontrar o usuário com o id: " + usuarioId));

        return CifragemRSA.getKeyAsString(key.getPublicKey());
    }

    @Override
    public String resetChavesUsuario(Long usuarioId) throws EntidadeNaoEncontradaException, CriptografiaException
    {
        UsuarioDTO usuarioDTO = usuarioService.buscarUsuarioPorId(usuarioId);
        Usuario usuario = UsuarioMapper.mapperFromDTO(usuarioDTO);
        usuarioRepository.save(usuario);
        return CifragemRSA.getKeyAsString(usuario.getPublicKey());
    }

}
