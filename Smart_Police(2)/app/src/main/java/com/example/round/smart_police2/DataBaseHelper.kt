package com.example.round.smart_police2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Round on 2017-09-20.
 */
class DataBaseHelper : SQLiteOpenHelper{

    //DataBase
    companion object {
        private val DATABASE_VER : Int = 1
        private val DATABASE_NAME : String = "Police"
    }

    constructor(context: Context) : super(context,DATABASE_NAME,null,DATABASE_VER)

    override fun onCreate(p0: SQLiteDatabase?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}