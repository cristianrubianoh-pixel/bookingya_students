# Etapa 1: Preparar el computador

En esta etapa solo vamos a validar herramientas. No vamos a programar todavía.

## 1) Abre PowerShell

- En Windows, presiona `Win`, escribe `PowerShell` y abre la terminal.

## 2) Ejecuta estos comandos uno por uno

```powershell
git --version
java -version
.\mvnw.cmd -version
mvn -version
docker --version
docker compose version
wsl --version
node --version
npm --version
```

## 3) Resultado esperado

Cada comando debe responder con una versión y no con error.

## 4) Si algo falla

- Si falla `docker` o `docker compose`: instala Docker Desktop y reinicia el PC.
- Si falla `wsl`: ejecuta `wsl --update` y luego `wsl --shutdown`.
- Si falla `java`: instala Java 17.
- Si falla `git`: instala Git for Windows.
- Si falla `mvnw.cmd`: ejecuta este comando dentro de la carpeta del proyecto.

## 5) Qué aprendiste aquí

Antes de programar, primero se valida el entorno. Esto evita perder tiempo buscando errores que no son del código.
