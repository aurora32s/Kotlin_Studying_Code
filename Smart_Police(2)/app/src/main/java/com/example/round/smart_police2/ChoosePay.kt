package com.example.round.smart_police2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout

/**
 * Created by Round on 2017-09-20.
 */
class ChoosePay : AppCompatActivity() {

    private val context :Context = this

    private lateinit var pay : LinearLayout
    private lateinit var again : LinearLayout
    private lateinit var home : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_pay)

        pay = findViewById(R.id.card) as LinearLayout
        again = findViewById(R.id.again) as LinearLayout
        home = findViewById(R.id.home) as LinearLayout

        //현장결제 module이랑 연동
//        pay.setOnClickListener{}

        again.setOnClickListener{
            var intent : Intent = Intent(context, CarActivity::class.java)
            startActivity(intent)
            finish()
        }

        home.setOnClickListener{
            var intent : Intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}