package org.philosophicas.checklistcomplete

/** Each single instruction of a checklist */
class Step {
    var instruction: String = ""
    var collation: String = ""
    var isTabuled: Boolean = false

    //Usado por el visor, para decir que ya fue visto este paso
    var isDone:Boolean = false

}