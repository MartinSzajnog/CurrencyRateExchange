package com.example.currencyexchangerate.controller;

import com.example.currencyexchangerate.model.CurrencyCode;
import com.example.currencyexchangerate.service.ExchangeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.currencyexchangerate.model.CurrencyCode.valueOf;

/**
 *
 */
@RestController
@RequestMapping
@AllArgsConstructor
public class CurrencyController {

    private final ExchangeService exchangeService;

    /**
     * @param src
     * @param dst
     * @param money @author:MartinSzajnog
     * @return
     * @throws IOException example:
     *                     http://localhost:8080/currency?src=PLN&dst=USD&money=100
     */
    @GetMapping("/currency")
    ResponseEntity getCurrency(@RequestParam String src,
                       @RequestParam String dst,
                       @RequestParam BigDecimal money
    ) throws IOException {
        List<String> errors = checkErrors(src, dst, money);
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors.toString());
        } else
          return ResponseEntity.ok().body(exchangeService.exchangeCurrency(valueOf(src.toUpperCase()), valueOf(dst.toUpperCase()), money));
    }

    private List<String> checkErrors(String src, String dst, BigDecimal money) {
        List<String> currencyCodes = Arrays.stream((CurrencyCode.values())).map(Enum::name).collect(Collectors.toList());
        List<String> errors = new ArrayList<>();

        if (money.compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Podano kwotę: " + money + ", kwota musi być większa od 0");
        }
        if (!currencyCodes.contains(src.toUpperCase())) {
            errors.add("Podano kod 'src' niezgodny ze słownikiem, dopuszczalne kody to: PLN, USD, EUR, GBP");
        }
        if (!currencyCodes.contains(dst.toUpperCase())) {
            errors.add("Podano kod 'dst' niezgodny ze słownikiem, dopuszczalne kody to: PLN, USD, EUR, GBP");
        }
        return errors;
    }
}


