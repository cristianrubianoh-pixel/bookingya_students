package com.project.bookingya.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.project.bookingya.models.Guest;
import com.project.bookingya.models.Reservation;
import com.project.bookingya.models.Room;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ReservationStepDefinitions {

    @Autowired
    private BookingYaBddState state;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private String baseUrl(String path) {
        return "http://127.0.0.1:" + port + "/api" + path;
    }

    @Given("que existe una habitacion disponible")
    public void habitacion_disponible() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", "BDD-" + UUID.randomUUID());
        body.put("name", "Habitacion BDD");
        body.put("city", "Bogota");
        body.put("maxGuests", 3);
        body.put("nightlyPrice", state.nightlyPrice());
        body.put("available", true);

        ResponseEntity<Room> resp = restTemplate.postForEntity(baseUrl("/room"), body, Room.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getId()).isNotNull();

        state.roomId = resp.getBody().getId();
    }

    @Given("que existe un huesped registrado")
    public void huesped_registrado() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("identification", "ID-" + UUID.randomUUID());
        body.put("name", "Maria BDD");
        body.put("email", "bdd-" + UUID.randomUUID() + "@example.com");

        ResponseEntity<Guest> resp = restTemplate.postForEntity(baseUrl("/guest"), body, Guest.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getId()).isNotNull();

        state.guestId = resp.getBody().getId();
    }

    @When("registro una reserva valida entre {string} y {string} con {int} personas")
    public void registro_reserva(String desde, String hasta, int personas) {
        LocalDateTime in = LocalDateTime.parse(desde);
        LocalDateTime out = LocalDateTime.parse(hasta);
        state.checkIn = in;
        state.checkOut = out;
        state.lastGuestsCount = personas;

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("guestId", state.guestId.toString());
        body.put("roomId", state.roomId.toString());
        body.put("checkIn", desde);
        body.put("checkOut", hasta);
        body.put("guestsCount", personas);
        body.put("notes", state.reservationNotes);

        ResponseEntity<Reservation> resp = restTemplate.postForEntity(baseUrl("/reservation"), body, Reservation.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        state.reservationId = resp.getBody().getId();
    }

    @Then("consulto todas las reservas y encuentro mi reserva por id")
    public void encuentro_mi_reserva() {
        ResponseEntity<List<Reservation>> response = restTemplate.exchange(
            baseUrl("/reservation"),
            HttpMethod.GET,
            HttpEntity.EMPTY,
            new ParameterizedTypeReference<List<Reservation>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        Reservation found = response.getBody().stream()
            .filter(r -> state.reservationId.equals(r.getId()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("La reserva creada no aparece en el listado"));

        assertThat(found.getRoomId()).isEqualTo(state.roomId);
        assertThat(found.getGuestId()).isEqualTo(state.guestId);
        assertThat(found.getCheckIn()).isEqualTo(state.checkIn);
        assertThat(found.getCheckOut()).isEqualTo(state.checkOut);
    }

    @When("consulto una reserva existente por su id")
    public void consulto_por_id() {
        ResponseEntity<Reservation> resp = restTemplate.getForEntity(
            baseUrl("/reservation/" + state.reservationId),
            Reservation.class
        );
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getId()).isEqualTo(state.reservationId);
    }

    @Then("los datos coinciden con la creacion inicial")
    public void coincide_con_creacion() {
        Reservation r = restTemplate.getForEntity(
            baseUrl("/reservation/" + state.reservationId),
            Reservation.class
        ).getBody();

        assertThat(r).isNotNull();
        assertThat(r.getNotes()).isEqualTo(state.reservationNotes);
        assertThat(r.getGuestsCount()).isEqualTo(state.lastGuestsCount);
        assertThat(r.getCheckIn()).isEqualTo(state.checkIn);
        assertThat(r.getCheckOut()).isEqualTo(state.checkOut);
    }

    @When("actualizo la reserva cambiando notas a {string} y ocupacion a {int}")
    public void actualizo_reserva(String notas, int personas) {
        state.lastGuestsCount = personas;

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("guestId", state.guestId.toString());
        body.put("roomId", state.roomId.toString());
        body.put("checkIn", state.checkIn.toString());
        body.put("checkOut", state.checkOut.toString());
        body.put("guestsCount", personas);
        body.put("notes", notas);

        ResponseEntity<Reservation> resp = restTemplate.exchange(
            baseUrl("/reservation/" + state.reservationId),
            HttpMethod.PUT,
            new HttpEntity<>(body),
            Reservation.class
        );
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isNotNull();
    }

    @Then("la reserva muestra las notas {string} y ocupacion {int}")
    public void muestra_notas_y_ocupacion(String esperadoNotas, int esperadoPersonas) {
        Reservation r = restTemplate.getForEntity(
            baseUrl("/reservation/" + state.reservationId),
            Reservation.class
        ).getBody();

        assertThat(r).isNotNull();
        assertThat(r.getNotes()).isEqualTo(esperadoNotas);
        assertThat(r.getGuestsCount()).isEqualTo(esperadoPersonas);
    }

    @When("cancelo la reserva mediante DELETE")
    public void cancelar_reserva() {
        ResponseEntity<Void> resp = restTemplate.exchange(
            baseUrl("/reservation/" + state.reservationId),
            HttpMethod.DELETE,
            HttpEntity.EMPTY,
            Void.class
        );
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Then("al consultar la reserva ya no debe existir")
    public void no_debe_existir() {
        @SuppressWarnings({"rawtypes", "unchecked"})
        ResponseEntity<Map> resp = restTemplate.getForEntity(
            baseUrl("/reservation/" + state.reservationId),
            Map.class
        );

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
