# 🏨 API de Reservas de Habitaciones — BookingYa
Apache-2.0 license  
Build • Java • Spring Boot • Dockerized

Sistema backend para la gestión de reservas de habitaciones al estilo **Booking / Airbnb**. Este proyecto ha sido desarrollado como una muestra de habilidades de un desarrollador backend senior, incorporando buenas prácticas de arquitectura, reglas de negocio robustas, pruebas automatizadas y despliegue en contenedores.

Además, el repositorio está organizado en dos ramas para fines académicos:

- **main**: proyecto completo con implementación funcional y **casos de prueba automatizados**
- **estudiantes**: mismo proyecto, misma arquitectura y misma estructura, pero **sin los test cases automatizados**, para que los estudiantes los desarrollen

---

## 🚀 Funcionalidades Principales

### 🛏️ Habitaciones
CRUD completo (`id`, `code`, `name`, `city`, `maxGuests`, `nightlyPrice`, `available`)

### 🧳 Huéspedes
CRUD completo (`id`, `identification`, `name`, `email`)

### 📅 Reservas
Crear, listar, consultar y cancelar reservas

Atributos principales:
`id`, `roomId`, `guestId`, `checkIn`, `checkOut`, `guestsCount`, `notes`

---

## 📋 Reglas de Negocio

❌ No se permite el solapamiento de reservas para una misma habitación  
❌ Un huésped no puede tener dos reservas al mismo tiempo  
✅ `checkIn` debe ser anterior a `checkOut`  
✅ Una habitación solo puede reservarse si está disponible  
✅ `guestsCount` no puede superar la capacidad máxima de la habitación  
✅ Posibilidad de consultar la disponibilidad de una habitación  
⚠️ Validaciones personalizadas con manejo de excepciones controlado

---

## ⚙️ Tecnologías Utilizadas

| Categoría | Tecnología |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot |
| ORM | JPA + JPQL |
| Base de datos | PostgreSQL |
| Documentación API | Swagger / OpenAPI |
| Testing | JUnit 5 + Spring Boot Test |
| Contenedores | Docker, Podman, Minikube (K8s) |

---

## 🗂️ Arquitectura del Proyecto

🧱 Basada en una arquitectura modular por capas, manteniendo el estilo del proyecto original.

```text
src/
├── main/
│   ├── java/com/project/bookingya/
│   │   ├── controllers/     # Exposición de endpoints REST
│   │   ├── dtos/            # Objetos de transferencia de datos
│   │   ├── entities/        # Entidades JPA
│   │   ├── exceptions/      # Excepciones y manejo global de errores
│   │   ├── models/          # Modelos de dominio
│   │   ├── repositories/    # Acceso a datos
│   │   ├── services/        # Lógica de negocio y reglas
│   │   └── shared/          # Configuración y utilidades compartidas
│   └── resources/
└── test/                    # Casos de prueba automatizados (solo en main)
```

---


## Evaluación académica: fases TDD, BDD y ATDD (copiar y pegar)

Trabajo de referencia con **pruebas en tres capas**.

**Requisitos:** JDK 17, Node.js + npm, PostgreSQL accesible (por ejemplo Docker) y archivo **`.env`** configurado desde [`.env.example`](.env.example). Abre cada bloque en **PowerShell** en la **raíz del proyecto** (`bookingya_students`).

---

### Paso 0 — Rama y ubicación del repo

Copia y pega (ajusta la ruta de `cd` a tu equipo):

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
git fetch origin
git checkout main
git pull origin main
```

---

### Paso 1 — Levantar la aplicación API

Terminal **1** (déjala abierta hasta terminar las demos):

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
.\mvnw.cmd spring-boot:run
```

En consola debe verse el arranque de Spring Boot y el Tomcat en el puerto **8080**.

- Si aparece **`Port 8080 was already in use`**, cierra la otra Java que está usando ese puerto o revisa procesos antes de repetir este paso.

---

### Paso 2 — Swagger UI (documentación HTTP)

Con la API en marcha, abre el navegador en:

[**http://localhost:8080/api/swagger-ui/index.html#/**](http://localhost:8080/api/swagger-ui/index.html#/)

Ahí puedes mostrar paso a paso los endpoints de **Rooms**, **Guests** y **Reservations** como apoyo visual (opcional ante el profesor; la evidencia automatizada viene de las siguientes fases).

---

### FASE 1 — TDD (JUnit / pruebas unitarias sobre `ReservationService`)

Ubicación del código: `src\test\java\com\project\bookingya\services\ReservationServiceTest.java`

Ejecutar **solo** esas pruebas (en **otra terminal**, con la carpeta raíz del repo):

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
.\mvnw.cmd "-Dtest=com.project.bookingya.services.ReservationServiceTest" test
```

**Qué debe mostrarse al final:** `Tests run:` con **`Failures: 0`**, **`Errors: 0`** y **`BUILD SUCCESS`**.

(Opcional) Toda la suite que corre en `test` (incluye más pruebas que solo `ReservationService`):

```powershell
.\mvnw.cmd test
```

Informes Surefire: `target\surefire-reports\`

---

### FASE 2 — BDD (Gherkin + Cucumber + Serenity)

Feature: `src\test\resources\features\reservations.feature`

Genera reporte Serenity con **verify** (no hace falta tener la API levantada; usa contexto de prueba/H2):

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
.\mvnw.cmd clean verify
```

**Qué mostrar:** log de Cucumber por escenarios y **`BUILD SUCCESS`**. Como evidencia BDD abierta aparte del texto de consola, abre en el navegador:

`target\site\serenity\index.html`

---

### FASE 3 — ATDD (Playwright + TypeScript contra la API REST)

**Requiere que la API esté arriba** (Paso 1 con `spring-boot:run` y mismo host/puerto que Playwright espera por defecto).

En **PowerShell nueva** en la raíz del repo:

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
npm ci
$env:BASE_URL = "http://localhost:8080"
npm run test:atdd
```

*(Si `npm ci` ya corrió y no cambiaste dependencias, el segundo comando puede ejecutarse solo tras fijar `BASE_URL`.)*

**Qué mostrar:** líneas **`N passed`** y tiempo total al cerrar Playwright.

Configuración: [`playwright.config.ts`](playwright.config.ts). Especificaciones: [`tests/atdd/reservations.api.spec.ts`](tests/atdd/reservations.api.spec.ts).

---


## 📘 Documentación Swagger

Disponible automáticamente en:

```text
http://localhost:8080/api/swagger-ui/index.html#/
```

---

## 🔌 Endpoints principales

### Habitaciones
- `GET /api/room`
- `GET /api/room/{id}`
- `GET /api/room/code/{code}`
- `POST /api/room`
- `PUT /api/room/{id}`
- `DELETE /api/room/{id}`

### Huéspedes
- `GET /api/guest`
- `GET /api/guest/{id}`
- `GET /api/guest/identification/{identification}`
- `POST /api/guest`
- `PUT /api/guest/{id}`
- `DELETE /api/guest/{id}`

### Reservas
- `GET /api/reservation`
- `GET /api/reservation/{id}`
- `GET /api/reservation/room/{roomId}`
- `GET /api/reservation/guest/{guestId}`
- `GET /api/reservation/availability/room/{roomId}?checkIn=2026-05-10T14:00:00&checkOut=2026-05-12T11:00:00`
- `POST /api/reservation`
- `PUT /api/reservation/{id}`
- `DELETE /api/reservation/{id}`

---

## 📦 Docker

### 🐳 Build & Run con Podman
```bash
docker build -t bookingya-app:latest .
docker compose up
```

---


## 👤 Autores

Desarrollado por: 
**Cristian Rubiano**  
**Joan Sebastian Correa**  
---

## 📄 Licencia

Este proyecto está licenciado bajo la **Apache License 2.0**.
