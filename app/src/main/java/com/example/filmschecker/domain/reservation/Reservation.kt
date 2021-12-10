package com.esgi.filmchecker.model

data class Reservation(
    val numeroSalle: Int? = null,
    val creneauId: String? = null,
    val userEmail: String? = null,
    val movieId: Int? = null
)