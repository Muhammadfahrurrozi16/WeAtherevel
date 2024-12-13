package com.example.weatheravel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RekomendasiAdapter(
    private val destinations: List<String>,
    private val onItemClick: (String) -> Unit // Listener untuk item klik
) : RecyclerView.Adapter<RekomendasiAdapter.RekomendasiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RekomendasiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rekomendasi_card, parent, false)
        return RekomendasiViewHolder(view)
    }

    override fun onBindViewHolder(holder: RekomendasiViewHolder, position: Int) {
        val destination = destinations[position]
        holder.bind(destination)
    }

    override fun getItemCount(): Int = destinations.size
    fun updateDestinations(destinationNames: List<String>) {
        TODO("Not yet implemented")
    }

    inner class RekomendasiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvItemTitle: TextView = itemView.findViewById(R.id.tv_item_title)

        fun bind(destination: String) {
            tvItemTitle.text = destination
            itemView.setOnClickListener {
                onItemClick(destination) // Panggil listener klik item
            }
        }
    }
}
