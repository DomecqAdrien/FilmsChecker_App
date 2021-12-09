package com.example.filmschecker.domain.reservation

data class Reservation(
    val seance: Seance,
    val userEmail: String
)