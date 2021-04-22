package org.philosophicas.checklistcomplete

/** Whole checklist */
class ChecklistComplete {
    var icao = "XXXX"
    var model = ""
    var information = ""
    val checklists = ArrayList<Checklist>()

    val identifier: String
        get() = "$icao $model $information".trim()

    fun getCheckListBy(mode: Mode, position: Int): Checklist? {
        val s = checklists.filter { it.mode == mode }
        if (position < s.size) {
            return s[position]
        } else {
            return null
        }
    }

    fun getCheckListSizeBy(mode: Mode) : Int {
        return checklists.filter { it.mode == mode }.size
    }

}