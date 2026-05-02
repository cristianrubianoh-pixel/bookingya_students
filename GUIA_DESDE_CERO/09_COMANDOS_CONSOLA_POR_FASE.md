# Comandos por consola — evaluación TDD / BDD / ATDD (rama `dev-sc`)

Documento para **mostrar al profesor** qué ejecutar en **PowerShell** y qué mensajes deben aparecer al final. Trabaja siempre en la raíz del repo:

`C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students`

## Prerrequisitos

- **JDK 17** y **Maven** (usa el wrapper del proyecto: `.\mvnw.cmd`).
- **Node.js** y **npm** (para ATDD).
- Rama recomendada: **`dev-sc`** (sincronizada con `origin`).

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
git fetch origin
git checkout dev-sc
git pull origin dev-sc
```

## Cómo levantar la API (elige una opción)

### Opción A — Perfil `test` con H2 (recomendada para ATDD y vídeo sin Docker)

Evita depender de PostgreSQL. Genera el JAR y arranca en **otra ventana** de PowerShell:

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
.\mvnw.cmd -B package -DskipTests
java -jar target\bookingya-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```

En consola debe aparecer algo equivalente a **Started** … **BookingyaApplication** y el servidor en el puerto **8080** con context-path **`/api`**.

### Opción B — PostgreSQL + `.env` (flujo de desarrollo “real”)

Sigue [04_LEVANTAR_API_Y_PROBAR.md](04_LEVANTAR_API_Y_PROBAR.md) y usa `.\mvnw.cmd spring-boot:run` (o el comando que allí indiques). **No muestres el contenido de `.env` en cámara.**

---

## FASE 1 — TDD (pruebas unitarias JUnit)

**Qué se automatiza (enunciado):** creación, consulta, actualización, eliminación, obtención por ID (y otros escenarios negativos / reglas de negocio en la misma clase).

**Ubicación del código:** `src\test\java\com\project\bookingya\services\ReservationServiceTest.java`

### Comando recomendado (solo tests unitarios de reservas)

En PowerShell, el parámetro `-Dtest=` debe ir **entre comillas** para que no se interprete como expresión:

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
.\mvnw.cmd "-Dtest=com.project.bookingya.services.ReservationServiceTest" test
```

### Qué debe verse al final en consola (ejemplo válido)

Líneas de resumen Maven similares a:

```text
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: ... -- in com.project.bookingya.services.ReservationServiceTest
[INFO] Tests run: 11, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

> El número **11** corresponde a la cantidad de métodos `@Test` en esa clase en la versión actual de `dev-sc`. Si cambian los tests, el número debe actualizarse, pero debe seguir apareciendo **Failures: 0**, **Errors: 0** y **BUILD SUCCESS**.

### Opción: toda la suite que ejecuta Surefire en `test`

Incluye además escenarios Cucumber / otros tests que estén en el mismo ciclo `test`:

```powershell
.\mvnw.cmd test
```

Aquí el total **Tests run** será **mayor** que el de solo `ReservationServiceTest`; lo importante sigue siendo **BUILD SUCCESS** y **Failures: 0**.

### Informes XML (opcional para entregar archivo)

Directorio típico: `target\surefire-reports\`

---

## FASE 2 — BDD (Gherkin + Cucumber + Serenity)

**Qué se automatiza:** comportamiento descrito en lenguaje Gherkin, ejecutado con Cucumber contra el contexto Spring; reporte Serenity tras la fase de agregación en **`verify`**.

**Feature:** `src\test\resources\features\reservations.feature`

### Comando recomendado

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
.\mvnw.cmd clean verify
```

### Qué debe verse en consola

- Ejecución de escenarios Cucumber por nombre (nombre del feature / escenarios en español o tags según tu `.feature`).
- Al final:

```text
[INFO] SERENITY TESTS:               | SUCCESS
...
[INFO] Tests passed                  | ...
[INFO] BUILD SUCCESS
```

(El texto exacto de Serenity puede variar en versión menor; debe quedar **SUCCESS** y **BUILD SUCCESS**.)

### Evidencia adicional fuera de consola (válida para BDD)

Tras **`verify`**, abre el reporte HTML en el navegador:

`target\site\serenity\index.html`

---

## FASE 3 — ATDD (Playwright + TypeScript, criterios de aceptación HTTP)

**Especificaciones:** `playwright.config.ts` y `tests\atdd\reservations.api.spec.ts`

**Requisito:** la API debe estar escuchando (Opción A o B arriba), usualmente **`http://localhost:8080`** (`BASE_URL`; el cliente HTTP de Playwright usa rutas tipo `/api/...` sobre ese host).

### Terminal 1 — API ya levantada

(ejemplo perfil test)

```powershell
java -jar target\bookingya-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```

### Terminal 2 — dependencias y pruebas

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
npm ci
$env:BASE_URL = "http://localhost:8080"
npm run test:atdd
```

Equivalente sin script npm:

```powershell
$env:BASE_URL = "http://localhost:8080"
npx playwright test
```

### Qué debe verse al final en consola

Líneas similares a:

```text
  ok ...
  ...

  N passed (...s)
```

> En la versión actual de la rama, **N** es **5** pruebas en secuencia en `reservations.api.spec.ts`. Si añades casos, N aumentará.

Para instalar navegadores la primera vez (si Hubiera error de browsers):

```powershell
npx playwright install chromium
```

---

## Notas prácticas (errores frecuentes)

1. **`clean` falla porque no puede borrar `target\...jar`**  
   Algún proceso Java sigue usando el JAR (API corriendo). Cierra ese proceso y vuelve a ejecutar `clean verify`.

2. **Maven interpreta mal `-Dtest=...`** en PowerShell  
   Usa siempre comillas: `"-Dtest=com.project.bookingya.services.ReservationServiceTest"`.

3. **Playwright falla con ECONNREFUSED**  
   La API no está en marcha o el puerto no es 8080; ajusta `BASE_URL` al host/puerto reales.

4. **Swagger** como apoyo visual en vídeo opcional (`07_VALIDACION_APIS_SWAGGER.md`), pero las **tres fases** se demueban con Maven y Playwright según esta guía.

---

## Referencia cruzada

- Lista de chequeo para grabar el vídeo: [08_EVIDENCIAS_PARA_VIDEO.md](08_EVIDENCIAS_PARA_VIDEO.md).
