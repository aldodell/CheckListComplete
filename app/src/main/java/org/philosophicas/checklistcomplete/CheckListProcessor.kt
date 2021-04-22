package org.philosophicas.checklistcomplete

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

enum class Mode(i: Int) {
    Unknown(0),
    Identifier(256),
    Normal(1),
    Emergency(4),
    Abnormal(2)
}

class CheckListProcessor(val context: Context) {


    fun parse(file: String): ChecklistCompleteCollection {

        //Objetos
        val checklistsComplete = ChecklistCompleteCollection()
        var checklistComplete: ChecklistComplete? = null
        var checklist: Checklist? = null

        // Obtenemos una referencia a los assts
        val assets = context.assets

        //Abrimos el archivo
        val stream = assets.open(file)
        val text = BufferedReader(InputStreamReader(stream))


        //Configuramos el parser
        val parser = LineParser(

            // Nuevo checklistcomplete
            { parser ->
                //Si existe una lista anterior la agregamos si no saltamos
                checklistComplete?.let {
                    checklistsComplete.add(checklistComplete!!)
                }
                //Creamos una lista nueva
                checklistComplete = ChecklistComplete()
                checklistComplete!!.apply {
                    icao = parser.ICAO!!
                    model = parser.model!!
                }

            },
            { parser ->
                //Si existe una lista anterior la incluimos
                checklist?.let {
                    if(parser.modePrevious == Mode.Unknown) {
                        it.mode = parser.mode
                    } else {
                        it.mode = parser.modePrevious
                    }
                    checklistComplete!!.checklists.add(checklist!!)
                }
                checklist = Checklist(parser.checklistName!!)
            },
            { parser ->
                val step = Step()
                step.apply {
                    instruction = parser.instruction!!
                    collation = parser.collation ?: ""
                    isTabuled = false
                }
                checklist!!.steps.add(step)
            },
            { parser ->
                val step = Step()
                step.apply {
                    instruction = parser.instruction!!
                    collation = parser.collation ?: ""
                    isTabuled = true
                }
                checklist!!.steps.add(step)
            }
        )

        //Procesamos línea a línea.
        //Funciona como una máquina de estados.
        text.forEachLine { line ->
            parser.parse(line)
        }

        //Agregamos la última checklistcomplete conseguida
        checklistComplete?.let {
            checklistsComplete.add(checklistComplete!!)
        }

        //Devolvemos las listas
        return checklistsComplete
    }

}