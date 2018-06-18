package com.deniskorotchenko.mapsp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.os.Handler
import android.widget.TextView


import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

            /*val fTransaction = supportFragmentManager.beginTransaction()
            fTransaction.replace(R.id.fragmentQuestion, questionTextFragment)
            fTransaction.addToBackStack(null)
            fTransaction.commit()*/
            val questionFragmentView = layoutInflater.inflate(R.layout.fragment_question_text, null)
            val questionWindow = PopupWindow(questionFragmentView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
                    )
            questionWindow.showAtLocation(fragmentQuestion, Gravity.CENTER, 0, 0)
        }




        imhere.setOnClickListener {
            val intent = Intent(this, NotRight::class.java)
            startActivity(intent)
        }
        imhere2.setOnClickListener {
            val intent = Intent(this, Right::class.java)
            startActivity(intent)
        }
      
      runTimer()

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        btnStart.setOnClickListener { onClickStart() }
        btnReset.setOnClickListener { onClickReset() }
        btnStop.setOnClickListener { onClickStop() }
    }


    fun onClickStop(){
        running = false
    }


    fun onClickStart(){
        running = true
    }


    fun onClickReset(){
        running = false
        sec = 0
    }


    fun runTimer() {
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
