package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import ru.yandex.practicum.pojo.Order;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.steps.OrderSteps;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderListTest extends BaseTest {
    private OrderSteps orderSteps;
    private Order order;

    @Before
    public void setUpGetOrderListTest() {
        orderSteps = new OrderSteps();
        order = new Order();
        orderSteps
                .makeOrder(order);
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    @Description("Проверяет, что запрос на получение списка заказов возвращает успешный статус HTTP 200 и содержит непустой массив заказов в ответе.")
    public void orderList() {
        orderSteps
                .orderList(order)
                .statusCode(HTTP_OK)
                .body("orders", notNullValue());
    }
}
