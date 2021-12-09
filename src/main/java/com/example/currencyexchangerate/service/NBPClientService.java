package com.example.currencyexchangerate.service;

import com.example.currencyexchangerate.model.CurrencyCode;
import com.example.currencyexchangerate.model.ExchangeRate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;


import lombok.NoArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;


@Component
@AllArgsConstructor
@NoArgsConstructor
public class NBPClientService implements Callable<ExchangeRate> {

    CurrencyCode currencyCode;
    LocalDateTime localDateTime;
    InputStream is;

    private static final String NBP_URL = "http://api.nbp.pl/api/exchangerates/rates/c/";

    public ExchangeRate getExchangeRateByCurrencyCode(@NotNull CurrencyCode currencyCode, LocalDateTime dateTime) throws IOException {

        this.currencyCode = currencyCode;
        this.localDateTime = dateTime;

        return call();

    }

    public InputStream getInputStream() throws IOException {
        URL url;
        if (this.localDateTime != null) {
            url = new URL(NBP_URL + this.currencyCode.name() + "/" + this.localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "?format=json");
        } else {
            url = new URL(NBP_URL + this.currencyCode.name() + "?format=json");
        }

        InputStream is = url.openStream();
        return is;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int currentRD;

        while ((currentRD = rd.read()) != -1) {
            sb.append((char) currentRD);
        }
        return sb.toString();
    }

    @Override
    public ExchangeRate call() throws IOException {
            this.is = getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(this.is, StandardCharsets.UTF_8));
                String jsonText = readAll(br);

                JSONObject json = new JSONObject(jsonText);

                BigDecimal bidRate = json.getJSONArray("rates").getJSONObject(0).getBigDecimal("bid");
                BigDecimal askRate = json.getJSONArray("rates").getJSONObject(0).getBigDecimal("ask");
                ExchangeRate exchangeRate = new ExchangeRate(bidRate, askRate);

                System.out.println("Waluta: " + currencyCode.name());
                System.out.println("Kurs zakupu: " + bidRate);
                System.out.println("Kurs sprzedaży: " + askRate);

                return  exchangeRate;
    }
}