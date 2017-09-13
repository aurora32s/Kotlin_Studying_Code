package com.example.round.kotlin_tutorial1

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var text  = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPlayString.setOnClickListener{

            var numbers = mutableListOf<Int>(1,2,3,4,5)
            for(i in numbers)
                Log.i("Number",i.toString())

            numbers.add(6)
            Log.i("Number","****************************************")

            for(i in numbers)
                Log.i("Number",i.toString())
        }
    }
}
