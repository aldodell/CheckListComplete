package org.philosophicas.checklistcomplete

class ChecklistCompleteCollection : ArrayList<ChecklistComplete>() {
    fun getByIdentifier (identifier : String) : ChecklistComplete? {
        return this.filter { it.identifier == identifier }.firstOrNull()
    }
}