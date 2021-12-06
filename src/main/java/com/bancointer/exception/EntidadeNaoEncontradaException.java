package com.bancointer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Não foi possível encontrar a entidade com esse id")
public class EntidadeNaoEncontradaException extends Exception
{
    static final long serialVersionUID = -3387516993334229948L;


    public EntidadeNaoEncontradaException(String message)
    {
        super(message);
    }

}
