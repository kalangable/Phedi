# Proposta — Base de Entidades, Auditoria e Soft-Delete em Todas as Tabelas (Opção A - JOINED)

> **Status:** AGUARDANDO APROVAÇÃO
> **Data:** 2026-09-24
> **Autor:** Big Pickle (a pedido do akrasovovski)
>
> Este documento descreve as alterações propostas para:
> 1. Adicionar **soft-delete (`is_deleted`)** e **auditoria (datas + usuário responsável)** em **todas** as entidades.
> 2. Criar uma **hierarquia de bases reutilizáveis** para as entidades JPA (ID/publicId → status active → auditoria).
> 3. Implementar a **Opção A** (persistência dos itens com `@Inheritance(JOINED)`, espelhando `party` → `organization`/`person`), criando a tabela base `party_item`.
> 4. Alterar **diretamente** o changelog `001-create-party-model.xml` (sem novo changeset de `ALTER`, pois o banco é recriado do zero).
> 5. Ajustar o **seed-data** (`infrastructure/docker/seed-data.sql`).

---

## 1. Decisões fixas (conforme pedido)

| # | Decisão |
|---|---------|
| D1 | **Todas** as entidades têm soft-delete: coluna `is_deleted BOOLEAN NOT NULL DEFAULT false`. |
| D2 | **Todas** as entidades têm auditoria: `created_at`, `updated_at`, `deleted_at` + `created_by`, `updated_by`, `deleted_by`. |
| D3 | A Migration é **reescrita no changeset 001 existente** — nada de `001-08 ALTER TABLE` adicionando colunas em tabelas existentes. |
| D4 | O seed-data é atualizado com os novos campos e com a nova estrutura de tabelas. |
| D5 | Itens de Party passam a usar **herança JOINED** (Opção A), espelhando `party` → `organization`/`person`. |
| D6 | **Hierarquia de bases flexível**: a base comum é decomposta em 3 camadas extensíveis (identidade → status → auditoria). |

> ⚠️ **Importante:** como os changesets existentes serão *editados* (checksum muda), é obrigatório **dropar e recriar o banco** antes de aplicar — exatamente o fluxo que você já usa.

---

## 2. Hierarquia de bases de entidade (persistência)

Novo pacote: `com.phedi.infrastructure.persistence.entity.base`

```
AbstractPersistentEntity            →  id (Long) + publicId (String)          [camada 1: identidade]
        │
        └── AbstractActivableEntity →  isActive (Boolean) activate/deactivate [camada 2: status]
                │
                └── AbstractAuditableEntity → auditoria + soft-delete          [camada 3: auditoria]

Entidades concretas estendem a camada que precisam (normalmente a 3).
```

### Camada 1 — `AbstractPersistentEntity`

```java
@MappedSuperclass
@Data
public abstract class AbstractPersistentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "public_id", nullable = false, unique = true, length = 36)
    private String publicId;
}
```

### Camada 2 — `AbstractActivableEntity`

```java
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractActivableEntity extends AbstractPersistentEntity {

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public void activate()   { this.isActive = true; }
    public void deactivate() { this.isActive = false; }
}
```

> **Mudança de nome:** as entidades de item hoje usam campo `active` (primitivo). Com a base, passa a ser **`isActive` (Boolean)** — alinhado ao domínio (`PartyBase.isActive`) e ao AGENTS.md. Os mappers de item serão ajustados (hoje mapmeam `active` → `isActive` manualmente; passarão a mapear sozinhos).

### Camada 3 — `AbstractAuditableEntity`

```java
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity extends AbstractActivableEntity {

    @CreatedBy
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "deleted_by", length = 100)
    private String deletedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public void softDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.isDeleted = false;
        this.deletedAt = null;
    }
}
```

> **Benefício:** o `@EntityListeners` passa a viver em **um único lugar** — resolve o bug anterior de `created_at` NULL e elimina a repetição nos 3 itens.

### Por que encadeado e não 3 interfaces?

- Java tem herança simples; encadear `Auditable → Activatable → Persistent` é a forma natural.
- Quem precisar só de ID+publicId estende `AbstractPersistentEntity`; quem precisar de status, estende a camada 2; o padrão de todas as tabelas do projeto usa a camada 3.
- Alternativa (interfaces `Identifiable`/`Activable`/`Auditable` + MappedSuperclass) adiciona complexidade sem ganho neste momento.

> **DECISÃO EM ABERTO:** tamanho das colunas de usuário (`VARCHAR(100)` proposto). Pode ser `VARCHAR(36)` (se for armazenar public_id de usuário) ou outro.

---

## 3. Impacto por entidade (persistência)

### 3.1 `PartyEntity` → estende `AbstractAuditableEntity`

Remove o que foi herdado (`id`, `publicId`, `isActive`, `isDeleted`, `deletedAt`, `createdAt`, `updatedAt`, `activate/deactivate`, `softDelete/restore`, `@EntityListeners`). Fica:

```java
@Entity
@Table(name = "party")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class PartyEntity extends AbstractAuditableEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "party_type", nullable = false, length = 20)
    private PartyType partyType;

    // construtores protegidos
}
```

### 3.2 `OrganizationEntity`, `PersonEntity` → extendem `PartyEntity`

Praticamente inalteradas (só `@EqualsAndHashCode(callSuper = true)` para respeitar a nova base).

### 3.3 NOVA `PartyItemEntity` → estende `AbstractAuditableEntity` (tabela base `party_item`)

```java
@Entity
@Table(name = "party_item")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class PartyItemEntity extends AbstractAuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "party_id", nullable = false)
    private PartyEntity party;

    @Column(name = "is_primary", nullable = false)
    private Boolean primary = false;

    public void markAsPrimary()     { this.primary = true; }
    public void demoteToSecondary() { this.primary = false; }
}
```

> **Nota:** `is_primary` muda de `boolean` (primitivo) para `Boolean`, seguindo a convenção do projeto.

### 3.4 `PartyIdentityDocumentEntity`, `PartyContactEntity`, `PartyAddressEntity`

Passam a **estender `PartyItemEntity`** com `@PrimaryKeyJoinColumn(name = "id")`, ficando apenas com os campos específicos:

```java
@Entity
@Table(name = "party_identity_document")
@PrimaryKeyJoinColumn(name = "id")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PartyIdentityDocumentEntity extends PartyItemEntity {
    // documentType, documentNumber, countryCode, issuerRegion,
    // issuingAuthority, issuedAt, expiresAt
}
```

> **Importante (herança JOINED):** as tabelas filhas mantêm **somente** as colunas específicas. As colunas comuns (`public_id`, `party_id`, `is_primary`, `is_active`, `is_deleted`, auditoria) passam a existir **uma única vez** em `party_item`.

---

## 4. Migration — reescrita do changeset `001-create-party-model.xml`

> Nenhum novo changeset de ALTER. Edição direta dos existentes, com o banco sendo recriado do zero.

### 4.1 `001-01` — tabela `party` (editar)

Colunas adicionadas:
| Coluna | Tipo | Regras |
|--------|------|--------|
| `created_by` | VARCHAR(100) | NULL (preenchido pela auditoria) |
| `updated_by` | VARCHAR(100) | NULL |
| `deleted_by` | VARCHAR(100) | NULL |

Já existentes e mantidas: `is_deleted`, `deleted_at`, `created_at`, `updated_at`.

### 4.2 NOVA parte — tabela base `party_item`

Chaves associadas ao changeset atual (novo bloco no arquivo 001):
| Coluna | Tipo | Regras |
|--------|------|--------|
| `id` | BIGINT | autoIncrement, PK |
| `public_id` | VARCHAR(36) | NOT NULL, UNIQUE |
| `party_id` | BIGINT | NOT NULL, FK → `party(id)` |
| `is_primary` | BOOLEAN | NOT NULL, DEFAULT false |
| `is_active` | BOOLEAN | NOT NULL, DEFAULT true |
| `is_deleted` | BOOLEAN | NOT NULL, DEFAULT false |
| `created_by` | VARCHAR(100) | NULL |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT now() |
| `updated_by` | VARCHAR(100) | NULL |
| `updated_at` | TIMESTAMP | NOT NULL, DEFAULT now() |
| `deleted_by` | VARCHAR(100) | NULL |
| `deleted_at` | TIMESTAMP | NULL |

Índice: `idx_party_item_party (party_id)`.

### 4.3 `001-05` — `party_identity_document` (reescrever)

Passa de "tabela completa" para "tabela filha":
| Coluna | Tipo | Regras |
|--------|------|--------|
| `id` | BIGINT | PK + FK → `party_item(id)` |
| `document_type` | VARCHAR(30) | NOT NULL |
| `document_number` | VARCHAR(100) | NOT NULL |
| `country_code` | VARCHAR(2) | NULL |
| `issuer_region` | VARCHAR(10) | NULL |
| `issuing_authority` | VARCHAR(100) | NULL |
| `issued_at` | DATE | NULL |
| `expires_at` | DATE | NULL |

### 4.4 `001-06` — `party_contact` (reescrever)

| Coluna | Tipo | Regras |
|--------|------|--------|
| `id` | BIGINT | PK + FK → `party_item(id)` |
| `contact_type` | VARCHAR(30) | NOT NULL |
| `purpose` | VARCHAR(20) | NULL |
| `contact_value` | VARCHAR(255) | NOT NULL |
| `country_code` | VARCHAR(2) | NULL |

### 4.5 `001-07` — `party_address` (reescrever)

| Coluna | Tipo | Regras |
|--------|------|--------|
| `id` | BIGINT | PK + FK → `party_item(id)` |
| `address_type` | VARCHAR(20) | NOT NULL |
| `label` | VARCHAR(50) | NULL |
| `street` | VARCHAR(255) | NOT NULL |
| `number` | VARCHAR(20) | NULL |
| `complement` | VARCHAR(100) | NULL |
| `district` | VARCHAR(100) | NULL |
| `city` | VARCHAR(100) | NOT NULL |
| `state_region` | VARCHAR(100) | NULL |
| `postal_code` | VARCHAR(20) | NULL |
| `country_code` | VARCHAR(2) | NOT NULL |

> ⚠️ **ALERTA — constraint `uq_party_contact_value` (produto da herança JOINED):**
> Hoje `party_contact` tem `UNIQUE (party_id, contact_type, contact_value)`. Com o JOINED, `party_id` mora em `party_item` e a constraint **não pode** referenciar a coluna de outra tabela. Opções:
> - **(A) Validação na aplicação** (recomendado para MVP): query `existsByParty_PublicIdAndContactTypeAndContactValueAndIsDeletedFalse` no serviço antes do insert. Sem constraint no banco.
> - **(B) Denormalizar `party_id`** também em `party_contact` só para sustentar a UNIQUE (contraria o objetivo de reduzir duplicação).
> - **(C) Trigger** de validação no banco.
>
> **Recomendação inicial: (A).** Confirmar na aprovação.

---

## 5. Auditoria — quem é o usuário?

Não existe sistema de autenticação ainda no projeto. A auditoria será populada por um bean `AuditorAware<String>`:

```java
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getName)
                .filter(name -> !name.isBlank() && !name.equals("anonymousUser"))
                .or(() -> Optional.of("SYSTEM")); // fallback de desenvolvimento
    }
}
```

- `created_by` / `updated_by` → `@CreatedBy` / `@LastModifiedBy`.
- `deleted_by` / `deleted_at` → preenchidos pelo método `softDelete()` do serviço/impl (a auditoria automática não cobre delete lógico).
- Hoje o fallback será `"SYSTEM"`; quando houver autenticação, o `AuditorAware` é trocado sem tocar nas entidades.

---

## 6. Repositórios JPA e impls de item (Opção A)

### 6.1 Renomear/estender a base JPA de item

- `PartyItemJpaRespository` (com typo) → **`PartyItemJpaRepository<E extends PartyItemEntity>`**, package-private, em `repository/item`:

```java
interface PartyItemJpaRepository<E extends PartyItemEntity> extends JpaRepository<E, Long> {
    Optional<E> findByParty_PublicIdAndPrimaryTrueAndIsDeletedFalse(String partyPublicId);
    Optional<E> findByPublicIdAndIsDeletedFalse(String publicId);
    List<E> findAllByParty_PublicIdAndIsDeletedFalse(String partyPublicId);
}
```

> As derives mudam de `...DeletedAtIsNull` para `...IsDeletedFalse` (novo padrão com `is_deleted`).

### 6.2 Impls de item → base genérica (reutilização)

- `AbsctractPartyItemRepository` (com typo) vira **`AbstractPartyItemRepository`** paramétrico:

```java
@Repository
@RequiredArgsConstructor
public abstract class AbstractPartyItemRepository<
        ITEM extends PartyItem,
        ENTITY extends PartyItemEntity,
        JPA extends PartyItemJpaRepository<ENTITY>,
        M extends PartyItemPersistenceMapper<ITEM, ENTITY>>
        implements PartyItemRepository<ITEM> {

    protected final JPA jpaRepository;
    protected final PartyJpaRepository partyJpaRepository;
    protected final PublicIdGenerator publicIdGenerator;
    protected final M mapper;

    @Override
    public ITEM insertFor(ITEM item, Identifier partyIdentifier) { ... }

    @Override
    public List<ITEM> findAllByParty(Identifier partyIdentifier) { ... }

    @Override
    public Optional<ITEM> findByIdentifier(Identifier identifier) { ... }

    @Override
    public ITEM update(ITEM domain) { ... }

    @Override
    public void deleteByIdentifier(Identifier identifier) { soft delete + deletedBy }

    @Override
    public void activate(Identifier identifier) { ... }

    @Override
    public void deactivate(Identifier identifier) { ... }
}
```

- `PartyIdentityDocumentRepositoryImpl` passa a ser:

```java
@Repository
public class PartyIdentityDocumentRepositoryImpl extends
        AbstractPartyItemRepository<PartyIdentityDocument,
                                    PartyIdentityDocumentEntity,
                                    PartyIdentityDocumentJpaRepository,
                                    PartyIdentityDocumentPersistenceMapper>
        implements PartyIdentityDocumentRepository {

    public PartyIdentityDocumentRepositoryImpl(
            PartyIdentityDocumentJpaRepository jpaRepository,
            PartyJpaRepository partyJpaRepository,
            PublicIdGenerator publicIdGenerator,
            PartyIdentityDocumentPersistenceMapper mapper) {
        super(jpaRepository, partyJpaRepository, publicIdGenerator, mapper);
    }

    @Override
    public void demotePrimary(Identifier partyIdentifier) { ... } // regra do item
}
```

> Ficam eliminados todos os `UnsupportedOperationException` stubs das impls atuais.
> Contatos e endereços ganham o mesmo padrão (JPA repos + mappers + impls) ao serem implementadas.

### 6.3 Mappers de item — base genérica

- Nova `PartyItemPersistenceMapper<DOMAIN extends PartyItem, ENTITY extends PartyItemEntity>` (interface MapStruct) com os mapeamentos comuns:

```java
public interface PartyItemPersistenceMapper<DOMAIN extends PartyItem, ENTITY extends PartyItemEntity>
        extends PersistenceMapper<DOMAIN, ENTITY> {

    @Mapping(target = "publicId", source = "identifier.value")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "party", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    ENTITY toEntity(DOMAIN domain);

    @Mapping(target = "identifier", source = "publicId")
    DOMAIN toDomain(ENTITY entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "party", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    void updateEntity(DOMAIN domain, @MappingTarget ENTITY entity);
}
```

- `PartyIdentityDocumentPersistenceMapper` passa a `extends PartyItemPersistenceMapper<PartyIdentityDocument, PartyIdentityDocumentEntity>` e herda os mapeamentos comuns (remevendo os `@Mapping` de `active`, que agora são automáticos).
- **DECISÃO EM ABERTO:** criar já os mappers de contact/address ou somente quando forem implementados os endpoints.

---

## 7. Seed-data (`infrastructure/docker/seed-data.sql`)

Alterações necessárias:

1. **`INSERT INTO party`**: adicionar colunas `created_by = 'seed'`, `updated_by = 'seed'` (mantendo `is_active`, `is_deleted=false`; `deleted_by`/`deleted_at` NULL).
2. **Itens**: dividir cada INSERT em **dois**:
   - `INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)` → valores comuns.
   - `INSERT INTO party_identity_document (id, document_type, document_number, ...)` ou `party_contact` / `party_address` → apenas colunas específicas (mesmo `id`).
3. **Sequences**:
   - `party_public_id_seq`: o `GREATEST(...)` deve incluir também `party_item`.
   - Novo `SELECT setval('party_item_id_seq', (SELECT MAX(id) FROM party_item));` (a PK agora é gerada na base `party_item`).
   - Remover `setval` de `party_identity_document_id_seq`/`party_contact_id_seq`/`party_address_id_seq` (não há mais identity nessas tabelas).
4. **Exemplo (Pessoa 1 — documento):**

```sql
-- antes: 2 colunas comuns + específicas na mesma tabela
INSERT INTO party_item (id, public_id, party_id, document_type, document_number, country_code,
                        is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (1, '1000053', 1, NULL, NULL, NULL, true, true, false, 'seed', NOW(), 'seed', NOW());
--       ↑ as colunas específicas NÃO existem em party_item →

-- → vão para a tabela filha:
INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (1, 'SSN', '123-45-6789', 'US');
```

> O comentário de alocação de `public_id` no topo do arquivo permanece válido (a sequence continua compartilhada).

---

## 8. O que NÃO muda

- **Domínio** (`PartyBase`, `Party`, `PartyItem`, `PartyIdentityDocument`, `PartyContact`, `PartyAddress`): intocado nesta proposta (os mappers continuam mapeando `identifier`/`isActive`/`primary`).
- Contratos de repositório de domínio: `PartyRepository`, `PartyBaseRepository`, `PartyItemRepository`, `PartyIdentityDocumentRepository` — **sem mudança de assinatura** (a única pendência é alinhar `demotePrimary` para `Identifier`, já feito na interface).
- Estratégia de geração de `public_id` (`party_public_id_seq`, compartilhada).
- `ddl-auto: validate` continua garantindo que entities ⇔ migration estejam alinhadas.

---

## 9. Arquivos afetados (resumo)

### Criar
1. `entity/base/AbstractPersistentEntity.java`
2. `entity/base/AbstractActivableEntity.java`
3. `entity/base/AbstractAuditableEntity.java`
4. `entity/item/PartyItemEntity.java`
5. `mapper/item/PartyItemPersistenceMapper.java` (base genérica)
6. `repository/item/PartyItemJpaRepository.java` (renomeando o atual, corrigindo typo)
7. `repository/item/AbstractPartyItemRepository.java` (renomeando o atual, corrigindo typo)

### Alterar
8. `entity/PartyEntity.java` — estende `AbstractAuditableEntity`
9. `entity/OrganizationEntity.java` / `entity/PersonEntity.java` — `@EqualsAndHashCode(callSuper = true)`
10. `entity/item/PartyIdentityDocumentEntity.java` (+ Contact/Address) — estendem `PartyItemEntity`
11. `mapper/item/PartyIdentityDocumentPersistenceMapper.java` — estende base genérica
12. `repository/item/PartyIdentityDocumentRepositoryImpl.java` — herda `AbstractPartyItemRepository`
13. `config/JpaAuditingConfig.java` — `AuditorAware<String>` bean
14. `phedi-migration/.../changes/001-create-party-model.xml` — reescrita (seções 4.1–4.5)
15. `infrastructure/docker/seed-data.sql` — seção 7

### Não utilizar
- `PartyItemJpaRespository` (typo) e `AbsctractPartyItemRepository` (typo) — substituídos pelos nomes corretos.

---

## 10. Pontos que preciso da sua decisão antes de implementar

1. **Opção (A) JOINED confirmada?** Sim, conforme conversa, mas reapresentada aqui para registro.
2. **Constraint `uq_party_contact_value`:** validação na aplicação (A)? Manter denormalizado (B)? Ou trigger (C)?
3. **Colunas de usuário:** `VARCHAR(100)` ok, ou `VARCHAR(36)` (se armazenará public_id de usuário futuro)?
4. **`is_primary` e `is_active`:** confirmar migração de `boolean`/`active` primitivos para **`Boolean`/`isActive`** nas entidades de item?
5. **Mappers de contact/address:** criar agora (junto) ou ficam para quando houver endpoints?
6. **`deleted_by` no fluxo atual:** como o soft-delete é feito via repo (update), o `deleted_by` será setado na impl genérica com o usuário do `AuditorAware`. OK?

Após aprovação (ou ajustes), a implementação segue nesta ordem:
**1) bases de entidade → 2) PartyEntity/filhos → 3) PartyItemEntity/itens → 4) mappers → 5) repos JPA + impls → 6) AuditorAware → 7) migration 001 → 8) seed-data → 9) build/validate (`mvn clean compile`) + seed aplicado.**