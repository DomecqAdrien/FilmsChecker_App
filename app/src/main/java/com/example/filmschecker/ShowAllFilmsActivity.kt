package com.example.filmschecker

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.filmschecker.adapter.FilmAdapter
import com.example.filmschecker.domain.Category
import com.example.filmschecker.domain.Film
import com.example.filmschecker.service.ApiManager
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowAllFilmsActivity : AppCompatActivity() {

    private var loading = true
    private var page = 1
    private var films : List<Film> = listOf()
    private lateinit var genres: List<Category>

    private lateinit var filmsRecyclerView: RecyclerView
    private lateinit var filmsAdapter : FilmAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)


        getCategories()

    }

    private fun setView() {

        setContentView(R.layout.activity_show_films)

        filmsAdapter = FilmAdapter(this)
        filmsRecyclerView = findViewById(R.id.reservation_creneaux)

        filmsRecyclerView.apply {
            adapter = filmsAdapter
        }

        val searchView = findViewById<SearchView>(R.id.search_bar)

        searchView.setOnCloseListener {
            page = 1
            films = emptyList()
            getFilms()
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                page = 1
                films = emptyList()
                getFilmsBySearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                page = 1
                Log.i("string",newText)
                films = emptyList()
                getFilmsBySearch(newText)

                return true
            }
        })

        filmsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if(!loading){
                        page++
                        getFilms()
                    }
                }
            }
        })
    }

    private fun getCategories(){
        val genresCall : Call<List<Category>> = ApiManager.getInstance().filmService.getGenres()
        Log.i("test", "test")
        genresCall.enqueue(object: Callback<List<Category>>{

            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                genres = response.body()?: emptyList()
                Log.i("films", genres.toString())
                getFilms()
            }
            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Log.i("error",t.message.toString())
            }
        })
    }

    private fun enqueue(filmsSearchCall: Observable<List<Film>>){
        Log.i("test","test")
        filmsSearchCall
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                initFilm(it ?: emptyList())
            }
    }

    private fun getFilmsBySearch(query: String){
        enqueue(ApiManager.getInstance().filmService.getFilmsBySearch(query))
    }

    private fun getFilms() {
        enqueue(ApiManager.getInstance().filmService.getAllFilms(page))
    }

    private fun initFilm(newFilms: List<Film>) {
        setView()
        for(f: Film in newFilms){
            f.genres = ArrayList()
            for(i in f.genresId.indices){
                f.genres.add(genres.stream().filter { it.id  == f.genresId[i]}.findFirst().get())
            }
        }
        films= films + newFilms
        filmsAdapter.films = films
        loading = false
        Log.i("init","end init film")
    }
}