package org.philosophicas.checklistcomplete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class SectionsAdapter(checklistComplete: ChecklistComplete,
                      var mode: Mode,
                      var onButtonSectionPressed : (Int)->Unit
                      ) :
    RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {

    var selectedSections =
        checklistComplete.checklists.filter { it.mode == mode }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var button: Button = itemView.findViewById(R.id.stepSectionBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row =
            LayoutInflater.from(parent.context).inflate(R.layout.section_layout, parent, false)
        return ViewHolder(row)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.button.setText(selectedSections[position].name)
        holder.button.setOnClickListener {
            onButtonSectionPressed(position)
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int = selectedSections.size

}