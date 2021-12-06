package com.bancointer.datatransferobject;

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
public class DigitoUnicoDTO
{

    private Long id;

    private String params;

    private Integer resultado;
}
