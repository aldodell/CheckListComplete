package org.philosophicas.checklistcomplete

import android.content.Context

class Preferences(context: Context) {
    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var defaultAircraft: String?
        get() = prefs.getString("defaultAircraft", null)
        set(value) {
            prefs.edit().putString("defaultAircraft", value).apply()
        }
}