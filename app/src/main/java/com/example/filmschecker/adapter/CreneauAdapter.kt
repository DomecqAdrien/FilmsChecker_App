package com.example.filmschecker.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.esgi.filmchecker.model.Creneau
import com.example.filmschecker.R

class ReservationAdapter : RecyclerView.Adapter<CreneauViewHolder>() {
    var onItemClick: ((Creneau) -> Unit)? = null
    var crenaux: List<Creneau> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreneauViewHolder {
        return CreneauViewHolder(parent.inflate(R.layout.layout_seances))
    }

    override fun getItemCount() = crenaux.size

    override fun onBindViewHolder(holder: CreneauViewHolder, position: Int) {
        val creneau = crenaux[position]
        holder.bindData(creneau)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(creneau)
        }
    }
}

class CreneauViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var tvJour: TextView = itemView.findViewById(R.id.reservation_jour)
    private var tvHeureDebut: TextView = itemView.findViewById(R.id.reservation_heure_debut)
    private var tvSalle: TextView = itemView.findViewById(R.id.reservation_salle)

    fun bindData(creneau: Creneau) {
        tvJour.text = creneau.dateJour
        tvHeureDebut.text = creneau.heureDebut
        tvSalle.text = "salle ${creneau.salleId}"

    }
}