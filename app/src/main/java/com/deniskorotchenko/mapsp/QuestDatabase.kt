package com.deniskorotchenko.mapsp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.android.synthetic.main.activity_quest_map.view.*

class QuestDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun getQuestion(number: Int) : String{
        val db = readableDatabase
        val cursor = db.query(QuestDatabase.TABLE, arrayOf(QuestDatabase.QUESTION), ID + " = " + number, null, null, null, null)

        var question = "-1"
        if (cursor.moveToFirst()) {
            question = cursor.getString(cursor.getColumnIndex(QuestDatabase.QUESTION))

        }else {

        }
        db.close()
        cursor.close()
        return question
    }

    fun getNumberOfQuestions() : Int{
        val db = readableDatabase
        val cursor = db.query(QuestDatabase.TABLE, null, null, null, null, null, null)
        db.close()
        return cursor.count
    }

    companion object {

        val DATABASE_VERSION = 1
        val DATABASE_NAME = "questTest"
        val TABLE = "quest"

        val ID = "id"
        val QUESTION = "question"
    }
}