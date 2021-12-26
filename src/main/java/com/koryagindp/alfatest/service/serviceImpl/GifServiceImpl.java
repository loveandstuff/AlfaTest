package com.koryagindp.alfatest.service.serviceImpl;

import com.google.gson.Gson;
import com.koryagindp.alfatest.apiclients.GifClient;
import com.koryagindp.alfatest.model.Gif.GIF;
import com.koryagindp.alfatest.service.serviceinterface.GifService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GifServiceImpl implements GifService {
    @Autowired
    private GifClient gifClient;

    @Value("${api_key}")
    private String api_key;

    @Override
    public GIF getGIF(String tag) {
        String json = ResponseEntity.ok(gifClient.getGif(this.api_key,tag)).getBody();
        GIF gifOriginal = new Gson().fromJson(json, GIF.class);
        gifOriginal.getData().getImages().getGifOriginal().setTag(tag);
        return gifOriginal;
    }
}
