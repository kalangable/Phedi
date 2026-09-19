# Phedi

Sistema global de cadastro de pessoas jurídicas e físicas baseado no padrão **Party Model** (inspirado em SAP, Oracle e Salesforce).

- **Java 21 · Spring Boot 4.0.2 · PostgreSQL 16 · Liquibase**
- Arquitetura limpa multi-módulos Maven
- API: `http://localhost:8080/api/v1`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Arquitetura

Módulos em `backend/`:

- **`phedi-core`** — domínio, aplicação de serviços e infraestrutura de persistência
  (entidades, repositórios, mappers MapStruct, validação).
- **`phedi-web`** — camada web (controllers, DTOs, mappers) e classe principal `com.phedi.Main`.
- **`phedi-migration`** — changesets Liquibase.

## Executando em Dev (recomendado) — modo reactor

Requere apenas Java 21+ e o PostgreSQL rodando na porta 5432 (container `phedi-postgres`, db `phedi`).

```bash
cd backend
./mvnw spring-boot:run -pl phedi-web -am
```

> **O que o `-am` faz:** `-am` (also-make) inclui o `phedi-core` no mesmo reactor do `spring-boot:run`. A dependência `com.phedi:phedi-core` é resolvida direto de `phedi-core/target/classes` — **não é necessário `./mvnw install`** nem artefatos `com.phedi` no `~/.m2` (pode até apagar `~/.m2/repository/com/phedi`).

Como os módulos são compilados e empacotados separadamente, a execução via reactor é o único fluxo dev que dispensa a instalação das "libs" internas.

## Hot Restart (Spring Boot DevTools)

O DevTools (`spring-boot-devtools`, runtime/optional em `phedi-web`) monitora os `target/classes` de **`phedi-web` e `phedi-core`** e reinicia o contexto automaticamente:

1. Edite código (core ou web) — controllers, services, DTOs, resources.
2. Compile:
   ```bash
   ./mvnw compile -pl phedi-web -am
   ```
   (ou use o build da IDE — o restart também dispara)
3. O DevTools aguarda a quietude e reinicia uma única vez (~3–4s depois), **sem derrubar a JVM**. Qualquer mudança em `phedi-core` também é aplicada.

### Ajuste de timing

Em `backend/phedi-web/src/main/resources/application-dev.yaml`:

```yaml
spring:
  devtools:
    restart:
      quiet-period: 3s
      poll-interval: 4s
```

- `quiet-period`: garante que o restart **nunca ocorra no meio** do build do Maven (que apaga e reescreve classes em lote).
- Regra rígida do DevTools: **`poll-interval` deve ser maior que `quiet-period`**.

## Produção — o fluxo dev não vaza

- O `spring-boot-maven-plugin` exclui o DevTools do JAR final automaticamente (`DEVTOOLS_EXCLUDE_FILTER`).
- A configuração de restart vive só em `application-dev.yaml` (profile `dev`); em outros profiles é ignorada.
- Mesmo que presente, o DevTools se auto-desabilita em app empacotada (`java -jar`).
- No `mvn package`, o `phedi-core` entra normalmente como dependência em `BOOT-INF/lib`.

## Comandos úteis

```bash
cd backend

# Executar em dev (hot restart ativo)
./mvnw spring-boot:run -pl phedi-web -am

# Compilar (pós-edição, dispara o restart do DevTools)
./mvnw compile -pl phedi-web -am

# Empacotar
./mvnw clean install -DskipTests

# Testes
./mvnw test -pl phedi-web -am

# Migrations Liquibase (direto na base de dev)
./mvnw liquibase:update -pl phedi-migration
```

## Troubleshooting

- **`ClassFormatError`, `bean not found` ou classes "duplicadas" estranhas**: sinal de `target/classes` corrompido (compilador incremental da IDE em estado de erro). Reset:
  ```bash
  ./mvnw clean compile -pl phedi-web -am
  ```
  Sempre compile pelo Maven (as classes geradas por IDE em erro podem contaminar o output).
- **Parar a app rodando em background**:
  ```bash
  pkill -f 'com[.]phedi[.]Main'
  ```