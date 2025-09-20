import Constants.CourierTestData;
import POJO.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
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
    @DisplayName("Успешная авторизовация при передаче всех обязательных полей")
    @Description("ОР: HTTP/1.1 200, body: возвращается id")

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
    @Description("ОР: HTTP/1.1 400 Bad Request, body: \"message\":  \"Недостаточно данных для входа\"")

    public void courierLoginWithoutLoginTest() {
        courier.withLogin("");
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("ОР: HTTP/1.1 400 Bad Request, body: \"message\":  \"Недостаточно данных для входа\"")

    public void courierLoginWithoutPasswordTest() {
        courier.withPassword("");
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_BAD_REQUEST)
                .body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без логина и пароля")
    @Description("ОР: HTTP/1.1 400 Bad Request, body: \"message\":  \"Недостаточно данных для входа\"")

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
    @Description("ОР: HTTP/1.1 404 Not Found, body: \"message\": \"Учетная запись не найдена\"")

    public void courierLoginWithIncorrectLoginTest() {
        courier.withLogin(CourierTestData.INCORRECT_LOGIN);
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("ОР: HTTP/1.1 404 Not Found, body: \"message\": \"Учетная запись не найдена\"")

    public void courierLoginWithIncorrectPasswordTest() {
        courier.withPassword(CourierTestData.INCORRECT_PASSWORD);
        courierSteps
                .loginCourier(courier)
                .statusCode(HTTP_NOT_FOUND)
                .body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Авторизация с неверным логином и паролем")
    @Description("ОР: HTTP/1.1 404 Not Found, body: \"message\": \"Учетная запись не найдена\"")

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
            if (courier.getLogin() != null && !courier.getLogin().isEmpty()) {
                try {
                    Integer id = courierSteps.loginCourier(courier)
                            .extract().body().path("id");
                    courier.withId(id);
                    courierSteps.deleteCourier(courier);
                } catch (Exception e) {
                    System.out.println("Не удалось получить ID курьера для удаления");
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при очистке данных: " + e.getMessage());
        }
    }
}

