package com.example.round.nationinfo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.InputStream
import java.io.InputStreamReader

class NationDetailActivity: AppCompatActivity() {

    companion object {
        val EXTRA_NATION_NAME = "nation_name"
    }

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)

        val nation : String = intent.getStringExtra(EXTRA_NATION_NAME)
        setContentView(R.layout.activity_nation_detail)

    }

    private fun getDataFromName(selected : String) : NationDetaliData?{

        val gson : Gson = GsonBuilder().create()
        val inputStream : InputStream = assets.open("nation_data.json")
        val reader : InputStreamReader = InputStreamReader(inputStream)
        val detailData = gson.fromJson(reader, GsonData::class.java)

        for(data in detailData.data){
            if(selected.equals(data.name)){
                return data
            }
        }
    }
}