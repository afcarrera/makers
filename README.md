# Loans Project

Este proyecto está desarrollado con **Spring Boot** utilizando **CQRS** y arquitectura reactiva. Incluye:  

- **Spring Security** para autenticación y autorización.  
- **Spring WebFlux** para APIs reactivas para lectura. 
- **Spring Web** para APIs consistentes para escritura.  
- **Spring Data JPA** y **R2DBC** para persistencia de datos.  
- **Keycloak** para gestión de usuarios y roles.  

El proyecto está dividido en dos módulos principales:  

1. **Loans Read** – servicios de lectura de préstamos.  
2. **Loans Write** – servicios de escritura y actualización de préstamos.  

---

## Configuración previa

### 1. Keycloak

1. Levanta un servidor Keycloak.  
2. Crea un **realm** y un **client** (`mk-sso` y `loans-web`).  
3. Descarga el archivo `keycloak.json` e importalo desde keycloak.  

### 2. Variables de entorno

Configura las siguientes variables antes de levantar la aplicación:  

