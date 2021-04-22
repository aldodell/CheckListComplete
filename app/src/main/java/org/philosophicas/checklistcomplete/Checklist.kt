package org.philosophicas.checklistcomplete

/**
 * Wrap steps and mode. It's a simple section of a whole checklist.
 */
class Checklist(var name: String = "") {
    var steps = ArrayList<Step>()
    var mode : Mode = Mode.Unknown
}