# Etapa 3: Levantar PostgreSQL con Docker

Aquí vas a prender la base de datos en un contenedor.

## 1) Ir a la carpeta del proyecto

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
```

## 2) Levantar solo la base de datos

```powershell
docker compose up -d db
```

## 3) Validar que está corriendo

```powershell
docker ps
```

Debes ver un contenedor de postgres activo.

## 4) Ver logs de la base

```powershell
docker compose logs db
```

Resultado esperado: logs de PostgreSQL sin error fatal.

## 5) Apagar la base (cuando termines)

```powershell
docker compose stop db
```

## 6) Si falla

- Error de credenciales: revisa `.env`.
- Puerto ocupado (`5432`): cierra otro PostgreSQL local o cambia `PORT_DB`.
- Error WSL/Docker Desktop: abre Docker Desktop y espera estado "Engine running".

## 7) Qué aprendiste aquí

Docker te permite levantar servicios externos (como DB) sin instalar todo manualmente en Windows.
