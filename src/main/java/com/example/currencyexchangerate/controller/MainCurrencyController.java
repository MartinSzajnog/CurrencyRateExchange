package com.example.currencyexchangerate.controller;


import com.example.currencyexchangerate.model.CurrencyCode;
import com.example.currencyexchangerate.service.ExchangeService;
import com.example.currencyexchangerate.service.NBPClientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;

@RestController
@RequestMapping
@AllArgsConstructor



public class MainCurrencyController {
    private final ExchangeService exchangeService;

    /**
     *
     * @param src
     * @param dst
     * @param money
     * @return   @author Martin Szajnog
     * @throws IOException
     */

    @GetMapping("/currency")
    @ResponseBody
    String getCurrency(@RequestParam(required = true)String src, @RequestParam(required = true) String dst, @RequestParam(required = true) BigDecimal money)
            throws IOException {
        return exchangeService.exchangeCurrency(CurrencyCode.valueOf(src.toUpperCase()),CurrencyCode.valueOf(dst.toUpperCase()),money);


    }

}
