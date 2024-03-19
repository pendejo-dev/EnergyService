package com.example.EnergyService.controller;

import com.example.EnergyService.dto.RequestBuyMarketDto;
import com.example.EnergyService.dto.ResponseBuyMarketDto;
import com.example.EnergyService.model.BuyMarketWithCredit;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/credit-order")
public class OrderWithCredit {
    @PostMapping("/buy-market-with-credit")
    public ResponseEntity<ResponseBuyMarketDto> buyMarketWithCredit(
            @org.springframework.web.bind.annotation.RequestBody RequestBuyMarketDto marketData
    ) {
        BuyMarketWithCredit buyMarketWithCredit = BuyMarketWithCredit.builder()
                .address("Откуда шлем")
                .target(marketData.target())
                .payment((marketData.amount() * 60))
                .resource(marketData.resource())
                .duration(3600) // Арендуем на 1 час.
                .api_key("HIDE")
                .build();

        System.out.println(buyMarketWithCredit.getDuration());
//        buyMarketWithCredit.setPayment(
//                (int) Math.round((buyMarketWithCredit.getPrice() * marketData.amount() * (buyMarketWithCredit.getDuration() + ((buyMarketWithCredit.getDuration() < 86400) ? 86400 : 0))) / 86400.0));

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<BuyMarketWithCredit> jsonAdapter = moshi.adapter(BuyMarketWithCredit.class);

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String jsonRequest = jsonAdapter.toJson(buyMarketWithCredit);

        System.out.println(jsonRequest);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonRequest, JSON);
        Request.Builder requestBuilder = new Request.Builder().url("https://api.tronenergy.market/order/new").post(body);
        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                assert response.body() != null;
                System.out.println(response.body().string());
                throw new IOException("Запрос к серверу не был успешен: " +
                        response.code() + " " + response.message());
            }
            assert response.body() != null;
            System.out.println(response.body().string());
        } catch (IOException e) {
            System.out.println("Ошибка подключения: " + e);
        }
        return ResponseEntity.ok(new ResponseBuyMarketDto("test"));
    }
}
