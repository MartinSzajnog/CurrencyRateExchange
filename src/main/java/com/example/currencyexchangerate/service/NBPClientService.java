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

    private static final BigDecimal COMMISION_FACTORY = BigDecimal.valueOf(0.98d);//98 procent kwoty
    // private static final MathContext MATH_CONTEXT_PRECISION_2 = new MathContext(4, RoundingMode.HALF_UP);


    public String exchangeCurrency(@NotNull(message = "Kod waluty żródłowej jest wymagany") CurrencyCode srcAsCurrencyCode,
                                   @NotNull CurrencyCode dstAsCurrencyCode, @NotNull BigDecimal money)
            throws IOException {



        if (srcAsCurrencyCode.equals(dstAsCurrencyCode)) {

            return "Nie można wymienić. Waluta żródłowa musi być różna od docelowej";

        }

        Map<CurrencyCode, ExchangeRate> currency = new HashMap<>();

        StringBuilder read = new StringBuilder();

        BigDecimal afterExchange;


        if (srcAsCurrencyCode.equals(CurrencyCode.PLN)) {

            afterExchange = buy(dstAsCurrencyCode, money, currency);

            read.append("Waluta docelowa: ").append(dstAsCurrencyCode.toString());
            read.append(", kurs sprzedaży: ").append(currency.get(dstAsCurrencyCode).getSell().toString());


        } else if (dstAsCurrencyCode.equals(CurrencyCode.PLN)) {


            afterExchange = sell(srcAsCurrencyCode, money, currency);

            read.append("Waluta źródłowa:").append(srcAsCurrencyCode.toString());
            read.append(", kurs sprzedaży: ").append(currency.get(srcAsCurrencyCode).getBuy().toString());

        } else {
            BigDecimal sellAmount = sell(srcAsCurrencyCode, money, currency);
            afterExchange = buy(dstAsCurrencyCode, sellAmount, currency);

            read.append("   Waluta źródłowa:").append(srcAsCurrencyCode.toString());
            read.append(",  kurs kupna: ").append(currency.get(srcAsCurrencyCode).getBuy().toString());
            read.append("   Waluta docelowa:").append(dstAsCurrencyCode.toString());
            read.append(",  kurs sprzedaży: ").append(currency.get(dstAsCurrencyCode).getSell().toString());

        }
        read.append(", wymieniono na:").append(afterExchange);
        read.append(" ").append(dstAsCurrencyCode);
        return read.toString();

    }

    private BigDecimal sell(CurrencyCode src, BigDecimal money, Map<CurrencyCode, ExchangeRate> currency)
            throws IOException {

        if (CurrencyCode.PLN == src) {
            return money;
        } else {
            ExchangeRate exchangeRate = getExchangeRateByCurrencyCode(src);
            currency.put(src, exchangeRate);
            BigDecimal exchangedAmount = money.multiply(exchangeRate.getBuy().setScale(2, BigDecimal.ROUND_HALF_UP));
            BigDecimal exchangedAmountAfterCommission = exchangedAmount.multiply(COMMISION_FACTORY);
            return exchangedAmountAfterCommission;


        }
    }



        private BigDecimal buy(CurrencyCode src, BigDecimal money, Map<CurrencyCode, ExchangeRate> currency)
            throws IOException {

                if (CurrencyCode.PLN != src) {
                    ExchangeRate exchangeRate = getExchangeRateByCurrencyCode(src);
                    currency.put(src, exchangeRate);
                    BigDecimal exchangedAmount = money.divide(exchangeRate.getSell(), 2);
                    // BigDecimal exchangedAmount = money.divide(exchangeRate.getSell(), 2, RoundingMode.HALF_UP); // wyliczenie kwoty obcej waluta
                    BigDecimal exchangedAmountAfterCommission = exchangedAmount.multiply(COMMISION_FACTORY);

                    return exchangedAmountAfterCommission;
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
