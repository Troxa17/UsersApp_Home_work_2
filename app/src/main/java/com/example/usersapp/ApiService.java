package com.example.usersapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("api/")
    Call<UserResponse> getRandomUser();
}
