package com.example.currencyexchangerate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


/**
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor



public class ExchangeRate {
    private BigDecimal buy;
    private BigDecimal sell;
}
