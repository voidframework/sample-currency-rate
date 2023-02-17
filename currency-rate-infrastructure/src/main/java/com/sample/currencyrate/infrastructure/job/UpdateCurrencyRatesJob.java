package com.sample.currencyrate.infrastructure.job;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sample.currencyrate.domain.service.CurrencyService;
import dev.voidframework.core.bindable.Bindable;
import dev.voidframework.scheduler.Scheduled;

/**
 * Job to update all currency rates at regular intervals.
 */
@Bindable
@Singleton
public final class UpdateCurrencyRatesJob {

    private final CurrencyService currencyService;

    /**
     * Build a new instance.
     *
     * @param currencyService Instance of the service "Currency"
     */
    @Inject
    public UpdateCurrencyRatesJob(final CurrencyService currencyService) {

        this.currencyService = currencyService;
    }

    /**
     * Updates all currency rates.
     */
    @Scheduled(cronZone = "UTC", cron = "0 0 * * * *")
    public void updateEveryHours() {

        this.currencyService.updateAllCurrencyRates();
    }

    @Scheduled(initialDelay = 1)
    public void updateOnce() {

        this.currencyService.updateAllCurrencyRates();
    }
}
