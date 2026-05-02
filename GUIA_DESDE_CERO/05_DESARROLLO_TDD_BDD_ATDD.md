# Etapa 5: Desarrollo de pruebas (TDD, BDD, ATDD)

Esta etapa responde exactamente al trabajo académico que debes entregar.

## Parte A: TDD (JUnit) - primero

## Objetivo

Crear pruebas unitarias para reservas:

- creación
- consulta
- actualización
- eliminación
- obtención por ID

## Dónde mirar código

- `src/main/java/com/project/bookingya/services/ReservationService.java`
- `src/main/java/com/project/bookingya/repositories/IReservationRepository.java`

## Dónde crear pruebas

- `src/test/java/com/project/bookingya/services/ReservationServiceTest.java`

## Cómo crear un test paso a paso (plantilla real)

Usa este flujo para cada test nuevo. No hagas varios al mismo tiempo.

### Paso 1: Define qué comportamiento vas a validar

Ejemplo:

- "crear reserva valida"
- "obtener reserva por id existente"
- "rechazar reserva con fechas invalidas"

### Paso 2: Crea el nombre del test en formato claro

Formato recomendado:

- `accion_shouldResultado_whenCondicion`

Ejemplos:

- `create_shouldCreateReservationSuccessfully`
- `getById_shouldReturnReservationWhenExists`
- `create_shouldThrowBusinessRuleException_whenDateRangeIsInvalid`

### Paso 3: Estructura el test con Arrange / Act / Assert

En el mismo metodo del test:

1. **Arrange**: datos de entrada + mocks (`when(...)`).
2. **Act**: ejecutar metodo del servicio.
3. **Assert**: validar resultado con `assert...` y `verify(...)`.

### Paso 4: Ejecuta solo esa clase de prueba

```powershell
.\mvnw.cmd -Dtest=ReservationServiceTest test
```

Si falla, corrige solo ese test y vuelve a ejecutar.

### Paso 5: Repite con el siguiente caso

No avances hasta ver `BUILD SUCCESS` en la prueba actual.

## Orden sugerido para construir tus tests de ReservationService

1. `create_shouldCreateReservationSuccessfully` (ya creado como base)
2. `getById_shouldReturnReservationWhenExists`
3. `update_shouldUpdateReservationSuccessfully`
4. `delete_shouldDeleteReservationWhenExists`
5. `getById_shouldThrowEntityNotExistsExceptionWhenNotFound`
6. Casos de negocio:
   - fechas invalidas
   - solapamiento por habitacion
   - solapamiento por huesped
   - capacidad excedida

## Por qué así

TDD te enseña a pensar primero en comportamiento esperado, luego en implementación.

## Qué hacer cuando un test falla

1. Lee el mensaje exacto de error (no adivines).
2. Ubica si el fallo es:
   - dato de prueba mal armado
   - mock incompleto
   - regla de negocio no contemplada
3. Corrige lo minimo necesario.
4. Repite ejecucion de la misma clase de test.

## Ejemplo detallado del archivo `ReservationServiceTest.java`

Archivo de referencia:

- `src/test/java/com/project/bookingya/services/ReservationServiceTest.java`

### 1) ¿Qué hace la cabecera del test?

- `@ExtendWith(MockitoExtension.class)` activa Mockito en JUnit 5.
- `@Mock` crea dobles de prueba para:
  - `IReservationRepository`
  - `IRoomRepository`
  - `IGuestRepository`
  - `ModelMapper`

Esto permite probar solo la logica de `ReservationService` sin depender de base de datos real.

### 2) ¿Qué hace `setUp()`?

- En `@BeforeEach` se crea una instancia nueva de `ReservationService`.
- Se inyectan los mocks en el constructor.
- Resultado: cada test arranca limpio y aislado.

### 3) ¿Qué hace el test base `create_shouldCreateReservationSuccessfully()`?

Sigue exactamente `Arrange -> Act -> Assert`:

#### Arrange (preparar datos y mocks)

- Crea UUID de `room`, `guest` y `reservation`.
- Arma el `ReservationDto` con fechas, cantidad de huespedes y notas.
- Arma `RoomEntity` y `GuestEntity` validos (disponible y con capacidad correcta).
- Arma `ReservationEntity` simulando lo que retornaria `saveAndFlush`.
- Configura `when(...)` para indicar comportamiento de repositorios y mapper:
  - room y guest existen
  - no hay solapamientos
  - mapper convierte DTO -> Entity y Entity -> Model
  - repository guarda y retorna entidad persistida

#### Act (ejecutar)

- Ejecuta `reservationService.create(dto)`.

#### Assert (validar)

- Verifica que el resultado no sea null.
- Compara campos clave (`id`, `roomId`, `guestId`, fechas, `guestsCount`, `notes`).
- Verifica interacciones:
  - se llama `saveAndFlush(...)`
  - no se llama `delete(...)`
- Verifica regla temporal basica: `checkIn < checkOut`.

### 4) ¿Qué hace `toReservation(...)`?

- Es un helper para convertir `ReservationEntity` a `Reservation` en el test.
- Se usa para mantener legible el mock de `mapper.map(savedEntity, Reservation.class)`.

### 5) ¿Qué debes copiar para cada test nuevo?

1. Mantener `Arrange/Act/Assert`.
2. Reutilizar la creacion de IDs y datos base.
3. Mockear solo lo que necesita ese caso.
4. Validar resultado + `verify(...)` de interacciones importantes.
5. Ejecutar y confirmar `BUILD SUCCESS` antes de pasar al siguiente test.

---

## Parte B: BDD (Serenity + Cucumber + Gherkin)

## Objetivo

Describir un comportamiento funcional en lenguaje natural y validarlo con automatización.

## Dónde crear archivos

- `src/test/resources/features/reservations.feature`
- Clases Java bajo `src/test/java/com/project/bookingya/bdd/` (configuración Spring, Glue y runner — ver tabla en “Ejecución y evidencias”).

## Qué debes hacer

1. Escribir escenario en Gherkin (`Given/When/Then`).
2. Implementar pasos Java.
3. Ejecutar pruebas BDD.
4. Generar y revisar reporte HTML de Serenity.

## Por qué así

BDD conecta el lenguaje del cliente/profesor con pruebas ejecutables.

---

## Parte C: ATDD (Playwright + TypeScript)

## Objetivo

Validar criterios de aceptación desde el punto de vista del usuario final.

## En este proyecto (backend API)

La opción recomendada es usar Playwright para validar flujos contra API, sin crear frontend nuevo.

## Dónde crear archivos

- `playwright.config.ts`
- `tests/atdd/reservations.api.spec.ts`

## Ejecución

```powershell
npx playwright test
```

## Por qué así

ATDD confirma que lo construido cumple expectativas de negocio, no solo de desarrollo técnico.

### Archivos reales del repo

| Archivo | Rol |
|---------|-----|
| `playwright.config.ts` | Base URL desde `BASE_URL` (por defecto `http://localhost:8080`). |
| `tests/atdd/reservations.api.spec.ts` | Escenarios HTTP contra `/api/...` (crear room, guest, reserva, listar, actualizar, borrar). |

### Cómo levantar la API para ATDD (sin PostgreSQL)

Tras un `.\mvnw.cmd clean package` o `verify`, el JAR queda en `target\`. Ejemplo:

```powershell
java -jar target\bookingya-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
```

En otra terminal (misma máquina):

```powershell
cd <raíz-del-repo>
npm ci
$env:BASE_URL = "http://localhost:8080"
npx playwright test
```

O en una sola línea (PowerShell): ` $env:BASE_URL="http://localhost:8080"; npx playwright test `

Script npm: `npm run test:atdd`.

---

## Ejecución y evidencias (resumen por fase)

### TDD (JUnit / Surefire)

```powershell
.\mvnw.cmd -Dtest=ReservationServiceTest test
.\mvnw.cmd test
```

Qué debe verse en consola:

- Final del build: **`BUILD SUCCESS`**.
- Fragmento típico: **`Tests run: N, Failures: 0`** (valor de `N` según tu suite).

Dónde quedan reportes XML/HTML de Surefire (útil si el profesor pide archivo):

- `target\surefire-reports\`

### BDD (Cucumber + Serenity)

**Rutas de código / feature en este repo:**

- Feature: `src/test/resources/features/reservations.feature`
- Contexto Spring: `src/test/java/com/project/bookingya/bdd/CucumberSpringConfiguration.java`
- Estado compartido: `src/test/java/com/project/bookingya/bdd/BookingYaBddState.java`
- Pasos Glue: `src/test/java/com/project/bookingya/bdd/ReservationStepDefinitions.java`
- Runner: `src/test/java/com/project/bookingya/bdd/BookingYaReservationBddTest.java`

Ejecución Maven (tests BDD dentro del ciclo **`verify`** con agregación Serenity):

```powershell
.\mvnw.cmd clean verify
```

Qué debe verse:

- Logs de Cucumber por escenario y **`BUILD SUCCESS`** al final del `verify`.

Reporte HTML Serenity:

- Abre en el navegador: `target\site\serenity\index.html` (tras `verify`).

**Nota técnica:** `cucumber.properties` fuerza `cucumber.object-factory=io.cucumber.spring.SpringFactory` para que Cucumber Spring y Glue compartan el contexto Spring.

### ATDD (Playwright + TypeScript)

Ver sección “Parte C” arriba. Salida esperada: **`N passed`** (en este proyecto, 5 pruebas en cadena contra la API).

Para plantillas de vídeo/capturas, usa `GUIA_DESDE_CERO\08_EVIDENCIAS_PARA_VIDEO.md` y `GUIA_DESDE_CERO\evidencias\README.md`.

### CI (GitHub Actions)

El workflow ejecuta **`mvn -B clean verify`** y después levanta el JAR con **`--spring.profiles.active=test`** y corre Playwright contra `http://127.0.0.1:8080`. Insignia verde = ambas partes pasaron en el mismo job.

---

## Criterio de progreso para esta etapa

No avances a la entrega final hasta tener evidencia de ejecución en las tres partes.
