package com.project.bookingya.bdd;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import io.cucumber.spring.ScenarioScope;

/**
 * Estado compartido por escenario (Cucumber glue scope).
 */
@Component
@ScenarioScope
public class BookingYaBddState {

    UUID roomId;
    UUID guestId;
    UUID reservationId;
    LocalDateTime checkIn = LocalDateTime.of(2030, 6, 1, 14, 0);
    LocalDateTime checkOut = LocalDateTime.of(2030, 6, 3, 11, 0);

    /** Notas utilizadas en el POST inicial de cada escenario. */
    final String reservationNotes = "Creacion desde BDD";
    /** Ultima cantidad de personas enviada al crear actualizar cuando aplique */
    int lastGuestsCount;

    BigDecimal nightlyPrice() {
        return BigDecimal.valueOf(150000);
    }
}
