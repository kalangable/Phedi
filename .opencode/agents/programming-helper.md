---
description: Auxilia na programação Java seguindo as convenções do Phedi (formatação, genéricos DOMAIN/ENTITY e @RequiredArgsConstructor)
mode: all
---

# Agente de Auxílio à Programação — Phedi

Você auxilia o desenvolvimento do backend Phedi (Java 21, Spring Boot 4.0.2, Maven multi-módulo: `phedi-core`, `phedi-web`, `phedi-migration`). Ao escrever, refatorar ou revisar código, aplique **estritamente** as convenções abaixo — elas são decisões fixas do autor.

## 1. Formatação de código (obrigatória)

### Declaração de classe em linha única
A declaração da classe **nunca** é quebrada em várias linhas, mesmo com genéricos longos:

```java
// ✅ Correto
public abstract class AbstractPartyBaseRepository<DOMAIN extends PartyBase, ENTITY extends AbstractEntity> implements PartyBaseRepository<DOMAIN> {

// ❌ Errado (não quebrar a declaração)
public abstract class AbstractPartyBaseRepository<
        DOMAIN extends PartyBase,
        ENTITY extends AbstractEntity> implements PartyBaseRepository<DOMAIN> {
```

### Construtor com um parâmetro por linha
Construtores explícitos seguem o padrão: abertura na linha da assinatura e **um parâmetro por linha** (o último fecha com `)`). O corpo faz a atribuição e preserva chamadas `super(...)`:

```java
protected AbstractPartyRepository(
        PartyBaseJpaRepository<ENTITY> jpaRepository,
        PartyPersistenceMapper<DOMAIN, ENTITY> mapper) {
    super(jpaRepository, mapper);
}
```

### Imports organizados
Ordem: (1) `java.*` / `javax.*`, (2) bibliotecas de terceiros (Jakarta, Spring, etc.), (3) imports do projeto (`com.phedi.*`), (4) `lombok.*`. Grupos separados por linha em branco:

```java
import java.util.List;

import org.springframework.stereotype.Repository;

import com.phedi.domain.party.model.Organization;

import lombok.RequiredArgsConstructor;
```

### Só Javadoc que já existia
Não adicione comentários/Javadoc novos em código existente, exceto quando o trecho foi criado do zero e o conteúdo agrega informação não óbvia. Evite ruído de manutenção. Se o usuário pedir "limpeza", remova Javadoc que o próprio agente adicionou.

## 2. Genéricos: `DOMAIN` e `ENTITY`

- **Todos** os type parameters usam exatamente os nomes **`DOMAIN`** (modelo de domínio) e **`ENTITY`** (entidade de persistência).
- **Nunca** usar `D`, `E`, `JPA` ou outras letras únicas como type parameter.
- A hierarquia de repositórios usa **2 genéricos** `<DOMAIN, ENTITY>` (sem 3º genérico de JPA).
- Exemplos canônicos:
  - `AbstractPartyBaseRepository<DOMAIN extends PartyBase, ENTITY extends AbstractEntity>`
  - `PartyBaseJpaRepository<ENTITY extends AbstractEntity>`
  - `PartyPersistenceMapper<DOMAIN, ENTITY>`

## 3. Uso de `@RequiredArgsConstructor`

É a **característica do autor**: usar `@RequiredArgsConstructor` sempre que **possível**.

### Quando usar ✅
Classes que podem ter o construtor gerado pelo Lombok, ou seja, que **não precisam chamar `super(...)`** no construtor e têm todos os campos de injeção `final`:

```java
@RequiredArgsConstructor
public abstract class AbstractPartyBaseRepository<...> implements PartyBaseRepository<DOMAIN> {
    protected final PartyBaseJpaRepository<ENTITY> jpaRepository;
    protected final PersistenceMapper<DOMAIN, ENTITY> mapper;

    @Autowired
    protected PublicIdGenerator publicIdGenerator; // non-final → entra por field injection
}
```

### Quando NÃO usar ❌
Classes cujo construtor **chama `super(...)`** (o Lombok não resolve argumentos para o construtor da superclasse) — essas mantêm construtor explícito:

```java
public abstract class AbstractPartyRepository<DOMAIN extends Party, ENTITY extends PartyEntity>
        extends AbstractPartyBaseRepository<DOMAIN, ENTITY> implements PartyRepository<DOMAIN> {

    protected AbstractPartyRepository(
            PartyBaseJpaRepository<ENTITY> jpaRepository,
            PartyPersistenceMapper<DOMAIN, ENTITY> mapper) {
        super(jpaRepository, mapper);
    }
}
```

### Mapa atual da camada de persistência
| Classe | `@RequiredArgsConstructor`? |
|---|---|
| `AbstractPartyBaseRepository` | ✅ usa (não chama `super`) |
| `AbstractPartyRepository` (raiz) | ❌ construtor explícito (chama `super`) |
| `AbstractPartyItemRepository` (itens) | ❌ construtor explícito (chama `super`) |
| `OrganizationRepositoryImpl` | ❌ construtor explícito (chama `super`) |
| `PartyIdentityDocumentRepositoryImpl` | ❌ construtor explícito (chama `super`) |

- Mappers e JPA repositories são **interfaces** → não se aplica.
- DTOs usam `@AllArgsConstructor` + `@NoArgsConstructor` (necessário para o Jackson) → **não** trocar.
- Se a classe usa `@AllArgsConstructor` num caso de campos não-final onde ele não é necessário para serialização, avaliar a troca para `@RequiredArgsConstructor` (ex.: `IdentificationValidationService` já foi trocada).

## 4. Outras convenções do projeto

- **Nomeação** de repositórios: `Abstract*Repository` = base abstrata; `*RepositoryImpl` = concreto (bean Spring).
- **Injeção**: construtor (via `@RequiredArgsConstructor` quando possível ou construtor explícito). Campos `@Autowired` em bases abstratas são `non-final`.
- **Soft delete**: filtros `...IsDeletedFalse` e valor `is_deleted = false`; auditoria via `AuditorAware<String>` (fallback `"SYSTEM"`).
- **IDs**: `Long` para entidades; `Identifier` (value object) para `public_id` no domínio; geração via `PublicIdGenerator` (sequence `party_public_id_seq`, compartilhada).
- **Entidades** usam `@Data` + `@EqualsAndHashCode(callSuper = true)` quando herdam; itens têm herança JOINED (`party_item` base).
- **Pacotes**:
  - `domain/party/model` + `model/item` → modelos de domínio
  - `domain/party/repository` + `repository/item` → contratos de repositório
  - `application/party` → serviços de aplicação
  - `infrastructure/persistence/party/{entity,mapper,repository,repository/base,repository/item,generator}` → persistência
  - `infrastructure/web/party` → web (controllers/DTOs)
- **Validação** de identificação via `IdentificationValidator` (Strategy Pattern).

## 5. Workflow de código

1. Consulte os arquivos existentes (`base/`, `item/`, `OrganizationRepositoryImpl`) **antes** de criar novos — eles são a referência canônica.
2. Ao criar uma nova raiz (ex.: Person), siga: contrato domínio → `AbstractPartyRepository` subclass → `*RepositoryImpl` → mapper → service → controller.
3. Ao criar um novo item (ex.: contato/endereço), siga: contrato domínio → `AbstractPartyItemRepository` subclass → `*RepositoryImpl` → mapper item.
4. Ao final, valide com:
   ```bash
   cd backend && mvn clean install -DskipTests
   ```
5. Não use Lombok para entidades quando o autor preferir getters/setters explícitos — consulte o padrão atual de cada entidade.