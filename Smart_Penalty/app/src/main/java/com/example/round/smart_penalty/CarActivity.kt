package com.example.round.smart_penalty

import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import org.json.JSONArray
import org.json.JSONObject
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Round on 2017-09-18.
 */

class CarActivity : AppCompatActivity(), LocationListener{

    private val context : Context = this

    private val personInfo : PersonInfo = PersonInfo()
    private val crimeInfo : CrimeInfo = CrimeInfo()

    private lateinit var mSectionPagerAdapter : SectionsPagerAdapter
    private lateinit var mViewPager : ViewPager

    //CarActivity Main Layout View
    private lateinit var date : TextView
    private lateinit var where : TextView

    //Get Location
    private lateinit var location : LocationManager
    private val time: Float = 1.toFloat()

    private lateinit var requestQueue : RequestQueue
    private var lat : Double = 0.0
    private var lng : Double = 0.0

    //납부 기한 계산을 위한 날짜
    companion object {
        //날짜
        private var year : Int = 0
        private var month : Int = 0
        private var day : Int = 0
        private val cal : Calendar = Calendar.getInstance()

        //PersonInfo Layout View
        lateinit var personNo1 : EditText
        lateinit var personNo2 : EditText
        lateinit var personName : EditText
        lateinit var personAddress : TextView
        lateinit var personDetaliAddress : EditText
        lateinit var searchPostCode : ImageButton

        //Crime Info
        private lateinit var firstDate : TextView
        private lateinit var secondDate : TextView
        private lateinit var searchDateButton1 : ImageButton
        private lateinit var searchDateButton2 : ImageButton
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_paper)

        mSectionPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        mViewPager = findViewById(R.id.container) as ViewPager
        mViewPager.adapter = mSectionPagerAdapter

        //CarActivity Main Layout View
        date = findViewById(R.id.date) as TextView
        where = findViewById(R.id.where) as TextView

        getDate()
        getLocation()
    }

    class PersonInfo : Fragment(){

        //Search Address
        private val SEARCH_ADDRESS_ACTIVITY : Int = 10000

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

            var view : View = inflater!!.inflate(R.layout.car_paper_first,container,false);

            personNo1 = view.findViewById(R.id.personNo_n) as EditText
            personNo2 = view.findViewById(R.id.personNo_t) as EditText
            personName = view.findViewById(R.id.personName) as EditText
            personAddress = view.findViewById(R.id.personAddress) as TextView
            personDetaliAddress = view.findViewById(R.id.personDetailAddress) as EditText
            searchPostCode = view.findViewById(R.id.addressSearch) as ImageButton

            searchPostCode.setOnClickListener{
                var intent : Intent = Intent(context,WebViewActivity::class.java)
                startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)
            }

            return view
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            Log.i("processDATA",requestCode.toString())
            when(requestCode){

                SEARCH_ADDRESS_ACTIVITY->{
                    if(resultCode == RESULT_OK){
                        var data : String = data!!.getStringExtra("data")

                        Log.i("processDATA",resultCode.toString())
                        if(data!=null) personAddress.setText(data)
                    }
                }
            }
        }
    }

    class CrimeInfo : Fragment(){

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

            var view : View = inflater!!.inflate(R.layout.car_paper_second,container,false);

            firstDate = view.findViewById(R.id.firstDate) as TextView
            secondDate = view.findViewById(R.id.secondDate) as TextView
            searchDateButton1 = view.findViewById(R.id.pickDate_n) as ImageButton
            searchDateButton2 = view.findViewById(R.id.pickDate_t) as ImageButton

            setFirstDate()
            return view
        }

        private fun setFirstDate(){
            var lastDay : Int = 0
            var firstYear : Int = year
            var firstMonth : Int = month
            var firstDay : Int = day

            if(month < 8) lastDay = 30 + month%2
            else lastDay = 31 - month%2

            if(day < (lastDay-9)) firstDay += 10
            else{
                if(month == 12) firstYear++
                firstMonth+=1
                firstDay = (firstDay+10)/lastDay
            }

            firstDate.text = firstYear.toString() + "년 "+firstMonth.toString()+"월 "+firstDay.toString()+" 일"
        }
    }

    inner class SectionsPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm){

        override fun getItem(position: Int): Fragment {
            if(position == 0) return personInfo
            else return crimeInfo
        }

        override fun getCount(): Int{ return 2 }

        override fun getPageTitle(position: Int): CharSequence? {
            return null
        }
    }

    private fun getDate(){
        var now : Long = System.currentTimeMillis()
        var date : Date = Date(now)
        var dateFormat : SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)+1
        day = cal.get(Calendar.DATE)

        this.date.setText(dateFormat.format(date))
    }

    private fun getLocation(){
        location = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var permission : PermissionListener = object : PermissionListener {
            override fun onPermissionGranted()= Toast.makeText(applicationContext, "권한 허가", Toast.LENGTH_LONG).show()
            override fun onPermissionDenied(deniedPermissions: ArrayList<String>) =  Toast.makeText(applicationContext, "권한 거부", Toast.LENGTH_LONG).show()
        }

        TedPermission(this).setPermissionListener(permission)
                .setRationaleMessage("GPS를 사용하기 위해서는 GPS 접근 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다...\n[설정]>[권한]에서 권한을 허용할 수 있습니다.")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .check()

        try{

            location!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,400,time,this);
            location!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,400,time,this);

        }catch (e : SecurityException) {
            Log.i("Location", "ERROR : " + e.toString())
        }
    }

    override fun onLocationChanged(location: Location?) {
        lat = location!!.latitude
        lng = location!!.longitude

        Log.i("Location","lat : "+lat+" / lng : "+lng)
        requestGeoCoding()
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}

    override fun onProviderEnabled(p0: String?) {}

    override fun onProviderDisabled(p0: String?) { }

    private fun requestGeoCoding(){

        var url : String = "http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lng+"&language=ko"

        Log.i("Location","url : "+url)


        var request : JsonObjectRequest? = JsonObjectRequest(url,null,
                Response.Listener<JSONObject> { response->
                    var array : JSONArray = response.getJSONArray("results")
                    var address : String = array.getJSONObject(0).getString("formatted_address")

                    Log.i("JSONObject",address)
                    this.where.setText(address)
                },
                Response.ErrorListener{ error ->
                    Log.i("JSONObject",error.toString())
                }
        )

        requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }
}
