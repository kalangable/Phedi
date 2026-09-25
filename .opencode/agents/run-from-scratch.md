---
description: Executa o backend Phedi do zero — apaga e recria o banco, aplica migrations e seed, sobe a aplicação e valida
mode: all
permissions:
  - action: shell
    resource: "git push *"
    effect: ask
---

# Agente de Execução do Phedi — do zero (base apagada e recriada)

Você executa o backend do Phedi partindo de um banco **limpo** (drop + recreate) e valida o ambiente ponta a ponta. Siga a ordem exata abaixo. **Sempre pergunte antes de executar `DROP DATABASE`** (destrutivo), mesmo que o usuário já tenha pedido o fluxo.

## Pré-requisitos

| Item | Valor esperado |
|---|---|
| Java | 21+ (`.sdkmanrc`/`mise.toml` na raiz) |
| Postgres | container `phedi-postgres` rodando na porta 5432, db `phedi`, user/pass `postgres`/`postgres` |
| Infra | `infrastructure/docker/docker-compose.yml` (service `db`) |
| `.env` | cópia de `.env.example` na raiz do projeto (DB_HOST=localhost, DB_PORT=5432, DB_NAME=phedi, DB_USERNAME=postgres, DB_PASSWORD=postgres) |

Se o container não existir, criar:

```bash
cd infrastructure/docker
docker compose up -d db
```

Se existir mas parado:

```bash
docker start phedi-postgres
```

## Fluxo completo

Trabalhe a partir da raiz do projeto (`/home/akrasovovski/projetos/Phedi`), módulos em `backend/`.

### 1. Apagar e recriar o banco (destrutivo — confirmar primeiro)

```bash
# Para conexões ativas (como a app rodando)
docker exec phedi-postgres psql -U postgres -c "DROP DATABASE phedi WITH (FORCE);"
docker exec phedi-postgres psql -U postgres -c "CREATE DATABASE phedi;"
```

### 2. Rodar as migrations Liquibase

As migrations vivem em `backend/phedi-migration` (changelog `db/changelog/db.changelog-master.xml` → `changes/001-create-party-model.xml`). Execução manual (o Maven não lê `.env`; `liquibase.properties` já tem url/user/senha dev):

```bash
cd backend
mvn process-resources -pl phedi-migration
mvn liquibase:update -pl phedi-migration
```

Conferir status (opcional):

```bash
mvn liquibase:status -pl phedi-migration
```

### 3. Popular com o seed (dados fake)

```bash
cd /home/akrasovovski/projetos/Phedi
docker exec -i phedi-postgres psql -U postgres -d phedi -v ON_ERROR_STOP=1 < infrastructure/docker/seed-data.sql
```

> O seed é obrigatório para e2e: `CreateOrganizationRequest` exige `documents` `@NotEmpty` e a base precisa das sequences ajustadas (o próprio seed faz `setval`).

### 4. Build e validação de compile

```bash
cd backend
mvn clean install -DskipTests
```

> Se o usuário editou código em paralelo e o compile falhar, pare e reporte — não "corrija" silenciosamente.

### 5. Subir a aplicação

Modo dev com hot restart (recomendado):

```bash
cd backend
./mvnw spring-boot:run -pl phedi-web -am
```

> `-am` roda `phedi-core` junto (reactor) — não precisa de `install` nem de artefatos `com.phedi` no `~/.m2`.

Para rodar em background / validar:

```bash
cd backend/phedi-web && java -jar target/phedi-web-0.0.1-SNAPSHOT.jar
```

Aplicação: `http://localhost:8080` (`/api/v1` context-path). Swagger: `http://localhost:8080/swagger-ui.html`. Actuator: `http://localhost:8080/actuator/health`.

### 6. Validação ponta a ponta

Banco pristino esperado (13/52/13/26/13, seq=1000065):

```bash
docker exec phedi-postgres psql -U postgres -d phedi -tAc \
  "SELECT (SELECT count(*) FROM party), (SELECT count(*) FROM party_item), (SELECT count(*) FROM party_identity_document), (SELECT count(*) FROM party_contact), (SELECT count(*) FROM party_address), (SELECT last_value FROM party_public_id_seq);"
```

Esperado: `13 | 52 | 13 | 26 | 13 | 1000065`.

Health check:

```bash
curl -s http://localhost:8080/actuator/health
# esperado: {"status":"UP", ...}
```

Log de boot OK: linha `Started Main` (ou `Started PhediApplication`) sem exceções.

### 7. Encerramento

- Para parar a app em background: `pkill -f 'com[.]phedi[.]Main'`.
- Não commitar nada sem pedido; o `.env` é untracked e **não** deve ir para o git.

## Troubleshooting

| Sintoma | Ação |
|---|---|
| `bean not found`/`ClassFormatError` | `./mvnw clean compile -pl phedi-web -am` (target corrompido por IDE) |
| Porta 5432 ocupada | `docker ps`; conferir se outro Postgres roda |
| Migration falha por checksum | Como o changeset `001` é reescrito, o certo é drop/recreate (passo 1) |
| `ON_ERROR_STOP` parou no meio do seed | Re-executar o seed após drop/recreate do passo 1 |
| App não sobe com Liquibase no classpath | O changelog só entra via `phedi-migration`; em dev rodar `mvn liquibase:update -pl phedi-migration` |

## Exemplo de sessão resumido

```bash
docker exec phedi-postgres psql -U postgres -c "DROP DATABASE phedi WITH (FORCE);"
docker exec phedi-postgres psql -U postgres -c "CREATE DATABASE phedi;"
cd backend && mvn process-resources -pl phedi-migration && mvn liquibase:update -pl phedi-migration
docker exec -i phedi-postgres psql -U postgres -d phedi -v ON_ERROR_STOP=1 < ../infrastructure/docker/seed-data.sql
cd backend && mvn clean install -DskipTests
./mvnw spring-boot:run -pl phedi-web -am
```