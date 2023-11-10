package com.example.c323p8moviesearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface OMDbApi {

    // To get a movie based on search
    @GET("/")
    fun getMovie(
        @Query("t") title: String,
        @Query("apikey") apiKey: String = "5d06405d"
    ): Call<Movie>
}