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

    //private val CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Tomamos un avión por defecto
        Preferences(this).apply {
            if (defaultAircraft == null) defaultAircraft = "C172/N"
        }

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


/*
        //Lanzamos el servicio
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) || Settings.canDrawOverlays(this)) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Settings.ACTION_MANAGE_OVERLAY_PERMISSION),
                    CODE_DRAW_OVER_OTHER_APP_PERMISSION
                )
            }
        } else {
            startPanicButtonService()
        }
        */


    }


    override fun onResume() {
        super.onResume()

        //Configuramos el indicador de avion seleccionado
        Preferences(this).defaultAircraft?.let {
            selectedAircraftTv.text = it
        }
    }

/*
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startPanicButtonService()
                } else {
                    Toast.makeText(
                        this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT

                    ).show()
                }
            }
        }


    }

    fun startPanicButtonService() {
        startService(Intent(this, PanicButtonService::class.java))

    }
    */


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