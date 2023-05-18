package com.sample.currencyrate.datastore.repository;

import com.sample.currencyrate.domain.entity.CurrencyRate;
import com.sample.currencyrate.domain.enumeration.Currency;
import com.sample.currencyrate.domain.repository.CurrencyRateRepository;
import dev.voidframework.core.bindable.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the repository "CurrencyRate".
 */
@Repository
public class CurrencyRateRepositoryImpl implements CurrencyRateRepository {

    final Map<Currency, CurrencyRate> currencyRateByCurrencyMap;

    /**
     * Build a new instance.
     */
    public CurrencyRateRepositoryImpl() {

        this.currencyRateByCurrencyMap = new ConcurrentHashMap<>();
    }

    @Override
    public List<CurrencyRate> findAllByCurrencyIn(final Collection<Currency> currencyCollection) {

        return currencyCollection.stream()
            .map(this.currencyRateByCurrencyMap::get)
            .toList();
    }

    @Override
    public void persist(final CurrencyRate currencyRate) {

        this.currencyRateByCurrencyMap.put(currencyRate.currency(), currencyRate);
    }
}
