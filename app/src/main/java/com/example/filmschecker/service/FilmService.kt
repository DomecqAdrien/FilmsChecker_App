package com.example.filmschecker.service

import com.esgi.filmchecker.model.Comment
import com.example.filmschecker.domain.reservation.Reservation
import com.example.filmschecker.domain.*
import com.example.filmschecker.domain.reservation.Creneau
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*

interface FilmService {
    @GET("genres")
    fun getGenres(): Call<List<Category>>

    @GET("films/{page}")
    fun getAllFilms(@Path("page") nbPage: Int): Observable<List<Film>>

    @GET("film/{film_id}")
    fun getOneFilm(@Path("film_id") filmId: Int) : Call<Film>

    @GET("film/{film_id}/actors")
    fun getActorsByFilm(@Path("film_id") filmId: Int): Call<APIParserDTO>

    @GET("search")
    fun getFilmsBySearch(@Query("query") query: String): Observable<List<Film>>

    @GET("film/{movieId}/user/{userEmail}/note/{note}")
    fun rateMovie(@Path("movieId") movieId: Int, @Path("userEmail") email: String, @Path("note") note: Int)

    @POST("film/{movieId}/user/{userEmail}/comment")
    fun commentMovie(@Path("movieId") movieId: Int, @Path("userEmail") email: String, @Body comment: Comment): Call<String>

    @GET("film/{movieId}/creneaux")
    fun getCreneauxByMovie(@Path ("movieId") movieId: Int): Observable<List<Creneau>>

    @GET("film/{movieId}/comments")
    fun getCommentsByMovie(@Path ("movieId") movieId: Int): Observable<List<Comment>>

    @POST("book-session/")
    fun bookSession(@Body reservation: Reservation): Call<String>

}