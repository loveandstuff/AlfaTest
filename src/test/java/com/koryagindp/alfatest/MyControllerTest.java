package com.koryagindp.alfatest;


import com.koryagindp.alfatest.controller.MyController;
import com.koryagindp.alfatest.model.CurrencyRates;
import com.koryagindp.alfatest.model.Gif.GIF;
import com.koryagindp.alfatest.model.Gif.GifData;
import com.koryagindp.alfatest.model.Gif.GifOriginal;
import com.koryagindp.alfatest.model.Gif.Images;
import com.koryagindp.alfatest.service.serviceinterface.ExchangeRatesService;
import com.koryagindp.alfatest.service.serviceinterface.GifService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(MyController.class)
public class MyControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GifService gifService;
    @MockBean
    private ExchangeRatesService exchangeRatesService;
    private GIF gif;
    @Value("${test_url}")
    private String test_url;
    @Value("rich")
    private String rich;
    @Value("broke")
    private String broke;
    @Value("nothing")
    private String nothing;
    @Value("RUB")
    private String currency;

    private CurrencyRates newRates;
    private CurrencyRates oldRates;




    @BeforeEach
    public void setUp(){
        this.test_url = "test_url=https://media0.giphy.com/media/3oriePqJOFAdhit8ze/giphy."+
                "gif?cid=3608ce57662679cf06cd09ed7e763a1e9d1f1b01d07527ef&rid=giphy.gif&ct=g";
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        this.newRates = new CurrencyRates();
        this.oldRates = new CurrencyRates();
        newRates.setBase("USD");
        oldRates.setBase("USD");
    }



    @Test
    public void shouldReturnRichGif () throws Exception {

        this.gif = new GIF(new GifData(new Images(new GifOriginal(test_url, rich))));
        Map<String, Double> mapFresh = new HashMap<>();
        mapFresh.put(currency, 10.0);
        Map<String, Double> mapOld = new HashMap<>();
        mapOld.put(currency, 1.0);
        newRates.setRates(mapFresh);
        oldRates.setRates(mapOld);

        Mockito.when(exchangeRatesService.compareCurrencyRates(currency))
                .thenReturn(1);
        Mockito.when(gifService.getGIF(rich))
                .thenReturn(this.gif);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/gifs/RUB"))
                .andExpect(status().isOk()).andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main-view"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("myGif"));
        assertThat(this.gifService).isNotNull();
        Mockito.verify(gifService, Mockito.times(1)).getGIF(rich);
        Mockito.verifyNoMoreInteractions(gifService);
    }

    @Test
    public void shouldReturnBrokeGif () throws Exception {

        this.gif = new GIF(new GifData(new Images(new GifOriginal(test_url, broke))));
        Map<String, Double> mapFresh = new HashMap<>();
        mapFresh.put(currency, 1.0);
        Map<String, Double> mapOld = new HashMap<>();
        mapOld.put(currency, 10.0);
        newRates.setRates(mapFresh);
        oldRates.setRates(mapOld);

        Mockito.when(exchangeRatesService.compareCurrencyRates(currency))
                .thenReturn(-1);
        Mockito.when(gifService.getGIF(broke))
                .thenReturn(this.gif);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/gifs/RUB"))
                .andExpect(status().isOk()).andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main-view"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("myGif"));
        assertThat(this.gifService).isNotNull();
        Mockito.verify(gifService, Mockito.times(1)).getGIF(broke);
        Mockito.verifyNoMoreInteractions(gifService);
    }

    @Test
    public void shouldReturnNothingGif () throws Exception {

        this.gif = new GIF(new GifData(new Images(new GifOriginal(test_url, nothing))));
        Map<String, Double> mapFresh = new HashMap<>();
        mapFresh.put(currency, 10.0);
        Map<String, Double> mapOld = new HashMap<>();
        mapOld.put(currency, 10.0);
        newRates.setRates(mapFresh);
        oldRates.setRates(mapOld);

        Mockito.when(exchangeRatesService.compareCurrencyRates(currency))
                .thenReturn(0);
        Mockito.when(gifService.getGIF(nothing))
                .thenReturn(this.gif);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/gifs/RUB"))
                .andExpect(status().isOk()).andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main-view"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("myGif"));
        assertThat(this.gifService).isNotNull();
        Mockito.verify(gifService, Mockito.times(1)).getGIF(nothing);
        Mockito.verifyNoMoreInteractions(gifService);
    }

    @Test
    public void shouldReturn404Gif () throws Exception {

        this.gif = new GIF(new GifData(new Images(new GifOriginal(test_url, "fatal error"))));
        newRates.setRates(null);
        oldRates.setRates(null);

        Mockito.when(exchangeRatesService.compareCurrencyRates("ABC"))
                .thenReturn(404);
        Mockito.when(gifService.getGIF("fatal error"))
                .thenReturn(this.gif);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/gifs/ABC"))
                .andExpect(status().isOk()).andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("main-view"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("myGif"));
        assertThat(this.gifService).isNotNull();
        Mockito.verify(gifService, Mockito.times(1)).getGIF("fatal error");
        Mockito.verifyNoMoreInteractions(gifService);
    }
}
