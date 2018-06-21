package com.deniskorotchenko.mapsp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.google.android.gms.maps.model.LatLng

class AllQuestsDataBase(context: Context) : SQLiteOpenHelper(context, Singleton.instance.DATABASE_NAME, null, Singleton.instance.DATABASE_VERSION)  {
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    override fun onCreate(p0: SQLiteDatabase?) {

    }

    fun getAllMarkers() : List<MarkerInAll>{
        val markers : MutableList<MarkerInAll> = mutableListOf()
        val db = readableDatabase
        val cursor = db.query(AllQuestsDataBase.TABLE, null, null, null, null, null, null)
        if (cursor.moveToFirst()){
            do{
                val markerID = cursor.getInt(cursor.getColumnIndex(AllQuestsDataBase.ID))
                val latLng = LatLng(cursor.getDouble(cursor.getColumnIndex(AllQuestsDataBase.LAT)),
                        cursor.getDouble(cursor.getColumnIndex(AllQuestsDataBase.LNG)))
                markers.add(MarkerInAll(markerID, latLng))
            } while (cursor.moveToNext())
        }
        Log.v("AllQuestsDB", cursor.count.toString())

        cursor.close()
        db.close()
        return markers

    }

    private val singleton = Singleton.instance

    companion object {

        val TABLE = "allquests"
        val TABLENAME = "tableName"
        val ID = "id"
        val LAT = "lat"
        val LNG = "lng"

    }

    fun getTableQuestById(tag : Int): String {
        val db = readableDatabase
        val cursor = db.query(AllQuestsDataBase.TABLE, arrayOf(QuestDataBase.TABLENAME), QuestDataBase.ID + " = " + tag, null, null, null, null)

        var ans = ""
        if (cursor.moveToFirst()) {
            ans = cursor.getString(cursor.getColumnIndex(QuestDataBase.TABLENAME))
        }
        db.close()
        cursor.close()
        return ans
    }
}