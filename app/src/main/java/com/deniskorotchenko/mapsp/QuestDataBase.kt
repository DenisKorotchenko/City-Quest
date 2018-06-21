package com.deniskorotchenko.mapsp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.android.gms.maps.model.LatLng

class QuestDataBase(context: Context) : SQLiteOpenHelper(context, Singleton.instance.DATABASE_NAME, null, Singleton.instance.DATABASE_VERSION) {

    val singleton = Singleton.instance

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun checkAnswer(answer : LatLng) : Boolean {
        val distance = getDistanceFromLatLonInKm(answer, currentQuestionLocation())

        Log.v("Distance", distance.toString())
        Log.v("Radius", currentQuestionRadius().toString())
        return distance < currentQuestionRadius()
    }

    private fun currentQuestionLocation() : LatLng{
        val db = readableDatabase
        val cursor = db.query(singleton.curentTableQuest, arrayOf(QuestDataBase.LAT, QuestDataBase.LNG), ID + " = " + singleton.nowQuestion, null, null, null, null)

        var lat = 0.0
        var lng = 0.0
        if (cursor.moveToFirst()) {
            lat = cursor.getDouble(cursor.getColumnIndex(QuestDataBase.LAT))
            lng = cursor.getDouble(cursor.getColumnIndex(QuestDataBase.LNG))
        }
        db.close()
        cursor.close()
        return LatLng(lat, lng)
    }

    private fun currentQuestionRadius() : Double {
        val db = readableDatabase
        val cursor = db.query(singleton.curentTableQuest, arrayOf(QuestDataBase.RADIUS), ID + " = " + singleton.nowQuestion, null, null, null, null)

        var ans = 0.001
        if (cursor.moveToFirst()) {
            ans *= cursor.getDouble(cursor.getColumnIndex(QuestDataBase.RADIUS)).toDouble()
        }
        db.close()
        cursor.close()
        return ans
    }

    private fun getDistanceFromLatLonInKm(place1: LatLng, place2: LatLng) : Double { // функция, высчитывающая расстояния
        val R = 6371 // Радиус Земли в км
        val dLat = deg2rad(place2.latitude-place1.latitude) // deg2rad находится ниже
        val dLon = deg2rad(place2.longitude-place1.longitude)
        val a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(place1.latitude)) * Math.cos(deg2rad(place2.latitude)) *
                Math.sin(dLon/2) * Math.sin(dLon/2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
        val d = R * c // расстояние в км
        return Math.abs(d)
    }

    fun deg2rad(deg: Double): Double{ // переводит градусы в радианы
        return deg * (Math.PI/180)
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
                + AllQuestsDataBase.ID + " INTEGER PRIMARY KEY, "
                + AllQuestsDataBase.LAT + " REAL, "
                + AllQuestsDataBase.LNG + " REAL, "
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
        contentValues.put(LAT, 59.980942)
        contentValues.put(LNG, 30.3247186)
        contentValues.put(RADIUS, 100)
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