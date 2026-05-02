# Etapa 7: Validar todas las APIs en Swagger (con ejemplos listos)

Este archivo te guía para probar todas las APIs del proyecto de forma ordenada.

## 0) Antes de empezar

Debes tener:

- Docker DB corriendo (`docker compose up -d db`)
- Backend corriendo (`.\mvnw.cmd spring-boot:run`)
- Swagger abierto en:
  - `http://localhost:8080/api/swagger-ui/index.html`

## 1) Orden recomendado de pruebas

1. Crear habitación (`POST /api/room`)
2. Crear huésped (`POST /api/guest`)
3. Crear reserva (`POST /api/reservation`)
4. Consultar reserva por ID
5. Consultar reservas por room y por guest
6. Probar disponibilidad de habitación
7. Actualizar reserva
8. Eliminar reserva

---

## 2) APIs de Room

## 2.1 Crear habitación

Endpoint: `POST /api/room`

```json
{
  "code": "HAB-101",
  "name": "Habitacion Doble",
  "city": "Bogota",
  "maxGuests": 2,
  "nightlyPrice": 150000,
  "available": true
}
```

Esperado: `200` con objeto Room (guarda el `id`).

## 2.2 Consultar todas las habitaciones

Endpoint: `GET /api/room`

Esperado: `200` con lista (vacia o con datos).

## 2.3 Consultar habitación por ID

Endpoint: `GET /api/room/{id}`

Usa el `id` creado en 2.1.

## 2.4 Consultar por código

Endpoint: `GET /api/room/code/{code}`

Usa `HAB-101`.

## 2.5 Actualizar habitación

Endpoint: `PUT /api/room/{id}`

```json
{
  "code": "HAB-101",
  "name": "Habitacion Doble Premium",
  "city": "Bogota",
  "maxGuests": 3,
  "nightlyPrice": 180000,
  "available": true
}
```

## 2.6 Eliminar habitación

Endpoint: `DELETE /api/room/{id}`

Nota: hazlo al final para no romper pruebas de reserva.

---

## 3) APIs de Guest

## 3.1 Crear huésped

Endpoint: `POST /api/guest`

```json
{
  "identification": "CC12345678",
  "name": "Juan Perez",
  "email": "juan.perez@example.com"
}
```

Esperado: `200` con objeto Guest (guarda `id`).

## 3.2 Consultar todos

Endpoint: `GET /api/guest`

## 3.3 Consultar por ID

Endpoint: `GET /api/guest/{id}`

## 3.4 Consultar por identificación

Endpoint: `GET /api/guest/identification/{identification}`

Usa `CC12345678`.

## 3.5 Actualizar huésped

Endpoint: `PUT /api/guest/{id}`

```json
{
  "identification": "CC12345678",
  "name": "Juan Perez Actualizado",
  "email": "juan.actualizado@example.com"
}
```

## 3.6 Eliminar huésped

Endpoint: `DELETE /api/guest/{id}`

Nota: hazlo al final para no romper pruebas de reserva.

---

## 4) APIs de Reservation

Necesitas `roomId` y `guestId` ya creados.

## 4.1 Crear reserva

Endpoint: `POST /api/reservation`

```json
{
  "guestId": "REEMPLAZAR_POR_UUID_GUEST",
  "roomId": "REEMPLAZAR_POR_UUID_ROOM",
  "checkIn": "2026-05-10T14:00:00",
  "checkOut": "2026-05-12T11:00:00",
  "guestsCount": 2,
  "notes": "Reserva de prueba"
}
```

Esperado: `200` con objeto Reservation (guarda `id`).

## 4.2 Consultar todas las reservas

Endpoint: `GET /api/reservation`

## 4.3 Consultar reserva por ID

Endpoint: `GET /api/reservation/{id}`

## 4.4 Consultar reservas por habitación

Endpoint: `GET /api/reservation/room/{roomId}`

## 4.5 Consultar reservas por huésped

Endpoint: `GET /api/reservation/guest/{guestId}`

## 4.6 Validar disponibilidad de habitación

Endpoint:

`GET /api/reservation/availability/room/{roomId}?checkIn=2026-05-15T14:00:00&checkOut=2026-05-16T11:00:00`

Esperado: `200` con:

```json
{
  "available": true
}
```

## 4.7 Actualizar reserva

Endpoint: `PUT /api/reservation/{id}`

```json
{
  "guestId": "REEMPLAZAR_POR_UUID_GUEST",
  "roomId": "REEMPLAZAR_POR_UUID_ROOM",
  "checkIn": "2026-05-11T14:00:00",
  "checkOut": "2026-05-13T11:00:00",
  "guestsCount": 2,
  "notes": "Reserva actualizada"
}
```

## 4.8 Eliminar reserva

Endpoint: `DELETE /api/reservation/{id}`

Esperado: `200` sin body.

---

## 5) Casos de error que debes validar (importante para aprender)

## 5.1 Reserva con fecha invalida

`checkIn` mayor o igual a `checkOut`:

```json
{
  "guestId": "REEMPLAZAR_POR_UUID_GUEST",
  "roomId": "REEMPLAZAR_POR_UUID_ROOM",
  "checkIn": "2026-05-12T11:00:00",
  "checkOut": "2026-05-10T14:00:00",
  "guestsCount": 2,
  "notes": "Error esperado"
}
```

Esperado: error de regla de negocio.

## 5.2 Reserva excediendo capacidad

`guestsCount` mayor que `maxGuests` de la habitación.

Esperado: error de capacidad.

## 5.3 Solapamiento de reservas

Crear otra reserva en la misma habitación con fechas cruzadas.

Esperado: error de solapamiento.

---

## 6) Plantilla de evidencias (para tu entrega)

Guarda capturas o registro de:

- `POST /api/room` exitoso
- `POST /api/guest` exitoso
- `POST /api/reservation` exitoso
- `GET /api/reservation` con datos
- 1 caso de error controlado (fecha invalida o solapamiento)

Esto te sirve para sustentar TDD/BDD/ATDD y para explicar comportamiento en el video.
