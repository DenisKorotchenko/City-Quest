package com.deniskorotchenko.mapsp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Fragment
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.view.View
import android.location.Location
import android.util.Log
import android.view.MotionEvent


import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_quest_map.*
import android.widget.RelativeLayout
import com.google.android.gms.maps.model.Circle

import com.google.android.gms.maps.model.CircleOptions
import kotlinx.android.synthetic.main.fragment_tip.view.*
import java.util.*


class QuestMapActivity :
        AppCompatActivity(),
        OnMapReadyCallback,
        AnswerFragment.AnswerFragmentListener,
        FalseAnswerFragment.FalseAnswerListener{

    private lateinit var mMap: GoogleMap
    private lateinit var mapView: View
    private var sec: Long = 0 //Для секундомера
    private var singleton = Singleton.instance
    var frgTrue : AnswerFragment = AnswerFragment.newInstance()
    var circle : Circle? = null // зона в которой находится нужное место
    private var loc: LatLng = LatLng(0.0, 0.0)//переменнадя с моими координатами
    var a  = LatLng(0.0,0.0)


    override fun onNext() {
        hideTip()
        showQuestion()
        drawCircle()
        numQuestion.text = "${singleton.nowQuestion}/${QuestDataBase(this).getNumberOfQuestions()}"
        val numQ = QuestDataBase(this).getNumberOfQuestions()
        val thisQ = singleton.nowQuestion
        progress.progress = (thisQ-1)*100/numQ
        if (thisQ == numQ)
            progress.secondaryProgress = 100
        else
            progress.secondaryProgress = (thisQ*100/numQ)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(a, 12.5F))
    }

    override fun onTip() {
        unhideTip()
    }


    override fun onBackFromAnswer() {
    }


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

        supportActionBar!!.hide()
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
        /*val questionFragmentView = layoutInflater.inflate(R.layout.fragment_question_text, null)
        val questionWindow = PopupWindow(questionFragmentView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                    true
        )
        val questDatabase = QuestDataBase(this)
        questionFragmentView.questionText.text = questDatabase.getQuestion(singleton.nowQuestion)
        questionFragmentView.questionText.movementMethod = ScrollingMovementMethod()
        questionFragmentView.numberQuestion.text="${singleton.nowQuestion}/${QuestDataBase(this).getNumberOfQuestions()}"
        questionWindow.showAtLocation(questionFragmentView, Gravity.CENTER, 0, 0)*/
        val fragmentTransaction = fragmentManager.beginTransaction()
        val frgQuestion = QuestionFragment.newInstance()
        fragmentTransaction.setCustomAnimations(R.animator.slide_in_bottom, R.animator.slide_to_bottom)
        fragmentTransaction.replace(R.id.container, frgQuestion as Fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
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
        val tipFragmentView = layoutInflater.inflate(R.layout.fragment_tip, null)
        val tipWindow = PopupWindow(tipFragmentView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true
        )
        val tipText = QuestDataBase(this).getTip(singleton.nowQuestion)
        tipFragmentView.tipTextView.text = tipText
        tipWindow.showAtLocation(tipFragmentView, Gravity.CENTER, 0, 0)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        }

        val center = LatLng(59.9367364, 30.3096995)

        //Эта штука переносит кнопку "Моё местоположение" в правый нижний угол
        val locationButton= (mapView.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
        val rlp=locationButton.layoutParams as (RelativeLayout.LayoutParams)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP,0)
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE)
        rlp.setMargins(0,0,30,60)
        
      
        mMap.setOnMyLocationChangeListener(object : GoogleMap.OnMyLocationChangeListener {
            override fun onMyLocationChange(p0: Location?) {
                println(p0?.latitude)
                println(p0?.longitude)
                loc = LatLng(p0?.latitude!!,p0?.longitude)// в этой переменной находятся свежие координаты
            }
        }) //какой-то звездец с получением координат
        onNext()
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


    fun drawCircle() {
        QuestDataBase(this).currentQuestionLocation()
        var random = Random()
        var long = (random.nextInt(30000) - 15000 ).toDouble() / 1000000 + QuestDataBase(this).currentQuestionLocation().longitude
        var lat = (random.nextInt(30000) - 15000 ).toDouble() / 1000000 + QuestDataBase(this).currentQuestionLocation().latitude
        a  = LatLng(lat, long)
        if (circle != null)
            circle!!.isVisible = false
        circle = mMap.addCircle(CircleOptions()
                .center(a)
                .radius(2500.0)
                .strokeColor(Color.argb(127, 0,0,255)).strokeWidth(10.0.toFloat())
                .fillColor(Color.argb(30,0,10,100)))
    }
}

