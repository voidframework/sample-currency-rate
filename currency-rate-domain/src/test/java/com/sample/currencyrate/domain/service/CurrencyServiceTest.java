package com.sample.currencyrate.domain.service;

import com.sample.currencyrate.domain.entity.CurrencyRate;
import com.sample.currencyrate.domain.entity.ExchangeInformation;
import com.sample.currencyrate.domain.enumeration.Currency;
import com.sample.currencyrate.domain.repository.CurrencyRateRepository;
import com.sample.currencyrate.domain.repository.EuropeanCentralBankRepository;
import dev.voidframework.test.annotation.VoidFrameworkJUnitExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(VoidFrameworkJUnitExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
final class CurrencyServiceTest {

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @Mock
    private EuropeanCentralBankRepository europeanCentralBankRepository;

    @InjectMocks
    private CurrencyService currencyService;

    @Test
    void convertEURtoUSD() {

        // Arrange
        final List<CurrencyRate> currencyRateList = List.of(
            new CurrencyRate(Currency.EUR, BigDecimal.ONE, BigDecimal.ONE),
            new CurrencyRate(Currency.USD, BigDecimal.valueOf(0.944555), BigDecimal.valueOf(1.058700)));
        Mockito.when(currencyRateRepository.findAllByCurrencyIn(List.of(Currency.EUR, Currency.USD))).thenReturn(currencyRateList);

        // Act
        final Optional<ExchangeInformation> resultOptional = currencyService.convert(
            Currency.EUR,
            Currency.USD,
            BigDecimal.valueOf(25.98));

        // Assert
        Assertions.assertTrue(resultOptional.isPresent());

        final ExchangeInformation exchangeInformation = resultOptional.get();
        Assertions.assertEquals(Currency.EUR, exchangeInformation.fromCurrency());
        Assertions.assertEquals(BigDecimal.valueOf(25.98), exchangeInformation.fromValue());
        Assertions.assertEquals(Currency.USD, exchangeInformation.toCurrency());
        Assertions.assertEquals(BigDecimal.valueOf(27.505026), exchangeInformation.toValue());

        Mockito.verify(currencyRateRepository, Mockito.times(1)).findAllByCurrencyIn(Mockito.any());
        Mockito.verify(currencyRateRepository, Mockito.times(0)).persist(Mockito.any());
        Mockito.verify(europeanCentralBankRepository, Mockito.times(0)).findAllCurrencyRateBaseEuro();
    }

    @Test
    void convertUSDtoGBP() {

        // Arrange
        final List<CurrencyRate> currencyRateList = List.of(
            new CurrencyRate(Currency.GBP, BigDecimal.valueOf(0.860850), BigDecimal.valueOf(1.161643)),
            new CurrencyRate(Currency.USD, BigDecimal.valueOf(0.944555), BigDecimal.valueOf(1.058700)));
        Mockito.when(currencyRateRepository.findAllByCurrencyIn(List.of(Currency.USD, Currency.GBP))).thenReturn(currencyRateList);

        // Act
        final Optional<ExchangeInformation> resultOptional = currencyService.convert(
            Currency.USD,
            Currency.GBP,
            BigDecimal.valueOf(25.98));

        // Assert
        Assertions.assertTrue(resultOptional.isPresent());

        final ExchangeInformation exchangeInformation = resultOptional.get();
        Assertions.assertEquals(Currency.USD, exchangeInformation.fromCurrency());
        Assertions.assertEquals(BigDecimal.valueOf(25.98), exchangeInformation.fromValue());
        Assertions.assertEquals(Currency.GBP, exchangeInformation.toCurrency());
        Assertions.assertEquals(new BigDecimal("23.67770163210"), exchangeInformation.toValue());

        Mockito.verify(currencyRateRepository, Mockito.times(1)).findAllByCurrencyIn(Mockito.any());
        Mockito.verify(currencyRateRepository, Mockito.times(0)).persist(Mockito.any());
        Mockito.verify(europeanCentralBankRepository, Mockito.times(0)).findAllCurrencyRateBaseEuro();
    }

    @Test
    void updateAllCurrencyRates() {

        // Arrange
        final List<CurrencyRate> currencyRateList = List.of(
            new CurrencyRate(Currency.USD, BigDecimal.valueOf(0.944555), BigDecimal.valueOf(1.058700)));
        Mockito.when(europeanCentralBankRepository.findAllCurrencyRateBaseEuro()).thenReturn(currencyRateList);

        // Act
        currencyService.updateAllCurrencyRates();

        // Assert
        Mockito.verify(europeanCentralBankRepository, Mockito.times(1)).findAllCurrencyRateBaseEuro();
        Mockito.verify(currencyRateRepository, Mockito.times(2)).persist(Mockito.any(CurrencyRate.class));
    }
}
