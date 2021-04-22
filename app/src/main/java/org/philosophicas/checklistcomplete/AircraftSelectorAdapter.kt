package org.philosophicas.checklistcomplete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AircraftSelectorAdapter(
    var aircrafts: ArrayList<String>,
    var onAircraftSelected: (information: String) -> Unit
) :
    RecyclerView.Adapter<AircraftSelectorAdapter.ViewHolder>() {

    private var filteredAircrafts = ArrayList<String>()


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTv = itemView.findViewById<TextView>(R.id.selector_name_tv)
        val cardView = itemView.findViewById<CardView>(R.id.selector_cv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view =
            LayoutInflater.from(parent.context).inflate(
                R.layout.selector_layout,
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = filteredAircrafts[position]

        holder.nameTv.setText(info)
        holder.cardView.setOnClickListener {
            onAircraftSelected(info)
        }

    }

    override fun getItemCount(): Int = filteredAircrafts.size

    fun filter(text: String? = null) {
        filteredAircrafts.clear()
        if (text != null) {
            filteredAircrafts.addAll(aircrafts.filter { it.indexOf(text, 0, true) > -1 })
        } else {
            filteredAircrafts.addAll(aircrafts)
        }

        filteredAircrafts.sort()

        notifyDataSetChanged()
    }



}