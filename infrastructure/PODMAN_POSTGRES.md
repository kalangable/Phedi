# Comandos Docker para PostgreSQL

## Criar e iniciar container PostgreSQL

```bash
# Criar container PostgreSQL 17 (última versão estável)
docker run -d \
  --name phedi-postgres \
  -e POSTGRES_DB=phedi \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -v phedi-data:/var/lib/postgresql/data \
  postgres:17-alpine

# Verificar se o container está rodando
docker ps

# Ver logs do container
docker logs phedi-postgres

# Parar o container
docker stop phedi-postgres

# Iniciar o container novamente
docker start phedi-postgres

# Remover o container (dados serão preservados no volume)
docker rm phedi-postgres

# Remover o volume (CUIDADO: apaga todos os dados!)
docker volume rm phedi-data

# Conectar ao PostgreSQL via psql
docker exec -it phedi-postgres psql -U postgres -d phedi
```

## Comandos úteis dentro do psql

```sql
-- Listar todas as tabelas
\dt

-- Descrever estrutura de uma tabela
\d party
\d person
\d organization

-- Ver todas as databases
\l

-- Ver todos os schemas
\dn

-- Ver changelog do Liquibase
SELECT * FROM databasechangelog;

-- Sair do psql
\q
```

## Configuração das variáveis de ambiente (.env)

Certifique-se de que o arquivo `.env` está configurado corretamente:

```properties
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=phedi
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_SCHEMA=public
```

## Testar conexão com o banco

```bash
# Via docker exec
docker exec -it phedi-postgres psql -U postgres -d phedi -c "SELECT version();"

# Via cliente local (se tiver psql instalado)
psql -h localhost -p 5432 -U postgres -d phedi -c "SELECT version();"
```

## Executar migrations do Liquibase manualmente

```bash
# A partir do diretório raiz do projeto
cd C:\develop\sources\person-registration-system

# Executar migrations
mvn liquibase:update -pl services/foundation

# Ver status das migrations
mvn liquibase:status -pl services/foundation

# Rollback da última migration
mvn liquibase:rollback -Dliquibase.rollbackCount=1 -pl services/foundation

# Ver SQL que será executado (sem executar)
mvn liquibase:updateSQL -pl services/foundation
```

## Notas importantes

1. **Porta 5432**: Certifique-se de que a porta 5432 não está sendo usada por outro processo
2. **Volume persistente**: Os dados são armazenados no volume `phedi-data` e persistem mesmo após remover o container
3. **Alpine**: Imagem `postgres:17-alpine` é mais leve (menor tamanho)
4. **Credenciais**: Estas são credenciais de desenvolvimento. NÃO use em produção!
5. **Spring Boot**: Ao iniciar a aplicação, o Liquibase executará as migrations automaticamente
