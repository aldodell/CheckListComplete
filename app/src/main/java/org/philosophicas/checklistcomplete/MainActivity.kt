package org.philosophicas.checklistcomplete

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


val aircraftsPath = "aircrafts/"

class MainActivity : AppCompatActivity() {

    private lateinit var selectAircraftBtn: Button
    private lateinit var goBtn: Button
    private lateinit var selectedAircraftTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Tomamos un avión por defecto
        Preferences(this).defaultAircraft = "C172/N"

        //Obtenemos las vistas
        selectAircraftBtn = findViewById(R.id.mainSelectAircraftBtn)
        goBtn = findViewById(R.id.mainGoBtn)
        selectedAircraftTv = findViewById(R.id.mainAircraftTypeSelectedTv)


        //Configuramos la búsqueda
        selectAircraftBtn.setOnClickListener {
            startActivity(Intent(this, AircraftSelector::class.java))
        }

        //Abrimos el visor
        goBtn.setOnClickListener {
            startActivity(Intent(this, ChecklistViewer::class.java))
        }


    }


    override fun onResume() {
        super.onResume()

        //Configuramos el indicador de avion seleccionado
        Preferences(this).defaultAircraft?.let {
            selectedAircraftTv.text = it
        }
    }

}


/*

Quitar espacios iniciales
^[a-z]+\.\s+

Cambiar las enumeracion de letras...
^[a-z]+\.\s+

Quitar los núemeros iniciales
^\d+\.\s+

Quitar puntos suspensivos
\.{2,}





 */