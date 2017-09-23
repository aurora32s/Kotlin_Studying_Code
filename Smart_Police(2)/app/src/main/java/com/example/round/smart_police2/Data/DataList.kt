package com.example.round.smart_police2.Data

class DataList {

    private var crimes : ArrayList<Crime> = ArrayList()

    fun addCrime(crime : Crime){
        crimes.add(crime)
    }

    fun getCrime(id : Int):Crime?{
        for(i : Int in 0..crimes.size){
            if(crimes.get(i).id == id){
                return crimes.get(i)
            }
        }
        return null
    }

    fun getAllCrime() : ArrayList<Crime>{
        return crimes
    }

    fun isCrime(id : Int) : Boolean{

        if(crimes.size >0) {
            for (i: Int in 0..crimes.size) {
                if (crimes.get(i).id == id) {
                    return true
                }
            }
        }
        return false
    }

    fun update(id : Int, crime : Crime){
        crimes.removeAt(id)
        crimes.add(crime)
    }
}