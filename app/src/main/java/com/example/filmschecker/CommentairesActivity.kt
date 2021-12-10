package com.example.filmschecker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.esgi.filmchecker.model.Comment
import com.example.filmschecker.adapter.CommentaireAdapter
import com.example.filmschecker.domain.Film
import com.example.filmschecker.service.ApiManager
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_commentaires.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentairesActivity : AppCompatActivity() {


    private lateinit var actorsRecyclerView: RecyclerView
    private lateinit var commentairesAdapter : CommentaireAdapter
    private lateinit var userEmail: String
    private lateinit var comments: MutableList<Comment>
    private var filmId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        filmId = this.intent.extras!!.getInt("film_id")
        userEmail = FirebaseAuth.getInstance().currentUser?.email.toString()
        enqueue(ApiManager.getInstance().filmService.getCommentsByMovie(filmId))
    }

    private fun initRecyclerView() {
        commentairesAdapter = CommentaireAdapter()
        actorsRecyclerView.apply {
            adapter = commentairesAdapter
        }
        btn_add_comment.setOnClickListener {
            addComment(et_comment.text.toString())
        }
    }

    private fun enqueue(creneaux: Observable<List<Comment>>){
        creneaux
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showCreneaux(it ?: emptyList())
            }
    }

    private fun showCreneaux(comments: List<Comment>) {
        Log.i("comm", comments.toString())
        setContentView(R.layout.activity_commentaires)
        actorsRecyclerView = findViewById(R.id.view_commentaires)
        initRecyclerView()
        this.comments = comments.toMutableList()
        commentairesAdapter.comments = comments
    }

    private fun addComment(comment: String) {
        val newC = Comment(userEmail, filmId, comment)
        ApiManager.getInstance().filmService.commentMovie(filmId, userEmail, newC).enqueue(object: Callback<String> {

            override fun onResponse(call: Call<String>, response: Response<String>) {

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.i("error", t.message.toString())
            }
        })
        this.comments.add(newC)
        commentairesAdapter.comments = this.comments
    }



}