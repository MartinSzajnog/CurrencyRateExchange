package com.example.currencyexchangerate.service;

import com.example.currencyexchangerate.model.CurrencyCode;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class ExchangeCurrencyResultAll {

    private CurrencyCode fromCurrency;
    private CurrencyCode destinationCurrency;
    private BigDecimal amountToExchange;
    private BigDecimal sellRate;
    private BigDecimal buyRate;
    private BigDecimal exchangedAmount;
}