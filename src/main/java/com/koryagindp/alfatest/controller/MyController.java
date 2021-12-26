package com.koryagindp.alfatest.controller;

import com.koryagindp.alfatest.model.Gif.GIF;
import com.koryagindp.alfatest.service.serviceinterface.ExchangeRatesService;
import com.koryagindp.alfatest.service.serviceinterface.GifService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class MyController {

    @Autowired
    private ExchangeRatesService exchangeRatesService;
    @Autowired
    private GifService gifService;


    @GetMapping("/gifs/{currency}")
    public String test(@PathVariable String currency,  Model model) throws Exception {

        String tag = "bart";
        int compareResult = exchangeRatesService.compareCurrencyRates(currency);

        switch (compareResult){
            case 1: tag = "rich";
                break;
            case -1: tag = "broke";
                break;
            case 0: tag = "nothing";
                break;
            case 404: tag = "fatal error";
                break;
        }

        GIF gif = gifService.getGIF(tag);
        String url = gif.getData().getImages().getGifOriginal().getUrl();
        model.addAttribute("myGif", url);
        return "main-view";
    }

    @RequestMapping("/get_gif/{tag}")
    public ResponseEntity<Object> getGif(@PathVariable String tag){
        return ResponseEntity.ok(gifService.getGIF(tag));
    }
}