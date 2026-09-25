-- =====================================================================
-- Script para popular tabelas com dados FAKE
-- =====================================================================
-- IMPORTANTE: Todos os dados aqui são fictícios e gerados aleatoriamente
-- Não representam pessoas ou empresas reais
-- =====================================================================
--
-- Estrutura (FASE 1, com herança JOINED):
--   party                  -> id, public_id, party_type, is_active, auditoria, ...
--   person / organization  -> dados específicos (SEM identificação)
--   party_item             -> base dos itens: id, public_id, party_id,
--                              is_primary, is_active, auditoria, soft-delete
--   party_identity_document-> CPF, CNPJ, SSN, EIN, ... (só colunas específicas)
--   party_contact          -> EMAIL, PHONE, MOBILE, ... (só colunas específicas)
--   party_address          -> RESIDENTIAL, COMMERCIAL, ... (só colunas específicas)
--
-- public_id: parties, documentos, contatos e endereços consomem a MESMA
-- sequence party_public_id_seq. Os valores abaixo estão explícitos na
-- ordem em que seriam gerados pela aplicação, portanto são contíguos.
-- =====================================================================
-- ALOCAÇÃO DE public_id (sequence compartilhada):
--   parties     1..13  -> '1000001' .. '1000013'  (tabela party)
--   docs        1..13  -> '1000053' .. '1000065'  (party_item, ids 1..13)
--   contacts    1..26  -> '1000014' .. '1000039'  (party_item, ids 14..39)
--   addresses   1..13  -> '1000040' .. '1000052'  (party_item, ids 40..52)
--
-- created_by/updated_by = 'seed' (o AuditorAware ainda não existe nas
-- escritas manuais do seed; a aplicação usará "SYSTEM" por padrão).
-- =====================================================================

-- =====================================================================
-- PESSOAS FÍSICAS (6 registros)
-- =====================================================================
-- Cada pessoa possui:
--   1 registro em party (base)
--   1 registro em person (dados específicos)
--   1 registro em party_item + party_identity_document (CPF/SSN principal)
--   2 registros em party_item + party_contact (EMAIL + PHONE/MOBILE)
--   1 registro em party_item + party_address (residencial)

-- Pessoa 1: John Smith (USA - SSN)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (1, '1000001', 'PERSON', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO person (id, first_name, middle_name, last_name, full_name, date_of_birth, gender)
VALUES (1, 'John', 'Michael', 'Smith', 'John Michael Smith', '1985-03-15', 'MALE');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (1, '1000053', 1, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (1, 'SSN', '123-45-6789', 'US');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (14, '1000014', 1, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (15, '1000015', 1, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (14, 'EMAIL', 'PERSONAL', 'john.smith@email.com', NULL),
  (15, 'PHONE', 'PERSONAL', '+1-555-0101',          'US');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (40, '1000040', 1, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (40, 'RESIDENTIAL', 'Home', '123 Main Street', '45', 'Apt 12', 'Downtown', 'Springfield', 'IL', '62701', 'US');

-- Pessoa 2: Maria Silva (Brasil - CPF)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (2, '1000002', 'PERSON', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO person (id, first_name, middle_name, last_name, full_name, date_of_birth, gender)
VALUES (2, 'Maria', 'Aparecida', 'Silva', 'Maria Aparecida Silva', '1990-07-22', 'FEMALE');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (2, '1000054', 2, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (2, 'CPF', '123.456.789-01', 'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (16, '1000016', 2, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (17, '1000017', 2, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (16, 'EMAIL',  'PERSONAL', 'maria.silva@email.com',   NULL),
  (17, 'MOBILE', 'PERSONAL', '+55-11-98765-4321',       'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (41, '1000041', 2, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (41, 'RESIDENTIAL', 'Casa', 'Rua das Flores', '1234', 'Apto 88', 'Bela Vista', 'São Paulo', 'SP', '01310-100', 'BR');

-- Pessoa 3: Carlos Santos (Brasil - CPF)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (3, '1000003', 'PERSON', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO person (id, first_name, middle_name, last_name, full_name, date_of_birth, gender)
VALUES (3, 'Carlos', 'Eduardo', 'Santos', 'Carlos Eduardo Santos', '1988-11-30', 'MALE');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (3, '1000055', 3, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (3, 'CPF', '987.654.321-09', 'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (18, '1000018', 3, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (19, '1000019', 3, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (18, 'EMAIL',  'PERSONAL', 'carlos.santos@email.com', NULL),
  (19, 'MOBILE', 'PERSONAL', '+55-21-91234-5678',       'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (42, '1000042', 3, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (42, 'RESIDENTIAL', 'Casa', 'Av. Atlântica', '500', 'Bloco B', 'Copacabana', 'Rio de Janeiro', 'RJ', '22021-001', 'BR');

-- Pessoa 4: Sarah Johnson (USA - SSN)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (4, '1000004', 'PERSON', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO person (id, first_name, middle_name, last_name, full_name, date_of_birth, gender)
VALUES (4, 'Sarah', 'Anne', 'Johnson', 'Sarah Anne Johnson', '1992-05-18', 'FEMALE');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (4, '1000056', 4, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (4, 'SSN', '987-65-4321', 'US');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (20, '1000020', 4, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (21, '1000021', 4, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (20, 'EMAIL', 'PERSONAL', 'sarah.johnson@email.com', NULL),
  (21, 'PHONE', 'PERSONAL', '+1-555-0202',             'US');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (43, '1000043', 4, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (43, 'RESIDENTIAL', 'Home', '456 Oak Avenue', '120', NULL, 'Lakeside', 'Austin', 'TX', '73301', 'US');

-- Pessoa 5: Ana Oliveira (Brasil - CPF)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (5, '1000005', 'PERSON', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO person (id, first_name, middle_name, last_name, full_name, date_of_birth, gender)
VALUES (5, 'Ana', 'Paula', 'Oliveira', 'Ana Paula Oliveira', '1995-09-08', 'FEMALE');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (5, '1000057', 5, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (5, 'CPF', '456.789.123-45', 'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (22, '1000022', 5, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (23, '1000023', 5, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (22, 'EMAIL',  'PERSONAL', 'ana.oliveira@email.com', NULL),
  (23, 'MOBILE', 'PERSONAL', '+55-31-99876-5432',      'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (44, '1000044', 5, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (44, 'RESIDENTIAL', 'Casa', 'Rua dos Pinheiros', '90', 'Casa 2', 'Savassi', 'Belo Horizonte', 'MG', '30140-100', 'BR');

-- Pessoa 6: Robert Williams (USA - SSN)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (6, '1000006', 'PERSON', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO person (id, first_name, middle_name, last_name, full_name, date_of_birth, gender)
VALUES (6, 'Robert', 'James', 'Williams', 'Robert James Williams', '1983-12-25', 'MALE');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (6, '1000058', 6, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (6, 'SSN', '555-12-3456', 'US');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (24, '1000024', 6, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (25, '1000025', 6, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (24, 'EMAIL', 'PERSONAL', 'robert.williams@email.com', NULL),
  (25, 'PHONE', 'PERSONAL', '+1-555-0303',               'US');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (45, '1000045', 6, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (45, 'RESIDENTIAL', 'Home', '789 Maple Street', '33', NULL, 'Greenville', 'Seattle', 'WA', '98101', 'US');


-- =====================================================================
-- ORGANIZAÇÕES / EMPRESAS (7 registros)
-- =====================================================================
-- Cada empresa possui:
--   1 registro em party (base)
--   1 registro em organization (dados específicos)
--   1 registro em party_item + party_identity_document (CNPJ/EIN principal)
--   2 registros em party_item + party_contact (EMAIL + PHONE comerciais)
--   1 registro em party_item + party_address (comercial)

-- Empresa 1: Tech Solutions Brasil (CNPJ)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (7, '1000007', 'ORGANIZATION', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO organization (id, legal_name, trade_name, brand_name, founding_date)
VALUES (7, 'Tech Solutions Brasil Ltda', 'Tech Solutions', 'TechSol', '2015-06-10');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (7, '1000059', 7, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (7, 'CNPJ', '12.345.678/0001-90', 'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (26, '1000026', 7, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (27, '1000027', 7, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (26, 'EMAIL', 'COMMERCIAL', 'contato@techsolutions.com.br', NULL),
  (27, 'PHONE', 'COMMERCIAL', '+55-11-3000-1000',             'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (46, '1000046', 7, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (46, 'COMMERCIAL', 'Sede', 'Av. Paulista', '1000', '12º andar', 'Bela Vista', 'São Paulo', 'SP', '01310-100', 'BR');

-- Empresa 2: Innovation Corp (USA - EIN)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (8, '1000008', 'ORGANIZATION', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO organization (id, legal_name, trade_name, brand_name, founding_date)
VALUES (8, 'Innovation Corporation Inc', 'Innovation Corp', 'InnovCorp', '2010-03-22');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (8, '1000060', 8, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (8, 'EIN', '12-3456789', 'US');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (28, '1000028', 8, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (29, '1000029', 8, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (28, 'EMAIL', 'COMMERCIAL', 'info@innovationcorp.com',  NULL),
  (29, 'PHONE', 'COMMERCIAL', '+1-555-1000',               'US');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (47, '1000047', 8, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (47, 'COMMERCIAL', 'Headquarters', '100 Innovation Drive', '100', 'Floor 5', 'Silicon Valley', 'San Jose', 'CA', '95134', 'US');

-- Empresa 3: Comercio Digital Ltda (CNPJ)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (9, '1000009', 'ORGANIZATION', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO organization (id, legal_name, trade_name, brand_name, founding_date)
VALUES (9, 'Comércio Digital Ltda', 'Comércio Digital', 'DigiShop', '2018-01-15');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (9, '1000061', 9, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (9, 'CNPJ', '98.765.432/0001-10', 'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (30, '1000030', 9, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (31, '1000031', 9, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (30, 'EMAIL', 'COMMERCIAL', 'vendas@comerciodigital.com.br', NULL),
  (31, 'PHONE', 'COMMERCIAL', '+55-21-2000-3000',              'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (48, '1000048', 9, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (48, 'COMMERCIAL', 'Centro de Distribuição', 'Rua dos Andradas', '150', 'Galpão 2', 'Centro', 'Rio de Janeiro', 'RJ', '20010-040', 'BR');

-- Empresa 4: Global Services Inc (USA - EIN)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (10, '1000010', 'ORGANIZATION', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO organization (id, legal_name, trade_name, brand_name, founding_date)
VALUES (10, 'Global Services International Inc', 'Global Services', 'GlobalServ', '2005-11-08');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (10, '1000062', 10, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (10, 'EIN', '98-7654321', 'US');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (32, '1000032', 10, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (33, '1000033', 10, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (32, 'EMAIL', 'COMMERCIAL', 'contact@globalservices.com',  NULL),
  (33, 'PHONE', 'COMMERCIAL', '+1-555-2000',                 'US');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (49, '1000049', 10, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (49, 'COMMERCIAL', 'Office', '212 Fifth Avenue', '212', 'Suite 800', 'Midtown', 'New York', 'NY', '10010', 'US');

-- Empresa 5: Logistica Express Ltda (CNPJ)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (11, '1000011', 'ORGANIZATION', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO organization (id, legal_name, trade_name, brand_name, founding_date)
VALUES (11, 'Logística Express Brasil Ltda', 'Logística Express', 'LogExpress', '2012-04-20');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (11, '1000063', 11, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (11, 'CNPJ', '45.678.901/0001-23', 'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (34, '1000034', 11, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (35, '1000035', 11, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (34, 'EMAIL', 'COMMERCIAL', 'sac@logisticaexpress.com.br', NULL),
  (35, 'PHONE', 'COMMERCIAL', '+55-31-4000-5000',            'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (50, '1000050', 11, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (50, 'COMMERCIAL', 'Matriz', 'Rodovia BR-381', '4500', 'Km 12', 'Contagem', 'Belo Horizonte', 'MG', '32210-010', 'BR');

-- Empresa 6: Alimentos Naturais Ltda (CNPJ)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (12, '1000012', 'ORGANIZATION', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO organization (id, legal_name, trade_name, brand_name, founding_date)
VALUES (12, 'Alimentos Naturais do Brasil Ltda', 'Alimentos Naturais', 'NaturalFood', '2019-08-05');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (12, '1000064', 12, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (12, 'CNPJ', '78.901.234/0001-56', 'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (36, '1000036', 12, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (37, '1000037', 12, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (36, 'EMAIL', 'COMMERCIAL', 'contato@alimentosnaturais.com.br', NULL),
  (37, 'PHONE', 'COMMERCIAL', '+55-41-5000-6000',                 'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (51, '1000051', 12, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (51, 'COMMERCIAL', 'Fábrica', 'Estrada das Araucárias', '2800', NULL, 'Cidade Industrial', 'Curitiba', 'PR', '81450-000', 'BR');

-- Empresa 7: Consultoria Empresarial Ltda (CNPJ)
INSERT INTO party (id, public_id, party_type, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (13, '1000013', 'ORGANIZATION', true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO organization (id, legal_name, trade_name, brand_name, founding_date)
VALUES (13, 'Consultoria Empresarial Brasil Ltda', 'CE Brasil', 'CEBrasil', '2016-02-28');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (13, '1000065', 13, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_identity_document (id, document_type, document_number, country_code)
VALUES (13, 'CNPJ', '34.567.890/0001-78', 'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES
  (38, '1000038', 13, true,  true, false, 'seed', NOW(), 'seed', NOW()),
  (39, '1000039', 13, false, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_contact (id, contact_type, purpose, contact_value, country_code)
VALUES
  (38, 'EMAIL', 'COMMERCIAL', 'info@consultoriaempresarial.com.br', NULL),
  (39, 'PHONE', 'COMMERCIAL', '+55-51-6000-7000',                   'BR');

INSERT INTO party_item (id, public_id, party_id, is_primary, is_active, is_deleted, created_by, created_at, updated_by, updated_at)
VALUES (52, '1000052', 13, true, true, false, 'seed', NOW(), 'seed', NOW());

INSERT INTO party_address (id, address_type, label, street, number, complement, district, city, state_region, postal_code, country_code)
VALUES (52, 'COMMERCIAL', 'Escritório', 'Rua dos Andradas', '1675', '14º andar', 'Centro Histórico', 'Porto Alegre', 'RS', '90020-007', 'BR');


-- =====================================================================
-- Ajustar sequences para próximo valor disponível
-- =====================================================================

-- public_id é gerado por uma ÚNICA sequence compartilhada
-- (party_public_id_seq) por party e party_item (que carrega os
-- public_id de documentos, contatos e endereços)
SELECT setval('party_public_id_seq',
    GREATEST(
        (SELECT MAX(CAST(public_id AS BIGINT)) FROM party),
        (SELECT MAX(CAST(public_id AS BIGINT)) FROM party_item)
    ));

-- ids técnicos (auto increment) das tabelas com inserts explícitos
SELECT setval('party_id_seq', (SELECT MAX(id) FROM party));
SELECT setval('party_item_id_seq', (SELECT MAX(id) FROM party_item));


-- =====================================================================
-- Verificar dados inseridos
-- =====================================================================
-- SELECT COUNT(*) as total_parties FROM party;
-- SELECT COUNT(*) as total_persons FROM person;
-- SELECT COUNT(*) as total_organizations FROM organization;
-- SELECT COUNT(*) as total_items FROM party_item;
-- SELECT COUNT(*) as total_documents FROM party_identity_document;
-- SELECT COUNT(*) as total_contacts FROM party_contact;
-- SELECT COUNT(*) as total_addresses FROM party_address;
--
-- -- Pessoas com seus dados
-- SELECT p.public_id, p.party_type, per.full_name, per.date_of_birth
-- FROM party p
-- JOIN person per ON p.id = per.id
-- ORDER BY p.id;
--
-- -- Organizações com seus dados
-- SELECT p.public_id, p.party_type, org.legal_name, org.trade_name
-- FROM party p
-- JOIN organization org ON p.id = org.id
-- ORDER BY p.id;
--
-- -- Documentos por party
-- SELECT p.public_id AS party_public_id, d.public_id AS document_public_id,
--        d.document_type, d.document_number, d.country_code
-- FROM party_identity_document d
-- JOIN party_item pi ON pi.id = d.id
-- JOIN party p ON p.id = pi.party_id
-- ORDER BY d.id;
--
-- -- Contatos por party
-- SELECT p.public_id AS party_public_id, c.public_id AS contact_public_id,
--        c.contact_type, c.purpose, c.contact_value
-- FROM party_contact c
-- JOIN party_item pi ON pi.id = c.id
-- JOIN party p ON p.id = pi.party_id
-- ORDER BY c.id;
--
-- -- Endereços por party
-- SELECT p.public_id AS party_public_id, a.public_id AS address_public_id,
--        a.address_type, a.street, a.city, a.country_code
-- FROM party_address a
-- JOIN party_item pi ON pi.id = a.id
-- JOIN party p ON p.id = pi.party_id
-- ORDER BY a.id;