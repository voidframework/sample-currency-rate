package com.sample.currencyrate.web.dto;

import com.sample.currencyrate.domain.enumeration.Currency;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Convert currency request.
 *
 * @param fromCurrency from currency (ex: EUR)
 * @param toCurrency   target currency (ex: USD)
 * @param value        value to convert from currency to target currency (ex: 123.45)
 */
public record ConvertCurrencyRequest(@NotNull Currency fromCurrency,
                                     @NotNull Currency toCurrency,
                                     @NotNull BigDecimal value) {
}
