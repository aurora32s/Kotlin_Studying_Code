package com.example.round.smart_police2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.example.round.smart_police2.Common.CurrentDate
import com.example.round.smart_police2.Common.CurrentLocation

/**
 * Created by Round on 2017-09-20.
 */
class CrimeActivity : AppCompatActivity() {

    private lateinit var currentLocation : CurrentLocation
    private val currentDate : CurrentDate = CurrentDate()

    //CarActivity Main Layout View
    private lateinit var date : TextView
    private lateinit var where : TextView
    private lateinit var confirm : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crime_paper)

        //CarActivity Main Layout View
        date = findViewById(R.id.date) as TextView
        where = findViewById(R.id.where) as TextView
        confirm = findViewById(R.id.confirm) as Button

        currentLocation = CurrentLocation(where,this)
        currentLocation.getCurrentLocation()
        currentDate.getDate(date)
    }
}