package com.sample.currencyrate.domain.entity;

import com.sample.currencyrate.domain.enumeration.Currency;

import java.math.BigDecimal;

/**
 * Currency rate.
 *
 * @param currency     currency
 * @param rateToBase   rate to convert to base
 * @param rateFromBase rate to convert from base
 */
public record CurrencyRate(Currency currency,
                           BigDecimal rateToBase,
                           BigDecimal rateFromBase) {
}
