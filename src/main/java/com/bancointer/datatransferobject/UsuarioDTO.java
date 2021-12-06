package com.bancointer.datatransferobject;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO
{
    private Long id;

    private String nome;

    private String email;

    private List<DigitoUnicoDTO> digitos;
}
