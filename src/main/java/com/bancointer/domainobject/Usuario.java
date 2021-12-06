package com.bancointer.domainobject;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Getter
@Setter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Usuario
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 1048)
    @NotNull(message = "Nome não pode ser nulo")
    private byte[] nome;

    @Column(nullable = false,  length = 1024)
    @NotNull(message = "Email não pode ser nulo")
    private byte[] email;

    @Cascade(CascadeType.ALL)
    @ManyToMany
    @JoinColumn(name = "digito_id", referencedColumnName = "id")
    private List<DigitoUnico> digitos;

    @Column(length = 2048)
    private PublicKey publicKey;

    @Column(length = 2048)
    private PrivateKey privateKey;

}
