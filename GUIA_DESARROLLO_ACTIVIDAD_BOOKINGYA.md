# Guía de Desarrollo de la Actividad — BookingYa

## 1) Contexto

Esta guía está diseñada para que desarrolles la actividad **paso a paso** y aprendas el razonamiento detrás de cada fase, sin que el trabajo quede resuelto automáticamente.

Objetivo académico principal (según enunciado): automatizar pruebas funcionales aplicando **TDD, BDD y ATDD**, ejecutar resultados e interpretar hallazgos para mejorar calidad y confiabilidad.

## 2) Estado actual validado del proyecto

- Proyecto base: API REST Spring Boot para reservas.
- Rama de trabajo creada: `dev-sc`.
- `AGENTS.md` del proyecto creado para guía local y **excluido del versionado** mediante `.git/info/exclude`.
- CI existente: `.github/workflows/ci.yml` ejecuta `mvn -B test --file pom.xml` en ramas `main` y `estudiantes`.

## 3) Archivos clave que debes estudiar primero

### Dominio y reglas

- `src/main/java/com/project/bookingya/services/ReservationService.java`
- `src/main/java/com/project/bookingya/repositories/IReservationRepository.java`
- `src/main/java/com/project/bookingya/shared/Constants.java`

Por qué: aquí vive la lógica de negocio (rango de fechas, capacidad, disponibilidad y solapamientos), que es el centro de TDD.

### Exposición API

- `src/main/java/com/project/bookingya/controllers/ReservationController.java`
- `src/main/java/com/project/bookingya/dtos/ReservationDto.java`

Por qué: define endpoints y contratos de entrada/salida para BDD y ATDD.

### Configuración y ejecución

- `pom.xml`
- `.github/workflows/ci.yml`
- `.env.example`
- `docker-compose.yml`
- `README.md`

Por qué: define dependencias, pipelines, variables de entorno y comandos para correr el proyecto.

## 4) Herramientas requeridas y estado local

## Instaladas (validadas)

- Git `2.54.0.windows.1`
- Java `17.0.12`
- Maven Wrapper (`mvnw.cmd`) funcional
- Maven global `3.9.14`
- Node.js `22.22.0`
- npm `11.5.1`

## Faltantes o no disponibles en PATH

- Docker
- Docker Compose (v2 integrado o plugin de Docker Desktop)

## Comandos de verificación rápida

```powershell
git --version
java -version
.\mvnw.cmd -version
mvn -version
docker --version
docker compose version
node --version
npm --version
```

## Acción recomendada

Instalar Docker Desktop (incluye Compose) y verificar nuevamente `docker --version` y `docker compose version`.

## 5) Preparación inicial obligatoria

1. Hacer fork del repositorio base.
2. Clonar tu fork localmente.
3. Crear archivo `.env` desde `.env.example`.
4. Trabajar sobre rama `dev-sc`.
5. Ejecutar la API localmente y confirmar acceso a Swagger.

Comandos sugeridos:

```powershell
copy .env.example .env
.\mvnw.cmd spring-boot:run
```

Swagger esperado:

- `http://localhost:8080/api/swagger-ui/index.html`

## 6) Fase 1 — TDD (JUnit)

## Qué debes desarrollar

Pruebas unitarias para:

- creación de reserva
- consulta de una reserva
- actualización de reserva existente
- eliminación de reserva
- obtención de reserva por ID

## Dónde desarrollarlo

Ruta sugerida:

- `src/test/java/com/project/bookingya/services/ReservationServiceTest.java`

Dependencias base ya presentes:

- `spring-boot-starter-test` (en `pom.xml`)

## Cómo validar

```powershell
.\mvnw.cmd test
```

## Por qué se hace así

TDD te obliga a definir primero el comportamiento esperado de la lógica de negocio y evita romper reglas críticas del dominio.

## Casos de aprendizaje recomendados (no obligatorios pero valiosos)

- rango de fechas inválido
- habitación no disponible
- capacidad excedida
- solapamiento por habitación
- solapamiento por huésped

## 7) Fase 2 — BDD (Serenity + Cucumber + Gherkin)

## Qué debes desarrollar

Un escenario de comportamiento en Gherkin y su automatización en Java con Serenity/Cucumber.

## Dónde desarrollarlo

Rutas sugeridas:

- `src/test/resources/features/reservations.feature`
- `src/test/java/com/project/bookingya/bdd/steps/ReservationSteps.java`
- `src/test/java/com/project/bookingya/bdd/runners/ReservationRunner.java`

## Qué deberás agregar en `pom.xml`

Dependencias/plugins de Serenity y Cucumber para Java (si aún no están en el proyecto).

## Cómo validar

- Ejecutar tests BDD desde Maven.
- Verificar reporte HTML generado por Serenity.

## Por qué se hace así

BDD traduce requisitos funcionales a lenguaje de negocio comprensible por el equipo y evaluadores, conectando análisis con automatización.

## 8) Fase 3 — ATDD (Playwright + TypeScript)

## Recomendación para este proyecto

Como el proyecto actual es backend API, la vía más coherente es ATDD con Playwright validando criterios de aceptación contra API (o flujo mínimo con datos preparados), en lugar de construir una UI desde cero.

## Qué debes desarrollar

- criterios de aceptación claros por funcionalidad
- pruebas automatizadas independientes con Playwright

## Dónde desarrollarlo

Rutas sugeridas:

- `playwright.config.ts`
- `tests/reservations/reservations.spec.ts`
- `tests/reservations/reservations.md`

## Cómo validar

```powershell
npx playwright test
```

## Por qué se hace así

ATDD asegura que el sistema cumple criterios del cliente final, no solo lógica interna o comportamiento técnico.

## 9) CI/CD y evidencia de entrega

## Lo que exige la entrega

- repositorio con código validado
- GitHub Actions en verde para pruebas de Fase 1 y Fase 2
- video de 3 a 5 minutos con participación de ambos integrantes
- transcripción o subtítulos del video

## Ajuste importante

Si entregas desde `dev-sc`, debes actualizar `.github/workflows/ci.yml` para incluir esta rama en `push` y `pull_request` o realizar PR/merge según tu estrategia de entrega.

## 10) Checklist de decisiones antes de implementar pruebas

- [ ] Definir si ATDD será contra API o con UI adicional.
- [ ] Confirmar rama exacta de entrega (`dev-sc` directa o merge a `main`).
- [ ] Definir convención de nombres para tests y escenarios.
- [ ] Acordar datos de prueba base para no tener falsos fallos.
- [ ] Confirmar si BDD validará un escenario mínimo o varios escenarios.
- [ ] Verificar criterio de evidencia para video (comandos, reportes, ejecución Playwright).

## 11) Riesgos detectados y cómo mitigarlos

- **Sin Docker en entorno local:** instalar Docker Desktop para replicar entorno y evitar diferencias de ejecución.
- **Dependencias BDD no configuradas aún:** agregar Serenity/Cucumber de forma incremental y validar en cada paso.
- **Rutas de resources no estándar en proyecto base:** evitar refactor grande de entrada; primero completar pruebas de actividad.
- **Pipeline no contempla `dev-sc`:** ajustar workflow o definir merge antes de entrega.

## 12) Estrategia incremental sugerida (aprendizaje + avance real)

1. TDD sobre `ReservationService` (primero casos felices, luego casos límite).
2. BDD con un escenario sólido de reserva y validación de comportamiento esperado.
3. ATDD con Playwright, priorizando criterios de aceptación medibles.
4. Ajustar CI y validar verde en Actions.
5. Preparar evidencia final (video, reporte Serenity, transcripción).

---

Este documento guía el desarrollo de la actividad para aprender el proceso, justificar decisiones y producir evidencia verificable en cada fase.
