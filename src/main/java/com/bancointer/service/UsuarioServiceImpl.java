package com.bancointer.service;

import com.bancointer.controller.mapper.UsuarioMapper;
import com.bancointer.dataaccessobject.UsuarioRepository;
import com.bancointer.datatransferobject.UsuarioDTO;
import com.bancointer.domainobject.Usuario;
import com.bancointer.exception.CriptografiaException;
import com.bancointer.exception.EntidadeNaoEncontradaException;
import com.bancointer.exception.ViolacaoConstraintException;
import com.bancointer.security.CifragemRSA;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService
{
    private static final Logger LOG = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    private UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository)
    {
        this.usuarioRepository = usuarioRepository;
    }


    public List<UsuarioDTO> buscarTodosUsuarios()
    {
        List<UsuarioDTO> listUsuario = new ArrayList<>();

        usuarioRepository.findAll().forEach(usuario -> {
            try
            {
                if (usuario.getPublicKey() != null)
                {
                    String keyAsString = CifragemRSA.getKeyAsString(usuario.getPublicKey());
                    listUsuario.add(UsuarioMapper.mapperToUsuarioDTO(usuario, keyAsString));
                }
                else
                {
                    listUsuario.add(UsuarioMapper.mapperToUsuarioDTO(usuario, null));
                }


            }
            catch (CriptografiaException e)
            {
                LOG.error("Erro na recuperação das chave do usuário de id: " + usuario.getId(), e);
            }
        });

        return listUsuario;
    }


    public UsuarioDTO buscarUsuarioPorId(Long id) throws CriptografiaException, EntidadeNaoEncontradaException
    {
        Usuario usuario = checarUsuario(id);
        return UsuarioMapper.mapperToUsuarioDTO(usuario, null);
    }


    public UsuarioDTO buscarUsuarioPorId(Long id, String key) throws EntidadeNaoEncontradaException, CriptografiaException
    {
        return UsuarioMapper.mapperToUsuarioDTO(checarUsuario(id), key);
    }


    public UsuarioDTO adicionarUsuario(UsuarioDTO usuarioDTO) throws ViolacaoConstraintException, CriptografiaException
    {
        Usuario usuario;
        try
        {
            usuario = usuarioRepository.save(UsuarioMapper.mapperFromDTO(usuarioDTO));
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.error("Violação de constraint na criação do usuario: {}", usuarioDTO, e);
            throw new ViolacaoConstraintException(e.getMessage());
        }

        return UsuarioMapper.mapperToUsuarioDTO(usuario, null);

    }


    public String adicionarUsuario(UsuarioDTO usuarioDTO, String key) throws ViolacaoConstraintException, CriptografiaException
    {
        Usuario usuario;
        try
        {
            usuario = usuarioRepository.save(UsuarioMapper.mapperFromDTO(usuarioDTO, key));
        }
        catch (DataIntegrityViolationException e)
        {
            LOG.error("Violação de constraint na criação do usuario: {}", usuarioDTO, e);
            throw new ViolacaoConstraintException(e.getMessage());
        }
        if(usuario.getPublicKey() != null){
            String keyAsString = CifragemRSA.getKeyAsString(usuario.getPublicKey());
            return keyAsString;
        }

        return "Usuario criado sem chave publica e privada";
    }


    public void deletarUsuario(Long usuarioId) throws EntidadeNaoEncontradaException
    {
        Usuario usuario = checarUsuario(usuarioId);

        usuarioRepository.delete(usuario);

    }


    public String atualizarUsuario(UsuarioDTO usuarioDTO) throws EntidadeNaoEncontradaException, CriptografiaException
    {
        checarUsuario(usuarioDTO.getId());

        Usuario usuario = usuarioRepository.save(UsuarioMapper.mapperFromDTO(usuarioDTO,null));

        if(usuario.getPublicKey() == null){
            return "Usuário atualizado";
        }

        return CifragemRSA.getKeyAsString(usuario.getPublicKey());

    }


    private Usuario checarUsuario(Long usuarioId) throws EntidadeNaoEncontradaException
    {
        return usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new EntidadeNaoEncontradaException("Não foi possível encontrar o usuário com o id: " + usuarioId));

    }


    @Override
    public PublicKey getChavePublicaUsuario(Long usuarioId) throws EntidadeNaoEncontradaException
    {
        Usuario usuario = checarUsuario(usuarioId);
        return usuario.getPublicKey();
    }

}
