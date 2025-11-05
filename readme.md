# RER - backend

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)](https://spring.io/projects/spring-boot) [![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/) [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue.svg)](https://www.postgresql.org/) [![PostGIS](https://img.shields.io/badge/PostGIS-3+-blue.svg)](https://postgis.net/) [![R2DBC](https://img.shields.io/badge/R2DBC-Reactive-purple.svg)](https://r2dbc.io/) [![Flyway](https://img.shields.io/badge/Flyway-10+-red.svg)](https://flywaydb.org/) [![JasperReports](https://img.shields.io/badge/JasperReports-6.20-orange.svg)](https://community.jaspersoft.com/) [![Docker](https://img.shields.io/badge/Docker-24+-blue.svg)](https://www.docker.com/)

## 📑 Table of Contents

- [RER - backend](#rer---backend)
  - [📑 Table of Contents](#-table-of-contents)
  - [About the Module](#about-the-module)
  - [Key Features](#key-features)
  - [Prerequisites](#prerequisites)
  - [Installation and Execution](#installation-and-execution)
    - [Integrated Execution](#integrated-execution)
    - [Standalone Execution](#standalone-execution)
    - [Local Development](#local-development)
  - [Service Access](#service-access)
  - [Technologies](#technologies)
  - [Project Structure](#project-structure)
  - [License](#license)
  - [Contribution](#contribution)
  - [Support](#support)

---

## About the Module

The **backend** is the main API of the RER system, developed in Spring Boot with PostGIS support. It manages the complete lifecycle of rural property registration, including people, documents, geometries, and business rules.

---

## Key Features

- 🌍 **Universal Registration System** - Property registration adaptable to multiple legislations and countries
- 🗺️ **Geospatial Data Model** - PostGIS with support for any coordinate system (SIRGAS2000, NAD83, etc.)
- 🔧 **Dynamic Attributes** - Extensible system of custom fields without code changes
- 📄 **Document Generation** - Automatic PDF reports via JasperReports
- 👥 **SHIP Model** - Intelligent management of relationships between people and properties
- 🔍 **Advanced Queries** - Search by any criteria (name, CPF/CNPJ, location)
- 🔐 **Unique Identification** - Encrypted and immutable tracking code
- 🔗 **Interoperability** - REST APIs for integration with government systems
- 📚 **Swagger Documentation** - Fully documented and testable API

---

## Prerequisites

- **Docker** version 24+ ([installation](https://docs.docker.com/engine/install/))
- **Docker Compose** version 2.20+ ([installation](https://docs.docker.com/compose/install/linux/#install-using-the-repository))
- **Java 21** (for local development) ([installation](https://openjdk.org/install/))
- **Gradle 8.12+** (for local development)
- **Git** ([installation](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git))

---

## Installation and Execution

### Integrated Execution

This module runs automatically as part of the main RER system:

```bash
cd /path/to/rer
./start.sh
```

### Standalone Execution

To run only the backend:

```bash
cd backend
docker-compose up -d
```

### Local Development

```bash
# Build
./gradlew clean build

# Run
./gradlew bootRun
```

---

## Service Access

After execution, services will be available at:

- **REST API:** `http://localhost/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>`
- **Swagger UI:** `http://localhost/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>/swagger-ui/index.html`
- **OpenAPI JSON:** `http://localhost/<BASE_URL>/<CORE_BACKEND_API_CONTEXT_PATH>/v3/api-docs`

> Variables `<BASE_URL>` and `<CORE_BACKEND_API_CONTEXT_PATH>` are defined in environment [configurations](../config/Main/environment/.env.example).

---

## Technologies

| Layer | Technology | Version | Description |
|-------|------------|---------|-------------|
| Framework | Spring Boot | 3.4.3 | Main framework |
| Language | Java | 21 | Programming language |
| Database | PostgreSQL | 15+ | Relational database |
| Spatial Extension | PostGIS | 3+ | Geospatial data |
| Connectivity | Spring Data R2DBC | 3.4.3 | Reactive database access |
| Migration | Flyway | 10+ | Schema versioning |
| Reports | JasperReports | 6.20+ | PDF generation |
| Documentation | SpringDoc OpenAPI | 2.3+ | Swagger/OpenAPI |
| Build | Gradle | 8.12+ | Build automation |
| Containers | Docker | 24+ | Containerization |

---

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── br/car/registration/
│   │   │       ├── controller/     # REST Controllers
│   │   │       ├── service/        # Business logic
│   │   │       ├── repository/     # Data access
│   │   │       ├── entity/         # JPA/R2DBC entities
│   │   │       └── config/         # Configurations
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── reports/            # JasperReports templates
│   │       └── db/migration/       # Flyway scripts
│   └── test/
│       └── java/                   # Unit and integration tests
├── gradle/
├── build.gradle
├── docker-compose.yml
└── Dockerfile
```

---

## License

This project is distributed under the [GPL-3.0](https://github.com/Rural-Environmental-Registry/core/blob/main/LICENSE).

---

## Contribution

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

By submitting a pull request or patch, you affirm that you are the author of the code and that you agree to license your contribution under the terms of the GNU General Public License v3.0 (or later) for this project. You also agree to assign the copyright of your contribution to the Ministry of Management and Innovation in Public Services (MGI), the owner of this project.

---

## Support

For technical support or project-related questions:

- **Documentation:** Check the individual READMEs for each submodule
- **Issues:** Report problems via the GitHub issue tracker
 
---

Copyright (C) 2024-2025 Ministry of Management and Innovation in Public Services (MGI), Government of Brazil.

This program was developed by Dataprev as part of a contract with the Ministry of Management and Innovation in Public Services (MGI).
