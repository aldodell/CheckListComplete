package org.philosophicas.checklistcomplete

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

class CheckListProcessor(val context: Context) {


    fun parse(file: String): List<ChecklistComplete> {

        //Objetos
        val completeChecklists = ArrayList<ChecklistComplete>()
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
                    completeChecklists.add(checklistComplete!!)
                }
                //Creamos una lista nueva
                checklistComplete = ChecklistComplete()
                checklistComplete!!.apply {
                    icao = parser.ICAO!!
                }

            },
            { parser ->
                //Si existe una lista anterior la incluimos
                checklist?.let {
                    when (parser.mode) {
                        LineParser.Mode.Normal -> checklistComplete!!.normalChecklists.add(checklist!!)
                        LineParser.Mode.Emergency -> checklistComplete!!.emergencyCheckLists.add(
                            checklist!!
                        )
                        else -> {
                        }
                    }
                }
                checklist = Checklist(parser.checklistName!!)
            },
            { parser ->
                val step = Step()
                step.apply {
                    instruction = parser.instruction!!
                    collation = parser.collation!!
                    isTabuled = false
                }
                checklist!!.steps.add(step)
            },
            { parser ->
                val step = Step()
                step.apply {
                    instruction = parser.instruction!!
                    collation = parser.collation!!
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


        //Devolvemos las listas
        return completeChecklists.toList()
    }

}