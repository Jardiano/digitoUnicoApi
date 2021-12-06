package com.bancointer.controller;

import com.bancointer.datatransferobject.UsuarioDTO;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.bancointer.util.Utils.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsuarioControllerTest
{

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testBCadastrarUsuario() throws Exception
    {
        UsuarioDTO usuarioDTO = UsuarioDTO.builder().id(1L).nome("Teste").email("teste@teste.com").build();

        this.mockMvc.perform(post("/v1/usuario")
            .content(asJsonString(usuarioDTO))
            .header("Authorization", "test")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", instanceOf(String.class)));
    }


    @Test
    public void testCListaUsuariosWithNotEmptyList() throws Exception
    {
        UsuarioDTO usuarioDTO = UsuarioDTO.builder().id(1L).nome("Teste").email("teste@teste.com").build();

        this.mockMvc.perform(post("/v1/usuario")
            .content(asJsonString(usuarioDTO))
            .header("Authorization", "test")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", instanceOf(String.class)));

        this.mockMvc.perform(get("/v1/usuario")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
    }


    @Test
    public void testDBuscarUsuarioPorIdWithAuthorization() throws Exception
    {
        UsuarioDTO usuarioDTO = UsuarioDTO.builder().id(1L).nome("Teste").email("teste@teste.com").build();

        MvcResult mvcResult = this.mockMvc.perform(post("/v1/usuario")
            .content(asJsonString(usuarioDTO))
            .header("Authorization", "test")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", instanceOf(String.class))).andReturn();

        String publicKey = mvcResult.getResponse().getContentAsString();

        this.mockMvc.perform(get("/v1/usuario/1")
            .header("Authorization", publicKey)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.nome", is(equalTo("Teste"))));
    }


    @Test
    public void testEAtualizarUsuario() throws Exception
    {
        UsuarioDTO usuarioDTOAtualizado = UsuarioDTO.builder().id(1L).nome("Tst").email("tste@teste.com").build();

        this.mockMvc.perform(put("/v1/usuario")
            .content(asJsonString(usuarioDTOAtualizado))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }


    @Test
    public void testFDeletarUsuario() throws Exception
    {
        this.mockMvc.perform(delete("/v1/usuario/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }


}