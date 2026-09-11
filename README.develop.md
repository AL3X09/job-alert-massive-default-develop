# PARA EL DESARROLLADOR
# job-alert-massive-default
Microservecio para la creación o notificación masiva de alertas
## Github
el servivio se encuentra en [GITHUB](https://devops-github.ath.net/alertasmoviles/job-alert-massive-default)
la rama principal es *develop* sin embargo periodiodicamente se deja actualizada la rama *master*

-Se le recomienda al desarrollador solo editar este archivo en la rama *develop*
## LOGS
estos quedan almacenados en /data/logs/admin_alertas
## .aplication.propertis desarrollo local
A continuación comparto el propertis que deberia tener en su entorno de desarrollo, recuerde tenerlo muy en cuenta para los demas ambientes o puede alterar el funcionamiento del microservicio

```bash

spring.application.name=job-alert-massive-default
logging.level.hello=INFO
logging.level.org.springframework.ws=DEBUG
server.port=31795
spring.jpa.open-in-view=false
spring.jpa.show-sql=false
spring.jpa.hibernate.ddl-auto=none
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
#spring.datasource.url=${SECRETS_URL_DB}
#spring.datasource.username=${SECRETS_USER_DB}
#spring.datasource.password=${SECRETS_PASSWORD_DB}
#url-api-security-data-inquiry=http://security-data-inq-service:31731
#---SWAGGER ACIFUENTES 2025 
springdoc.swagger-ui.path=/alert/massive/default/swagger-ui
springdoc.api-docs.path=/alert/massive/default/v3/api-docs
# Forzar Swagger UI a usar la ruta correcta del JSON
springdoc.swagger-ui.url=/alert/massive/default/v3/api-docs
#--ELIMINAR
#spring.datasource.url=jdbc:oracle:thin:@10.130.14.87:1528:AMCOREQA1
spring.datasource.url=jdbc:oracle:thin:@10.130.13.62:1528:AMCOREPT
spring.datasource.username=ADMIN_ALERTAS
spring.datasource.password=Ath12345
url-api-security-data-inquiry=http://localhost:5000

```
TIP de desarrollo
```bash
// ver cron CRON
		// System.out.println("CRON configurado: " +
		// getJobMassiveAlertDefaultInactivateProcess);
		// configurado: 0 39 15 * * *
```
## inicio de contrucción del micorservicio
[JIRA-AM-231 2024](https://jira-ath.atlassian.net/browse/AM-231)

[CONFLUENCE](https://jira-ath.atlassian.net/wiki/spaces/EAM/pages/349701007/4.+DOCUMENTACI+N+T+CNICA)

# BASE DE DATOS

```bash
SELECT * FROM "CORE_AM"."SCHEDULE"
WHERE TASK_NAME = "JOB_MASSIVE_ALERT_DEFAULT"
AND TASK_NAME = "JOB_MASSIVE_INACTIVATE_ALERT_DEFAULT";
```

# SUAGGER
en local
http://localhost:31795/alert/massive/default/swagger-ui/index.html
en PT
http://10.130.13.60:31380/alert/massive/default/swagger-ui/index.html
en QA
http://10.130.13.88:31380/alert/massive/default/swagger-ui/index.html
en PRD
# DESARROLLADO POR
2024 - CRISTIAN VELANDIA