import { test, expect, request } from '@playwright/test';

// URL base configurada en playwright.config.ts
const BASE_URL = 'http://localhost:8080/api';

// Variables compartidas entre tests
let guestId: string;
let roomId: string;
let reservationId: string;

// Se ejecuta UNA vez antes de todos los tests
// Crea los datos necesarios en la BD real
test.beforeAll(async () => {
  const api = await request.newContext({
    baseURL: BASE_URL,
    extraHTTPHeaders: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    }
  });
  const suffix = Date.now().toString().slice(-6);

  // Crear huésped real
  const guestRes = await api.post(`${BASE_URL}/guest`, {
    data: {
      identification: `ATDD-${suffix}`,
      name: 'Huesped ATDD',
      email: `atdd${suffix}@test.com`
    }
  });
  const guest = await guestRes.json();
  guestId = guest.id;

  // Crear habitación real
  const roomRes = await api.post(`${BASE_URL}/room`, {
    data: {
      code: `ATDD-${suffix}`,
      name: 'Habitacion ATDD',
      city: 'Bogota',
      maxGuests: 3,
      nightlyPrice: 150000,
      available: true
    }
  });
  const room = await roomRes.json();
  roomId = room.id;

  await api.dispose();
});

// TEST 1 — Consultar todas las reservas
test('AT-01: Debe retornar lista de reservas', async ({ request }) => {
  const response = await request.get(`${BASE_URL}/reservation`);

  expect(response.status()).toBe(200);

  const body = await response.json();
  expect(Array.isArray(body)).toBeTruthy();

  console.log(`Total reservas: ${body.length}`);
});

// TEST 2 — Crear una reserva
test('AT-02: Debe crear una reserva exitosamente', async ({ request }) => {
  const response = await request.post(`${BASE_URL}/reservation`, {
    data: {
      guestId: guestId,
      roomId: roomId,
      checkIn: '2027-08-01T14:00:00',
      checkOut: '2027-08-05T12:00:00',
      guestsCount: 2,
      notes: 'Prueba ATDD Playwright'
    }
  });

  expect(response.status()).toBe(200);

  const reserva = await response.json();
  expect(reserva.id).toBeTruthy();
  expect(reserva.guestId).toBe(guestId);
  expect(reserva.roomId).toBe(roomId);

  reservationId = reserva.id;
  console.log(`Reserva creada: ${reservationId}`);
});

// TEST 3 — Obtener reserva por ID
test('AT-03: Debe obtener una reserva por su ID', async ({ request }) => {
  const response = await request.get(`${BASE_URL}/reservation/${reservationId}`);

  expect(response.status()).toBe(200);

  const reserva = await response.json();
  expect(reserva.id).toBe(reservationId);
  expect(reserva.guestId).toBe(guestId);
  expect(reserva.roomId).toBe(roomId);
});

// TEST 4 — Actualizar una reserva
test('AT-04: Debe actualizar una reserva existente', async ({ request }) => {
  const response = await request.put(`${BASE_URL}/reservation/${reservationId}`, {
    data: {
      guestId: guestId,
      roomId: roomId,
      checkIn: '2027-08-01T14:00:00',
      checkOut: '2027-08-05T12:00:00',
      guestsCount: 3,
      notes: 'Actualizado por ATDD'
    }
  });

  expect(response.status()).toBe(200);

  const reserva = await response.json();
  expect(reserva.guestsCount).toBe(3);
  console.log(`Reserva actualizada. Huespedes: ${reserva.guestsCount}`);
});

// TEST 5 — Eliminar una reserva
test('AT-05: Debe eliminar una reserva existente', async ({ request }) => {
  const deleteRes = await request.delete(`${BASE_URL}/reservation/${reservationId}`);
  expect(deleteRes.status()).toBe(200);

  // Verificar que ya no existe
  const getRes = await request.get(`${BASE_URL}/reservation/${reservationId}`);
  expect(getRes.status()).toBe(404);

  console.log(`Reserva ${reservationId} eliminada correctamente`);
});
