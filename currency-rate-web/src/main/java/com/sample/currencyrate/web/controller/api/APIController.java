package com.sample.currencyrate.web.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sample.currencyrate.domain.entity.ExchangeInformation;
import com.sample.currencyrate.domain.service.CurrencyService;
import com.sample.currencyrate.web.dto.ConvertCurrencyRequest;
import com.sample.currencyrate.web.dto.ConvertCurrencyToAllRequest;
import dev.voidframework.core.utils.JsonUtils;
import dev.voidframework.validation.Validated;
import dev.voidframework.validation.Validation;
import dev.voidframework.web.bindable.WebController;
import dev.voidframework.web.http.HttpMethod;
import dev.voidframework.web.http.Result;
import dev.voidframework.web.http.annotation.NoCSRF;
import dev.voidframework.web.http.annotation.RequestBody;
import dev.voidframework.web.http.annotation.RequestRoute;

import java.util.List;

/**
 * Controller API.
 */
@NoCSRF
@WebController(prefixRoute = "api")
@Singleton
public final class APIController {

    private final CurrencyService currencyService;
    private final Validation validation;

    /**
     * Build a new instance.
     *
     * @param currencyService instance of the service "Currency"
     * @param validation      instance of the service "Validation"
     */
    @Inject
    public APIController(final CurrencyService currencyService,
                         final Validation validation) {

        this.currencyService = currencyService;
        this.validation = validation;
    }

    /**
     * Convert from a given base currency to others available currencies.
     *
     * @param request the request
     * @return a list of conversion rate
     */
    @RequestRoute(method = HttpMethod.POST, route = "/convert-currency-to-all")
    public Result convertToAll(@RequestBody final ConvertCurrencyToAllRequest request) {

        // Validates request
        final Validated<ConvertCurrencyToAllRequest> requestValidated = validation.validate(request);
        if (requestValidated.hasError()) {
            return Result.badRequest(JsonUtils.toJson(requestValidated.getError()));
        }

        // Converts value from currency to all others
        final List<ExchangeInformation> exchangeInformationList = this.currencyService.convertToAll(
            request.fromCurrency(),
            request.value());

        // Returns result as JSON
        final JsonNode payload = JsonUtils.toJson(exchangeInformationList);
        return Result.ok(payload);
    }

    /**
     * Convert from a given base currency to others available currencies.
     *
     * @param request the request
     * @return a list of conversion rate
     */
    @RequestRoute(method = HttpMethod.POST, route = "/convert-currency")
    public Result convertCurrency(@RequestBody final ConvertCurrencyRequest request) {

        // Validates request
        final Validated<ConvertCurrencyRequest> requestValidated = validation.validate(request);
        if (requestValidated.hasError()) {
            return Result.badRequest(JsonUtils.toJson(requestValidated.getError()));
        }

        // Converts value from currency to another + returns result as JSON
        return this.currencyService.convert(
                request.fromCurrency(),
                request.toCurrency(),
                request.value())
            .map(JsonUtils::toJson)
            .map(Result::ok)
            .orElseGet(Result::internalServerError);
    }
}
