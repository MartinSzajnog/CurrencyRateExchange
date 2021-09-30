package com.example.currencyexchangerate.service;

import com.example.currencyexchangerate.model.CurrencyCode;
import com.example.currencyexchangerate.model.ExchangeRate;
import jakarta.validation.constraints.NotNull;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Component

public class NBPClientService {


    private static final String NBP_URL = "http://api.nbp.pl/api/exchangerates/rates/c/";
    private static final BigDecimal COMMISION_FACTOR = BigDecimal.valueOf(0.98d);


    public String getCurrencyMap(CurrencyCode srcAsCurrencyCode, CurrencyCode dstAsCurrencyCode, BigDecimal money) throws IOException {

        Map<CurrencyCode, ExchangeRate> currency = new HashMap<>();

        BigDecimal sellAmount = sell(srcAsCurrencyCode, money, currency);
        BigDecimal buyAmount = buy(dstAsCurrencyCode, sellAmount, currency);


        return "Waluta żródłowa: ".concat(dstAsCurrencyCode.toString()).concat(", waluta docelowa: ")
                .concat(dstAsCurrencyCode.toString()).concat(", kurs wymiany: ")
                .concat(currency.get(srcAsCurrencyCode).getBuy().toString());

    }

    private BigDecimal sell(CurrencyCode src, BigDecimal money, Map<CurrencyCode, ExchangeRate> currency) throws IOException {

        if (CurrencyCode.PLN == src) {
            return money;
        } else {

            ExchangeRate exchangeRate = getExchangeRateByCurrencyCode(src);
            currency.put(src, exchangeRate);
            BigDecimal exchangedAmount = money.multiply(exchangeRate.getSell());
            BigDecimal exchangedAmountAfterCommisson = exchangedAmount.multiply(COMMISION_FACTOR);
            return exchangedAmountAfterCommisson;

        }
    }

    private BigDecimal buy(CurrencyCode src, BigDecimal money, Map<CurrencyCode, ExchangeRate> currency) throws IOException {

        if (CurrencyCode.PLN != src) {
            ExchangeRate exchangeRate = getExchangeRateByCurrencyCode(src);
            currency.put(src, exchangeRate);
            BigDecimal exchangedAmount = money.divide(exchangeRate.getBuy(), RoundingMode.HALF_UP);
            BigDecimal exchangedAmountAfterCommisson = exchangedAmount.multiply(COMMISION_FACTOR);
            return exchangedAmountAfterCommisson;

        } else {
            return money;
        }

    }


    public ExchangeRate getExchangeRateByCurrencyCode(@NotNull CurrencyCode currencyCode) throws IOException {

        URL url = new URL(NBP_URL + currencyCode.name() + "?format=json");

        try (InputStream is = url.openStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String jsonText = readAll(br);

            JSONObject json = new JSONObject(jsonText);

            BigDecimal bidRate = json.getJSONArray("rates").getJSONObject(0).getBigDecimal("bid");
            BigDecimal askRate = json.getJSONArray("rates").getJSONObject(0).getBigDecimal("ask");
            ExchangeRate exchangeRate = new ExchangeRate(bidRate, askRate);

            // Wstępnie przeglądamy zawartość z strumienia czy dobrze zaczytuje dane walut

            System.out.println("Waluta: " + currencyCode.name());
            System.out.println("Kurs zakupu: " + bidRate);
            System.out.println("Kurs sprzedaży: " + askRate);

            return exchangeRate;
        }
        //Po wykonaniu zczytania zamykamy kran z strumieniem. Zamykamy strumień wejściowy.

    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int currentRD;

        while ((currentRD = rd.read()) != -1) {
            sb.append((char) currentRD);
        }
        return sb.toString();
    }


}
