package com.project.bookingya.bdd;

import org.junit.runner.RunWith;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

/**
 * Runner BDD: Cucumber JUnit 4 Classic + Vintage + Serenity.
 */
@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features = "classpath:features/reservations.feature",
    glue = "com.project.bookingya.bdd",
    tags = "@reservations",
    plugin = { "pretty" }
)
public class BookingYaReservationBddTest {
}
