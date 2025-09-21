package steps;

import Constants.ApiEndPoints;
import POJO.Courier;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    @Step("Создание курьера")
        public ValidatableResponse createCourier(Courier courier) {
        return given()
                .body(courier)
                .when()
                .post(ApiEndPoints.COURIER_CREATE_POST)
                .then();
    }

    @Step("Авторизация курьера")
        public ValidatableResponse loginCourier(Courier courier) {
        return given()
                .body(courier)
                .when()
                .post(ApiEndPoints.COURIER_LOGIN_POST)
                .then();
    }

    @Step("Удаление курьера")
        public ValidatableResponse deleteCourier(Courier courier) {
        return given()
                .pathParams("id", courier.getId())
                .when()
                .delete(ApiEndPoints.COURIER_ID_DELETE)
                .then();
    }
}
