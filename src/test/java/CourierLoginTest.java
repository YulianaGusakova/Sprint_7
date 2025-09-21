import Constants.CourierTestData;
import POJO.Courier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;

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
    }

    @Test
    @DisplayName("Успешная авторизация при передаче всех обязательных полей")

    public void courierLoginTest() {
        courierSteps
                .createCourier(courier);
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_OK)
                .body("id", notNullValue());
    }


    @Test
    @DisplayName("Авторизация без логина")

    public void courierLoginWithoutLoginTest() {

        courier.withLogin("");
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")

    public void courierLoginWithoutPasswordTest() {
        courier.withPassword("");
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без логина и пароля")

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

    public void courierLoginWithIncorrectLoginTest() {
        courier.withLogin(CourierTestData.INCORRECT_LOGIN);
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")

    public void courierLoginWithIncorrectPasswordTest() {
        courier.withPassword(CourierTestData.INCORRECT_PASSWORD);
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином и паролем")

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

