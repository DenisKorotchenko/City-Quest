package com.deniskorotchenko.mapsp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AllQuestsDataBase(context: Context) : SQLiteOpenHelper(context, Singleton.instance.DATABASE_NAME, null, Singleton.instance.DATABASE_VERSION)  {
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    override fun onCreate(p0: SQLiteDatabase?) {

    }

    private val singleton = Singleton.instance

    companion object {

        val TABLE = "allquests"
        val TABLENAME = "tableName"
        val ID = "id"
        val QUESTION = "question"
    }
}