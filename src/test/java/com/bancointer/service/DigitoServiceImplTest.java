package com.bancointer.service;

import com.bancointer.dataaccessobject.DigitoUnicoRepository;
import com.bancointer.datatransferobject.UsuarioDTO;
import com.bancointer.exception.CriptografiaException;
import com.bancointer.exception.EntidadeNaoEncontradaException;
import com.bancointer.exception.ValorInvalidException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.when;

public class DigitoServiceImplTest
{

    @InjectMocks
    private DigitoServiceImpl digitoService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private DigitoUnicoRepository digitoUnicoRepository;

    private UsuarioDTO usuarioDTO;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setDigitos(new ArrayList<>());

        when(usuarioService.atualizarUsuario(any(UsuarioDTO.class))).thenReturn("");
        when(usuarioService.buscarUsuarioPorId(anyLong())).thenReturn(usuarioDTO);
        when(digitoUnicoRepository.save(any())).thenAnswer(RETURNS_DEFAULTS);
        digitoService = new DigitoServiceImpl(digitoUnicoRepository, usuarioService);
    }


    @Test
    public void calculaDigitoUnicoReturningValidValue() throws ValorInvalidException, EntidadeNaoEncontradaException, CriptografiaException
    {
        String valor = "9875";
        Integer val = digitoService.calculaDigitoUnico(3L,valor);
        assertThat(val, is(equalTo(2)));
    }

    @Test
    public void calculaDigitoUnicoReturningWithSingleValue() throws ValorInvalidException, EntidadeNaoEncontradaException, CriptografiaException
    {
        String valor = "9";
        Integer val = digitoService.calculaDigitoUnico(2L,valor);
        assertThat(val, is(equalTo(9)));
    }



    @Test(expected = ValorInvalidException.class)
    public void calculaDigitoUnicoWithInvalidValueThrowsValorInvalidException() throws ValorInvalidException, EntidadeNaoEncontradaException, CriptografiaException
    {
        String valor = "9a75";
        Integer val = digitoService.calculaDigitoUnico(1L,valor);
        assertThat(val, is(equalTo(2)));
    }


    @Test(expected = ValorInvalidException.class)
    public void calculaDigitoUnicoWithEmptyValue() throws ValorInvalidException, EntidadeNaoEncontradaException, CriptografiaException
    {
        String valor = "";
        Integer val = digitoService.calculaDigitoUnico(1L,valor);
        assertThat(val, is(equalTo(0)));
    }

    @Test
    public void testCalculaDigitoUnico() throws ValorInvalidException, EntidadeNaoEncontradaException, CriptografiaException
    {
        String valor = "9875";
        Integer repeticoes = 4;

        Integer val = digitoService.calculaDigitoUnico(1L,valor, repeticoes);
        assertThat(val, is(equalTo(8)));

    }

    @Test
    public void testCalculaDigitoUnicoWithMaxValues() throws ValorInvalidException, EntidadeNaoEncontradaException, CriptografiaException
    {
        Integer valInt = 10 ^ 100000;
        String valor = String.valueOf(valInt);
        Integer repeticoes = 10 ^ 5;

        Integer val = digitoService.calculaDigitoUnico(1L,valor, repeticoes);
        assertThat(val, is(equalTo(3)));

    }

    @Test
    public void testCalculaDigitoUnicoWithRepticoesNull() throws ValorInvalidException, EntidadeNaoEncontradaException, CriptografiaException
    {
        Integer valInt = 9845;
        String valor = String.valueOf(valInt);
        Integer repeticoes = null;

        Integer val = digitoService.calculaDigitoUnico(1L,valor, repeticoes);
        assertThat(val, is(equalTo(8)));

    }


    @Test
    public void testCalculaDigitoUnicoRecoveringFromTheCache() throws ValorInvalidException, EntidadeNaoEncontradaException, CriptografiaException
    {
        String valor = "9875";
        Integer val = digitoService.calculaDigitoUnico(1L,valor);

        Integer val2 = digitoService.calculaDigitoUnico(1L,valor);

        assertThat(val2, is(equalTo(2)));

    }

    @Test
    public void testCalculaDigitoUnicoClearingCacheWithTenValues() throws ValorInvalidException, EntidadeNaoEncontradaException, CriptografiaException
    {
        String valor = "9875";
        Integer val = 0;
        for(int i=0; i <=10; i++){
            val = digitoService.calculaDigitoUnico(1L,valor+i);
        }

        assertThat(val, is(equalTo(3)));

    }

}