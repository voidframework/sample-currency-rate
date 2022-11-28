package com.sample.currencyrate.infrastructure.module;

import com.google.inject.AbstractModule;
import com.sample.currencyrate.datastore.repository.CurrencyRateRepositoryImpl;
import com.sample.currencyrate.datastore.repository.EuropeanCentralBankRepositoryImpl;
import com.sample.currencyrate.domain.repository.CurrencyRateRepository;
import com.sample.currencyrate.domain.repository.EuropeanCentralBankRepository;

/**
 * Guice module of the current application.
 */
public class CurrencyRateModule extends AbstractModule {

    @Override
    protected void configure() {

        // Repository
        bind(CurrencyRateRepository.class).to(CurrencyRateRepositoryImpl.class);
        bind(EuropeanCentralBankRepository.class).to(EuropeanCentralBankRepositoryImpl.class);
    }
}
