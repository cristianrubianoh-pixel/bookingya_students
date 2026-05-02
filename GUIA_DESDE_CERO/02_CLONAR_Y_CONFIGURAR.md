# Etapa 2: Clonar proyecto y configurar `.env`

Aquí vamos a dejar el proyecto listo para correr localmente.

## 1) Clonar tu fork

Si no lo has clonado:

```powershell
git clone <URL_DE_TU_FORK>
cd bookingya_students
```

Si ya lo tienes, entra a la carpeta:

```powershell
cd C:\Users\Sebastian-PC\DEV\ESPECIALIDAD\bookingya_students
```

## 2) Crear rama de trabajo

```powershell
git switch -c dev-sc
```

Si ya existe, usa:

```powershell
git switch dev-sc
```

## 3) Crear archivo `.env`

```powershell
copy .env.example .env
```

## 4) Editar `.env` (muy importante)

Deja valores correctos y sin espacios extra:

```env
HOST_DB=localhost
PORT_DB=5432
POSTGRES_DB=bookingya_students
USERNAME_DB=postgres
PASSWORD_DB=tu_password
```

Notas:
- No pongas espacios después del `=`.
- Usa el nombre de base de datos sin errores de escritura.

## 5) Qué aprendiste aquí

`.env` conecta tu app con la base de datos. Si está mal, la app no arranca.
