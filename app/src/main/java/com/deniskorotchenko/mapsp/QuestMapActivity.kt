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
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory


import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_quest_map.*

class QuestMapActivity : AppCompatActivity(), OnMapReadyCallback {


    private lateinit var mMap: GoogleMap
    private var sec: Int = 0 //Для секундомера
    private var running: Boolean = false // Для секундомера


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quest_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.questMap) as SupportMapFragment
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

        btnStart.setOnClickListener { onClickStart() } // настраиваю кнопки таймера (здесь и ниже)
        btnReset.setOnClickListener { onClickReset() }
        btnStop.setOnClickListener { onClickStop() }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }

        val center = LatLng(59.9367364, 30.3096995)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center , 15F))
    }


    fun onClickStop(){ //функция секундомера
        running = false
    }



    fun onClickStart(){ // ещё функция секундомера
        running = true
    }


 
    fun onClickReset(){ // и ещё функция секундомера
        running = false
        sec = 0
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
