package com.sample.currencyrate.web.converter;

import com.sample.currencyrate.domain.enumeration.Currency;
import dev.voidframework.core.conversion.TypeConverter;

/**
 * Converts a value from {@code String} to {@code Currency}.
 */
public class StringToCurrencyConverter implements TypeConverter<String, Currency> {

    @Override
    public Currency convert(final String source) {

        try {
            return Currency.valueOf(source);
        } catch (final IllegalArgumentException ignore) {
            return null;
        }
    }
}
