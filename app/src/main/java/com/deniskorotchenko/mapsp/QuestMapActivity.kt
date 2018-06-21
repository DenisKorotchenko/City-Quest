package com.deniskorotchenko.mapsp

import android.Manifest
import android.app.Fragment
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import android.location.Location


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_quest_map.*
import kotlinx.android.synthetic.main.activity_quest_map.view.*
import android.widget.RelativeLayout
import java.util.*


class QuestMapActivity :
        AppCompatActivity(),
        OnMapReadyCallback,
        AnswerFragment.AnswerFragmentListener,
        FalseAnswerFragment.FalseAnswerListener{
    override fun onNext() {
        hideTip()
        showQuestion()
    }
    override fun onTip() {
        unhideTip()
    }
    override fun onBackFromAnswer() {

    }

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: View
    private var sec: Long = 0 //Для секундомера
    private var singleton = Singleton.instance
    var frgTrue : AnswerFragment = AnswerFragment.newInstance()
    private var loc: LatLng = LatLng(0.0, 0.0)//переменнадя с моими координатами


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quest_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.questMap) as SupportMapFragment
        mapView = mapFragment.view!!
        mapFragment.getMapAsync(this)



        buttonToQuestion.setOnClickListener {
            showQuestion()
        }

        tip.setOnClickListener {
            showTip()
        }

        imhere2.setOnClickListener {
            showFragment()
        }


        runTimer()
        init()
    }

    private fun init() {
        singleton.nowQuestion = 1

        if (singleton.startTime == 0.toLong()) {
            singleton.startTime = Calendar.getInstance().timeInMillis
        }
    }

    private fun hideTip(){
        tip.visibility = View.GONE
    }

    private fun unhideTip(){
        tip.visibility = View.VISIBLE
        showTip()
    }

    private fun showQuestion(){
        val questionFragmentView = layoutInflater.inflate(R.layout.fragment_question_text, null)
        val questionWindow = PopupWindow(questionFragmentView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
        )
        val questDatabase = QuestDataBase(this)
        questionFragmentView.textView.text = questDatabase.getQuestion(singleton.nowQuestion)
        questionWindow.showAtLocation(questionFragmentView, Gravity.CENTER, 0, 0)
    }

    private fun showFragment(){
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (QuestDataBase(this).checkAnswer(loc))
            fragmentTransaction.replace(R.id.container, frgTrue as Fragment)
        else {
            val frgFalse : FalseAnswerFragment = if (this.tip.visibility == View.GONE)
                FalseAnswerFragment.newInstance(true)
            else{
                FalseAnswerFragment.newInstance(false)
            }
            fragmentTransaction.replace(R.id.container, frgFalse as Fragment)
        }
        fragmentTransaction.addToBackStack(null)
        //fragmentTransaction.add(R.id.container, frg1 as? Fragment)
        fragmentTransaction.commit()
    }

    private fun showTip(){
        val questionFragmentView = layoutInflater.inflate(R.layout.fragment_tip, null)
        val questionWindow = PopupWindow(questionFragmentView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        )
        questionWindow.showAtLocation(questionFragmentView, Gravity.CENTER, 0, 0)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }

        val center = LatLng(59.9367364, 30.3096995)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 15F))

        //Эта штука переносит кнопку "Моё местоположение" в правый нижний угол
        val locationButton= (mapView.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
        val rlp=locationButton.layoutParams as (RelativeLayout.LayoutParams)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP,0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE)
        rlp.setMargins(0,0,30,30)
      
        mMap.setOnMyLocationChangeListener(object : GoogleMap.OnMyLocationChangeListener {
            override fun onMyLocationChange(p0: Location?) {
                println(p0?.latitude)
                println(p0?.longitude)
                loc = LatLng(p0?.latitude!!,p0?.longitude)// в этой переменной находятся свежие координаты
            }
        }) //какой-то звездец с получением координат

        showQuestion()
        hideTip()
    }

    private fun runTimer() { // сам секундомер
        val timerView = textView
        val handler = Handler()
        handler.post(object : Runnable {
            override fun run() {
                val hours = sec / 3600
                val minutes = sec % 3600 / 60
                val seconds = sec % 60
                val time = String.format("%d:%02d:%02d", hours, minutes, seconds)
                timerView.text = time
                sec = (Calendar.getInstance().timeInMillis - singleton.startTime) / 1000
                handler.postDelayed(this, 1000)
            }
        })
    }


}
