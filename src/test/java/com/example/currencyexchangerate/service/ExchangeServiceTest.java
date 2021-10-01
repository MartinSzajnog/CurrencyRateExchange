/*
package com.example.currencyexchangerate.service;

import com.example.currencyexchangerate.model.CurrencyCode;
import com.example.currencyexchangerate.model.ExchangeRate;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {

    @InjectMocks
    NBPClientService nbpClientService = new NBPClientService();
    @InjectMocks
    ExchangeService exchangeService = new ExchangeService(nbpClientService);

    @Test
    void checkExchangeFromPlnToUsd() throws IOException, JSONException {


        //given
        NBPClientService mock = Mockito.mock(NBPClientService.class);
        ExchangeRate exchangeRate = new ExchangeRate(new BigDecimal(4.0225), new BigDecimal(3.9429));
        when(mock.getExchangeRateByCurrencyCode(CurrencyCode.USD)).thenReturn(exchangeRate);

        URL urlUSD = new URL("http://api.nbp.pl/api/exchangerates/rates/c/USD/2021-09-30?format=json");
        //when
        String outputTextPLNToUSD = exchangeService.exchangeCurrency(CurrencyCode.PLN, CurrencyCode.USD, new BigDecimal(100));
        String outputTextUSDToPLN = exchangeService.exchangeCurrency(CurrencyCode.PLN, CurrencyCode.USD, new BigDecimal(100));
        String outputTextPLNToPLN = exchangeService.exchangeCurrency(CurrencyCode.PLN, CurrencyCode.USD, new BigDecimal(100));

        System.out.println(outputTextPLNToUSD);
        System.out.println(outputTextUSDToPLN);
        System.out.println(outputTextPLNToPLN);

        //then
        Assertions.assertEquals("Waluta docelowa: USD,kurs sprzedaży:4.0225,wymieniono na:24.36 USD",outputTextPLNToUSD);
        assertTrue(outputTextPLNToUSD.equals("Waluta żródłowa: USD,kurs sprzedaży:3.9429,wymieniono na: 386.12 PLN"));
        assertTrue(outputTextPLNToUSD.equals("Nie można wymienić. Waluta żródłowa musi być różna od docelowej"));
//TODO: Do poprawienia

    }
}*/
