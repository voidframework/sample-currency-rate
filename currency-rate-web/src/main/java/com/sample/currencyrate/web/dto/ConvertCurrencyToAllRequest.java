package com.sample.currencyrate.web.dto;

import com.sample.currencyrate.domain.enumeration.Currency;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * Convert currency to all request.
 *
 * @param fromCurrency from currency (ex: EUR)
 * @param value        value to convert from currency to target currency (ex: 123.45)
 */
public record ConvertCurrencyToAllRequest(@NotNull Currency fromCurrency,
                                          @NotNull BigDecimal value) {
}
