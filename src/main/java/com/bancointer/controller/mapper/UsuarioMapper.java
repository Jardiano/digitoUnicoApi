package com.bancointer.controller.mapper;

import com.bancointer.datatransferobject.DigitoUnicoDTO;
import com.bancointer.datatransferobject.UsuarioDTO;
import com.bancointer.domainobject.DigitoUnico;
import com.bancointer.domainobject.Usuario;
import com.bancointer.exception.CriptografiaException;
import com.bancointer.security.CifragemRSA;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper
{
    private UsuarioMapper()
    {
    }


    public static UsuarioDTO mapperToUsuarioDTO(Usuario usuario, String key) throws CriptografiaException
    {
        if (usuario != null)
        {
            List<DigitoUnicoDTO> digitoUnicoDTOS = new ArrayList<>();

            if(usuario.getDigitos() != null){
                digitoUnicoDTOS = usuario.getDigitos().stream()
                    .map(DigitoUnicoMapper::mapperToDigitoUnicoDTO)
                    .collect(Collectors.toList());
            }

            if(key != null && usuario.getPublicKey() != null){
                String keyAsString = CifragemRSA.getKeyAsString(usuario.getPublicKey());

                if(key.equals(keyAsString))
                {
                    return UsuarioDTO.builder()
                        .id(usuario.getId())
                        .nome(CifragemRSA.decripta(usuario.getNome(), usuario.getPrivateKey()))
                        .email(CifragemRSA.decripta(usuario.getEmail(), usuario.getPrivateKey()))
                        .digitos(digitoUnicoDTOS)
                        .build();
                }

            }

            return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(new String(usuario.getNome()))
                .email(new String(usuario.getEmail()))
                .digitos(digitoUnicoDTOS)
                .build();
        }

        return null;
    }

    public static Usuario mapperFromDTO(UsuarioDTO usuarioDTO) throws CriptografiaException
    {
        if (usuarioDTO != null)
        {
            List<DigitoUnico> digitos = new ArrayList<>();

            if(usuarioDTO.getDigitos() != null){
                digitos = usuarioDTO.getDigitos().stream()
                    .map(DigitoUnicoMapper::mapperFromDTO)
                    .collect(Collectors.toList());
            }

            //Aplicacao da criptografia nos dados de nome e email
            CifragemRSA cifragem = new CifragemRSA();

            return Usuario.builder()
                .id(usuarioDTO.getId())
                .nome(CifragemRSA.encripta(usuarioDTO.getNome(),cifragem.getPublicKey()))
                .email(CifragemRSA.encripta(usuarioDTO.getEmail(),cifragem.getPublicKey()))
                .digitos(digitos)
                .publicKey(cifragem.getPublicKey())
                .privateKey(cifragem.getPrivateKey())
                .build();
        }

        return null;
    }


    public static Usuario mapperFromDTO(UsuarioDTO usuarioDTO, String key) throws CriptografiaException
    {
        if (usuarioDTO != null)
        {
            List<DigitoUnico> digitos = new ArrayList<>();

            if(usuarioDTO.getDigitos() != null){
                digitos = usuarioDTO.getDigitos().stream()
                    .map(DigitoUnicoMapper::mapperFromDTO)
                    .collect(Collectors.toList());
            }

            if(key != null){

                //Aplicacao da critpografia nos dados de nome e email
                CifragemRSA cifragem = new CifragemRSA();

                PublicKey publicKeyFromString = cifragem.getPublicKey();
                PrivateKey privateKeyFromString = cifragem.getPrivateKey();

                return Usuario.builder()
                    .id(usuarioDTO.getId())
                    .nome(CifragemRSA.encripta(usuarioDTO.getNome(),publicKeyFromString))
                    .email(CifragemRSA.encripta(usuarioDTO.getEmail(),publicKeyFromString))
                    .digitos(digitos)
                    .publicKey(publicKeyFromString)
                    .privateKey(privateKeyFromString)
                    .build();
            }else {
                return Usuario.builder()
                    .id(usuarioDTO.getId())
                    .nome(usuarioDTO.getNome().getBytes())
                    .email(usuarioDTO.getEmail().getBytes())
                    .digitos(digitos)
                    .build();

            }
        }

        return null;
    }


}
