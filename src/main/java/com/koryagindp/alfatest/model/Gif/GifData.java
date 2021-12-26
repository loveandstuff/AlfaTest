package com.koryagindp.alfatest.model.Gif;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GifData {
    @SerializedName("images")
    private Images images;
}
