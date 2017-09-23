package com.example.round.smart_police2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.round.smart_police2.Data.Crime

/**
 * Created by Round on 2017-09-19.
 */
class CarPaperConfirm : AppCompatActivity() {

    private val context : Context = this

    private var crime : Crime? = null

    //Layout View
    private lateinit var date : TextView
    private lateinit var place : TextView
    private lateinit var firstDate : TextView
    private lateinit var secondDate : TextView
    private lateinit var personNo : TextView
    private lateinit var personName : TextView
    private lateinit var crimActivity : TextView
    private lateinit var firstCost : TextView
    private lateinit var secondCost : TextView

    //Button
    private lateinit var change : Button
    private lateinit var confirm : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_paper_confirm)

        var intent : Intent = intent
        var id : Int = intent.getIntExtra("id",1)

        crime = CarActivity.dataList.getCrime(id)

        //Layout View
        date = findViewById(R.id.dateConfirm) as TextView
        place = findViewById(R.id.place) as TextView
        firstDate = findViewById(R.id.firstDate) as TextView
        secondDate = findViewById(R.id.secondDate) as TextView
        personNo = findViewById(R.id.personNo) as TextView
        personName = findViewById(R.id.name) as TextView
        crimActivity = findViewById(R.id.crime) as TextView
        firstCost = findViewById(R.id.firstCost) as TextView
        secondCost = findViewById(R.id.secondCost) as TextView

        date.setText(crime!!.getCurrentDate())
        place.setText(crime!!.getLocation())
        firstDate.setText(crime!!.getFirstDate())
        secondDate.setText(crime!!.getSecondDate())
        personNo.setText(crime!!.getPersonNo(0).toString()+" - "+crime!!.getPersonNo(1).toString())
        personName.setText(crime!!.person.personName)
        crimActivity.setText(crime!!.getCrimActivity())
        firstCost.setText(crime!!.getFirstCost().toString())
        secondCost.setText(crime!!.getSecondCost().toString())

        //Button
        change = findViewById(R.id.change) as Button
        confirm = findViewById(R.id.confirm) as Button

        //전페이지로 이동
        change.setOnClickListener{ finish() }
        confirm.setOnClickListener{
            //server로 부과자료 전송
            Toast.makeText(applicationContext,"Server로 납부 고지서 자료 전송 :-D",Toast.LENGTH_LONG).show()

            //납부 고지서 전송하고, 전자 납부 번호랑 가상계좌번호 발급받는거 확인하고 실행
            var intent : Intent = Intent(context,ChoosePay::class.java)
            intent.getIntExtra("id",crime!!.id)
            startActivity(intent)
        }
    }
}