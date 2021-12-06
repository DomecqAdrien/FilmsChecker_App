package com.esgi.filmchecker.model

data class Comment(
    val userEmail: String,
    val movieId: Int,
    val comment: String
)