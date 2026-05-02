@reservations
Feature: Gestion end-to-end de reservas BookingYa via API REST

  Background:
    Given que existe una habitacion disponible
    And que existe un huesped registrado

  Scenario: Creacion de reserva y consulta masiva via GET
    When registro una reserva valida entre "2030-06-01T14:00:00" y "2030-06-03T11:00:00" con 2 personas
    Then consulto todas las reservas y encuentro mi reserva por id

  Scenario: Consulta de una reserva por identificador
    When registro una reserva valida entre "2030-06-05T14:00:00" y "2030-06-07T11:00:00" con 2 personas
    And consulto una reserva existente por su id
    Then los datos coinciden con la creacion inicial

  Scenario: Actualizacion de reserva existente
    When registro una reserva valida entre "2030-07-01T14:00:00" y "2030-07-04T11:00:00" con 2 personas
    When actualizo la reserva cambiando notas a "Actualizada desde BDD" y ocupacion a 1
    Then la reserva muestra las notas "Actualizada desde BDD" y ocupacion 1

  Scenario: Eliminacion (cancelacion) de reserva
    When registro una reserva valida entre "2030-08-01T14:00:00" y "2030-08-03T11:00:00" con 2 personas
    When cancelo la reserva mediante DELETE
    Then al consultar la reserva ya no debe existir
