package com.deniskorotchenko.mapsp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_last.*

class Last : AppCompatActivity() {

    val singleton = Singleton.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last)
        showResultTime()
        tostart.setOnClickListener {
            val intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
        }
    }

    fun showResultTime (){
        val sec = (singleton.finishTime - singleton.startTime)/1000
        val hours = sec / 3600
        val minutes = sec % 3600 / 60
        val seconds = sec % 60
        val time = String.format("%d:%02d:%02d", hours, minutes, seconds)
        resultTime.text = time
    }
}
