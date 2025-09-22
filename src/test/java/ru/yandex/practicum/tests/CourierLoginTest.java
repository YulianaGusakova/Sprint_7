package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import ru.yandex.practicum.constants.CourierTestData;
import ru.yandex.practicum.pojo.Courier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.steps.CourierSteps;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierLoginTest extends BaseTest {
    private final CourierSteps courierSteps = new CourierSteps();
    private Courier courier;

    @Before
    public void setUP() {
        Faker faker = new Faker();
        courier = new Courier();
               courier.withLogin(faker.name().firstName().toLowerCase() +
                        faker.number().digits(2))
                .withPassword(faker.internet().password());
        courierSteps.createCourier(courier);
    }

    @Test
    @DisplayName("Успешная авторизация при передаче всех обязательных полей")
    @Description("Проверяет, что при авторизации со всеми обязательными параметрами, авторизация завершается успешно (HTTP/1.1 200), и получаем id курьера")

    public void courierLoginTest() {
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_OK)
                .body("id", notNullValue());
    }


    @Test
    @DisplayName("Авторизация без логина")
    @Description("Проверяет, что при попытке авторизации курьера без логина, получаем ошибку: HTTP/1.1 400 Bad Request, body: \"message\":  \"Недостаточно данных для входа\"")

    public void courierLoginWithoutLoginTest() {

        courier.withLogin("");
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Проверяет, что при попытке авторизации курьера без пароля, получаем ошибку: HTTP/1.1 400 Bad Request, body: \"message\":  \"Недостаточно данных для входа\"")

    public void courierLoginWithoutPasswordTest() {
        courier.withPassword("");
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без логина и пароля")
    @Description("Проверяет, что при попытке авторизации курьера без логина и пароля, получаем ошибку: HTTP/1.1 400 Bad Request, body: \"message\":  \"Недостаточно данных для входа\"")

    public void courierLoginWithoutLoginAndPasswordTest() {
        courier.withLogin("");
        courier.withPassword("");
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Проверяет, что при попытке авторизации курьера с несуществующим логином, получаем ошибку: HTTP/1.1 404 Not Found, body: \"message\": \"Учетная запись не найдена\"")

    public void courierLoginWithIncorrectLoginTest() {
        courier.withLogin(CourierTestData.INCORRECT_LOGIN);
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверяет, что при попытке авторизации курьера с существующим логином и неверным паролем, получаем ошибку: HTTP/1.1 404 Not Found, body: \"message\": \"Учетная запись не найдена\"")

    public void courierLoginWithIncorrectPasswordTest() {
        courier.withPassword(CourierTestData.INCORRECT_PASSWORD);
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином и паролем")
    @Description("Проверяет, что при попытке авторизации курьера с несуществующим логином и неверным паролем, получаем ошибку: HTTP/1.1 404 Not Found, body: \"message\": \"Учетная запись не найдена\"")

    public void courierLoginWithIncorrectLoginAndPasswordTest() {
        courier.withLogin(CourierTestData.INCORRECT_LOGIN);
        courier.withPassword(CourierTestData.INCORRECT_PASSWORD);
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        try {
            // Проверяем, создан ли курьер и есть ли у него ID
            if (courier != null && courier.getId() != null) {
                try {
                    // Пытаемся удалить курьера по ID
                    courierSteps.deleteCourier(courier);
                } catch (Exception e) {
                    System.out.println("Не удалось удалить курьера с ID: " + courier.getId());
                }
            } else {
                // Если ID нет, пытаемся получить его через авторизацию
                try {
                    ValidatableResponse response = courierSteps.loginCourier(courier);
                    Integer id = response.extract().body().path("id");
                    if (id != null) {
                        courier.withId(id);
                        courierSteps.deleteCourier(courier);
                    }
                } catch (Exception e) {
                    System.out.println("Не удалось получить ID курьера для удаления");
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при очистке данных: " + e.getMessage());
        }
    }
}

