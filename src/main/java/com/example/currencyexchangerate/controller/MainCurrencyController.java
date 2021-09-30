package com.example.currencyexchangerate.controller;


import com.example.currencyexchangerate.model.CurrencyCode;
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

    private final NBPClientService nbpClientService;

    @GetMapping("/currency")
    @ResponseBody
    String getCurrency(@RequestParam(required = true)String src, @RequestParam(required = true) String dst, @RequestParam(required = true) BigDecimal money)
            throws IOException {
        return nbpClientService.getCurrencyMap(CurrencyCode.valueOf(src.toUpperCase()),CurrencyCode.valueOf(dst.toUpperCase()),money);


    }

}
