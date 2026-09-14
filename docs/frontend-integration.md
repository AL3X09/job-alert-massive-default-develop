# Integración Frontend - Job Alert Massive Default

Este documento contiene la información necesaria para que un cliente (como una aplicación frontend en Angular) consuma los servicios expuestos por este microservicio.

## Información General
* **Context Path base:** `/alert/massive/default`
* **Content-Type por defecto (Respuestas):** `application/json`
* **Autenticación/Autorización:**
  Actualmente, los endpoints no incluyen un mecanismo de autenticación integrado en el código del microservicio (como validación de JWT por Spring Security).
  Si el servicio está expuesto a través de un API Gateway corporativo, es muy probable que deba enviar un token JWT en la cabecera `Authorization: Bearer <token>`. Consulte con el equipo de infraestructura para los detalles del ambiente de despliegue.

## Convenciones
* Los nombres de las propiedades en los JSON de respuesta mezclan PascalCase (`Status`, `RqUID`) con camelCase (`startTime`, `finishTime`). Asegúrese de configurar sus interfaces en TypeScript o modelos de cliente HTTP (como HttpClient de Angular) para mapear correctamente estas propiedades exactas.

## Endpoints

### 1. Creación Masiva de Alertas (Procesamiento Diario)
Inicia el proceso asíncrono o síncrono para crear alertas masivas leyendo el archivo de texto asociado a la fecha dada.

* **Método HTTP:** `GET`
* **Ruta:** `/daily/{date}`
* **Parámetros de Ruta (Path):**
  * `date` (String, requerido): Fecha en formato `YYYYMMDD`. Ejemplo: `20250723`.

**Ejemplo de Petición (Angular HttpClient):**
```typescript
this.http.get<ApiResponse>(`/alert/massive/default/daily/20250723`)
  .subscribe(res => console.log(res));
```

**Ejemplo de Respuesta Exitosa (202 Accepted):**
```json
{
    "Status": {
        "statusCode": "0",
        "statusDesc": "Transacción Exitosa",
        "serverStatusCode": "OK",
        "serverStatusDesc": "OK",
        "severity": "Info"
    },
    "RqUID": "00000-abcd-1234-xyz",
    "startTime": "2023-10-27T10:00:00.000+00:00",
    "finishTime": null
}
```

**Ejemplo de Respuesta de Error (202 Accepted, pero con error interno):**
*Nota: El servicio retorna HTTP 202 en ambos casos, pero cambia el `serverStatusCode` en el payload y la severidad.*
```json
{
    "Status": {
        "statusCode": "0",
        "statusDesc": "Fallas al procesar las alertas del archivo",
        "serverStatusCode": "INTERNAL_SERVER_ERROR",
        "serverStatusDesc": "INTERNAL_SERVER_ERROR",
        "severity": "Error"
    },
    "RqUID": "00000-abcd-1234-xyz",
    "startTime": "2023-10-27T10:00:00.000+00:00",
    "finishTime": null
}
```

### 2. Inactivación Masiva de Alertas
Inicia el proceso para inactivar alertas masivas leyendo el archivo de inactivación asociado a la fecha.

* **Método HTTP:** `GET`
* **Ruta:** `/inactivate/daily/{date}`
* **Parámetros de Ruta (Path):**
  * `date` (String, requerido): Fecha en formato `YYYYMMDD`. Ejemplo: `20250723`.

**Ejemplo de Petición (Angular):**
```typescript
this.http.get<ApiResponse>(`/alert/massive/default/inactivate/daily/20250723`)
  .subscribe(res => console.log(res));
```

**Ejemplo de Respuesta Exitosa (202 Accepted):**
```json
{
    "Status": {
        "statusCode": "0",
        "statusDesc": "Transacción Exitosa",
        "serverStatusCode": "OK",
        "serverStatusDesc": "OK",
        "severity": "Info"
    },
    "RqUID": "00000-efgh-5678-qwe",
    "startTime": "2023-10-27T10:05:00.000+00:00",
    "finishTime": null
}
```

### 3. Health Check
Verifica el estado del servicio.

* **Método HTTP:** `GET`
* **Ruta:** `/health`
* **Respuesta Esperada (200 OK):** (Retorna un texto plano/JSON básico)
```text
OK - Version: AM-330.23
```

## Modelos Angular (TypeScript) Recomendados

Para consumir estos servicios de forma tipada, puede usar las siguientes interfaces:

```typescript
export interface ApiStatus {
  statusCode: string;
  statusDesc: string;
  serverStatusCode: string;
  serverStatusDesc: string;
  severity: string;
}

export interface ApiResponse {
  Status: ApiStatus;
  RqUID: string;
  startTime: Date | string;
  finishTime?: Date | string | null;
}
```
