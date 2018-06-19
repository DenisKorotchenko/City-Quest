package com.deniskorotchenko.mapsp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class QuestDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(("create table if not exists " + TABLE + " ( " + ID
                + " INTEGER PRIMARY KEY, " + QUESTION + " text " +");"))

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("drop table if exists " + TABLE)
//
//        onCreate(db)

    }

    companion object {

        val DATABASE_VERSION = 1
        val DATABASE_NAME = "questTest"
        val TABLE = "quest"

        val ID = "id"
        val QUESTION = "question"
    }
}