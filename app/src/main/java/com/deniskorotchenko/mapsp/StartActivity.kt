package com.deniskorotchenko.mapsp

import android.content.ContentValues
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        QuestDataBase(this).initDataBase()
        setContentView(R.layout.activity_start)
        button.setOnClickListener{val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)}

        supportActionBar!!.hide()
    }


}
