package com.example.round.kotlin_tutorial3

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        start.setOnClickListener{
            var intent : Intent = Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
