package com.example.round.kotlin_reverse_geocoding

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : AppCompatActivity(), LocationListener{

    private val TAG : String = ".MainActivity"
    private val context: Context = this

    private lateinit var btnGetAddress : Button
    private lateinit var txt_address : TextView

    //Get Location
    var locationManager : LocationManager? = null
    var lat : Double = 0.0
    var lng : Double = 0.0
    var time : Float = 1.toFloat()

    var address : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnGetAddress = findViewById(R.id.btnGetAddress) as Button
        txt_address = findViewById(R.id.address) as TextView

        getLocation()

        btnGetAddress.setOnClickListener{
            object : Thread(){
                override fun run() {
                    super.run()

                    getAddress()
                }
            }.start()
        }

    }

    private fun getLocation(){
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var permission : PermissionListener = object : PermissionListener{
            override fun onPermissionGranted()=Toast.makeText(applicationContext, "권한 허가", Toast.LENGTH_LONG).show()
            override fun onPermissionDenied(deniedPermissions: ArrayList<String>) =  Toast.makeText(applicationContext, "권한 거부", Toast.LENGTH_LONG).show()
        }

        TedPermission(this).setPermissionListener(permission)
                .setRationaleMessage("GPS를 사용하기 위해서는 GPS 접근 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다...\n[설정]>[권한]에서 권한을 허용할 수 있습니다.")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .check()

        try{

            locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER,400,time,this);
            locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,400,time,this);

        }catch (e : SecurityException) {
            Log.i(TAG, "ERROR : " + e.toString())
        }
    }

//    private inner class GetAddress : AsyncTask<String? ,Void, String?>(){
//        private val dialog : ProgressDialog = ProgressDialog(context)
//
//        override fun onPreExecute() {
//            super.onPreExecute()
//
//            dialog.setMessage("Please wait...")
//            dialog.setCanceledOnTouchOutside(false)
//            dialog.show()
//        }
//
//        override fun doInBackground(vararg strings: String?): String? {
//            try{
//
//                var lat : Double = strings[0]!!.split(",")[0] as Double
//                var lng : Double = strings[0]!!.split(",")[1] as Double
//
//
//                Log.i("Response","lat : "+lat.toString()+" / lng : "+lng.toString())
//
//
//
//
//            }catch (ex :Exception){
//                ex.printStackTrace()
//            }
//            return null
//        }
//
//        override fun onPostExecute(result: String?) {
//            try{
//
//            }catch (e : JSONException){
//                e.printStackTrace()
//            }
//
//            if(dialog.isShowing){
//                dialog.dismiss()
//            }
//        }
//    }

    private fun getAddress(){

        var http : HttpDataHandler? = HttpDataHandler()
        var url : String = String.format("http://maps.googleapis.com/maps/api/geocode/json?latlng=%.4f,%.4f&amp;sensor=true",lat,lng)

        Log.i("Address", url?:"null")

        var response : String = http!!.GetHTTPData(url)
        var jsonObject : JSONObject? = JSONObject(response)

        Log.i("Address", jsonObject!!.toString()?:"null")
        address = (jsonObject!!.get("results") as JSONArray).getJSONObject(0).get("formatted_address").toString()

        mHandler.sendEmptyMessage(0)
//        txt_address.setText(address)

    }

    override fun onLocationChanged(location: Location?) {

        lat = location!!.latitude
        lng = location!!.longitude

        Log.i("Address", "LAT : "+lat+" / LNG : "+lng)

        object : Thread(){
            override fun run() {
                super.run()

                getAddress()
            }
        }.start()

    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
    }
    override fun onProviderEnabled(p0: String?) {
    }

    override fun onProviderDisabled(p0: String?) {
    }

    private val mHandler : Handler = object : Handler(){
        override fun handleMessage(msg: Message?) {
            txt_address.setText(address)
        }
    }

}
