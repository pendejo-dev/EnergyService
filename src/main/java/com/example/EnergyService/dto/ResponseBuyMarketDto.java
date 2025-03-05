package com.example.EnergyService.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResponseBuyMarketDto {
    private Long order;
    private String message;
    private Double trxPrice;

    public ResponseBuyMarketDto(Long order, String message, Double trxPrice) {
        this.order = order;
        this.message = message;
        this.trxPrice = trxPrice;
    }
}
