package com.example.round.kotlin_volley_geocoding

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
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
import javax.net.ssl.SSLEngineResult

class MainActivity : AppCompatActivity(), LocationListener {

    private lateinit var requestQueue : RequestQueue
    private var lat : Double = 0.0
    private var lng : Double = 0.0

    private lateinit var address : TextView
    private lateinit var date : TextView

    private val context: Context = this
    private lateinit var postCode : TextView
    private lateinit var detaliPostCode : EditText
    private lateinit var totalPostCode : TextView
    private lateinit var search : Button
    private lateinit var confirm : Button
    private val SEARCH_ADDRESS_ACTIVITY : Int = 10000

    private lateinit var location : LocationManager
    private val time: Float = 1.toFloat()

    private lateinit var pickDate : Button
    lateinit var pickedDate : TextView
    private var year : Int = 0
    private var month : Int = 0
    private var day : Int = 0
    private val cal : Calendar = Calendar.getInstance()
    private val DIALOG_ID : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        address = findViewById(R.id.address) as TextView
        date = findViewById(R.id.date) as TextView

        postCode = findViewById(R.id.postcode) as TextView
        search = findViewById(R.id.search) as Button
        confirm = findViewById(R.id.confirm) as Button
        detaliPostCode = findViewById(R.id.detailpostCode) as EditText
        totalPostCode = findViewById(R.id.liveAddress) as TextView

        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        day = cal.get(Calendar.DATE)

        getDate()
        getLoaction()
        showDialogOnButtonClick()

        search.setOnClickListener{
            var intent : Intent = Intent(context, WebViewActivity::class.java)
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)
        }

        confirm.setOnClickListener{
            var totalAddress : String = postCode.text.toString()
            totalAddress += detaliPostCode.text.toString()

            totalPostCode.setText(" 현재 주소지 : "+totalAddress)
        }

    }

    private fun getDate(){
        var now : Long = System.currentTimeMillis()
        var date : Date = Date(now)
        var dateFormat : SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

        this.date.setText(dateFormat.format(date))
    }

    private fun getLoaction(){
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
                Response.Listener<JSONObject> {response->
                    var array : JSONArray = response.getJSONArray("results")
                    var address : String = array.getJSONObject(0).getString("formatted_address")

                    Log.i("JSONObject",address)
                    this.address.setText(address)
                },
                Response.ErrorListener{ error ->
                    Log.i("JSONObject",error.toString())
                }
        )

        requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.i("processDATA",requestCode.toString())
        when(requestCode){

            SEARCH_ADDRESS_ACTIVITY->{
                if(resultCode == RESULT_OK){
                    var data : String = data!!.getStringExtra("data")

                    Log.i("processDATA",resultCode.toString())
                    if(data!=null) postCode.setText(data)
                }
            }
        }
    }

    public fun showDialogOnButtonClick(){

        pickDate = findViewById(R.id.pick) as Button
        pickedDate = findViewById(R.id.pickDate) as TextView

        pickDate.setOnClickListener{
            showDialog(DIALOG_ID)
        }
    }

    override fun onCreateDialog(id: Int): Dialog? {
        if( id == DIALOG_ID) return DatePickerDialog(this,{
            view,year,month,day ->
                this.year = year
                this.month = month + 1
                this.day = day
                pickedDate.setText(this.year.toString()+" 년 "+this.month.toString()+" 월 "+this.day.toString()+" 일")
        }, year,month,day)
        return null
    }
}
