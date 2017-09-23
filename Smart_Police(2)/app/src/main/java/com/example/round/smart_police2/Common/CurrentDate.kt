package com.example.round.smart_police2.Common

import android.widget.TextView
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Round on 2017-09-19.
 */
class CurrentDate{

    //날짜
    var year : Int = 0
    var month : Int = 0
    var day : Int = 0
    private val cal : Calendar = Calendar.getInstance()

    //1차 납부일
    var firstYear : Int = 0
    var firstMonth : Int = 0
    var firstDay : Int = 0

    //2차 납부일
    var secondYear : Int = 0
    var secondMonth : Int = 0
    var secondDay : Int = 0

    fun getDate(txtDate : TextView){
        var now : Long = System.currentTimeMillis()
        var date : Date = Date(now)
        var dateFormat : SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)+1
        day = cal.get(Calendar.DATE)

        txtDate.setText(dateFormat.format(date))
    }

    fun setFirstDate(txtFirstDate : TextView){
        var lastDay : Int = 0
        firstYear = year
        firstMonth  = month
        firstDay = day

        if(month < 8) lastDay = 30 + month%2
        else lastDay = 31 - month%2

        if(day < (lastDay-9)) firstDay += 10
        else{
            if(month == 12) {
                firstYear++
                firstMonth = 1
            }
            else{
                firstMonth+=1
            }
            firstDay = (firstDay+10)/lastDay
        }

        txtFirstDate.text = firstYear.toString() + "년 "+firstMonth.toString()+"월 "+firstDay.toString()+" 일"
    }

    fun setSecondDate(txtSecondDate : TextView){
        var lastDay : Int = 0
        secondYear = year
        secondMonth = month
        secondDay= day

        if(month < 8) lastDay = 30 + month%2
        else lastDay = 31 - month%2

        if(lastDay == 30) secondMonth++
        else{
            if(day == 1) secondDay = lastDay
            else{
                secondMonth++
                secondDay = day-1
            }
        }

        if(secondMonth > 12){
            secondMonth = 1
            secondYear++
        }
        txtSecondDate.text = secondYear.toString() + "년 "+secondMonth.toString()+"월 "+secondDay.toString()+" 일"
    }
}