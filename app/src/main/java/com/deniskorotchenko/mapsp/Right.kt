package com.deniskorotchenko.mapsp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import kotlinx.android.synthetic.main.activity_right.*

class Right : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_right)
        next1.setOnClickListener{val intent = Intent(this, QuestMapActivity::class.java)
            startActivity(intent)}
        tofinish.setOnClickListener{val intent = Intent(this, Last::class.java)
            startActivity(intent)}
    }
}
