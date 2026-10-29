# API de Soporte Técnico

API RESTful desarrollada con **Java 17** y **Spring Boot 3** para registrar, consultar, actualizar y eliminar solicitudes de soporte técnico. Los datos se guardan **en memoria** (sin base de datos), usando `Map` y `AtomicLong` para simular la persistencia.

> Evaluación parcial – Desarrollo de los Componentes del Negocio – IDAT

## Integrantes y roles

| Integrante | Rol |
|---|---|
| _Nombre 1_ | Modelo y servicios (lógica de negocio, persistencia en memoria) |
| _Nombre 2_ | Controladores, DTOs, validaciones y manejo de excepciones |
| _Nombre 3_ | Documentación (Swagger/README), pruebas en Postman e informe |

## Requisitos

- JDK 17 o superior
- Maven 3.8+ (o usar el IDE: IntelliJ IDEA / Eclipse / VS Code)
- Postman (para las pruebas funcionales)

## Instalación y ejecución

```bash
git clone <URL-DEL-REPOSITORIO>
cd soporte-api
mvn clean install
mvn spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Para ejecutar las pruebas automatizadas: `mvn test`

## Estructura del proyecto

```
src/main/java/com/idat/soporte
├── SoporteApiApplication.java      # Clase principal
├── model/                          # Entidades: Persona (abstracta), Cliente, Tecnico, Solicitud, enums
├── dto/                            # Objetos de entrada/salida con validaciones (records)
├── service/                        # Interfaces de la lógica de negocio
│   └── impl/                       # Implementaciones con almacenamiento en memoria
├── controller/                     # Endpoints REST
├── exception/                      # Excepciones propias y @RestControllerAdvice
└── config/                         # Configuración de Swagger
```

Arquitectura en capas: **Controller → Service (interfaz) → ServiceImpl → Model**. Los controladores no contienen lógica de negocio.

## Datos de ejemplo precargados

- Clientes: `1` María Torres, `2` Luis Ramírez
- Técnicos: `1` Carlos Mendoza, `2` Ana Quispe

## Endpoints

Base URL: `http://localhost:8080/api/v1`

| Método | Ruta | Descripción | Respuesta OK |
|---|---|---|---|
| GET | `/solicitudes` | Lista solicitudes (filtro opcional `?estado=PENDIENTE`) | 200 |
| GET | `/solicitudes/{id}` | Obtiene una solicitud | 200 |
| POST | `/solicitudes` | Registra una solicitud | 201 + `Location` |
| PUT | `/solicitudes/{id}` | Actualiza una solicitud | 200 |
| DELETE | `/solicitudes/{id}` | Elimina una solicitud | 204 |
| GET/POST/PUT/DELETE | `/clientes`, `/clientes/{id}` | CRUD de clientes | 200/201/204 |
| GET/POST/PUT/DELETE | `/tecnicos`, `/tecnicos/{id}` | CRUD de técnicos | 200/201/204 |

Valores permitidos: `prioridad` = `BAJA | MEDIA | ALTA | CRITICA`; `estado` = `PENDIENTE | EN_PROCESO | RESUELTA | CANCELADA`.

### Ejemplo: crear solicitud

`POST /api/v1/solicitudes`

```json
{
  "titulo": "Fallo en el servidor de correo",
  "descripcion": "No se pueden enviar correos desde la mañana",
  "prioridad": "ALTA",
  "clienteId": 1,
  "tecnicoId": 1
}
```

Respuesta `201 Created`:

```json
{
  "id": 1,
  "titulo": "Fallo en el servidor de correo",
  "descripcion": "No se pueden enviar correos desde la mañana",
  "prioridad": "ALTA",
  "estado": "PENDIENTE",
  "clienteId": 1,
  "clienteNombre": "María Torres",
  "tecnicoId": 1,
  "tecnicoNombre": "Carlos Mendoza",
  "fechaCreacion": "2026-10-25T16:30:00",
  "fechaActualizacion": "2026-10-25T16:30:00"
}
```

## Validaciones y manejo de errores

Se usan `@Valid`, `@NotBlank`, `@NotNull`, `@Size`, `@Email` y `@Pattern`. Todos los errores devuelven la misma estructura mediante `@RestControllerAdvice` (`GlobalExceptionHandler`):

```json
{
  "timestamp": "2026-10-25T16:31:10",
  "status": 400,
  "error": "Bad Request",
  "mensaje": "Los datos enviados no son válidos",
  "ruta": "/api/v1/solicitudes",
  "detalles": [
    "descripcion: La descripción es obligatoria",
    "titulo: El título es obligatorio"
  ]
}
```

| Código | Cuándo ocurre |
|---|---|
| 400 | Datos inválidos, JSON mal formado o parámetro con valor incorrecto |
| 404 | El recurso (solicitud, cliente o técnico) no existe |
| 409 | Regla de negocio (email duplicado, técnico no disponible, modificar solicitud CANCELADA) |
| 500 | Error interno no esperado |

## Pruebas con Postman

1. Importar `postman/Soporte-API.postman_collection.json` en Postman.
2. Con la aplicación en ejecución, correr las peticiones en orden (o usar *Run collection*).
3. Cada petición incluye tests automáticos que validan el código de estado.

Las capturas de las pruebas se encuentran en la carpeta `docs/capturas/` y en el informe técnico.
