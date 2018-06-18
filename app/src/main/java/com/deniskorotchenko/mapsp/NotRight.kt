package com.deniskorotchenko.mapsp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_not_right.*

class NotRight : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_right)
        no.setOnClickListener {
            val intent = Intent(this, QuestMapActivity::class.java)
            startActivity(intent)
        }

    }
}
