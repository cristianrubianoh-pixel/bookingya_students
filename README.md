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

✅ Separación clara de responsabilidades  
✅ Diseño orientado al dominio  
✅ Módulos desacoplados y escalables  
✅ Arquitectura fácil de extender y mantener

---

## 🌿 Estructura de ramas

### `main`
Incluye:
- implementación funcional completa
- reglas de negocio
- pruebas automatizadas
- configuración lista para evaluación técnica

### `estudiantes`
Incluye:
- misma base funcional
- misma arquitectura y estructura de carpetas
- sin los casos de prueba automatizados

Esta rama está pensada para que los estudiantes implementen su propia estrategia de testing sin alterar la estructura principal del proyecto.

---

## 🚀 Primeros pasos para estudiantes

### 1️⃣ Haz fork del repositorio
Haz clic en el botón **Fork** en la parte superior derecha de este repositorio.

### 2️⃣ Clona tu fork
```bash
git clone https://github.com/tu-usuario/bookingya
cd bookingya
```

### 3️⃣ Cambia a la rama de trabajo
```bash
git checkout estudiantes
```

### 4️⃣ Configura las variables de entorno
Copia el archivo `.env.example` y renómbralo a `.env`:

```bash
cp .env.example .env
```

Luego abre el archivo `.env` y llena tus propias credenciales de PostgreSQL:

```env
HOST_DB=127.0.0.1
PORT_DB=5432
POSTGRES_DB=bookingya
USERNAME_DB=postgres
PASSWORD_DB=tu_contraseña
```

### 5️⃣ Corre el proyecto
```bash
./mvnw spring-boot:run
```

### 6️⃣ Corre los tests
Solo en la rama `main`:

```bash
./mvnw test
```

---

## Evaluación académica: fases TDD, BDD y ATDD (copiar y pegar)

Trabajo de referencia con **pruebas en tres capas** en la rama **`dev-sc`** (ajusta la rama si tu docente usa otra).

**Requisitos:** JDK 17, Node.js + npm, PostgreSQL accesible (por ejemplo Docker) y archivo **`.env`** configurado desde [`.env.example`](.env.example). Abre cada bloque en **PowerShell** en la **raíz del proyecto** (`bookingya_students`).

---

### Paso 0 — Rama y ubicación del repo

Copia y pega (ajusta la ruta de `cd` a tu equipo):

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
git fetch origin
git checkout dev-sc
git pull origin dev-sc
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

### Guía ampliada (misma información con más contexto)

- [GUIA_DESDE_CERO/09_COMANDOS_CONSOLA_POR_FASE.md](GUIA_DESDE_CERO/09_COMANDOS_CONSOLA_POR_FASE.md) — comandos por fase, perfil **`test`/JAR para H2**, y errores típicos  
- [GUIA_DESDE_CERO/05_DESARROLLO_TDD_BDD_ATDD.md](GUIA_DESDE_CERO/05_DESARROLLO_TDD_BDD_ATDD.md) — rol de cada herramienta  
- [GUIA_DESDE_CERO/08_EVIDENCIAS_PARA_VIDEO.md](GUIA_DESDE_CERO/08_EVIDENCIAS_PARA_VIDEO.md) — cómo ordenar evidencias para sustentación

---

## 🧪 Ejecución y Pruebas

### ▶️ Ejecución Local
```bash
# Instalar dependencias y compilar
mvn install

# Ejecutar el proyecto
./mvnw spring-boot:run
```

O después de compilar:

```bash
java -jar target/bookingya-0.0.1-SNAPSHOT.jar
```

### 🧪 Ejecutar Tests
```bash
mvn test
```

O:

```bash
./mvnw test
```

Si aparece:

```bash
zsh: permission denied
```

Ejecuta:

```bash
chmod +x mvnw
```

### ⚠️ Requisitos previos Mac
Si tienes problemas al correr Maven o los tests, ejecuta:

```bash
export MAVEN_OPTS="-Xmx1024m"
```

O para dejarlo permanente:

```bash
echo 'export MAVEN_OPTS="-Xmx1024m"' >> ~/.zshrc
source ~/.zshrc
```

### 🧪 Cobertura esperada en la rama `main`
Los test cases automatizados validan, entre otros:

- creación de habitaciones
- creación de huéspedes
- reservas válidas
- rango de fechas inválido
- capacidad máxima por habitación
- solapamiento de reservas por habitación
- solapamiento de reservas por huésped
- consulta de disponibilidad

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

## 📦 Docker / Contenedores

### 🐳 Build & Run con Podman
```bash
podman build -t bookingya-app:latest .
podman compose up
```

También puedes usar Docker si lo prefieres:

```bash
docker build -t bookingya-app:latest .
docker compose up
```

---

## ☸️ Despliegue en Kubernetes con Minikube

### ✅ Requisitos
- Minikube instalado
- Podman o Docker
- Manifiestos en `k8s/`

### 🚀 Pasos de Despliegue
```bash
minikube delete
minikube start
minikube addons enable metrics-server

# Crear y exportar imagen
podman build -t bookingya-app:latest .
podman save -o bookingya-app.tar bookingya-app:latest

# Cargar imagen en Minikube
minikube image load bookingya-app.tar

# Aplicar manifiestos K8s
kubectl apply -f k8s/

# Ver logs o exponer servicio
kubectl logs <pod-name>
minikube service bookingya-app
```

---

## 🧠 Buenas Prácticas Aplicadas

- Separación modular clara (`Room`, `Guest`, `Reservation`)
- Subcarpetas por responsabilidad: `Controller`, `DTO`, `Entity`, `Service`, `Repository`
- Centralización de lógica común: `shared/`, `exceptions/`
- Validaciones robustas mediante reglas de negocio y excepciones personalizadas
- Documentación accesible con Swagger + comandos en README
- Preparado para producción con Docker, K8s y configuración desacoplada
- Separación académica por ramas para evaluación práctica (`main` y `estudiantes`)

---

## 📌 Qué Demuestra Este Proyecto

| Habilidad | Evidencia |
|---|---|
| Diseño de dominios | Entidades ricas + reglas de negocio aplicadas |
| Arquitectura escalable | Modular, separación de capas y responsabilidades |
| Testing profesional | JUnit, validaciones y casos límite cubiertos |
| Persistencia y modelado | JPA, relaciones entre entidades y consultas |
| DevOps básico | Docker, Podman, Minikube |
| Documentación y mantenimiento | Código limpio + README claro + Swagger |

---

## 👤 Autor

Desarrollado por **Alejandro Aguirre**  
LinkedIn • GitHub • Backend Engineer

---

## 📄 Licencia

Este proyecto está licenciado bajo la **Apache License 2.0**.
