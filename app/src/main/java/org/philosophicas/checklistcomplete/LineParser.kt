package org.philosophicas.checklistcomplete

import android.util.Log

class LineParser(


    /** Call when reach a new aircraft Id tag */
    var onNewCompleteCheckList: (parser: LineParser) -> Unit,

    /** Call when reach a new checklist */
    var onNewCheckList: (parser: LineParser) -> Unit,

    /** Call when reach a new step */
    var onNewStep: (parser: LineParser) -> Unit,

    /** Call when reach a new tabuled step */
    var onNewTabuleStep: (parser: LineParser) -> Unit
) {


    private val stepDelimiter: String = "..."

    var mode: Mode = Mode.Unknown
    var modePrevious = Mode.Unknown

    var text: String? = null

    val ICAO: String?
        get() {
            if (mode == Mode.Identifier) {
                return text?.split("/")?.first()
            }
            return null
        }

    val model: String?
        get() {
            if (mode == Mode.Identifier) {
                return text?.split("/")?.get(1)
            }
            return null
        }


    val instruction: String?
        get() {
            if (mode == Mode.Normal || mode == Mode.Emergency || mode == Mode.Abnormal) {
                val t = text?.split(stepDelimiter)
                if (t?.size!! < 2) return text!!
                return t[0]
            }
            return null
        }

    val collation: String?
        get() {
            if (mode == Mode.Normal || mode == Mode.Emergency || mode == Mode.Abnormal) {
                val t = text?.split(stepDelimiter)
                if (t?.size!! < 2) return null
                return t[1]
            }
            return null
        }

    val checklistName: String?
        get() {
            if (mode == Mode.Normal || mode == Mode.Emergency || mode == Mode.Abnormal) {
                return text
            }
            return null
        }


    private val reservedCodes: Map<String, String> = mapOf(
        "{" to "}",
        "[" to "]",
        "#" to "#",
        "<" to ">"
    )

    fun parse(line: String) {

        if (line.trim() != "") {

            //Obtenemos el primer caracter
            val first = line.first().toString()
            Log.d("aldox", line)

            //Si el primer caracter es de los reservados tomamos el texto interno para establecer el
            // modo y recuperamos el texto de la etiqueta

            if (reservedCodes.containsKey(first)) {
                val closeTag = reservedCodes[first]!!
                val lastPosition = line.indexOf(closeTag, 1)

                if (lastPosition < 0) {
                    text = line.substring(1)
                } else {
                    text = line.substring(1, lastPosition)
                }

                when (first) {

                    "#" -> {
                        modePrevious = mode
                        mode = Mode.Identifier
                        onNewCompleteCheckList(this)
                    }

                    "{" -> {
                        modePrevious = mode
                        mode = Mode.Emergency
                        onNewCheckList(this)
                    }

                    "[" -> {
                        modePrevious = mode
                        mode = Mode.Normal
                        onNewCheckList(this)
                    }

                    "<" -> {
                        modePrevious = mode
                        mode = Mode.Abnormal
                        onNewCheckList(this)
                    }

                    else -> {
                    }
                }
            } else {
                text = line
                //No se corresponde con los caracteres especiales. Entonces significa que estamos en
                //Un paso. Ahora bien, los pasos pueden ser tabulados.
                when (first) {
                    "-" -> {
                        onNewTabuleStep(this)
                    }
                    else -> {
                        onNewStep(this)
                    }
                }
            }
        }
    }
}