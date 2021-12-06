package com.bancointer.security;

import com.bancointer.exception.CriptografiaException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

public class CifragemRSA
{
    private static CifragemRSA instancia;

    private static final Logger LOG = LoggerFactory.getLogger(CifragemRSA.class);

    private static final String RSA = "RSA";

    @Getter
    private PrivateKey privateKey;

    @Getter
    private PublicKey publicKey;


    public  CifragemRSA() throws CriptografiaException
    {
        KeyPair keyPair = genKeyPair();
        this.publicKey = keyPair.getPublic();
        this.privateKey = keyPair.getPrivate();
    }



/*    public static synchronized CifragemRSA getInstance() throws CriptografiaException
    {
        if(instancia == null){
            instancia = new CifragemRSA();
        }
        return instancia;
    }*/

    private KeyPair genKeyPair() throws  CriptografiaException
    {
    try{
        KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA) ;
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();

        return keyPair;
    }catch (Exception e){
        LOG.error("Não foi possível gerar as chaves publica e privada ");
        throw new CriptografiaException(e);
    }

   }

    public static byte[] encripta(String string,PublicKey publicKey) throws CriptografiaException
    {
        Cipher cipher;
        try
        {
            cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(string.getBytes());

        }
        catch (Exception e)
        {
            LOG.error("Erro na geração das chaves para criptografia", e);
            throw new CriptografiaException(e);
        }
    }



    public static String getKeyAsString(Key key)
    {
        byte[] keyBytes = key.getEncoded();
        return Base64Utils.encodeToString(keyBytes);
    }


    public static String decripta(byte[] bytes, PrivateKey privateKey) throws CriptografiaException
    {
        try
        {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(bytes));
        }
        catch (Exception e)
        {
            LOG.error("Erro na descriptografia dos dados");
            throw new CriptografiaException(e);
        }
    }



    public static PrivateKey getPrivateKeyFromString(String key) throws CriptografiaException
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64Utils.decodeFromString(key));
            return keyFactory.generatePrivate(privateKeySpec);
        }
        catch (Exception e)
        {
            LOG.error("Erro na recuperação da chave privada");
            throw new CriptografiaException(e);
        }
    }


    public static PublicKey getPublicKeyFromString(String key) throws CriptografiaException
    {
        try
        {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64Utils.decodeFromString(key));
            return keyFactory.generatePublic(publicKeySpec);
        }
        catch (Exception e)
        {
            LOG.error("Erro na recuperação da chave publica");
            throw new CriptografiaException(e);
        }

    }

}