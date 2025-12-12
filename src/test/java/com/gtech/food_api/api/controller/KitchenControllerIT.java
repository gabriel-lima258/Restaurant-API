package com.gtech.food_api.api.controller;

import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.hamcrest.Matchers;
import com.gtech.food_api.util.DatabaseCleaner;
import com.gtech.food_api.domain.model.Kitchen;
import com.gtech.food_api.domain.repository.KitchenRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class KitchenControllerIT {

    private static final String BASE_PATH = "/kitchens";
    private static final String KITCHEN_NAME_BRASILEIRA = "Brasileira";
    private static final String KITCHEN_NAME_JAPONESA = "Japonesa";
    private static final String KITCHEN_NAME_CHILENA = "Chilena";
    private static final int EXPECTED_KITCHENS_COUNT = 2;
    private static final long NON_EXISTENT_KITCHEN_ID = 999L;

    @LocalServerPort
    private int port;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private KitchenRepository kitchenRepository;

    private Long brasileiraKitchenId;

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.basePath = BASE_PATH;
        RestAssured.port = port;
        
        databaseCleaner.clearTables();
        prepareData();
    }

    @Test
    public void shouldReturnOkWhenListKitchens() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void shouldReturnOkWithCorrectKitchensWhenListKitchens() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("", Matchers.hasSize(EXPECTED_KITCHENS_COUNT))
            .body("name", Matchers.hasItems(KITCHEN_NAME_BRASILEIRA, KITCHEN_NAME_JAPONESA));
    }

    @Test
    public void shouldReturnCreatedWhenCreateKitchen() {
        given()
            .body(createKitchenJson(KITCHEN_NAME_CHILENA))
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void shouldReturnOkWithKitchenWhenFindById() {
        given()
            .pathParam("id", brasileiraKitchenId)
            .accept(ContentType.JSON)
        .when()
            .get("/{id}")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("name", Matchers.equalTo(KITCHEN_NAME_BRASILEIRA));
    }

    @Test
    public void shouldReturnNotFoundWhenKitchenDoesNotExist() {
        given()
            .pathParam("id", NON_EXISTENT_KITCHEN_ID)
            .accept(ContentType.JSON)
        .when()
            .get("/{id}")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    private void prepareData() {
        brasileiraKitchenId = createAndSaveKitchen(KITCHEN_NAME_BRASILEIRA).getId();
        createAndSaveKitchen(KITCHEN_NAME_JAPONESA);
    }

    private Kitchen createAndSaveKitchen(String name) {
        Kitchen kitchen = new Kitchen();
        kitchen.setName(name);
        return kitchenRepository.save(kitchen);
    }

    private String createKitchenJson(String name) {
        return String.format("{\"name\": \"%s\"}", name);
    }
}
