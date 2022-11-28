package com.sample.currencyrate.domain.repository;

import com.sample.currencyrate.domain.entity.CurrencyRate;

import java.util.List;

/**
 * Repository: European Central Bank.
 */
public interface EuropeanCentralBankRepository {

    /**
     * Returns currency rates indexed on to the EUR currency.
     *
     * @return currency rates
     */
    List<CurrencyRate> findAllCurrencyRateBaseEuro();
}
