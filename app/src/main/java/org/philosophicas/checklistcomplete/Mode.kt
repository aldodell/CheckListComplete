package org.philosophicas.checklistcomplete

enum class Mode(i: Int) {
    Unknown(0),
    Normal(1),
    Info(2),
    Abnormal(4),
    Emergency(8),
    Identifier(256)

}