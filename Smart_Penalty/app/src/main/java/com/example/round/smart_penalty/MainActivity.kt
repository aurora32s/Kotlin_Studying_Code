package com.example.round.smart_penalty

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //Layout View
    private lateinit var car : LinearLayout
    private lateinit var person : LinearLayout
    private lateinit var crime : LinearLayout
    private lateinit var search : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //layout Setting
        car = findViewById(R.id.car) as LinearLayout
        person = findViewById(R.id.person) as LinearLayout
        crime = findViewById(R.id.crime) as LinearLayout
        search = findViewById(R.id.search) as LinearLayout

        //layout onClick Listener Setting
        car.setOnClickListener(this)
        person.setOnClickListener(this)
        crime.setOnClickListener(this)
        search.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        var intent : Intent? = null

        when(view){
            car -> { intent = Intent(this, CarActivity::class.java) }
            person -> { intent = Intent(this, PersonActivity::class.java) }
            crime -> { intent = Intent(this, CrimeActivity::class.java) }
            search -> { intent = Intent(this, SearchActivity::class.java) }
        }

        startActivity(intent)
    }
}
