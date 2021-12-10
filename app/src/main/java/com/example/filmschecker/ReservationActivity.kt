package com.example.filmschecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.filmschecker.domain.reservation.Reservation
import com.example.filmschecker.adapter.ReservationAdapter
import com.example.filmschecker.domain.reservation.Creneau
import com.example.filmschecker.service.ApiManager
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservationActivity : AppCompatActivity() {


    private lateinit var actorsRecyclerView: RecyclerView
    private lateinit var reservationAdapter : ReservationAdapter
    private lateinit var userEmail: String
    private var filmId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        filmId = this.intent.extras!!.getInt("film_id")
        userEmail = FirebaseAuth.getInstance().currentUser?.email.toString()
        enqueue(ApiManager.getInstance().filmService.getCreneauxByMovie(filmId))
    }

    private fun initRecyclerView() {
        reservationAdapter = ReservationAdapter()
        actorsRecyclerView.apply {
            adapter = reservationAdapter
        }

        reservationAdapter.onItemClick = {
            ApiManager.getInstance().filmService.bookSession(Reservation(it.salleId, it.id, userEmail, filmId)).enqueue(object:
                Callback<String> {

                override fun onResponse(call: Call<String>, response: Response<String>) {

                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.i("error", t.message.toString())
                }
            })
        }


    }

    private fun enqueue(creneaux: Observable<List<Creneau>>){
        creneaux
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showCreneaux(it ?: emptyList())
            }
    }

    private fun showCreneaux(creneaux: List<Creneau>) {
        Log.i("creneaux", creneaux.toString())
        setContentView(R.layout.activity_reservation)
        actorsRecyclerView = findViewById(R.id.reservation_creneaux)
        initRecyclerView()
        reservationAdapter.crenaux = creneaux
    }


}