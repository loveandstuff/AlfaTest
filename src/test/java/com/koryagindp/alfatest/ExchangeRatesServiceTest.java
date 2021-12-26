package com.koryagindp.alfatest;


import com.koryagindp.alfatest.apiclients.OpenExchangeClient;
import com.koryagindp.alfatest.model.CurrencyRates;
import com.koryagindp.alfatest.service.serviceinterface.ExchangeRatesService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExchangeRatesServiceTest {

    @MockBean
    OpenExchangeClient currenciesClient;

    @Autowired
    private ExchangeRatesService exchangeRatesService;
    @Value("${app_id}")
    private String kek;

    private CurrencyRates newRates;
    private CurrencyRates oldRates;
    private Map<String, String> testMapOfCurrencies;

    @BeforeEach
    public void setUp(){
        this.newRates = new CurrencyRates();
        this.oldRates = new CurrencyRates();
        newRates.setBase("USD");
        oldRates.setBase("USD");
        this.testMapOfCurrencies = new HashMap<>();
        this.testMapOfCurrencies.put("RUB", "Russian Ruble");
        this.testMapOfCurrencies.put("JPY", "Japanese Yen");
        this.testMapOfCurrencies.put("EUR", "Euro");
        Map<String, Double> mapFresh = new HashMap<>();
        Map<String, Double> mapOld = new HashMap<>();
        mapFresh.put("RUB", 10.0);
        mapFresh.put("EUR", 1.0);
        mapFresh.put("JPY", 5.0);
        mapOld.put("RUB", 1.0);
        mapOld.put("EUR", 10.0);
        mapOld.put("JPY", 5.0);
        this.newRates.setRates(mapFresh);
        this.oldRates.setRates(mapOld);
    }

    @Test
    public void whenNewRatesBiggerThanOldRates()  throws Exception {

        Mockito.when(currenciesClient.getCurrencies()).thenReturn(this.testMapOfCurrencies);
        Mockito.when(currenciesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.newRates);
        Mockito.when(currenciesClient.getYesterdayRates(anyString(), anyString(), anyString()))
                .thenReturn(this.oldRates);

        int result = exchangeRatesService.compareCurrencyRates("RUB");
        Assertions.assertEquals(1, result);
    }

    @Test
    public void whenNewRatesLowerThanOldRates()  throws Exception {

        Mockito.when(currenciesClient.getCurrencies()).thenReturn(this.testMapOfCurrencies);
        Mockito.when(currenciesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.newRates);
        Mockito.when(currenciesClient.getYesterdayRates(anyString(), anyString(), anyString()))
                .thenReturn(this.oldRates);

        int result = exchangeRatesService.compareCurrencyRates("EUR");
        Assertions.assertEquals(-1, result);
    }

    @Test
    public void whenBothRatesAreEquals()  throws Exception {

        Mockito.when(currenciesClient.getCurrencies()).thenReturn(this.testMapOfCurrencies);
        Mockito.when(currenciesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.newRates);
        Mockito.when(currenciesClient.getYesterdayRates(anyString(), anyString(), anyString()))
                .thenReturn(this.oldRates);

        int result = exchangeRatesService.compareCurrencyRates("JPY");
        Assertions.assertEquals(0, result);
    }

    @Test
    public void whenWrongCurrencyCode()  throws Exception {

        Mockito.when(currenciesClient.getCurrencies()).thenReturn(this.testMapOfCurrencies);
        Mockito.when(currenciesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.newRates);
        Mockito.when(currenciesClient.getYesterdayRates(anyString(), anyString(), anyString()))
                .thenReturn(this.oldRates);

        int result = exchangeRatesService.compareCurrencyRates("ABC");
        Assertions.assertEquals(404, result);
    }

    @Test
    public void whenNullInput()  throws Exception {

        Mockito.when(currenciesClient.getCurrencies()).thenReturn(this.testMapOfCurrencies);
        Mockito.when(currenciesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.newRates);
        Mockito.when(currenciesClient.getYesterdayRates(anyString(), anyString(), anyString()))
                .thenReturn(this.oldRates);

        int result = exchangeRatesService.compareCurrencyRates(null);
        Assertions.assertEquals(404, result);
    }

    @Test
    public void whenNewRatesAreNull()  throws Exception {

        Mockito.when(currenciesClient.getCurrencies()).thenReturn(this.testMapOfCurrencies);
        Mockito.when(currenciesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(null);
        Mockito.when(currenciesClient.getYesterdayRates(anyString(), anyString(), anyString()))
                .thenReturn(this.oldRates);

        int result = exchangeRatesService.compareCurrencyRates("ABC");
        Assertions.assertEquals(404, result);
    }

    @Test
    public void whenOldRatesAreNull()  throws Exception {

        Mockito.when(currenciesClient.getCurrencies()).thenReturn(this.testMapOfCurrencies);
        Mockito.when(currenciesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.newRates);
        Mockito.when(currenciesClient.getYesterdayRates(anyString(), anyString(), anyString()))
                .thenReturn(null);

        int result = exchangeRatesService.compareCurrencyRates("ABC");
        Assertions.assertEquals(404, result);
    }

    @Test
    public void whenBothRatesAreNull()  throws Exception {

        Mockito.when(currenciesClient.getCurrencies()).thenReturn(this.testMapOfCurrencies);
        Mockito.when(currenciesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(null);
        Mockito.when(currenciesClient.getYesterdayRates(anyString(), anyString(), anyString()))
                .thenReturn(null);

        int result = exchangeRatesService.compareCurrencyRates("ABC");
        Assertions.assertEquals(404, result);
    }

    @Test
    public void getCurrenciesShouldReturnTrue(){

        Mockito.when(currenciesClient.getCurrencies()).thenReturn(this.testMapOfCurrencies);

        Assertions.assertTrue(exchangeRatesService.getCurrencies());
    }
}
