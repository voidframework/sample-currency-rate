package com.sample.currencyrate.domain.entity;

import com.sample.currencyrate.domain.enumeration.Currency;

import java.math.BigDecimal;

/**
 * Exchange information.
 *
 * @param fromCurrency from currency (ex: EUR)
 * @param fromValue    value to convert (ex: 123.45)
 * @param toCurrency   to currency (ex: USD)
 * @param toValue      value converted (ex: 90.15)
 */
public record ExchangeInformation(Currency fromCurrency,
                                  BigDecimal fromValue,
                                  Currency toCurrency,
                                  BigDecimal toValue) {
}
