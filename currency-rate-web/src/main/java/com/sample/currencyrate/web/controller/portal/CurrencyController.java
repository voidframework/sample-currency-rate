package com.sample.currencyrate.web.controller.portal;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sample.currencyrate.domain.entity.ExchangeInformation;
import com.sample.currencyrate.domain.enumeration.Currency;
import com.sample.currencyrate.domain.service.CurrencyService;
import dev.voidframework.web.bindable.WebController;
import dev.voidframework.web.http.Result;
import dev.voidframework.web.http.TemplateResult;
import dev.voidframework.web.http.annotation.NoCSRF;
import dev.voidframework.web.http.annotation.RequestRoute;
import dev.voidframework.web.http.annotation.RequestVariable;
import dev.voidframework.web.http.routing.Router;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller: Currency.
 */
@NoCSRF
@WebController
@Singleton
public final class CurrencyController {

    private final CurrencyService currencyService;
    private final Router router;

    /**
     * Build a new instance.
     *
     * @param currencyService instance of the service "Currency"
     * @param router          instance of "Router"
     */
    @Inject
    public CurrencyController(final CurrencyService currencyService,
                              final Router router) {

        this.currencyService = currencyService;
        this.router = router;
    }

    /**
     * Display the home page.
     *
     * @return the home page
     */
    @RequestRoute(name = "home_page")
    public Result displayHomePage(@RequestVariable("from") final Currency fromCurrency,
                                  @RequestVariable("to") final Currency toCurrency,
                                  @RequestVariable("value") final BigDecimal value) {

        final List<Currency> currencyList = List.of(Currency.values());
        final ExchangeInformation exchangeInformation = this.currencyService.convert(fromCurrency, toCurrency, value).orElse(null);

        final Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("currencyList", currencyList);
        dataModel.put("exchangeInformation", exchangeInformation);
        dataModel.put("fromCurrency", fromCurrency == null ? Currency.EUR : fromCurrency);
        dataModel.put("toCurrency", toCurrency == null ? Currency.USD : toCurrency);
        dataModel.put("value", value == null ? BigDecimal.ONE : value);

        return Result.ok(TemplateResult.of("portal/homepage.ftl", dataModel));
    }
}
