# Etapa 8: Evidencias para el vídeo y la sustentación

Objetivo: grabar una demostración de **3–5 minutos** mostrando TDD, BDD y ATDD con comandos reproducibles y, si el profesor lo pide, capturas PNG.

---

## Lista de chequeo antes de grabar

1. JDK 17 y Maven wrapper funcionando (`.\mvnw.cmd -v`).
2. Node/npm instalados (`node -v`, `npm -v`).
3. Para ATDD contra API **sin Docker**: tener el **JAR** construido o poder ejecutar `verify` antes.
4. No mostrar valores reales del archivo `.env` (contraseñas de PostgreSQL). En la grabación usa **perfil `test`** (H2) para backend.

---

## Guión corto recomendado (orden)

| Min | Qué decir | Qué mostrar en pantalla |
|-----|-----------|--------------------------|
| 0:00–0:45 | Alcance del entregable: API de reservas, tres niveles de prueba | Repo en el IDE o README |
| 0:45–1:45 | **TDD**: pruebas unitarias de negocio con JUnit/Mockito | Terminal: `.\mvnw.cmd -Dtest=ReservationServiceTest test` → `BUILD SUCCESS` |
| 1:45–3:00 | **BDD**: Gherkin + Serenity después de verify | Terminal: `.\mvnw.cmd clean verify` → navegador: `target\site\serenity\index.html` |
| 3:00–4:15 | **ATDD**: Playwright contra HTTP | Terminal: API levantada con `--spring.profiles.active=test`; luego `npx playwright test` |
| 4:15–5:00 | **CI opcional**: flujo GitHub Actions verde | Navegador: pestaña Actions del repositorio, job completado |

---

## Comandos → qué grabar vs qué transcribir

| Comando | En vídeo (pantalla) | En documento/transcripción |
|---------|---------------------|----------------------------|
| `.\mvnw.cmd -Dtest=ReservationServiceTest test` | Consola completa hasta `BUILD SUCCESS`; fragmento `Tests run: …` | Copiar texto de `Tests run:` y tiempo |
| `.\mvnw.cmd clean verify` | Inicio del build y línea final `BUILD SUCCESS` | Nota: “Se generó Serenity en `target/site/serenity/`” |
| Abrir Serenity | Navegador con el índice del reporte y un escenario verde | Ruta exacta del archivo |
| `java -jar target\...\jar --spring.profiles.active=test` | Log de arranque con “Started …Application” | Indicar puerto `8080` y context-path `/api` |
| `npx playwright test` | Salida “N passed” | Número de tests y tiempo |
| GitHub Actions | Job `verify-and-atdd` verde | Enlace al run o screenshot de la lista de pasos |

---

## Dónde guardar PNG (opcional)

Usa la carpeta `GUIA_DESDE_CERO/evidencias/` y nombres sugeridos:

- `YYYYMMDD_tdd_console.png` — resultado de Maven test (TDD).
- `YYYYMMDD_bdd_serenity_report.png` — reporte Serenity abierto en el navegador.
- `YYYYMMDD_atdd_playwright.png` — consola tras `npx playwright test`.
- `YYYYMMDD_github_actions_green.png` — workflow verde.

No incluyas secretos ni tokens en las capturas.

---

## Swagger (opcional)

Si quieres mostrar los endpoints al profesor en el vídeo:

- Con la API en marcha, abre la URL de OpenAPI/Swagger que tengas configurada (consulta `04_LEVANTAR_API_Y_PROBAR.md`).
- Mantén la demostración breve: un GET y un POST bastan como apoyo visual; la **evidencia obligatoria** son las suites automatizadas.

---

## Transcripción

Puedes pegar debajo de cada sección en `evidencias/README.md` (plantilla) el texto literal de la consola el día de la grabación, para no depender solo del audio del vídeo.
