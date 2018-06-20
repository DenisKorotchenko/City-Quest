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
    }

    /*fun initDataBase(){
        val dbHelper = QuestDataBase(this)
        val db = dbHelper.writableDatabase
        try {
            db.delete(QuestDataBase.TABLE, null, null)
        }
        catch (e : Exception){

        }
        db.execSQL(("create table if not exists " + QuestDataBase.TABLE + " ( " + QuestDataBase.ID
                + " INTEGER PRIMARY KEY, " + QuestDataBase.QUESTION + " text " +");"))

        val contentValues = ContentValues()
        contentValues.clear()
        contentValues.put(QuestDataBase.QUESTION, "Это первый вопрос")
        db.insert(QuestDataBase.TABLE, null, contentValues)
        contentValues.clear()
        contentValues.put(QuestDataBase.QUESTION, "Это второй вопрос")
        db.insert(QuestDataBase.TABLE, null, contentValues)
        contentValues.clear()
        contentValues.put(QuestDataBase.QUESTION, "Это третий вопрос")
        db.insert(QuestDataBase.TABLE, null, contentValues)


        db.close()
        dbHelper.close()
    }*/

}
