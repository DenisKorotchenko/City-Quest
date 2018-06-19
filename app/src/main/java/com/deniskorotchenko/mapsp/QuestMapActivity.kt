package com.deniskorotchenko.mapsp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import android.location.Location


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_quest_map.*
import android.widget.RelativeLayout


class QuestMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: View
    private var sec: Int = 0 //Для секундомера
    private var running: Boolean = true // Для секундомера


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.questMap) as SupportMapFragment
        mapView = mapFragment.getView()!!
        mapFragment.getMapAsync(this)

        buttonToQuestion.setOnClickListener {
            val questionFragmentView = layoutInflater.inflate(R.layout.fragment_question_text, null)
            val questionWindow = PopupWindow(questionFragmentView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
            )
            questionWindow.showAtLocation(questionFragmentView, Gravity.CENTER, 0, 0)
        }

        tip.setOnClickListener {
            val questionFragmentView = layoutInflater.inflate(R.layout.fragment_tip, null)
            val questionWindow = PopupWindow(questionFragmentView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
            )
            questionWindow.showAtLocation(questionFragmentView, Gravity.CENTER, 0, 0)
        }

       imhere.setOnClickListener {
           val NotRightFragmentView = layoutInflater.inflate(R.layout.fragment_fragmentnotright, null)
          val questionWindow = PopupWindow(NotRightFragmentView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
            )
            questionWindow.showAtLocation(NotRightFragmentView, Gravity.CENTER, 0, 0)
        }

       imhere2.setOnClickListener {
            val NotRightFragmentView = layoutInflater.inflate(R.layout.fragment_fragmentright, null)
            val questionWindow = PopupWindow(NotRightFragmentView,
                   LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
            )
           questionWindow.showAtLocation(NotRightFragmentView, Gravity.CENTER, 0, 0)

        }
        runTimer()

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }

        val center = LatLng(59.9367364, 30.3096995)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center , 15F))

        //Эта штука переносит кнопку "Моё местоположение" в правый нижний угол
        val locationButton= (mapView.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
        val rlp=locationButton.layoutParams as (RelativeLayout.LayoutParams)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP,0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE)
        rlp.setMargins(0,0,30,30)
    }


    fun onClickStop(){ //функция секундомера
        running = false
    }

    fun onClickReset(){ // ещё функция секундомера

        running = false
        sec = 0
    }

    private fun formatLocation(location: Location?): String {
        return if (location == null) ""
        else String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3\$tF %3\$tT",
                location.latitude, location.longitude)
    }


    fun runTimer() { // сам секундомер
        val timerView: TextView = findViewById(R.id.textView)
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                val hours = sec / 3600
                val minutes = sec % 3600 / 60
                val seconds = sec % 60
                val time = String.format("%d:%02d:%02d", hours, minutes, seconds)
                timerView.text = time
                if (running) {
                    sec++
                }
                handler.postDelayed(this, 1000)
            }
        })
    }


}
