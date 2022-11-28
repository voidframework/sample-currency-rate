package com.sample.currencyrate.web.controller;

import com.sample.currencyrate.domain.entity.CurrencyRate;
import com.sample.currencyrate.domain.enumeration.Currency;
import com.sample.currencyrate.domain.repository.CurrencyRateRepository;
import com.sample.currencyrate.domain.repository.EuropeanCentralBankRepository;
import com.sample.currencyrate.web.controller.api.APIController;
import com.sample.currencyrate.web.dto.ConvertCurrencyRequest;
import dev.voidframework.test.annotation.VoidFrameworkJUnitExtension;
import dev.voidframework.web.http.HttpContentTypes;
import dev.voidframework.web.http.HttpReturnCode;
import dev.voidframework.web.http.Result;
import dev.voidframework.web.http.resultprocessor.ResultProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

@ExtendWith(VoidFrameworkJUnitExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
final class APIControllerTest {

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @Mock
    private EuropeanCentralBankRepository europeanCentralBankRepository;

    // No need to explicitly inject CurrencyService, it will be automatically
    // instantiated with the above mocked repository.

    @InjectMocks
    private APIController apiController;

    @Test
    void convertCurrencyEURtoUSD() throws IOException {

        // Arrange
        final List<CurrencyRate> currencyRateList = List.of(
            new CurrencyRate(Currency.EUR, BigDecimal.ONE, BigDecimal.ONE),
            new CurrencyRate(Currency.USD, BigDecimal.valueOf(0.944555), BigDecimal.valueOf(1.058700)));
        Mockito.when(currencyRateRepository.findAllByCurrencyIn(List.of(Currency.EUR, Currency.USD))).thenReturn(currencyRateList);

        final ConvertCurrencyRequest request = new ConvertCurrencyRequest(
            Currency.EUR,
            Currency.USD,
            BigDecimal.ONE);

        // Act
        final Result result = apiController.convertCurrency(request);

        // Assert
        Mockito.verify(currencyRateRepository, Mockito.times(1)).findAllByCurrencyIn(Mockito.any());
        Mockito.verify(currencyRateRepository, Mockito.times(0)).persist(Mockito.any());
        Mockito.verify(europeanCentralBankRepository, Mockito.times(0)).findAllCurrencyRateBaseEuro();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(HttpReturnCode.OK, result.getHttpCode());
        Assertions.assertEquals(HttpContentTypes.APPLICATION_JSON, result.getContentType());

        final ResultProcessor resultProcessor = result.getResultProcessor();
        Assertions.assertNotNull(resultProcessor);

        final byte[] resultContent = resultProcessor.getInputStream().readAllBytes();
        final String resultContentAsString = new String(resultContent, StandardCharsets.UTF_8);
        Assertions.assertNotNull(resultContentAsString);
        Assertions.assertEquals(
            "{\"fromCurrency\":\"EUR\",\"fromValue\":1,\"toCurrency\":\"USD\",\"toValue\":1.0587}",
            resultContentAsString);
    }
}
