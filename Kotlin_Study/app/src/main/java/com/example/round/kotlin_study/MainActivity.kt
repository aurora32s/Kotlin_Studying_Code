package com.example.round.kotlin_study

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private val TAG : String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        Log.i(TAG,"For************************in")
//        for(i : Int in 2..9){
//            Log.i(TAG,i.toString())
//        }
//        Log.i(TAG,"For************************downTo")
//        for(i : Int in 9 downTo 2){
//            Log.i(TAG,i.toString())
//        }
//        Log.i(TAG,"For************************step")
//        for(i : Int in 2..9 step 2){
//            Log.i(TAG,i.toString())
//        }
//        Log.i(TAG,"For************************step")
//        for(i : Int in 10 downTo 1 step 2){
//            Log.i(TAG,i.toString())
//        }

        val adapter = MyListAdapter(this, arrayListOf("bill","mike"),{view -> Toast.makeText(this,"ButtonClicked",Toast.LENGTH_LONG).show()})

    }

    class MyListAdapter(val context: Context, val itemArray : ArrayList<String>, val itemClick : (View?)->Unit) : BaseAdapter(){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            return convertView!!
        }

        override fun getItem(position: Int): Any {
            return itemArray.get(position)
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return itemArray.size
        }

    }
}
