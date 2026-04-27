# language: es
Característica: Gestión de Reservas
  Como usuario de la plataforma BookingYa
  Quiero crear una reserva de habitación
  Para confirmar mi alojamiento

  Escenario: Crear una reserva exitosamente
    Dado que existe una habitación disponible y un huésped registrado
    Cuando el cliente envía los datos de la reserva
    Entonces el sistema responde con código 200
    Y la reserva creada tiene un ID asignado
