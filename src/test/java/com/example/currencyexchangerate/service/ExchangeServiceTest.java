


/*
package com.example.currencyexchangerate.service;

import com.example.currencyexchangerate.model.CurrencyCode;
import com.example.currencyexchangerate.model.ExchangeRate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.json.JSONObject;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {

    @InjectMocks
    NBPClientService nbpClientService = new NBPClientService();
    @InjectMocks
    ExchangeService exchangeService = new ExchangeService(nbpClientService);

    @Test
    void checkExchangeFromPlnToUsd() throws IOException {


        //given
        NBPClientService mock = Mockito.mock(NBPClientService.class);
        ExchangeRate exchangeRate = new ExchangeRate(new BigDecimal(4.0107), new BigDecimal(3.9313));
        when(mock.getExchangeRateByCurrencyCode(CurrencyCode.USD)).thenReturn(exchangeRate);

        // URL urlUSD = new URL("http://api.nbp.pl/api/exchangerates/rates/c/USD/2021-10-01?format=json");

        //when

        String outputTextPLNToUSD = exchangeService.exchangeCurrency(CurrencyCode.PLN, CurrencyCode.USD, new BigDecimal(100));
        String outputTextUSDToPLN = exchangeService.exchangeCurrency(CurrencyCode.USD, CurrencyCode.PLN, new BigDecimal(100));
        //  String outputTextPLNToPLN = exchangeService.exchangeCurrency(CurrencyCode.PLN, CurrencyCode.USD, new BigDecimal(100));

      //  System.out.println(outputTextPLNToUSD);
        //System.out.println(outputTextUSDToPLN);
        //  System.out.println(outputTextPLNToPLN);

        //then
        Assertions.assertEquals("Waluta docelowa: USD, kurs sprzedaży: 4.0107, wymieniono na:24.43 USD",outputTextPLNToUSD);
       // assertTrue(outputTextPLNToUSD.equals("Waluta docelowa: USD, kurs sprzedaży: 4.0107, wymieniono na:24.43 USD"));
        Assertions.assertEquals("Waluta żródłowa:USD, kurs sprzedaży: 3.9313, wymieniono na:385.14 PLN",outputTextUSDToPLN);

}}
*/


