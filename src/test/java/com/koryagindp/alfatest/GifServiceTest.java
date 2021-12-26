package com.koryagindp.alfatest;

import com.koryagindp.alfatest.apiclients.GifClient;
import com.koryagindp.alfatest.service.serviceImpl.GifServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GifServiceTest {

    @MockBean
    GifClient gifClient;

    @Autowired
    private GifServiceImpl gifService;

    private String stringGif;

    @BeforeEach
    public void setUp(){
        this.stringGif ="{\"data\":{\"images\":{\"original" +
                "\":{\"url\":\"https://media3.giphy.com/media/UsGiYCYbY2vcTutV8z/giphy.gif?cid\\"+
                "u003d3608ce578455a5fc14a6fb1a8c226a2a3f326b018405c108\\u0026rid\\u003dgiphy.gif\\u0026ct\\u003dg\",\"tag\":\"Homer\"}}}}";
    }

    @Test
    public void shouldRetrunGifWithHomerTag(){

        Mockito.when(gifClient.getGif(anyString(), anyString()))
                .thenReturn(this.stringGif);

        String testTag = gifService.getGIF("Homer").getData().getImages().getGifOriginal().getTag();
        assertEquals("Homer", testTag);
    }
}
