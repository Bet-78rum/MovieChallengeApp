package com.example.moviechallengeapp.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val release_date: String,
    val poster_path: String?,
    val vote_average: Double
)
