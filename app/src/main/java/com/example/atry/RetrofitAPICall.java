package com.example.atry;

import retrofit2.Call;
import retrofit2.http.GET;
public interface RetrofitAPICall {
    // as we are making a get request specifying annotation as get and adding a url end point to it.
    @GET("/random")
    Call<RandomDuckImage> getRandomDuckPicture();
}