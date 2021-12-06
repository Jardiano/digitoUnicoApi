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
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DigitoUnicoControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testACalculaDigitoUnicoWithUsuarioIdAndRepeticoes() throws Exception
    {
        UsuarioDTO usuarioDTO = UsuarioDTO.builder().id(1L).nome("Teste").email("teste@teste.com").build();

        MvcResult mvcResult = this.mockMvc.perform(post("/v1/usuario")
            .content(asJsonString(usuarioDTO))
            .header("Authorization", "test")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$", instanceOf(String.class))).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        this.mockMvc.perform(get("/v1/digito")
            .header("Authorization", contentAsString)
            .param("valor", String.valueOf(9845))
            .param("repeticoes", String.valueOf(4))
            .param("usuarioId", "1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(equalTo(5))));

        this.mockMvc.perform(get("/v1/usuario/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.digitos", hasSize(3)));

    }


    @Test
    public void testBCalculaDigitoUnicoWithoutUsuarioIdAndRepeticoes() throws Exception
    {
        this.mockMvc.perform(get("/v1/digito")
            .param("valor", String.valueOf(9845))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(equalTo(8))));
    }

    @Test
    public void testCCalculaDigitoUnicoWithoutUsuarioIdAndWithRepeticoes() throws Exception
    {
        this.mockMvc.perform(get("/v1/digito")
            .param("valor", String.valueOf(9845))
            .param("repeticoes", String.valueOf(4))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", is(equalTo(5))));
    }

}