package com.example.c323p8moviesearch

import androidx.recyclerview.widget.DiffUtil

class MovieDiffItemCallback :DiffUtil.ItemCallback<Movie>() {
    //checks if contents and items are the same or not
    override fun areContentsTheSame(oldItem: Movie, newItem: Movie)
        = (oldItem == newItem)

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie)
        = (oldItem.Title == newItem.Title)
    }