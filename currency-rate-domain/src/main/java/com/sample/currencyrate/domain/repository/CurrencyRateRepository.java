package com.sample.currencyrate.domain.repository;

import com.sample.currencyrate.domain.entity.CurrencyRate;
import com.sample.currencyrate.domain.enumeration.Currency;

import java.util.Collection;
import java.util.List;

/**
 * Repository: Currency rate.
 */
public interface CurrencyRateRepository {

    /**
     * Finds all currency rates for given currencies.
     *
     * @param currencyCollection a collection of currencies
     * @return found currency rates
     */
    List<CurrencyRate> findAllByCurrencyIn(final Collection<Currency> currencyCollection);

    /**
     * Persists a single currency rate.
     *
     * @param currencyRate currency rate to persist
     */
    void persist(final CurrencyRate currencyRate);
}
