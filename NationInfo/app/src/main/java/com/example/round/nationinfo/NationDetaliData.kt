package com.example.round.nationinfo

import java.util.*

/**
 * Created by Round on 2017-09-14.
 */
data class NationDetaliData(val name:String, val capital : String, val location : String, val volume : String, val weather : String, val language : String)

data class GsonData(val data: ArrayList<NationDetaliData>)