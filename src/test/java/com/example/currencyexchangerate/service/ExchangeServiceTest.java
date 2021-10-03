
package com.example.currencyexchangerate.service;

import com.example.currencyexchangerate.model.CurrencyCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExchangeServiceTest {

    NBPClientService nbpClient = new NBPClientService();
    ExchangeService exchangeService = new ExchangeService(nbpClient);

    @DisplayName("Weryfikacja przewalutowania z PLN na USD")
    void checkExchangeFromPlnToUsd() throws IOException {
        //given
        LocalDateTime date20210920 = LocalDateTime.of(2021, 9, 20, 00, 00);

        //when
        String outputTextPLNToUSD = exchangeService.exchangeCurrency(CurrencyCode.PLN, CurrencyCode.USD, new BigDecimal(100), date20210920);

        //then
        Assertions.assertEquals(outputTextPLNToUSD,("Waluta docelowa: USD, kurs sprzedaży: 3.9402, wymieniono na:24.87 USD"));
    }

    @Test
    @DisplayName("Weryfikacja przewalutowania z USD na PLN")
    void checkExchangeFromUsdToPln() throws IOException {
        //given
        LocalDateTime date20210920 = LocalDateTime.of(2021, 9, 20, 00, 00);

        //when
        String outputTextUSDToPLN = exchangeService.exchangeCurrency(CurrencyCode.USD, CurrencyCode.PLN, new BigDecimal(100), date20210920);

        //then
        Assertions.assertEquals(outputTextUSDToPLN,("Waluta źródłowa:USD, kurs kupna: 3.8622, wymieniono na:378.28 PLN"));
    }

    @Test
    @DisplayName("Weryfikacja przewalutowania z GBP na USD")
    void checkExchangeFromGbpToEur() throws IOException {
        //given
        LocalDateTime date20210920 = LocalDateTime.of(2021, 9, 20, 00, 00);

        //when
        String outputTextGBPToEUR = exchangeService.exchangeCurrency(CurrencyCode.GBP, CurrencyCode.EUR, new BigDecimal(100), date20210920);

        System.out.println(outputTextGBPToEUR);

        //then
        Assertions.assertEquals(outputTextGBPToEUR, "   Waluta źródłowa:GBP,  kurs kupna: 5.3206   Waluta docelowa:EUR,  kurs sprzedaży: 4.6321, wymieniono na:110.30 EUR");
    }

    @Test
    @DisplayName("Weryfikacja przewalutowania z PLN na PLN")
    void checkExchangeFromPlnToPln() throws IOException {
        //given
        LocalDateTime date20210920 = LocalDateTime.of(2021, 9, 20, 00, 00);

        //when
        String outputTextPLNToPLN = exchangeService.exchangeCurrency(CurrencyCode.PLN, CurrencyCode.PLN, new BigDecimal(100), date20210920);

        //then
        Assertions.assertEquals(outputTextPLNToPLN,("Nie można wymienić. Waluta żródłowa musi być różna od docelowej"));

    }}

