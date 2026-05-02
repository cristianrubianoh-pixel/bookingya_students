# Proyecto actual: BookingYA Students

## Contexto

`bookingya_students` es una API REST backend para gestionar un sistema de reservas de habitaciones.  
Está orientado a prácticas académicas de ingeniería de software con foco en reglas de negocio y calidad.

## Stack tecnológico

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA + PostgreSQL
- Bean Validation
- OpenAPI/Swagger (`springdoc`)
- ModelMapper
- Maven Wrapper (`mvnw`)
- Docker + Docker Compose
- Kubernetes manifests (deploy básico)

## Estructura principal del proyecto

- `src/main/java/com/project/bookingya`
  - `controllers`: endpoints REST
  - `services`: reglas de negocio
  - `repositories`: acceso a datos (JPA)
  - `entities`: entidades persistentes
  - `dtos` y `models`: contratos de entrada/salida
  - `exceptions`: manejo de errores y validaciones
  - `shared`: utilidades compartidas
- `src/main/java/resources`
  - `application.properties` (configuración actual)
- `k8s`
  - manifiestos de app y base de datos
- `.github/workflows`
  - pipeline de CI con ejecución de tests

## Funcionalidad implementada

### Gestión de habitaciones

- Crear, listar, consultar, actualizar y eliminar habitaciones.
- Búsqueda por `id` y por código de habitación.

### Gestión de huéspedes

- Crear, listar, consultar, actualizar y eliminar huéspedes.
- Búsqueda por identificación.

### Gestión de reservas

- Crear, listar, consultar, actualizar y eliminar reservas.
- Consultas por habitación y por huésped.
- Validación de disponibilidad.

## Reglas de negocio clave (reservas)

En `ReservationService` se valida:

- Rango de fechas válido (`checkIn < checkOut`).
- Existencia de habitación y huésped.
- Disponibilidad para el rango solicitado.
- Capacidad máxima de la habitación.
- Ausencia de solapamientos:
  - misma habitación en fechas superpuestas
  - mismo huésped en fechas superpuestas

## Flujo general de arquitectura

1. El cliente invoca un endpoint en `controllers`.
2. El controlador delega en `services`.
3. El servicio aplica reglas de negocio y usa `repositories`.
4. Los `repositories` persisten/consultan entidades con JPA.
5. Se mapean entidades a DTO/model y se responde al cliente.
6. Excepciones de validación y negocio se resuelven en handler global.

## Ejecución local y build

- Desarrollo:
  - `./mvnw spring-boot:run`
- Compilación:
  - `mvn clean package`
- Tests:
  - `./mvnw test`
- Docker:
  - `docker compose up --build`

## Estado actual y observaciones técnicas

- El proyecto tiene una base funcional clara para dominio hotelero (rooms, guests, reservations).
- La CI ejecuta `mvn test`, pero en esta variante de estudiantes pueden no existir tests reales aún.
- La configuración de resources está en rutas no estándar (`src/main/java/resources` y `src/main/test/resources`), lo cual conviene corregir hacia:
  - `src/main/resources`
  - `src/test/resources`
- Existen credenciales visibles en archivos de despliegue/configuración de ejemplo; conviene moverlas a variables de entorno/secret managers.

## Recomendaciones incrementales

1. Estandarizar rutas de resources sin reescritura masiva.
2. Incorporar pruebas unitarias de `ReservationService` como primer paso de cobertura.
3. Añadir perfiles `dev/test/prod` para separar configuración.
4. Integrar análisis estático (Checkstyle/SpotBugs o PMD) en Maven y CI.

---

Documento generado para describir puntualmente el estado actual del proyecto cargado.
