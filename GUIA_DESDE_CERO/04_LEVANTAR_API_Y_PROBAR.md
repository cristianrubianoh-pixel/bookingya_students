# Etapa 4: Levantar API y comprobar que responde

Con la base arriba, ahora arranca el backend.

Nota: en este repositorio ya se corrigio la causa raiz de configuracion.
Ahora `application.properties` esta en rutas estandar:

- `src/main/resources/application.properties`
- `src/test/resources/application.properties`

## 1) Arrancar API

```powershell
.\mvnw.cmd spring-boot:run
```

## 2) Qué debes ver en consola (si todo está bien)

- Mensaje de Spring Boot iniciado.
- Puerto `8080` activo.
- Sin errores de DataSource.

Si aparece algo como:

- `Failed to configure a DataSource`
- `'url' attribute is not specified`

significa que la app no está leyendo bien la configuración de base de datos.

## 3) Acciones exactas cuando sale ese error

Ejecuta estos pasos en este orden.

### Paso A: Validar que exista `.env`

```powershell
if (Test-Path .env) { "OK: .env existe" } else { "ERROR: falta .env" }
```

Si falta, créalo:

```powershell
copy .env.example .env
```

### Paso B: Corregir `.env`

Abre `.env` y deja exactamente este formato (sin espacios después de `=`):

```env
HOST_DB=localhost
PORT_DB=5432
POSTGRES_DB=bookingya_students
USERNAME_DB=postgres
PASSWORD_DB=tu_password
```

Errores comunes:
- poner `PASSWORD_DB= mi_clave` (ese espacio rompe conexión)
- typo en `POSTGRES_DB` (por ejemplo `stundets`)

### Paso C: Levantar base en Docker

```powershell
docker compose up -d db
docker ps
docker compose logs db
```

Debes ver el contenedor `db` corriendo sin error fatal.

### Paso D: Reintentar arranque de la API

```powershell
.\mvnw.cmd spring-boot:run
```

Si aún falla, para diagnóstico:

```powershell
.\mvnw.cmd spring-boot:run -e
```

## 4) Validar con Swagger

En navegador:

- `http://localhost:8080/api/swagger-ui/index.html`

Si abre, la API está funcionando.

## 5) Prueba rápida de endpoint

Desde Swagger:
- prueba `GET /api/reservation`
- valida respuesta HTTP 200

## 6) Estado de tu caso actual (registro de aprendizaje)

Con base en el error visto en terminal:

- La API intentó arrancar.
- Tomcat sí subió en `8080`.
- Falló al crear DataSource porque no encontró URL válida.

Conclusión: el siguiente intento debe hacerse solo después de validar `.env` + `docker compose up -d db`.

## 7) Qué aprendiste aquí

Primero se valida "configuración de entorno + base de datos", luego "arranque de app", y solo después "pruebas funcionales".
