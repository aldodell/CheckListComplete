package org.philosophicas.checklistcomplete

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ChecklistViewer : AppCompatActivity() {

    private lateinit var emergencyBtn: Button
    private lateinit var normalBtn: Button
    private lateinit var abnormalBtn: Button
    private lateinit var infoBtn: Button

    private lateinit var nextBtn: Button
    private lateinit var backBtn: Button
    private lateinit var doneBtn: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var aircraftTv: TextView
    private lateinit var checkListName: TextView
    private var checklistComplete: ChecklistComplete? = null
    private var checklistIndex = 0
    private var currentMode = Mode.Unknown

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist_viewer)
        emergencyBtn = findViewById(R.id.viewerBtnEmergency)
        normalBtn = findViewById(R.id.viewerBtnNormal)
        abnormalBtn = findViewById(R.id.viewerBtnAbnormal)
        infoBtn = findViewById(R.id.viewerInfoBtn)

        nextBtn = findViewById(R.id.viewerBtnNext)
        backBtn = findViewById(R.id.viewerBtnBack)
        doneBtn = findViewById(R.id.viewerBtnDone)
        recyclerView = findViewById(R.id.viewerRV)
        aircraftTv = findViewById(R.id.viewerAircraftTv)
        checkListName = findViewById(R.id.viewerChecklistName)
        emergencyBtn = findViewById(R.id.viewerBtnEmergency)


        //Configuramos el RV
        recyclerView.layoutManager = LinearLayoutManager(this)


        //Cargamos y procesamos las listas de chequeo
        Thread {
            val proc = ChecklistProcessor()
            val aircraftIdentifier = Preferences(this).defaultAircraft
            val file = aircraftsPath + aircraftIdentifier!!.substring(0, 4)
            checklistComplete = proc.parse(assets.open(file)).getByIdentifier(aircraftIdentifier)
            runOnUiThread {
                aircraftTv.text = checklistComplete!!.identifier

                //Cagamos la inicial
                loadSections(Mode.Normal)
                checkListName.setText(R.string.normal)
            }
        }.start()


        //Mostramos las secciones de emergencias
        emergencyBtn.setOnClickListener {
            loadSections(Mode.Emergency)

        }

        //Mostramos las secciones normales
        normalBtn.setOnClickListener {
            loadSections(Mode.Normal)
        }

        //Mostramos las secciones anormales
        abnormalBtn.setOnClickListener {
            loadSections(Mode.Abnormal)
        }

        //Mostramos las secciones anormales
        infoBtn.setOnClickListener {
            loadSections(Mode.Info)
        }


        //Configuramos el bot??n DONE:
        doneBtn.setOnClickListener {

            (recyclerView.adapter as? StepsAdapter)?.markStepAsDone()?.let { stepN ->
                (recyclerView.layoutManager as? LinearLayoutManager)?.let {
                    if (stepN >= it.findLastVisibleItemPosition()) {
                        it.scrollToPosition(it.findLastVisibleItemPosition() + 1)
                    }
                }

            }


        }

        //Configuramos el bot??n next:
        nextBtn.setOnClickListener {
            checklistIndex++
            loadChecklist(checklistIndex)


        }

        //Configuramso el botn back
        backBtn.setOnClickListener {
            checklistIndex--
            loadChecklist(checklistIndex)

        }

        //https://dev.to/aldok/how-to-scroll-recyclerview-to-a-certain-position-5ck4


    }

    fun loadSections(mode: Mode) {
        checklistComplete?.let {
            recyclerView.adapter =
                SectionsAdapter(checklistComplete!!, mode) { p ->
                    loadChecklist(p)
                }
            recyclerView.adapter?.notifyDataSetChanged()
            currentMode = mode
        }

        when (mode) {
            Mode.Normal -> {
                checkListName.setText(R.string.normal)
                checkListName.setBackgroundColor(getColor(R.color.normal))
            }

            Mode.Emergency -> {
                checkListName.setText(R.string.emergency)
                checkListName.setBackgroundColor(getColor(R.color.emergency))
            }


            Mode.Abnormal -> {
                checkListName.setText(R.string.abnormal)
                checkListName.setBackgroundColor(getColor(R.color.abnormal))
            }

            Mode.Info -> {
                checkListName.setText(R.string.info)
                checkListName.setBackgroundColor(getColor(R.color.info))
            }

            else -> {
            }

        }
    }

    fun loadChecklist(position: Int) {

        val max = checklistComplete!!.getCheckListSizeBy(currentMode)
        var pos = position

        if (pos >= max) {
            pos = 0
        }

        if (position < 0) {
            pos = max - 1
        }

        //Homologamos el ??ndece de esta interfaz con
        //Cualquier otro cambio hecho desde otras partes
        //M??s profundas de la interfaz
        checklistIndex = pos


        recyclerView.adapter = StepsAdapter(
            checklistComplete!!,
            currentMode,
            pos,
            getColor(R.color.white),
            getColor(R.color.green)

        )
        recyclerView.adapter?.notifyDataSetChanged()
        checklistComplete!!.getCheckListBy(currentMode, pos)?.let {
            checkListName.setText(it.name)
        }

    }

}