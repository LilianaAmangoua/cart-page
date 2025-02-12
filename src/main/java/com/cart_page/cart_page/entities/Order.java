package com.cart_page.cart_page.entities;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private int orderId;
    private int userId;

    @Positive(message = "Le total doit être positif")
    private BigDecimal total;

    @PastOrPresent(message = "La date ne peut pas être dans le futur")
    private Date order_date;

    public Order(){

    }

    public Order(int orderId, int userId, BigDecimal total, Date order_date) {
        this.orderId = orderId;
        this.userId = userId;
        this.total = total;
        this.order_date = order_date;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Date getOrder_date() {
        return order_date;
    }

    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }
}
