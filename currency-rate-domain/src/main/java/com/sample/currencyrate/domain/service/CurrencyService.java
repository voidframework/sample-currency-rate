package com.sample.currencyrate.domain.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sample.currencyrate.domain.entity.CurrencyRate;
import com.sample.currencyrate.domain.entity.ExchangeInformation;
import com.sample.currencyrate.domain.enumeration.Currency;
import com.sample.currencyrate.domain.repository.CurrencyRateRepository;
import com.sample.currencyrate.domain.repository.EuropeanCentralBankRepository;
import dev.voidframework.core.bindable.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service to convert currencies.
 */
@Service
@Singleton
public final class CurrencyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);

    private final CurrencyRateRepository currencyRateRepository;
    private final EuropeanCentralBankRepository europeanCentralBankRepository;

    /**
     * Build a new instance.
     *
     * @param currencyRateRepository        instance of the repository "CurrencyRate"
     * @param europeanCentralBankRepository instance of the repository "EuropeanCentralBank"
     */
    @Inject
    public CurrencyService(final CurrencyRateRepository currencyRateRepository,
                           final EuropeanCentralBankRepository europeanCentralBankRepository) {

        this.currencyRateRepository = currencyRateRepository;
        this.europeanCentralBankRepository = europeanCentralBankRepository;
    }

    /**
     * Convert currency to another.
     *
     * @param fromCurrency   from currency (ex: EUR)
     * @param targetCurrency target currency (ex: USD)
     * @param value          value to convert from currency to target currency (ex: 123.45)
     * @return the converted value
     */
    public Optional<ExchangeInformation> convert(final Currency fromCurrency,
                                                 final Currency targetCurrency,
                                                 final BigDecimal value) {

        if (fromCurrency == null || targetCurrency == null || value == null) {
            return Optional.empty();
        }

        if (fromCurrency == targetCurrency) {
            return Optional.of(
                new ExchangeInformation(fromCurrency, value, targetCurrency, value));
        }

        final List<CurrencyRate> currencyRateList = this.currencyRateRepository.findAllByCurrencyIn(
            List.of(fromCurrency, targetCurrency));
        if (currencyRateList.size() != 2) {
            throw new RuntimeException("Bad number of currency rate");
        }

        final BigDecimal rateToBase = currencyRateList.get(0).rateToBase();
        final BigDecimal rateToCurrency = currencyRateList.get(1).rateFromBase();
        final BigDecimal valueInEUR = rateToBase.multiply(value);

        return Optional.of(
            new ExchangeInformation(
                fromCurrency,
                value,
                targetCurrency,
                valueInEUR.multiply(rateToCurrency)));
    }

    /**
     * Convert currency to  all other available currencies.
     *
     * @param fromCurrency from currency (ex: EUR)
     * @param value        value to convert (ex: 123.45)
     * @return a list with values converter in named currency
     */
    public List<ExchangeInformation> convertToAll(final Currency fromCurrency,
                                                  final BigDecimal value) {

        if (fromCurrency == null || value == null) {
            return Collections.emptyList();
        }

        final List<ExchangeInformation> resultList = new ArrayList<>();

        for (final Currency toCurrency : Currency.values()) {
            if (fromCurrency != toCurrency) {
                this.convert(fromCurrency, toCurrency, value).ifPresent(resultList::add);
            }
        }

        return resultList;
    }

    /**
     * Updates all currency rates.
     */
    public void updateAllCurrencyRates() {

        final CurrencyRate baseCurrencyRate = new CurrencyRate(Currency.EUR, BigDecimal.ONE, BigDecimal.ONE);
        this.currencyRateRepository.persist(baseCurrencyRate);

        final List<CurrencyRate> currencyRateList = this.europeanCentralBankRepository.findAllCurrencyRateBaseEuro();
        for (final CurrencyRate currencyRate : currencyRateList) {
            this.currencyRateRepository.persist(currencyRate);

            LOGGER.info("Currency {} conversion rate changed to {}", currencyRate.currency(), currencyRate.rateToBase());
        }
    }
}
