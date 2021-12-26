package com.koryagindp.alfatest.service.serviceinterface;

public interface ExchangeRatesService {
    void getLatestCurrencyRates(String currency);

    void getYesterdayCurrencyRates(String currency);

    int compareCurrencyRates(String currency) throws Exception;

    boolean getCurrencies();

}
