package com.example.round.kotlin_tutorial3

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_start.*

/**
 * Created by Round on 2017-09-05.
 */

class StartActivity : AppCompatActivity(){

    private val options :ArrayList<String> = arrayListOf("0","1","2","3","4","5","6","7","8","9")

    private var mArray : ArrayList<Int> = arrayListOf()
    private var po1 : Int = 0
    private var po2 : Int = 0
    private var po3 : Int = 0

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        num1.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)

        num1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mArray.add(0,options[position].toInt())

                changeTextColor(po1,Color.BLACK)
                changeTextColor(position,Color.RED)

                po1 = position
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        num2.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)

        num2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mArray.add(1,options[position].toInt())

                changeTextColor(po2,Color.BLACK)
                changeTextColor(position,Color.RED)

                po2 = position
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        num3.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,options)

        num3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mArray.add(2,options[position].toInt())

                changeTextColor(po3,Color.BLACK)
                changeTextColor(position,Color.RED)

                po3 = position
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        select.setOnClickListener{
            val intent : Intent = Intent(this,GameActivity::class.java)
            intent.putExtra("num1",mArray[0])
            intent.putExtra("num2",mArray[1])
            intent.putExtra("num3",mArray[2])

            startActivity(intent)
            finish()
        }
    }

    private fun changeTextColor(position : Int, color: Int):Unit{
        Log.i("TextColor","************"+position)
        when(position){
            0->zero.setTextColor(color)
            1->one.setTextColor(color)
            2->two.setTextColor(color)
            3->three.setTextColor(color)
            4->four.setTextColor(color)
            5->five.setTextColor(color)
            6->six.setTextColor(color)
            7->seven.setTextColor(color)
            8->eight.setTextColor(color)
            9->nine.setTextColor(color)
        }
    }
}