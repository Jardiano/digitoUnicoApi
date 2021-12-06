package com.bancointer.service;

import com.bancointer.dataaccessobject.DigitoUnicoRepository;
import com.bancointer.datatransferobject.DigitoUnicoDTO;
import com.bancointer.datatransferobject.UsuarioDTO;
import com.bancointer.exception.CriptografiaException;
import com.bancointer.exception.EntidadeNaoEncontradaException;
import com.bancointer.exception.ValorInvalidException;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DigitoServiceImpl implements DigitoService
{
    private static final Logger LOG = LoggerFactory.getLogger(DigitoServiceImpl.class);

    private Map<String, Integer> cache = new LinkedHashMap<>(10);

    private DigitoUnicoRepository digitoUnicoRepository;
    private UsuarioService usuarioService;

    @Autowired
    public DigitoServiceImpl(DigitoUnicoRepository digitoUnicoRepository, UsuarioService usuarioService)
    {
        this.digitoUnicoRepository = digitoUnicoRepository;
        this.usuarioService = usuarioService;
    }


    @Override
    public Integer calculaDigitoUnico(Long usuarioId, String valor) throws EntidadeNaoEncontradaException, CriptografiaException, ValorInvalidException
    {
        Integer soma = 0;

        Integer digitoUnico = 0;

        if (cache.containsKey(valor))
        {
            return cache.get(valor);
        }

        try
        {
            if (valor.length() == 1)
            {
                digitoUnico = Integer.parseInt(valor);
            }
            else
            {
                BigInteger valorInt = new BigInteger(valor);

                if (BigInteger.valueOf(9).compareTo(valorInt) < 0)
                {
                    String[] valores = valor.split("");

                    for (String val : valores)
                    {
                        soma += Integer.parseInt(val);
                    }
                    digitoUnico = calculaDigitoUnico(usuarioId,String.valueOf(soma));

                }
            }
        }
        catch (NumberFormatException e)
        {
            LOG.error("Erro na geração do dígito unico", e);
            throw new ValorInvalidException(e.getMessage());
        }

        if(usuarioId != null){
            associaUsuarioComCalculo(usuarioId,valor,digitoUnico);
        }

        atualizaCache(valor, digitoUnico);
        return digitoUnico;

    }


    private void atualizaCache(String valor, Integer digitoUnico)
    {
        cache.entrySet().removeIf(e -> cache.size() == 10);
        cache.putIfAbsent(valor, digitoUnico);
    }


    @Override
    public Integer calculaDigitoUnico(Long usuarioId, String valor, Integer repeticoes) throws ValorInvalidException, EntidadeNaoEncontradaException, CriptografiaException
    {
        StringBuilder valorMultiplicado = new StringBuilder(valor);

        if(repeticoes == null){
            repeticoes = 0;
        }

        for (int i = 1; i < repeticoes; i++)
        {
            valorMultiplicado.append(valor);
        }

        return calculaDigitoUnico(usuarioId,valorMultiplicado.toString());
    }

    private void associaUsuarioComCalculo(Long usuarioId, String params,Integer valor) throws EntidadeNaoEncontradaException, CriptografiaException
    {
        UsuarioDTO usuarioDTO = usuarioService.buscarUsuarioPorId(usuarioId);

        List<DigitoUnicoDTO> list  = usuarioDTO.getDigitos();

        DigitoUnicoDTO digitoUnicoDTO = DigitoUnicoDTO.builder()
            .params(params)
            .resultado(valor)
            .build();

        list.add(digitoUnicoDTO);

        usuarioDTO.setDigitos(list);

        usuarioService.atualizarUsuario(usuarioDTO);
    }


}
