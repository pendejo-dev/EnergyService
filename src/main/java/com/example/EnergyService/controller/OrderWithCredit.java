package com.example.EnergyService.controller;

import com.example.EnergyService.dto.RequestBuyMarketDto;
import com.example.EnergyService.dto.ResponseBuyMarketDto;
import com.example.EnergyService.model.BuyMarketWithCredit;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@RestController
@RequestMapping(value = "/order")
public class OrderWithCredit {
    @GetMapping("/list")
    public void list(@RequestParam(required = true) String limit, @RequestParam(required = true) String skip,
                     @RequestParam(required = true) String address) {

    }

    // TODO: Перенести код в service.
    @PostMapping("/buy-market-with-credit")
    public ResponseEntity<ResponseBuyMarketDto> buyMarketWithCredit(
            @org.springframework.web.bind.annotation.RequestBody RequestBuyMarketDto marketData
    ) {
        BuyMarketWithCredit buyMarketWithCredit = BuyMarketWithCredit.builder()
                .address("TDXthYRWWhvg4h9ddW7tfkR2b7BiwBNNN7")
                .target(marketData.target())
                .resource(marketData.resource())
                .duration(900) // Арендуем на 10 мин.
                .api_key("897f175af24433fb948f3f2237b861152d1080d388e3ceb5348e8721299fd90c")
                .build();

        buyMarketWithCredit.setPayment(
                ((long) buyMarketWithCredit.getPrice() * marketData.amount() * (buyMarketWithCredit.getDuration() + (86400)) / 86400)
        );

        long payment = buyMarketWithCredit.getPayment();
        BigDecimal divisor = BigDecimal.valueOf(1_000_000);
        BigDecimal result = BigDecimal.valueOf(payment).divide(divisor, 6, RoundingMode.HALF_UP);

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<BuyMarketWithCredit> jsonAdapter = moshi.adapter(BuyMarketWithCredit.class);

        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String jsonRequest = jsonAdapter.toJson(buyMarketWithCredit);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonRequest, JSON);
        Request.Builder requestBuilder = new Request.Builder().url("https://api.tronenergy.market/order/new").post(body);
        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                assert response.body() != null : "Response body is null";
                return ResponseEntity.status(response.code()).body(new ResponseBuyMarketDto(
                    0L, response.message(), result.doubleValue()
                ));
            }

            assert response.body() != null : "Response body is null";

            JsonAdapter<ResponseBuyMarketDto> jsonAdapterResponse = moshi.adapter(ResponseBuyMarketDto.class);
            ResponseBuyMarketDto responseBuyMarket = jsonAdapterResponse.fromJson(response.body().string());
            assert responseBuyMarket != null;
            responseBuyMarket.setTrxPrice(result.doubleValue());
            return ResponseEntity.ok(responseBuyMarket);
        } catch (IOException | AssertionError e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseBuyMarketDto(
                0L, e.getMessage(), result.doubleValue()
            ));
        }
    }
}
