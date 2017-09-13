package com.example.round.kotlin_tutorial2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val RESET_COUNT: Int = 0
    private var count:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        count = number.text.toString().toInt()

        inc.setOnClickListener{
            count++
            setCount()
        }

        dec.setOnClickListener{
            count--
            setCount()
        }

        reset.setOnClickListener{
            count = RESET_COUNT
            setCount()
        }
    }

    private fun setCount() = number.setText(count.toString())
}
