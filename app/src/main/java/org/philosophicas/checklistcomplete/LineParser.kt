package org.philosophicas.checklistcomplete

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

    enum class Mode(i: Int) {
        Unknown(0),
        Identifier(1),
        Normal(4),
        Emergency(2),
    }

    var mode: Mode = Mode.Unknown
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
            if (mode == Mode.Normal || mode == Mode.Emergency) {
                val t = text?.split(":")
                if (t?.size!! < 2) return null
                return t[0]
            }
            return null
        }

    val collation: String?
        get() {
            if (mode == Mode.Normal || mode == Mode.Emergency) {
                val t = text?.split(":")
                if (t?.size!! < 2) return null
                return t[1]
            }
            return null
        }

    val checklistName: String?
        get() {
            if (mode == Mode.Normal || mode == Mode.Emergency) {
                return text?.split(":")?.get(0)
            }
            return null
        }


    private val reservedCodes: Map<String, String> = mapOf(
            "{" to "}",
            "[" to "]",
            "#" to "#"
    )

    fun parse(line: String) {

        //Obtenemos el primer caracter
        val first = line.first().toString()

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
                    mode = Mode.Identifier
                    onNewCompleteCheckList(this)
                }

                "{" -> {
                    mode = Mode.Emergency
                    onNewCheckList(this)
                }

                "[" -> {
                    mode = Mode.Normal
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