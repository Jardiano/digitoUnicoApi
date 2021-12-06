package com.bancointer.controller;

import com.bancointer.datatransferobject.UsuarioDTO;
import com.bancointer.exception.CriptografiaException;
import com.bancointer.exception.EntidadeNaoEncontradaException;
import com.bancointer.exception.ViolacaoConstraintException;
import com.bancointer.service.UsuarioServiceImpl;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/usuario")
public class UsuarioController
{

    private UsuarioServiceImpl usuarioService;

    @Autowired
    public UsuarioController(UsuarioServiceImpl usuarioService)
    {
        this.usuarioService = usuarioService;
    }

    @ApiOperation(value = "Lista todos os usuários cadastrados")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UsuarioDTO> listaUsuarios(){
        return usuarioService.buscarTodosUsuarios();
    }

    @ApiOperation(value = "Busca o usuário pelo id cadastrado no sistema",
        notes = "Caso o header Authorization com a chave publica não seja passado, os dados de nome e email serão retornados criptografados.")
    @GetMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioDTO buscarUsuarioPorId(@PathVariable Long usuarioId,@RequestHeader(value = "Authorization",required = false) String key) throws EntidadeNaoEncontradaException, CriptografiaException
    {
        return usuarioService.buscarUsuarioPorId(usuarioId,key);
    }

    @ApiOperation(value = "Cadastra um usuário no sistema",
        notes = "Será reteornada a chave pública após a criação do usuário, caso o header Authorization não seja passado, será retornada a mensagem 'Usuario criado sem chave " +
            "publica e privada', ou seja, não será atribuída uma chave publica e privada para esse usuário.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String cadastrarUsuario(@Valid @NotNull @RequestBody UsuarioDTO usuarioDTO, @RequestHeader(value = "Authorization",required = false) String key) throws ViolacaoConstraintException,
                                                                                                                                   CriptografiaException
    {
        return usuarioService.adicionarUsuario(usuarioDTO,key);
    }

    @ApiOperation(value = "Remove o usuário do sistema")
    @DeleteMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletarUsuario(@NotNull @PathVariable Long usuarioId) throws EntidadeNaoEncontradaException{
        usuarioService.deletarUsuario(usuarioId);
    }

    @ApiOperation(value = "Atualiza dados do usuário no sistema", notes = "Retorna a chave publica do usuário após a atualização dos dados")
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String atualizarUsuario(@Valid @NotNull @RequestBody UsuarioDTO usuarioDTO) throws EntidadeNaoEncontradaException, CriptografiaException
    {
        return usuarioService.atualizarUsuario(usuarioDTO);
    }


}
