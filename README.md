# Digito Unico API


### Como compilar e executar a aplicação

Para compliar e executar a aplicação basta executar o comando:
```sh
mvn spring-boot:run
```

### Como executar os testes unitários e de integração.
Para a execução dos testes basta executar o comando:
```sh
mvn test
```

### Endpoints

Os endpoints criados foram:

| Path | Request Type | Paramametros aceitos| Descricao
| ------ | ------ | ------ | ------ | 
| /v1/usuario | GET | Nenhum | Retorna a lista de usuários
| /v1/usuario/{usuarioId} | GET | PathVariable: usuarioId* - Long, RequestHeader: Authorization - String| Busca o usuário pelo id cadastrado no sistema
| /v1/usuario | POST | RequestHeader: Authorization - String| Cadastra um usuário no sistema
| /v1/usuario/{usuarioId} | DELETE | PathVariable: usuarioId* - Long| Remove o usuário do sistema
| /v1/usuario | PUT | RequestBody: usuarioDTO - UsuarioDTO | Atualiza dados do usuário no sistema
| /v1/digito | GET | RequestParam: usuarioId - Long, RequestParam: valor* - String, RequestParam: repeticoes - Integer | Realiza o calculo dos digitos
| v1/auth/{usuarioId} | GET | PathVariable: usuarioId - Long | Recupera a chave publica do usuário

*Required

### Modelos
``` json
UsuarioDTO{
    digitos	[{
        id	integer($int64)
        params	string
        resultado	integer($int32)
    }]
    email	string
    id	integer($int64)
    nome	string
}

DigitoUnicoDTO{
    id	integer($int64)
    params	string
    resultado	integer($int32)
}
```

