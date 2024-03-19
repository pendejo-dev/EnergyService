package com.example.EnergyService.dto;

public record RequestBuyMarketDto(
        String target, // Целевой адрес, куда уходит энергия
        Byte resource, // Тип ресурса 0 - энергия, 1 - сеть.
        Integer amount // Количество энергии
) { }
