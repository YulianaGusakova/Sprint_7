package ru.yandex.practicum.tests;

import io.qameta.allure.Description;
import ru.yandex.practicum.pojo.Order;
import io.qameta.allure.junit4.DisplayName;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practicum.steps.OrderSteps;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class MakeOrderColorTest extends BaseTest {
    private OrderSteps orderSteps = new OrderSteps();
    private Order order;
    private final List<String> color;

    public MakeOrderColorTest(List<String> color) {
        this.color = color;
    }

    @Before
    public void setUpOrder() {
        Faker faker = new Faker();
        order = new Order();
        order.withFirstName(faker.name().firstName())
                .withLastName(faker.name().lastName())
                .withAddress(faker.address().fullAddress())
                .withMetroStation("Озерки")
                .withPhone(faker.phoneNumber().phoneNumberNational())
                .withRentTime(faker.number().numberBetween(1, 30))
                .withDeliveryDate(String.valueOf(faker.date().future(10, TimeUnit.DAYS)))
                .withComment("")
                .withColor(new ArrayList<>());

    }

    @Parameterized.Parameters(name = "Выбор цвета самоката")
    public static Object[][] scooterColor() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()},
        };
    }

    @Test
    @DisplayName("Создание заказа с указанием и без указания цвета самоката")
    @Description("Проверяет возможность создания заказа с указанием различных цветов самоката, включая одиночные и множественные выборы, а также отсутствие выбора цвета.")
    public void makeOrderColorTest() {
        order.withColor(color);
        orderSteps
                .makeOrder(order)
                .statusCode(HTTP_CREATED)
                .body("track", notNullValue());

    }

    @After
    public void tearDownOrder() {
        try {
            if (order.getTrack() != null) {
                Integer track = orderSteps
                        .makeOrder(order)
                        .extract().body().path("track");
                order.withTrack(track);
                orderSteps.cancelOrder(track);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при очистке данных" + e.getMessage());
        }
    }
}
