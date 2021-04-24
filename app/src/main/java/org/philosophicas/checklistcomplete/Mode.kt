package org.philosophicas.checklistcomplete

enum class Mode(i: Int) {
    Unknown(0),
    Identifier(256),
    Normal(1),
    Emergency(4),
    Abnormal(2)
}