import { test, expect } from '@playwright/test';

/**
 * Criterios de aceptación HTTP sobre el prefijo `/api` (server.servlet.context-path).
 * Requiere API en ejecución; en local: `java -jar target/bookingya-0.0.1-SNAPSHOT.jar --spring.profiles.active=test`
 * o `./mvnw spring-boot:run -Dspring-boot.run.profiles=test`.
 */
test.describe.serial('Reservas — API ATDD', () => {
  const suffix = Date.now();
  let roomId: string;
  let guestId: string;
  let reservationId: string;

  test('POST /api/room y POST /api/guest crean entidades válidas', async ({ request }) => {
    const roomRes = await request.post('/api/room', {
      data: {
        code: `ATDD-R-${suffix}`,
        name: 'Habitación ATDD',
        city: 'Medellín',
        maxGuests: 4,
        nightlyPrice: 120.5,
        available: true,
      },
    });
    expect.soft(roomRes.status(), 'crear habitación').toBe(200);
    const room = await roomRes.json();
    expect(room.id).toBeTruthy();
    roomId = room.id as string;

    const guestRes = await request.post('/api/guest', {
      data: {
        identification: `DOC-ATDD-${suffix}`,
        name: 'Huésped ATDD',
        email: `atdd.${suffix}@example.com`,
      },
    });
    expect.soft(guestRes.status(), 'crear huésped').toBe(200);
    const guest = await guestRes.json();
    expect(guest.id).toBeTruthy();
    guestId = guest.id as string;
  });

  test('POST /api/reservation crea reserva y GET /api/reservation/{id} la devuelve', async ({
    request,
  }) => {
    const checkIn = '2030-06-01T14:00:00';
    const checkOut = '2030-06-05T11:00:00';

    const createRes = await request.post('/api/reservation', {
      data: {
        guestId,
        roomId,
        checkIn,
        checkOut,
        guestsCount: 2,
        notes: 'ATDD acceptance',
      },
    });
    expect(createRes.ok(), await createRes.text()).toBeTruthy();
    expect(createRes.status()).toBe(200);
    const created = await createRes.json();
    expect(created.id).toBeTruthy();
    reservationId = created.id as string;

    const getOne = await request.get(`/api/reservation/${reservationId}`);
    expect(getOne.ok()).toBeTruthy();
    const body = await getOne.json();
    expect(body.roomId).toBe(roomId);
    expect(body.guestId).toBe(guestId);
    expect(body.checkIn).toContain('2030-06-01');
    expect(body.notes).toBe('ATDD acceptance');
  });

  test('GET /api/reservation incluye la reserva creada', async ({ request }) => {
    const list = await request.get('/api/reservation');
    expect(list.ok()).toBeTruthy();
    const arr = await list.json();
    expect(Array.isArray(arr)).toBeTruthy();
    const found = arr.find((r: { id: string }) => r.id === reservationId);
    expect(found).toBeTruthy();
  });

  test('PUT /api/reservation/{id} actualiza la reserva', async ({ request }) => {
    const put = await request.put(`/api/reservation/${reservationId}`, {
      data: {
        guestId,
        roomId,
        checkIn: '2030-06-02T14:00:00',
        checkOut: '2030-06-06T11:00:00',
        guestsCount: 3,
        notes: 'ATDD updated',
      },
    });
    expect(put.ok(), await put.text()).toBeTruthy();
    const updated = await put.json();
    expect(updated.guestsCount).toBe(3);
    expect(updated.notes).toBe('ATDD updated');
  });

  test('DELETE /api/reservation/{id} elimina y GET posterior falla', async ({ request }) => {
    const del = await request.delete(`/api/reservation/${reservationId}`);
    expect(del.ok()).toBeTruthy();

    const get404 = await request.get(`/api/reservation/${reservationId}`);
    expect(get404.status()).toBe(404);
  });
});
