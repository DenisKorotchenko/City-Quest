package com.deniskorotchenko.mapsp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class QuestDataBase(context: Context) : SQLiteOpenHelper(context, Singleton.instance.DATABASE_NAME, null, Singleton.instance.DATABASE_VERSION) {

    val singleton = Singleton.instance

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun getQuestion(number: Int) : String{
        val db = readableDatabase
        val cursor = db.query(singleton.curentTableQuest, arrayOf(QuestDataBase.QUESTION), ID + " = " + number, null, null, null, null)

        var question = "-1"
        if (cursor.moveToFirst()) {
            question = cursor.getString(cursor.getColumnIndex(QuestDataBase.QUESTION))
        }
        db.close()
        cursor.close()
        return question
    }

    fun getNumberOfQuestions() : Int{
        val db = readableDatabase
        val cursor = db.query(singleton.curentTableQuest, null, null, null, null, null, null)
        val res = cursor.count
        db.close()
        cursor.close()
        return res
    }

    fun initDataBase(){
        Log.v("INITDATABASE", "start")
        val db = this.readableDatabase
        val contentValues = ContentValues()
        val allTable = AllQuestsDataBase.TABLE
        try {
            db.delete(allTable, null, null)
        }
        catch (e : Exception){}
        Log.v("INITDATABASE", "first try")
        db.execSQL(("create table if not exists " + allTable + " ( "
                + ID + " INTEGER PRIMARY KEY, "
                + LAT + " REAL, "
                + LNG + " REAL, "
                + AllQuestsDataBase.TABLENAME +" TEXT "
                +");"))
        Log.v("INITDATABASE", "1table")
        var table : String = ""

        table = "quest1"
        contentValues.clear()
        contentValues.put(LAT, 59.980556)
        contentValues.put(LNG, 30.324234)
        contentValues.put(AllQuestsDataBase.TABLENAME, "quest1")
        db.insert(allTable, null, contentValues)

        try {
            db.delete(table, null, null)
        }
        catch (e : Exception){}
        db.execSQL(("create table if not exists " + table + " ( "
                + ID + " INTEGER PRIMARY KEY, "
                + QUESTION + " TEXT, "
                + LAT + " REAL, "
                + LNG + " REAL, "
                + RADIUS + " INTEGER "
                +");"))

        contentValues.clear()
        contentValues.put(QUESTION, "БЦ Таймс")
        contentValues.put(LAT, 59.980556)
        contentValues.put(LNG, 30.324234)
        contentValues.put(RADIUS, 20)
        db.insert(table, null, contentValues)

        table = "quest2"
        contentValues.clear()
        contentValues.put(LAT, 50.0)
        contentValues.put(LNG, 30.0)
        contentValues.put(AllQuestsDataBase.TABLENAME, "quest2")
        db.insert(allTable, null, contentValues)

        try {
            db.delete(table, null, null)
        }
        catch (e:Exception){}
        db.execSQL(("create table if not exists " + table + " ( "
                + ID + " INTEGER PRIMARY KEY, "
                + QUESTION + " TEXT, "
                + LAT + " REAL, "
                + LNG + " REAL, "
                + RADIUS + " INTEGER "
                +");"))

        contentValues.clear()
        contentValues.put(QUESTION, "Где-то далеко!")
        contentValues.put(LAT, 50.0)
        contentValues.put(LNG, 30.0)
        contentValues.put(RADIUS, 100)
        db.insert(table, null, contentValues)

        db.close()
    }

    companion object {

        val ID = "id"
        val QUESTION = "question"
        val LAT = "lat"
        val LNG = "lng"
        val RADIUS = "radius"
        val TABLENAME = "tableName"
    }
}