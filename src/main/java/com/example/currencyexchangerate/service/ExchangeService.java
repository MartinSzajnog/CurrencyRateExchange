package com.example.currencyexchangerate.service;

import com.example.currencyexchangerate.model.CurrencyCode;
import com.example.currencyexchangerate.model.ExchangeRate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Validated
@Service
@AllArgsConstructor
public class ExchangeService {

    private static final BigDecimal COMMISION_FACTORY = BigDecimal.valueOf(0.98d);//98 procent kwoty
    // private static final MathContext MATH_CONTEXT_PRECISION_2 = new MathContext(4, RoundingMode.HALF_UP);

    private final NBPClientService nbpClient;

    /**
     * @param srcAsCurrencyCode
     * @param dstAsCurrencyCode
     * @param money
     * @throws IOException
     */

    public String exchangeCurrency(@NotNull(message = "Kod waluty żródłowej jest wymagany") CurrencyCode srcAsCurrencyCode,
                                   @NotNull CurrencyCode dstAsCurrencyCode, @NotNull BigDecimal money) throws IOException {
        return exchangeCurrency(srcAsCurrencyCode, dstAsCurrencyCode, money, null);
    }

    public String exchangeCurrency(@NotNull(message = "Kod waluty żródłowej jest wymagany") CurrencyCode srcAsCurrencyCode,
                                   @NotNull CurrencyCode dstAsCurrencyCode, @NotNull BigDecimal money, LocalDateTime dateTime)
            throws IOException {


        if (srcAsCurrencyCode.equals(dstAsCurrencyCode)) {
            return "Nie można wymienić. Waluta żródłowa musi być różna od docelowej";
        }

        Map<CurrencyCode, ExchangeRate> currency = new HashMap<>();
        StringBuilder read = new StringBuilder();
        BigDecimal afterExchange;


        if (srcAsCurrencyCode.equals(CurrencyCode.PLN)) {
            afterExchange = buy(dstAsCurrencyCode, money, currency, dateTime);

            read.append("Waluta docelowa: ").append(dstAsCurrencyCode.toString());
            read.append(", kurs sprzedaży: ").append(currency.get(dstAsCurrencyCode).getSell().toString());

        } else if (dstAsCurrencyCode.equals(CurrencyCode.PLN)) {
            afterExchange = sell(srcAsCurrencyCode, money, currency, dateTime);

            read.append("Waluta źródłowa:").append(srcAsCurrencyCode.toString());
            read.append(", kurs sprzedaży: ").append(currency.get(srcAsCurrencyCode).getBuy().toString());

        } else {
            BigDecimal sellAmount = sell(srcAsCurrencyCode, money, currency, dateTime);
            afterExchange = buy(dstAsCurrencyCode, sellAmount, currency, dateTime);

            read.append("   Waluta źródłowa:").append(srcAsCurrencyCode.toString());
            read.append(",  kurs kupna: ").append(currency.get(srcAsCurrencyCode).getBuy().toString());
            read.append("   Waluta docelowa:").append(dstAsCurrencyCode.toString());
            read.append(",  kurs sprzedaży: ").append(currency.get(dstAsCurrencyCode).getSell().toString());

        }
        read.append(", wymieniono na:").append(afterExchange);
        read.append(" ").append(dstAsCurrencyCode);
        return read.toString();

    }

    private BigDecimal sell(CurrencyCode src, BigDecimal money, Map<CurrencyCode, ExchangeRate> currency, LocalDateTime dateTime)
            throws IOException {

        if (CurrencyCode.PLN == src) {
            return money;
        } else {
            ExchangeRate exchangeRate = nbpClient.getExchangeRateByCurrencyCode(src, dateTime);
            currency.put(src, exchangeRate);

            BigDecimal exchangedAmount = money.multiply(exchangeRate.getBuy().setScale(2, RoundingMode.HALF_UP));

            return exchangedAmount.multiply(COMMISION_FACTORY).setScale(2, RoundingMode.HALF_UP);
        }

    }

    // src = GB za money = 100 zl
    private BigDecimal buy(CurrencyCode src, BigDecimal money, Map<CurrencyCode, ExchangeRate> currency, LocalDateTime dateTime)
            throws IOException {

        if (CurrencyCode.PLN != src) {
            ExchangeRate exchangeRate = nbpClient.getExchangeRateByCurrencyCode(src, dateTime);
            currency.put(src, exchangeRate);
            BigDecimal exchangedAmount = money.divide(exchangeRate.getSell(), 2, RoundingMode.HALF_UP);

            return exchangedAmount.multiply(COMMISION_FACTORY).setScale(2, RoundingMode.HALF_UP);
        } else {
            return money;

        }

    }
}