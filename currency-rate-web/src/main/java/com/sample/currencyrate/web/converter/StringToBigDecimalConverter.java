package com.sample.currencyrate.web.converter;

import dev.voidframework.core.conversion.TypeConverter;

import java.math.BigDecimal;

/**
 * Converts a value from {@code String} to {@code BigDecimal}.
 */
public class StringToBigDecimalConverter implements TypeConverter<String, BigDecimal> {

    @Override
    public BigDecimal convert(final String source) {

        try {
            return new BigDecimal(source);
        } catch (final IllegalArgumentException ignore) {
            return null;
        }
    }
}
