package com.example.filmschecker.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esgi.filmchecker.model.Comment
import com.example.filmschecker.R

class CommentaireAdapter : RecyclerView.Adapter<CommentaireViewHolder>() {

    var comment: List<Comment> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentaireViewHolder {
        return CommentaireViewHolder(parent.inflate(R.layout.layout_commentaires))
    }

    override fun getItemCount() = comment.size

    override fun onBindViewHolder(holder: CommentaireViewHolder, position: Int) {
        val weapon = comment[position]
        holder.bindData(weapon)
    }
}

class CommentaireViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var tvCommentaireNom: TextView = itemView.findViewById(R.id.commentaire_nom)
    private var tvCommentaireTexte: TextView = itemView.findViewById(R.id.commentaire_texte)

    fun bindData(comment: Comment) {
        tvCommentaireNom.text = comment.userEmail
        tvCommentaireTexte.text = comment.comment
    }
}