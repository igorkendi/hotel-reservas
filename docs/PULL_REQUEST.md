# feat: cadastrar cliente com arquitetura em camadas (US02)

## O que foi feito

Implementação da User Story **US02 — Cadastrar cliente** organizada em camadas
(Controller → Service → Repository), seguindo o fluxo apresentado na aula 09.

- `POST /clientes` cadastra um cliente; `GET /clientes/{id}` e `GET /clientes?nome=` consultam.
- Endereço modelado como `@Embeddable`, refletindo a **composição** Cliente–Endereco do diagrama de classes (aula 08).
- CPF devolvido mascarado na resposta.

## Decisão arquitetural

| Onde | Por quê |
|---|---|
| **Controller** só valida o formato (`@Valid` no DTO) e delega | Mantém o Controller enxuto; ele apenas traduz HTTP em chamada da aplicação. |
| **Service** concentra as regras (CPF válido, CPF único, titular maior de idade) | Regras de negócio ficam em um lugar só e podem ser testadas sem HTTP e sem banco. |
| **Repository** com Spring Data (`existsByCpf`) | Quem usa não precisa conhecer SQL; trocar o banco não afeta as outras camadas. |
| **Model** com `Cpf` e `Cliente.isMaiorDeIdade()` | Regras que dependem só do conceito ficam no domínio (alta coesão). |
| **DTOs + Mapper** | A API não expõe a entidade JPA; mudanças no banco não quebram o contrato. |
| **GlobalExceptionHandler** | Converte exceções de negócio em 400/404/409/422 sem `try/catch` no Controller. |
| `Clock` injetado | Permite fixar a data nos testes da regra de idade. |

Resultado: **baixo acoplamento** (cada camada depende só da de baixo) e **alta coesão**
(`ClienteService` trata apenas casos de uso de cliente).

## Como testar

```bash
mvn test
mvn spring-boot:run
# POST de exemplo no README
```

## Commits

1. `chore`: estrutura inicial do projeto
2. `feat(model)`: entidade Cliente, Endereco e regras de CPF
3. `feat(repository)`: ClienteRepository
4. `feat(dto)`: DTOs e mapper
5. `feat(service)`: regras do cadastro
6. `feat(controller)`: endpoints REST
7. `feat(exception)`: tratamento global de erros
8. `test`: testes de domínio, service e controller
9. `docs`: README com diagrama da arquitetura
