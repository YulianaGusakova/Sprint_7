import POJO.Order;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetOrderListTest extends BaseTest {
    private OrderSteps orderSteps;
    private Order order;

    @Before
    public void setUpGetOrderListTest() {
        orderSteps = new OrderSteps();
        order = new Order();
    }

    @Test
    @DisplayName("Проверка получения списка заказов")
    public void orderList() {
        orderSteps
                .makeOrder(order);
        orderSteps
                .orderList(order)
                .statusCode(HTTP_OK)
                .body("orders", notNullValue());
    }
}
