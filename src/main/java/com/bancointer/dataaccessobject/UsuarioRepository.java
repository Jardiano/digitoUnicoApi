package com.bancointer.dataaccessobject;

import com.bancointer.domainobject.Usuario;
import java.security.PublicKey;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario,Long>
{
    Usuario findByPublicKey(PublicKey publicKey);
}
