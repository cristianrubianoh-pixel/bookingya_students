package com.project.bookingya.bdd.steps;

import io.cucumber.java.Before;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.java.es.Y;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReservationSteps {

    @LocalServerPort
    private int port;

    private Response response;
    private UUID guestId;
    private UUID roomId;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }

    @Dado("que existe una habitación disponible y un huésped registrado")
    public void existeHabitacionYHuesped() {

        // Creamos una habitación real en H2
        String uniqueCode = "BDD-" + UUID.randomUUID().toString().substring(0, 8);
        String roomJson = String.format("""
            {
                "code": "%s",
                "name": "Habitación BDD",
                "city": "Bogotá",
                "maxGuests": 3,
                "nightlyPrice": 150000,
                "available": true
            }
            """, uniqueCode);

        Response roomResponse = given()
            .contentType(ContentType.JSON)
            .body(roomJson)
            .when()
            .post("/room");

        roomId = UUID.fromString(roomResponse.jsonPath().getString("id"));

        // Creamos un huésped real en H2
        String uniqueEmail = "bdd-" + UUID.randomUUID().toString().substring(0, 8) + "@test.com";
        String uniqueId = "BDD-" + UUID.randomUUID().toString().substring(0, 8);
        String guestJson = String.format("""
            {
                "identification": "%s",
                "name": "Huésped BDD",
                "email": "%s"
            }
            """, uniqueId, uniqueEmail);

        Response guestResponse = given()
            .contentType(ContentType.JSON)
            .body(guestJson)
            .when()
            .post("/guest");

        guestId = UUID.fromString(guestResponse.jsonPath().getString("id"));
    }

    @Cuando("el cliente envía los datos de la reserva")
    public void elClienteEnviaLosDatos() {

        String reservationJson = String.format("""
            {
                "guestId": "%s",
                "roomId": "%s",
                "checkIn": "2027-06-01T14:00:00",
                "checkOut": "2027-06-05T12:00:00",
                "guestsCount": 2,
                "notes": "Prueba BDD"
            }
            """, guestId, roomId);

        response = given()
            .contentType(ContentType.JSON)
            .body(reservationJson)
            .when()
            .post("/reservation");
    }

    @Entonces("el sistema responde con código {int}")
    public void elSistemaRespondeConCodigo(int codigoEsperado) {
        assertEquals(codigoEsperado, response.statusCode());
    }

    @Y("la reserva creada tiene un ID asignado")
    public void laReservaCreadaTieneUnId() {
        String id = response.jsonPath().getString("id");
        assertNotNull(id, "La reserva debe tener un ID asignado");
    }
}
