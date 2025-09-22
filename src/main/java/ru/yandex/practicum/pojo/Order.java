package ru.yandex.practicum.pojo;

import java.util.List;

public class Order {
    private String firstName;
    private String lastName;
    private String address;
    private String metroStation;
    private String phone;
    private Integer rentTime;
    private String deliveryDate;
    private String comment;
    private List<String> color;
    private Integer track;

    public Integer getTrack() {
        return track;
    }

    public Order withTrack(Integer track) {
        this.track = track;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Order withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Order withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public Order withAddress(String address) {
        this.address = address;
        return this;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public Order withMetroStation(String metroStation) {
        this.metroStation = metroStation;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Order withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Integer getRentTime() {
        return rentTime;
    }

    public Order withRentTime(Integer rentTime) {
        this.rentTime = rentTime;
        return this;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public Order withDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Order withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public List<String> getColor() {
        return color;
    }

    public Order withColor(List<String> color) {
        this.color = color;
        return this;
    }
}
