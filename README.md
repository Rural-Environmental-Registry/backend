# RER-DPG - Core-Backend

## Sobre o Módulo

O **Core-Backend** é o backend principal do RER-DPG, desenvolvido em Spring Boot com suporte a PostGIS para gerenciamento de cadastros de imóveis, pessoas e atributos relacionados. Fornece uma API REST completa com documentação Swagger para todas as operações do sistema.

**Principais características:**

- 🗺️ Suporte a dados geoespaciais com PostGIS
- ⚡ API REST performática com Spring Boot
- 📚 Documentação completa com Swagger
- 🏠 Gerenciamento de cadastros de imóveis rurais
- 👥 Sistema de gerenciamento de pessoas e atributos
- 🔧 Configurações centralizadas e transparentes
- 🐳 Containerização com Docker

---

## Pré-requisitos

- **Docker** versão 24+ ([instalação](https://docs.docker.com/engine/install/))
- **Docker Compose** versão 2.20 ou superior ([instalação](https://docs.docker.com/compose/install/linux/#install-using-the-repository))
- **Java 21** ([instalação](https://jdk.java.net/21/))
- **Git** ([instalação](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git))

---

## Instalação e Execução

### Execução Integrada

Este módulo é executado automaticamente como parte do sistema RER-DPG principal. Para executar o sistema completo:

1. **No diretório principal do projeto:**
   ```bash
   ./start.sh
   ```

### Desenvolvimento Local

#### Build do Projeto

```bash
./gradlew build
```

#### Build da Imagem Docker

```bash
docker build -t car-registration:0.0.1-SNAPSHOT .
```

**Para CPUs ARM:**

```bash
docker buildx build --platform linux/amd64 -t car-registration:0.0.1-SNAPSHOT .
```

#### Execução Standalone

Para executar apenas o módulo Core-Backend:

```bash
docker compose up
```

Este comando iniciará:

- **car-db:** Banco PostgreSQL com PostGIS
- **car-registration:** Aplicação Spring Boot

---

## Acesso aos Serviços

Após a execução, os serviços estarão disponíveis:

- **API REST:** http://localhost/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>
- **Documentação Swagger:** http://localhost/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>/swagger-ui/index.html

> As variáveis `<BASE_URL>` e `<CORE_BACKEND_API_CONTEXT_PATH>` são definidas nas configurações do ambiente.

---

## Funcionalidades da API

### Endpoints Principais

- **Cadastro de Imóveis:** Gerenciamento completo de propriedades rurais
- **Gerenciamento de Pessoas:** CRUD de pessoas físicas e jurídicas
- **Atributos Customizados:** Sistema flexível de atributos
- **Dados Geoespaciais:** Suporte completo a geometrias com PostGIS
- **Configurações:** Endpoint para visualização de configurações do sistema

### Tecnologias

- Spring Boot 3.x
- Spring Data JPA
- PostGIS/PostgreSQL
- Gradle
- Docker
- Swagger/OpenAPI

---

## Estrutura do Projeto

```
Core-Backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/car/registration/
│   │   │       ├── controller/     # Controllers REST
│   │   │       ├── service/        # Lógica de negócio
│   │   │       ├── repository/     # Acesso a dados
│   │   │       ├── model/          # Entidades JPA
│   │   │       └── config/         # Configurações
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── reports/            # Arquivos do Recibo
│   │       ├── images/             # Imagens disponíveis ao Recibo
│   │       └── db/migration/       # Scripts Flyway
│   ├── test/
│   │   ├── java/                   # Testes unitários
│   │   └── resources/
│   │       └── testdata/           # Dados de teste
│   └── tools/                      # Ferramentas auxiliares
├── gradle/                         # Wrapper do Gradle
├── build.gradle                    # Configuração do build
├── docker-compose.yml              # Orquestração local
└── Dockerfile                      # Imagem Docker
```

---

## Configurações

O sistema oferece visualização centralizada de todas as configurações através do endpoint `/v1/admin/app-info`, incluindo:

- Propriedades da aplicação
- Variáveis de ambiente
- Atributos padrão do sistema
- Configurações de banco de dados

---

## Gerenciamento de Containers

### Verificar Status

```bash
docker compose ps
```

### Parar Serviços

```bash
docker compose down
```

### Logs

```bash
docker compose logs -f car-registration
```

---

## Licença

Este projeto é distribuído sob a [Licença MIT](https://opensource.org/license/mit).

---

## Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

**Desenvolvido pela Superintendência de Inteligência Artificial e Inovação da Dataprev**
