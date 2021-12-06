package com.bancointer.controller.mapper;

import com.bancointer.datatransferobject.DigitoUnicoDTO;
import com.bancointer.domainobject.DigitoUnico;

public class DigitoUnicoMapper
{

    public static DigitoUnicoDTO mapperToDigitoUnicoDTO(DigitoUnico digitoUnico)
    {
        if (digitoUnico != null)
        {
            return DigitoUnicoDTO.builder()
                .id(digitoUnico.getId())
                .params(digitoUnico.getParams())
                .resultado(digitoUnico.getResultado())
                .build();
        }
        return null;
    }


    public static DigitoUnico mapperFromDTO(DigitoUnicoDTO digitoUnicoDTO)
    {
        if(digitoUnicoDTO != null){
            return DigitoUnico.builder()
                .id(digitoUnicoDTO.getId())
                .params(digitoUnicoDTO.getParams())
                .resultado(digitoUnicoDTO.getResultado())
                .build();
        }

        return null;
    }

}
