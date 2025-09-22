package ru.yandex.practicum.tests;

import ru.yandex.practicum.pojo.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.steps.CourierSteps;

import static java.net.HttpURLConnection.*;

import static org.hamcrest.CoreMatchers.*;

public class CourierCreateTest extends BaseTest {
    private CourierSteps courierSteps = new CourierSteps();
    private Courier courier;

    @Before
    public void setUP() {
        Faker faker = new Faker();
        courier = new Courier();
        courier.withLogin(faker.name().firstName().toLowerCase() +
                        faker.number().digits(2))
                .withPassword(faker.internet().password());
    }

    @Test
    @DisplayName("Успешное создание курьера с передачей всех обязательных полей")
    @Description("Проверяет, что при передаче всех обязательных параметров будет успешно создана учетная запись курьера: HTTP/1.1 201 Created, body: ok: true")
    public void courierSuccessfulCreateTest() {
        courierSteps
                .createCourier(courier)
                .statusCode(HTTP_CREATED)
                .body("ok", is(true));
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    @Description("Проверяет, что при повторном создании курьера с уже существующим логином, получаем ошибку: HTTP/1.1 409 Сonflict, body: \"message\": \"Этот логин уже используется\"")
    public void courierDoubleCreateTest() {
        courierSteps
                .createCourier(courier);
        courierSteps
                .createCourier(courier)
                .statusCode(HTTP_CONFLICT)
                .body("message", is("Этот логин уже используется"));
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверяет, что при попытке создать курьера без логина,получаем ошибку: HTTP/1.1 400 Bad Request, body: \"message\": \"Недостаточно данных для создания учетной записи\"")
    public void courierWithoutLoginCreateTets() {
        courier.withLogin("");
        courierSteps
                .createCourier(courier)
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверяет, что при попытке создать курьера без пароля, получаем ошибку: HTTP/1.1 400 Bad Request, body: \"message\": \"Недостаточно данных для создания учетной записи\"")

    public void courierWithoutPasswordCreateTest() {
        courier.withPassword("");
        courierSteps
                .createCourier(courier)
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без логина и пароля")
    @Description("Проверяет, что при попытке создать курьера без логина и пароля, получаем ошибку: HTTP/1.1 400 Bad Request, body: \"message\": \"Недостаточно данных для создания учетной записи\"")

    public void courierWithoutLoginAndPasswordCreateTest() {
        courier.withLogin("");
        courier.withPassword("");
        courierSteps
                .createCourier(courier)
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", is("Недостаточно данных для создания учетной записи"));
    }


    @After
    public void tearDown() {
        try {
            if (courier.getId() != null) {
                Integer id = courierSteps
                        .loginCourier(courier)
                        .extract().body().path("id");
                courier.withId(id);
                courierSteps.deleteCourier(courier);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при очистке данных" + e.getMessage());
        }
    }
}
