package com.example.round.kotlin_reverse_geocoding

import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL

class HttpDataHandler() {

    fun GetHTTPData(requestUrl : String) : String{

        var url : URL;
        var response = ""

        try {
            url = URL(requestUrl)
            var conn : HttpURLConnection = url.openConnection() as HttpURLConnection

            conn.readTimeout = 15000
            conn.connectTimeout = 15000
            conn.requestMethod = "GET"
            //conn.setRequestProperty("Content-Type","application/json")
            conn.doOutput = true

            var responseCode : Int = conn.responseCode

            if(responseCode == HttpURLConnection.HTTP_OK){
                Log.i("Response_String",conn.inputStream.toString());

                var line : String?
                var br : BufferedReader = BufferedReader(InputStreamReader(conn.inputStream))

                line = br.readLine()
                while(line != null){
                    Log.i("Response_String",line);
                    response += line
                    line = br.readLine()
                }
            }
        }catch (e : ProtocolException){
            e.printStackTrace()
        }catch (e: MalformedURLException){
            e.printStackTrace()
        }catch (e : IOException){
            e.printStackTrace()
        }

        return response
    }
}