package com.example.round.smart_police2.Data

class Crime(val id : Int ,val person : Person){

    //납부 고지서 발행 일자
    private var date : String = ""
    //납부 고지서 발행 장소
    private var location : String =""

    //1차 납부 기간 일자
    private var firstYear : Int = 0
    private var firstMonth : Int = 0
    private var firstDay : Int = 0

    //2차 납부 기간 일자
    private var secondYear : Int = 0
    private var secondMonth : Int = 0
    private var secondDay : Int = 0

    //1차 범칙금
    private var firstCost : Int = 0
    //2차 범칙금
    private var secondCost : Int = 0

    //적용법조
    private var rule : String = ""
    //범칙내용 / 범칙행동
    private var crime : String = ""

    //결락 확인
    private var block : Boolean = false

    fun setDate(date : String, firstYear : Int,firstMonth : Int,firstDay : Int,secondYear : Int,secondMonth : Int,secondDay : Int){
        this.date = date
        this.firstYear = firstYear
        this.firstMonth = firstMonth
        this.firstDay = firstDay
        this.secondYear = secondYear
        this.secondMonth = secondMonth
        this.secondDay = secondDay
    }

    fun setCost(firstCost : Int, secondCost : Int){
        this.firstCost = firstCost
        this.secondCost =secondCost
    }

    fun setLocation(location : String){
        this.location = location
    }

    fun setCrime(rule : String, crime : String){
        this.rule = rule
        this.crime = crime
    }

    fun setBlock(){ this.block = true}

    fun getCurrentDate () : String{
        return date
    }

    fun getFirstDate() : String{
        return firstYear.toString() + "년 "+firstMonth.toString()+"월 "+firstDay.toString()+" 일"
    }

    fun getSecondDate() : String{
        return secondYear.toString() + "년 "+secondMonth.toString()+"월 "+secondDay.toString()+" 일"
    }

    fun getLocation() : String{ return this.location}

    fun getFirstCost() : Int { return this.firstCost}
    fun getSecondCost() : Int{ return this.secondCost}

    fun getRule() : String { return this.rule}
    fun getCrimActivity() : String { return this.crime}

    fun getPersonNo(id : Int) : Int{return this.person.personNo.get(id)}

}