package com.deniskorotchenko.mapsp

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.app.Fragment
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.TextView
import android.location.Location
import android.net.Uri
import android.os.Build
import android.support.annotation.RequiresApi



import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_quest_map.*
import kotlinx.android.synthetic.main.activity_quest_map.view.*
import android.widget.RelativeLayout
import java.util.*


class QuestMapActivity : AppCompatActivity(), OnMapReadyCallback, fragmentright.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: View
    private var sec: Long = 0 //Для секундомера
    private var singleton = Singleton.instance
    private var running: Boolean = true
    var frg1 : fragmentright = fragmentright.newInstance("","")


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quest_map)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.questMap) as SupportMapFragment
        mapView = mapFragment.view!!
        mapFragment.getMapAsync(this)

        /* получаем экземпляр FragmentTransaction
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager
                .beginTransaction()

        // добавляем фрагмент
        val myFragment = MyFragment()
        fragmentTransaction.add(R.id.container, myFragment)
        fragmentTransaction.commit()*/



        buttonToQuestion.setOnClickListener {
            val questionFragmentView = layoutInflater.inflate(R.layout.fragment_question_text, null)
            val questionWindow = PopupWindow(questionFragmentView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
                    )
            val questDatabase = QuestDatabase(this)
            questionFragmentView.textView.text = questDatabase.getQuestion(singleton.nowQuestion)
            questionWindow.showAtLocation(questionFragmentView, Gravity.CENTER, 0, 0)
            Log.v("DB", questDatabase.getNumberOfQuestions().toString())
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


       /*imhere.setOnClickListener {
           val NotRightFragmentView = layoutInflater.inflate(R.layout.fragment_fragmentnotright, null)
          val questionWindow = PopupWindow(NotRightFragmentView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
            )
            questionWindow.showAtLocation(NotRightFragmentView, Gravity.CENTER, 0, 0)
        }*/
      
        imhere2.setOnClickListener {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, frg1 as Fragment)
            fragmentTransaction.addToBackStack(null)
            //fragmentTransaction.add(R.id.container, frg1 as? Fragment)
            fragmentTransaction.commit()
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
                var loc = LatLng(p0?.latitude!!,p0?.longitude)// в этой переменной находятся свежие координаты
            }
        }) //какой-то звездец с получением координат
    }

    fun onClickStop(){ //функция секундомера
        running = false
    }

    fun onClickReset() { // ещё функция секундомера
        running = false
        sec = 0
    }

    private fun formatLocation(location: Location?): String {
        return if (location == null) ""
        else String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3\$tF %3\$tT",
                location.latitude, location.longitude)
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
