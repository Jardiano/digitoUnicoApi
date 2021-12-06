package com.bancointer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Valor inválido para geração do dígito unico")
public class ValorInvalidException extends Exception
{
    static final long serialVersionUID = -3387516993224229947L;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public ValorInvalidException(String message)
    {
        super(message);
    }
}
