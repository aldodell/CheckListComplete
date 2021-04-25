package org.philosophicas.checklistcomplete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StepsAdapter(
    checklistComplete: ChecklistComplete,
    mode: Mode,
    position: Int,
    var colorNormal: Int,
    var colorDone: Int
) :
    RecyclerView.Adapter<StepsAdapter.ViewHolder>() {

    var checklist = checklistComplete.getCheckListBy(mode, position)
    private var indexStep = 0


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var instruction: TextView = itemView.findViewById(R.id.step_instruction)
        var collation: TextView = itemView.findViewById(R.id.step_collation)
        var points: TextView = itemView.findViewById(R.id.step_points)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.step_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val s = checklist!!.steps[position]
        val inst = if (s.isTabuled) "    ${s.instruction.substring(1).trim()}" else s.instruction.trim()


        holder.instruction.text = inst

        if (s.collation == "") {
            holder.points.visibility = View.GONE
            holder.collation.visibility = View.GONE
        } else {
            holder.points.visibility = View.VISIBLE
            holder.collation.visibility = View.VISIBLE
            holder.collation.setText(s.collation.trim())
        }

        if (s.isDone) {
            holder.instruction.setTextColor(colorDone)
            holder.points.setTextColor(colorDone)
            holder.collation.setTextColor(colorDone)


        } else {
            holder.instruction.setTextColor(colorNormal)
            holder.points.setTextColor(colorNormal)
            holder.collation.setTextColor(colorNormal)

        }

    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return checklist!!.steps.size
    }


    fun markStepAsDone(): Int {
        val n = indexStep
        checklist!!.steps[indexStep].isDone = true
        notifyDataSetChanged()

        indexStep++
        if (indexStep == checklist!!.steps.size) {
            indexStep = 0
        }
        return n
    }
}