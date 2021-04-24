package org.philosophicas.checklistcomplete

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AircraftSelector : AppCompatActivity() {
    private val aircraftsPath = "aircrafts"
    val aircrafts = ArrayList<String>()
    lateinit var adapter: AircraftSelectorAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var selectedAircraftTv: TextView
    lateinit var searchView: SearchView
    lateinit var selectAircraftBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aircraft_selector)

        //Obtenemos las vistas
        recyclerView = findViewById(R.id.selector_rv)
        selectedAircraftTv = findViewById(R.id.selector_aircraft_selected_tv)
        searchView = findViewById(R.id.selector_sv)
        selectAircraftBtn = findViewById(R.id.selector_btn)


        //Contruimos el adaptador
        adapter = AircraftSelectorAdapter(aircrafts) {
            selectedAircraftTv.setText(it)
        }

        //Confuguramos el recycler
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        /*
        //Obtenemos el procesador
        val proc = CheckListProcessor(this)

        //Mandamos procesar los aviones en un hilo
        Thread {
            //Obtenemos las carpetas
            assets.list(aircraftsPath)?.let {
                it.forEach {
                    val cls = proc.parse("$aircraftsPath/$it")
                    cls.forEach { cl -> aircrafts.add(cl.identifier) }
                }
            }
            runOnUiThread {
                //Al terminar notificamos al adaptador que hay datos
                adapter.filter(null)
            }
        }.start()
*/

        /* Obtenemos los nombres de los aviones dentro de todos los archivos */
        val proc = ChecklistProcessor()
        Thread {
            assets.list(aircraftsPath)?.let {
                it.forEach { file ->
                    val stream = assets.open("$aircraftsPath/$file")
                    proc.parseIdentifier(stream).forEach { aircraft ->
                        aircrafts.add(aircraft)
                    }

                }
            }
            runOnUiThread {
                //Al terminar notificamos al adaptador que hay datos
                adapter.filter(null)
            }

        }.start()


        //Configuramos el buscador
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                adapter.filter(p0)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0?.length == 0) {
                    adapter.filter(null)
                } else {
                    adapter.filter(p0)
                }
                return true
            }

        })

        //Configuramos el botón
        selectAircraftBtn.setOnClickListener {
            val p = Preferences(this)
            p.defaultAircraft = selectedAircraftTv.text.toString()
            finish()
        }

    }


}