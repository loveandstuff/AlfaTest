package com.koryagindp.alfatest.apiclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "giphy-api", url = "${urlGiphy}")
public interface GifClient {

    @GetMapping("/random")
    String getGif(@RequestParam("api_key") String api_key, @RequestParam("tag") String tag);

}