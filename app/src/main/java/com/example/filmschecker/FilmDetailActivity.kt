package com.example.filmschecker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filmschecker.adapter.ActorsAdapter
import com.example.filmschecker.databinding.ActivityFilmsBinding
import com.example.filmschecker.domain.*
import com.example.filmschecker.service.ApiManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FilmDetailActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityFilmsBinding
    private lateinit var film: Film
    private var filmId: Int = 0
    private lateinit var actorsRecyclerView: RecyclerView
    private lateinit var actors : List<Actor>
    private lateinit var crew : List<Crew>
    private lateinit var actorsAdapter : ActorsAdapter
    private lateinit var userEmail :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityFilmsBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_loading)
        //val view = binding.root
        //setContentView(view)
        actorsRecyclerView = binding.actorsRecyclerView
        initRecyclerView()
        filmId = this.intent.extras!!.getInt("film_id")
        val isReservable = this.intent.extras?.getBoolean("isReservable")
        userEmail = FirebaseAuth.getInstance().currentUser?.email.toString()

        getFilm()
    }

    private fun initRecyclerView() {
        actorsAdapter = ActorsAdapter()
        actorsRecyclerView.apply {
            adapter = actorsAdapter
        }
    }

    private fun getFilm() {
        val filmsCall : Call<Film> = ApiManager.getInstance().filmService.getOneFilm(filmId)
        filmsCall.enqueue(object: Callback<Film> {
            override fun onResponse(call: Call<Film>, response: Response<Film>) {
                film = response.body()!!
                initFilm()
                getActors()
                val view = binding.root
                setContentView(view)
            }
            override fun onFailure(call: Call<Film>, t: Throwable) {
                Log.i("error",t.message.toString())
            }
        })
    }

    private fun getActors() {

        val creditsCall : Call<APIParserDTO> = ApiManager.getInstance().filmService.getActorsByFilm(filmId)

        creditsCall.enqueue(object : Callback<APIParserDTO>{
            override fun onResponse(call: Call<APIParserDTO>, response: Response<APIParserDTO>) {
                actors = response.body()?.actors?: emptyList()
                crew = response.body()?.crew?: emptyList()
                Log.i("crew size",crew.size.toString())
                actorsAdapter.actors = actors
            }

            override fun onFailure(call: Call<APIParserDTO>, t: Throwable) {}
        })
    }



    private fun initFilm() {
        with(binding){

            btnCommentaires.setOnClickListener {
                val intent = Intent(this@FilmDetailActivity, CommentairesActivity::class.java)
                startActivity(Intent())
            }

            manageFavori(true)
            Glide.with(applicationContext)
                .load("https://image.tmdb.org/t/p/w500"+film.affiche)
                .placeholder(R.drawable.ic_launcher_background)
                .into(filmAffiche)
            filmTitle.text = film.title
            filmDate.text = film.date!!.subSequence(0,4)
            filmRestriction.text = film.restriction
            val heure = film.runtime / 60
            val minute = film.runtime % 60
            filmDuration.text = "$heure h $minute min"
            filmDescription.text = film.overview
            var genres = ""
            for(i in film.genres.indices){
                genres += "${film.genres[i].name}"
                if (i != film.genres.size.minus(1)) {
                    genres += ", "
                }
            }
            filmCategories.text = genres
            filmNote.text = "${film.voteAverage}/10"

            val drawable = when {
                film.popularity > 65 -> getDrawable(R.drawable.square_green)
                film.popularity > 40 -> getDrawable(R.drawable.square_orange)
                else -> getDrawable(R.drawable.square_red)
            }
            filmScoreSquare.setImageDrawable(drawable)
            filmPopularity.text = film.popularity.toInt().toString()

        }
    }

    private fun manageFavori(isCheckButton: Boolean){
        database = FirebaseDatabase.getInstance().reference
        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.i("error",error.message)
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("favoris/$userEmail-$filmId".replace(".","*"))){
                    if(!isCheckButton){
                        removeFavori()
                    }else{
                        binding.filmManageFavori.setBackgroundColor(getColor(R.color.herbGreen))
                        binding.filmManageFavori.text = getText(R.string.favori)
                    }
                    Log.i("data","EXISTS")
                }

                else{
                    Log.i("data","EXISTS PAS")
                    if(!isCheckButton)
                        addFavori()
                }
            }
        })
    }
    fun onClickFavori(v: View?=null){
        manageFavori(false)
    }

    fun onClickPlace(v: View?=null) {
        startActivity(Intent(this, ReservationActivity::class.java))
    }

    private fun addFavori() {
        val f = Favori(filmId, userEmail)
        Log.i("dburl", database.toString())
        database.child("favoris").child("$userEmail-$filmId".replace(".", "*")).setValue(f)
            .addOnSuccessListener {
                Log.i("db", "success")
                with(binding) {
                    filmManageFavori.setBackgroundColor(getColor(R.color.herbGreen))
                    filmManageFavori.text = getText(R.string.favori)
                }
            }
            .addOnFailureListener {
                Log.i("db", it.toString())
            }
    }

    private fun removeFavori(){
        Log.i("data","REMOVE FAV")
        database.child("favoris").child("$userEmail-$filmId".replace(".", "*")).removeValue()
        with(binding){
            filmManageFavori.setBackgroundColor(getColor(R.color.holoDarkBlue))
            filmManageFavori.text = getText(R.string.addFav)
        }
    }
}