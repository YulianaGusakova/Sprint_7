package steps;

import Constants.ApiEndPoints;
import POJO.Order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Создание заказа")
    public ValidatableResponse makeOrder(Order order) {
        return given()
                .body(order)
                .when()
                .post(ApiEndPoints.MAKE_ORDER_POST)
                .then();
    }
@Step("Получаем список заказов")
    public ValidatableResponse orderList(Order order) {
        return given()
                .body(order)
                .when()
                .get(ApiEndPoints.ORDER_LIST_GET)
                .then();
    }
@Step("Отменяем заказ")
    public ValidatableResponse cancelOrder(int track) {
        return given()
                .pathParams("track", track)
                .when()
                .put(ApiEndPoints.CANCEL_ORDER_PUT)
                .then();
    }
}
