package com.jdbc.sharding.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Order {
    Long orderNo;
    BigDecimal price;
    int count;
}
