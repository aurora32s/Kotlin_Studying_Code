package com.example.round.nationinfo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.InputStream
import java.io.InputStreamReader
import kotlinx.android.synthetic.main.activity_nation_detail.*

class NationDetailActivity: AppCompatActivity() {

    companion object {
        val EXTRA_NATION_NAME = "nation_name"
    }

    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)

        val nation : String = intent.getStringExtra(EXTRA_NATION_NAME)
        setContentView(R.layout.activity_nation_detail)

        val data : NationDetaliData? = getDataFromName(nation)
        img_flag.setImageResource(getResourId(nation))
        initView(data)
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

        return null
    }

    private fun getResourId(selected: String) : Int
    {
        var resourId:Int = 0
        when(selected)
        {
            "벨기에"->{
                resourId = R.drawable.l_flag_belgium
            }
            "아르헨티나"->{
                resourId = R.drawable.l_flag_argentina
            }
            "브라질"->{
                resourId = R.drawable.l_flag_brazil
            }
            "캐나다"->{
                resourId = R.drawable.l_flag_canada
            }
            "중국"->{
                resourId = R.drawable.l_flag_china
            }
            "크로아티아"->{
                resourId = R.drawable.l_flag_croatia
            }
            "체코"->{
                resourId = R.drawable.l_flag_czech
            }
            "독일"->{
                resourId = R.drawable.l_flag_germany
            }
            "가나"->{
                resourId = R.drawable.l_flag_ghana
            }
            "그리스"->{
                resourId = R.drawable.l_flag_greece
            }
            "대한민국"->{
                resourId = R.drawable.l_flag_korea
            }
            "노르웨이"->{
                resourId = R.drawable.l_flag_norway
            }
            "미국"->{
                resourId = R.drawable.l_flag_united_states
            }
            else->{
                resourId = 0
            }
        }
        return resourId
    }

    private fun initView(data:NationDetaliData?)
    {
        txt_name.text = data?.name
        capital.text = data?.capital
        volume.text = data?.volume
        weather.text = data?.weather
        language.text = data?.language
        location.text = data?.location
    }

}