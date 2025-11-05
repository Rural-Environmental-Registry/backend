# RER - Backend

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot) [![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/) [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg)](https://www.postgresql.org/) [![PostGIS](https://img.shields.io/badge/PostGIS-3+-blue.svg)](https://postgis.net/) [![R2DBC](https://img.shields.io/badge/R2DBC-Reactive-purple.svg)](https://r2dbc.io/) [![Flyway](https://img.shields.io/badge/Flyway-10+-red.svg)](https://flywaydb.org/) [![JasperReports](https://img.shields.io/badge/JasperReports-6.20-orange.svg)](https://community.jaspersoft.com/) [![Docker](https://img.shields.io/badge/Docker-24+-blue.svg)](https://www.docker.com/)

## 📑 Índice

- [RER - Backend](#rer---backend)
  - [📑 Índice](#-índice)
  - [Sobre o Módulo](#sobre-o-módulo)
  - [Principais Características](#principais-características)
  - [Pré-requisitos](#pré-requisitos)
  - [Instalação e Execução](#instalação-e-execução)
    - [Execução Integrada](#execução-integrada)
    - [Execução Standalone](#execução-standalone)
    - [Desenvolvimento Local](#desenvolvimento-local)
  - [Acesso aos Serviços](#acesso-aos-serviços)
  - [Tecnologias](#tecnologias)
  - [Estrutura do Projeto](#estrutura-do-projeto)
  - [Licença](#licença)
  - [Contribuição](#contribuição)
  - [Suporte](#suporte)

---

## Sobre o Módulo

O **backend** é a API principal do sistema RER, desenvolvida em Spring Boot com suporte a PostGIS. Gerencia o ciclo de vida completo do cadastro de propriedades rurais, incluindo pessoas, documentos, geometrias e regras de negócio.

---

## Principais Características

- 🌍 **Sistema Universal de Cadastro** - Registro de propriedades adaptável a múltiplas legislações e países
- 🗺️ **Modelo de Dados Geoespacial** - PostGIS com suporte a qualquer sistema de coordenadas (SIRGAS2000, NAD83, etc.)
- 🔧 **Atributos Dinâmicos** - Sistema extensível de campos customizados sem alteração de código
- 📄 **Geração de Documentos** - Relatórios PDF automáticos via JasperReports
- 👥 **Modelo SHIP** - Gestão inteligente de relacionamentos entre pessoas e propriedades
- 🔍 **Consultas Avançadas** - Busca por qualquer critério (nome, CPF/CNPJ, localização)
- 🔐 **Identificação Única** - Código de rastreamento criptografado e imutável
- 🔗 **Interoperabilidade** - APIs REST para integração com sistemas governamentais
- 📚 **Documentação Swagger** - API totalmente documentada e testável

---

## Pré-requisitos

- **Docker** versão 24+ ([instalação](https://docs.docker.com/engine/install/))
- **Docker Compose** versão 2.20+ ([instalação](https://docs.docker.com/compose/install/linux/#install-using-the-repository))
- **Java 21** (para desenvolvimento local) ([instalação](https://openjdk.org/install/))
- **Gradle 8.12+** (para desenvolvimento local)
- **Git** ([instalação](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git))

---

## Instalação e Execução

### Execução Integrada

Este módulo é executado automaticamente como parte do sistema RER principal:

```bash
cd /caminho/para/rer
./start.sh
```

### Execução Standalone

Para executar apenas o backend:

```bash
cd backend
docker-compose up -d
```

### Desenvolvimento Local

```bash
# Build
./gradlew clean build

# Executar
./gradlew bootRun
```

---

## Acesso aos Serviços

Após a execução, os serviços estarão disponíveis em:

- **API REST:** `http://localhost/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>`
- **Swagger UI:** `http://localhost/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>/swagger-ui/index.html`
- **OpenAPI JSON:** `http://localhost/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>/v3/api-docs`

> As variáveis `<BASE_URL>` e `<CORE_BACKEND_API_CONTEXT_PATH>` são definidas nas [configurações](../config/Main/environment/.env.example) do ambiente.

---

## Tecnologias

| Camada | Tecnologia | Versão | Descrição |
|--------|------------|--------|-----------|
| Framework | Spring Boot | 3.4.3 | Framework principal |
| Linguagem | Java | 21 | Linguagem de programação |
| Banco de Dados | PostgreSQL | 15+ | Banco relacional |
| Extensão Espacial | PostGIS | 3+ | Dados geoespaciais |
| Conectividade | Spring Data R2DBC | 3.4.3 | Acesso reativo ao banco |
| Migração | Flyway | 10+ | Versionamento de schema |
| Relatórios | JasperReports | 6.20+ | Geração de PDFs |
| Documentação | SpringDoc OpenAPI | 2.3+ | Swagger/OpenAPI |
| Build | Gradle | 8.12+ | Automação de build |
| Containers | Docker | 24+ | Containerização |

---

## Estrutura do Projeto

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/car/registration/
│   │   │       ├── controller/     # Controllers REST
│   │   │       ├── service/        # Lógica de negócio
│   │   │       ├── repository/     # Acesso a dados
│   │   │       ├── entity/         # Entidades JPA/R2DBC
│   │   │       └── config/         # Configurações
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── reports/            # Templates JasperReports
│   │       └── db/migration/       # Scripts Flyway
│   └── test/
│       └── java/                   # Testes unitários e integração
├── gradle/
├── build.gradle
├── docker-compose.yml
└── Dockerfile
```

---

## Licença

Este projeto é distribuído sob a [GPL-3.0](https://github.com/Rural-Environmental-Registry/core/blob/main/LICENSE).

---

## Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

Ao submeter um pull request ou patch, você afirma que é o autor do código e que concorda em licenciar sua contribuição sob os termos da Licença Pública Geral GNU v3.0 (ou posterior) deste projeto. Você também concorda em ceder os direitos autorais da sua contribuição ao Ministério da Gestão e Inovação em Serviços Públicos (MGI), titular deste projeto.

---

## Suporte

Para suporte técnico ou dúvidas sobre o projeto:

- **Documentação:** Consulte os READMEs individuais de cada submódulo
- **Issues:** Reporte problemas através do sistema de issues do repositório
 
---

Copyright (C) 2024-2025 Ministério da Gestão e Inovação em Serviços Públicos (MGI), Governo do Brasil.

Este programa foi desenvolvido pela Dataprev como parte de um contrato com o Ministério da Gestão e Inovação em Serviços Públicos (MGI).
