package com.example.EnergyService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyMarketWithCredit {
    private String address; // Целевой адрес на бирже.
    private String target; // Целевой адрес, куда уходит энергия
    private Byte resource; // Тип ресурса 0 - энергия, 1 - сеть.
    private Integer duration; // Время на которое закупаем.
    private String api_key; // API ключик

    @Builder.Default
    private String market = "Open";
    @Builder.Default
    private Boolean partfill = false; //true for allowing several address to fill your order. if false it will force to only be allowed from 1 address
    //true for creating several orders at once with the same energy, for this working target must be an array of address and payment must be the total of the orders.
    @Builder.Default
    private Boolean bulk = false;
    @Builder.Default
    private String signed_ms = null; // Необходимо при отсутствии аккаунта на бриже
    @Builder.Default
    private String signed_tx = null; // Необходимо при отсутствии аккаунта на бриже
    @Builder.Default
    private Integer price = 91; // Сумма в sun, дефолт будет 85.
    @Builder.Default
    private Long payment = 0L; // we need to increment 1 day of duration per orders smaller than 24 hours / 86400 seconds
    @Builder.Default
    private Boolean instant = true;
}
