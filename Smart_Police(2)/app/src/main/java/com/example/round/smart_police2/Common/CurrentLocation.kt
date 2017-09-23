package com.example.round.smart_police2.Common

import android.app.Activity
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
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
import java.util.ArrayList

class CurrentLocation(var where : TextView, var context : AppCompatActivity) :LocationListener{

    //1. 현재 장소 받아오기
    private lateinit var location : LocationManager
    private val time : Float = 1.toFloat()

    private lateinit var requestQueue : RequestQueue
    private var lat : Double = 0.0
    private var lng : Double = 0.0

    fun getCurrentLocation(){

        var permission : PermissionListener = object : PermissionListener {
            override fun onPermissionGranted()= Toast.makeText(context, "권한 허가", Toast.LENGTH_LONG).show()
            override fun onPermissionDenied(deniedPermissions: ArrayList<String>) =  Toast.makeText(context, "권한 거부", Toast.LENGTH_LONG).show()
        }

        TedPermission(context).setPermissionListener(permission)
                .setRationaleMessage("GPS를 사용하기 위해서는 GPS 접근 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다...\n[설정]>[권한]에서 권한을 허용할 수 있습니다.")
                .setPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .check()

        try{

            location = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
                    where.setText(address)
                },
                Response.ErrorListener{ error ->
                    Log.i("JSONObject",error.toString())
                }
        )

        requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }
}