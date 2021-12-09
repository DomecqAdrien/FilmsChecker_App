package com.example.filmschecker.domain.reservation

import com.example.filmschecker.domain.Film

data class Seance(
    val salle: Salle,
    val creneau: Creneau,
    val film: Film
)
