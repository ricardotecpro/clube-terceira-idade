# Cadastro de Associados

Este projeto consiste em um backend Spring Boot e um frontend Angular para gerenciar associados, pagamentos e eventos.

## Estrutura do Projeto

- `backend/`: Contém a aplicação Spring Boot.
- `frontend/`: Contém a aplicação Angular.

## Backend (Spring Boot)

### Tecnologias
- Spring Boot
- Spring Data JPA
- Spring Security
- H2 Database (desenvolvimento)
- PostgreSQL (produção)
- Lombok

### Configuração de Banco de Dados

O backend está configurado para usar perfis de ambiente:
- **Desenvolvimento (`dev`):** Utiliza H2 Database em memória. Os dados são semeados automaticamente via `data.sql` ao iniciar a aplicação.
- **Produção (`prod`):** Utiliza PostgreSQL. As configurações devem ser fornecidas via variáveis de ambiente.

**Para iniciar o backend em desenvolvimento:**

1.  Certifique-se de ter o Java 17+ e Maven instalados.
2.  Navegue até o diretório `backend/`:
    ```bash
    cd backend
    ```
3.  Execute a aplicação:
    ```bash
    ./mvnw spring-boot:run
    ```
    (No Windows, use `mvnw.cmd spring-boot:run`)

A aplicação estará disponível em `http://localhost:8080`.

### Usuários para Login (Backend)

Dois usuários foram configurados no `data.sql` para testes:

| Usuário    | Senha      | Papel      |
| :--------- | :--------- | :--------- |
| `admin`    | `password` | `ADMIN`    |
| `secretario` | `password` | `SECRETARIO` |

**ATENÇÃO:** As senhas no `data.sql` (`{BCRYPT_HASH_DA_SENHA_ADMIN}` e `{BCRYPT_HASH_DA_SENHA_SECRETARIO}`) são placeholders. Você **DEVE** gerar os hashes BCrypt para a senha "password" e substituí-los no `backend/src/main/resources/data.sql` antes de iniciar o backend.

Para gerar os hashes, você pode usar o seguinte código Java:

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode("password");
        System.out.println("Hash para 'password': " + hashedPassword);
    }
}
```
Execute este código, copie o hash gerado e cole-o no `data.sql` para ambos os usuários.

## Frontend (Angular)

### Tecnologias
- Angular
- TypeScript
- HTML/CSS

### Configuração

O frontend está configurado com um proxy (`proxy.conf.json`) para redirecionar as chamadas `/api` para o backend Spring Boot (`http://localhost:8080`), evitando problemas de CORS durante o desenvolvimento.

### Para iniciar o frontend:

1.  Certifique-se de ter o Node.js e o npm (ou yarn) instalados.
2.  Navegue até o diretório `frontend/`:
    ```bash
    cd frontend
    ```
3.  Instale as dependências:
    ```bash
    npm install
    # ou yarn install
    ```
4.  Inicie o servidor de desenvolvimento:
    ```bash
    ng serve
    ```

A aplicação estará disponível em `http://localhost:4200`.

## Fluxo de Login

1.  Ao acessar `http://localhost:4200`, você será redirecionado para a tela de login.
2.  Use as credenciais `admin`/`password` ou `secretario`/`password` (após atualizar os hashes no `data.sql` do backend) para fazer login.
3.  Após o login bem-sucedido, você será redirecionado para o Dashboard.
4.  O botão "Sair" no menu lateral fará o logout.
