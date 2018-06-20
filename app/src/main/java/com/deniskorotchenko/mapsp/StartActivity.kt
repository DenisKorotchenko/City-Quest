package com.deniskorotchenko.mapsp

import android.content.ContentValues
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDataBase()
        setContentView(R.layout.activity_start)
        button.setOnClickListener{val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)}

    }

    fun initDataBase(){
        val dbHelper = QuestDatabase(this)
        val db = dbHelper.writableDatabase
        try {
            db.delete(QuestDatabase.TABLE, null, null)
        }
        catch (e : Exception){

        }
        db.execSQL(("create table if not exists " + QuestDatabase.TABLE + " ( " + QuestDatabase.ID
                + " INTEGER PRIMARY KEY, " + QuestDatabase.QUESTION + " text " +");"))

        val contentValues = ContentValues()
        contentValues.clear()
        contentValues.put(QuestDatabase.QUESTION, "Это первый вопрос")
        db.insert(QuestDatabase.TABLE, null, contentValues)
        contentValues.clear()
        contentValues.put(QuestDatabase.QUESTION, "Это второй вопрос")
        db.insert(QuestDatabase.TABLE, null, contentValues)
        contentValues.clear()
        contentValues.put(QuestDatabase.QUESTION, "Это третий вопрос")
        db.insert(QuestDatabase.TABLE, null, contentValues)


        db.close()
        dbHelper.close()
    }
}
