package com.example.round.kotlin_webview

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sendButton.setOnClickListener{ loadWebpage() }
    }

    @Throws(UnsupportedOperationException::class)
    fun buildUri(authority: String) : Uri {
        val builder = Uri.Builder()

        builder.scheme("https").authority(authority)
        return builder.build()
    }

    fun loadWebpage(){
        webview.loadUrl("")

        try{
            val url = buildUri(uriText.text.toString())
            webview.loadUrl(url.toString())
        }catch (e: UnsupportedOperationException){
            e.printStackTrace()
        }
    }
}
