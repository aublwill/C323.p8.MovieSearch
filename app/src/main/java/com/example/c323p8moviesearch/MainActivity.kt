package com.example.c323p8moviesearch

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.c323p8moviesearch.databinding.MovieItemBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//imdb api
private const val BASE_URL = "https://www.omdbapi.com"
private const val API_KEY = "5d06405d"

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    //binding vaiables
    private var _binding: MovieItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = MovieItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //creates retrofit instance
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //api
        val omDbApi: OMDbApi = retrofit.create(OMDbApi::class.java)

        /*
        when search button is pressed
            -if search is not empty, get movie information from imdb api
            -fill in respective info on screen
         */
        binding.bSearch.setOnClickListener {
            val title = binding.etMSearch.text.toString()
            if (title.isNotEmpty()) {
                val call: Call<Movie> = omDbApi.getMovie(title, API_KEY)
                Log.i(TAG, call.toString())
                call?.enqueue(object : Callback<Movie?> {
                    override fun onResponse(call: Call<Movie?>, response: Response<Movie?>) {
                        if (response.isSuccessful) {
                            val movie = response.body()
                            if (movie != null) {
                                //fill in blanks/information on screen
                                binding.tvName.text = movie.Title
                                binding.tvYear.text = getString(R.string.year, movie.Year)
                                binding.tvTime.text = getString(R.string.runTime,movie.Runtime)
                                binding.tvGenre.text = getString(R.string.genre, movie.Genre)
                                binding.tvLink.text = getString(R.string.link, movie.imdbID)
                                binding.imbdRateBar.rating = movie.imdbRating/2
                                binding.rating.text = movie.Rated
                                //creates/gets poster image for movie
                                Glide.with(this@MainActivity)
                                    .load(movie.Poster)
                                    .into(binding.imageView)
                            } else {
                                Log.w(TAG, "Did not receive valid response body from Movie API..yay")
                            }
                        }else{
                            Log.w(TAG, "Did not receive valid response")
                        }
                    }
                    /*
                    creates log is there was an error
                     */
                    override fun onFailure(call: Call<Movie?>, t: Throwable) {
                        Log.i(TAG, "onFailure $t")
                    }
                })
            } else {
                // show toast if search was empty
                Toast.makeText(this, "Please enter movie title.", Toast.LENGTH_SHORT).show()
            }
        }
        /*
        creates link to imdb webpage of the movie/ takes you to the web
         */
        binding.tvLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(binding.tvLink.text.toString()))
            startActivity(intent)
        }
        /*
        creates sharing available with text, title, and link to movie
         */
        binding.bShare.setOnClickListener {
            val title = binding.tvName.text.toString()
            val link = binding.tvLink.text.toString()
            val txt = "Check out this movie! $title \n $link"

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share Movie")
            shareIntent.putExtra(Intent.EXTRA_TEXT, txt)
            startActivity(Intent.createChooser(shareIntent, "Share Movie Details"))
        }
        /*
        sets up feedback email for user
         */
        binding.bFeedback.setOnClickListener {
            val devEmail = "aublwill@iu.edu"
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:$devEmail")
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
            startActivity(emailIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}