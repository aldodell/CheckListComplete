package org.philosophicas.checklistcomplete

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader


class ChecklistProcessor2 {


    private val tags: Map<String, String> = mapOf(
        "#" to "#",
        "[" to "]",
        "{" to "}",
        "<" to ">",
    )

    /**
     * Return only identifier from this input stream
     */
    fun parseIdentifier(stream: InputStream): List<String> {

        var text = ""

        var result = ArrayList<String>()

        //Obtenemos todo el contenido del archivo
        val code = BufferedReader(InputStreamReader(stream))

        //Separamos por línea
        code.forEachLine { pureLine ->

            //Borramos espacios en blanco
            val line = pureLine.trim()

            //Solo procesamos si la línea no está vacía
            if (line.isNotEmpty()) {

                val initialTag = line.first().toString()
                if (initialTag == "#") {
                    val finalTag = line.indexOf("#", 1)

                    //Evaluamos si existe un tag de cierre que se cooresponda con el de apertura
                    if (finalTag > -1) {
                        text = line.substring(1, finalTag)
                    } else {
                        text = line.substring(1)
                    }
                    result.add(text)

                }
            }
        }
        return result.toList()
    }


    /** Parse a InputStream a return a collecction of ChecklistComplete */
    fun parse(stream: InputStream): ChecklistCompleteCollection {

        var text = ""

        val checklistCompleteCollection = ChecklistCompleteCollection()

        //Obtenemos todo el contenido del archivo
        val code = BufferedReader(InputStreamReader(stream))

        //Separamos por línea
        code.forEachLine { pureLine ->

            //Borramos espacios en blanco
            val line = pureLine.trim()

            //Solo procesamos si la línea no está vacía
            if (line.isNotEmpty()) {

                //Tomamos el primer caracter
                val initialTag = line.first().toString()

                //Evaluams si ese caracter es un tag
                if (tags.containsKey(initialTag)) {

                    //Buscamos el tag final
                    val finalTagPos = line.indexOf(tags[initialTag]!!, 1)

                    //Evaluamos si existe un tag de cierre que se cooresponda con el de apertura
                    if (finalTagPos > -1) {
                        text = line.substring(1, finalTagPos)
                    } else {
                        text = line.substring(1)
                    }
                } else {
                    text = line
                }


                //Evaluamos ahora cada el caso de cada tag
                when (initialTag) {

                    //Identificación del avión
                    "#" -> {
                        val clc = ChecklistComplete()
                        val tokens = text.split("/")

                        tokens.elementAtOrNull(0)?.let {
                            clc.icao = it
                        }

                        tokens.elementAtOrNull(1)?.let {
                            clc.model = it
                        }

                        tokens.elementAtOrNull(2)?.let {
                            clc.information = it
                        }

                        checklistCompleteCollection.add(clc)
                    }


                    //Sección normal
                    "[" -> {
                        val cl = Checklist()
                        cl.name = text
                        cl.mode = Mode.Normal
                        checklistCompleteCollection.last().checklists.add(cl)
                    }

                    //Sección anormal
                    "<" -> {
                        val cl = Checklist()
                        cl.name = text
                        cl.mode = Mode.Abnormal
                        checklistCompleteCollection.last().checklists.add(cl)
                    }

                    //Sección emergencia
                    "{" -> {
                        val cl = Checklist()
                        cl.name = text
                        cl.mode = Mode.Emergency
                        checklistCompleteCollection.last().checklists.add(cl)
                    }

                    //Agregamos los pasos tabulados
                    "-" -> {
                        val s = Step()
                        s.isTabuled = true
                        val tokens = text.split("...")
                        tokens.elementAtOrNull(0)?.let {
                            s.instruction = it
                        }
                        tokens.elementAtOrNull(1)?.let {
                            s.collation = it
                        }
                        checklistCompleteCollection.last().checklists.last().steps.add(s)
                    }

                    //El resto se supone que son pasos no tabulados
                    else -> {
                        val s = Step()
                        s.isTabuled = false
                        val tokens = text.split("...")
                        tokens.elementAtOrNull(0)?.let {
                            s.instruction = it
                        }
                        tokens.elementAtOrNull(1)?.let {
                            s.collation = it
                        }
                        checklistCompleteCollection.last().checklists.last().steps.add(s)
                    }
                }
            }
        }
        return checklistCompleteCollection
    }


}