package org.philosophicas.checklistcomplete

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.util.Log
import android.view.*
import android.widget.ImageView

class PanicButtonService : Service() {
    lateinit var panicButton: View
    lateinit var winMan: WindowManager
    lateinit var imagenView : ImageView


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        panicButton = LayoutInflater.from(this).inflate(R.layout.panic_button_layout, null)
        imagenView = panicButton.findViewById(R.id.panicButtonIv)

        val params = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        } else {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)
        }

        params.gravity = Gravity.TOP + Gravity.START
        params.x = 100
        params.y = 100

        winMan = getSystemService(WINDOW_SERVICE) as WindowManager
        winMan.addView(panicButton, params)

        Log.d("aldox", "servicio creado")

        /*
        imagenView.setOnTouchListener { iv, event ->
            event?.let { e ->

                when (e.action) {

                    MotionEvent.ACTION_DOWN -> {

                    }


                }


            }

            false
        }
        */
    }

    override fun onDestroy() {
        super.onDestroy()
        winMan.removeView(panicButton)
    }

}