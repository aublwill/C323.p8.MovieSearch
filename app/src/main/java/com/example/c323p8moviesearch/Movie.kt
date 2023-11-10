package com.example.c323p8moviesearch
import com.google.gson.annotations.SerializedName

//movie data
data class Movie (
    val Title:String,
    val Rated:String,
    val Genre:String,
    val Year:String,
    val Runtime:String,
    val imdbRating:Float,
    @SerializedName("Poster") val Poster:String,
    @SerializedName("imdbID") val imdbID:String

    )