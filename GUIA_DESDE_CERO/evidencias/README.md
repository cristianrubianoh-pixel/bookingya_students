# Carpeta de evidencias (vídeo y entrega)

Aquí puedes **pegar capturas de pantalla** y **texto de consola** para la sustentación. No subas archivos con contraseñas ni datos personales reales.

## Convención de nombres (PNG)

| Archivo sugerido | Contenido |
|------------------|-----------|
| `YYYYMMDD_tdd_console.png` | Salida de `.\mvnw.cmd -Dtest=ReservationServiceTest test` o `.\mvnw.cmd test` |
| `YYYYMMDD_bdd_serenity_report.png` | Reporte `target/site/serenity/index.html` en el navegador |
| `YYYYMMDD_atdd_playwright.png` | Salida de `npx playwright test` |
| `YYYYMMDD_github_actions_green.png` | Job de CI verde en GitHub |

Sustituye `YYYYMMDD` por la fecha de la grabación (ej. `20260502`).

## Plantilla de texto (copiar y rellenar)

### TDD — consola

```text
Fecha / hora:
Comando ejecutado:
Resultado (pegar desde “Tests run:” hasta “BUILD SUCCESS”):

```

### BDD — Serenity

```text
Fecha / hora:
Comando: .\mvnw.cmd clean verify
Ruta del reporte abierto: target/site/serenity/index.html
Notas (escenarios vistos en el reporte):

```

### ATDD — Playwright

```text
Fecha / hora:
Cómo levantaste la API (perfil test / H2):
Comando: npx playwright test
Resultado (líneas “N passed”):

```

### CI

```text
Enlace al workflow run:
Rama:
Job que pasó (nombre en GitHub Actions):

```

## Privacidad

- No incluyas el contenido real de `.env`.
- Si compartes enlace a Actions, revisa que el repo y la política de la universidad lo permitan.
