# Microservicio Job Alert Massive Default

Microservicio encargado de la creación o notificación masiva de alertas, así como de su inactivación masiva, basado en la lectura de archivos diarios (`.TXT`).

## 🚀 Valor del Proyecto
Este servicio procesa grandes volúmenes de alertas, procesando archivos diarios que especifican las alertas a crear o inactivar en el sistema, asegurando que los usuarios finales reciban (o dejen de recibir) notificaciones de manera oportuna de acuerdo al estado de sus productos o eventos en el banco.

## 🛠 Stack Tecnológico
* **Lenguaje:** Java 21
* **Framework:** Spring Boot 3.3.2
* **Persistencia:** Spring Data JPA / Hibernate
* **Base de Datos:** Oracle Database (ojdbc11)
* **Cliente HTTP:** Spring Cloud OpenFeign
* **Documentación API:** Springdoc OpenAPI (Swagger v3)
* **Librería de utilidades:** Lombok

## 🏗 Arquitectura y Estructura del Proyecto

La aplicación sigue una arquitectura por capas tradicional de Spring Boot (Controller, Service, Repository, Entity, DTO):

```text
.
├── src/
│   ├── main/
│   │   ├── java/co/com/ath/alert/massive/
│   │   │   ├── config/      # Configuraciones (Swagger, Feign, Rutas de archivos, Cron)
│   │   │   ├── controller/  # Controladores REST que exponen la API
│   │   │   ├── feign/       # Clientes Feign para comunicación con otros microservicios
│   │   │   ├── model/       # Entidades JPA (entity) y objetos de transferencia (dto)
│   │   │   ├── repository/  # Interfaces Spring Data JPA para acceso a datos
│   │   │   ├── service/     # Lógica de negocio (procesamiento de archivos y BD)
│   │   │   ├── task/        # Tareas programadas (Schedulers / Cron)
│   │   │   └── util/        # Constantes y métodos de utilidad estáticos
│   │   └── resources/
│   │       ├── application.properties # Archivo principal de configuración
│   │       └── logback-spring.xml     # Configuración de logs
│   └── test/                # Pruebas unitarias
```

## 📋 Prerrequisitos

* Java 21 instalado (JDK).
* Apache Maven (o utilizar el wrapper `./mvnw` incluido).
* Acceso a la base de datos Oracle configurada (se requieren credenciales válidas).
* Otro microservicio (Security Data Inquiry) en ejecución local o entorno accesible si se requieren consultas de seguridad (a través de Feign).

## ⚙️ Configuración del Entorno Local

Se debe proveer las variables de entorno para que `application.properties` las resuelva, o establecerlas explícitamente durante el desarrollo:

```properties
# .env.example / configuraciones clave en application.properties:
spring.application.name=job-alert-massive-default
server.port=31795

# Base de Datos
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=${SECRETS_URL_DB} # Ej: jdbc:oracle:thin:@10.130.13.62:1528:AMCOREPT
spring.datasource.username=${SECRETS_USER_DB}
spring.datasource.password=${SECRETS_PASSWORD_DB}

# APIs y URLs
url-api-security-data-inquiry=http://localhost:5000

# Archivos procesados (Rutas Locales o de Servidor)
file.paths.daily=/data/alertas_paycentral/billpay_C_YYYYMMDD.txt
file.paths.inactivate=/data/alertas_paycentral/billpay_I_YYYYMMDD.txt
```

## 🚀 Guía de Ejecución

### Desarrollo y Compilación
Para compilar y empaquetar la aplicación ignorando las pruebas:
```bash
./mvnw clean install -DskipTests
```

### Ejecutar Localmente
Para iniciar el servidor local usando Spring Boot:
```bash
./mvnw spring-boot:run
```
El microservicio iniciará por defecto en el puerto **31795**.

### Testing
Para ejecutar la suite de pruebas unitarias:
```bash
./mvnw test
```

## 📡 Endpoints Principales

La API expone funcionalidades para iniciar el procesamiento de los archivos diarios y revisar la salud del servicio:

1. **Creación Masiva de Alertas**
   * **Endpoint:** `GET /alert/massive/default/daily/{date}`
   * **Propósito:** Inicia el procesamiento de creación masiva a partir del archivo de creación (ej: `billpay_C_{date}.txt`).

2. **Inactivación Masiva de Alertas**
   * **Endpoint:** `GET /alert/massive/default/inactivate/daily/{date}`
   * **Propósito:** Inicia el procesamiento de inactivación de alertas a partir del archivo de inactivación (ej: `billpay_I_{date}.txt`).

3. **Health Check**
   * **Endpoint:** `GET /alert/massive/default/health`
   * **Propósito:** Retorna el estado y versión del servicio.

*Nota: La documentación de la API se puede acceder a través de Swagger UI cuando el servicio está en ejecución:*
* `http://localhost:31795/alert/massive/default/swagger-ui/index.html`

## 🤝 Flujo de Contribución

1. Este proyecto utiliza Git como sistema de control de versiones.
2. El repositorio original se aloja en `devops-github.ath.net/alertasmoviles/job-alert-massive-default`.
3. Todo desarrollo nuevo debe realizarse en ramas creadas a partir de la rama `develop`.
4. El código debe pasar por validación en herramientas de análisis de código estático (recomendaciones Sonar integradas).
5. Se recomienda no publicar contraseñas o secretos en `application.properties`. Usar siempre variables de entorno.
