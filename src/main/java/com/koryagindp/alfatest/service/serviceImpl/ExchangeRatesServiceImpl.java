package com.koryagindp.alfatest.service.serviceImpl;

import com.koryagindp.alfatest.apiclients.OpenExchangeClient;
import com.koryagindp.alfatest.model.CurrencyRates;
import com.koryagindp.alfatest.service.serviceinterface.ExchangeRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {
    @Autowired
    private OpenExchangeClient currenciesClient;

    @Value("${app_id}")
    private String app_id;

    private CurrencyRates newRates;
    private CurrencyRates oldRates;

    private Map<String, String> currencies;

    @Override
    public boolean getCurrencies(){
        Map<String, String> tempMap = ResponseEntity.ok(currenciesClient.getCurrencies()).getBody();
        if (tempMap != null){
            this.currencies = tempMap;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void getLatestCurrencyRates (String currency) {

        this.newRates = ResponseEntity.ok(currenciesClient.getLatestRates(this.app_id, currency)).getBody();
    }

    @Override
    public void getYesterdayCurrencyRates(String currency) {

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cal.add(Calendar.DATE, -1);
        String yesterdayDate = dateFormat.format(cal.getTime());

        this.oldRates = ResponseEntity.ok(currenciesClient.getYesterdayRates(yesterdayDate, this.app_id, currency)).getBody();
    }

    @Override
    public int compareCurrencyRates(String incomeCurrency) {

        this.getCurrencies();
        int result = 0;
        if (incomeCurrency != null && currencies.containsKey(incomeCurrency)){
            getLatestCurrencyRates(incomeCurrency);
            getYesterdayCurrencyRates(incomeCurrency);
            if (this.newRates != null && this.oldRates != null){
                result = Double.compare(this.newRates.getRates().get(incomeCurrency), this.oldRates.getRates().get(incomeCurrency));
            }
        } else {
            result = 404;
        }
        return result;
    }
}
