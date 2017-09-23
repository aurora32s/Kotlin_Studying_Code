package com.example.round.smart_police2.Common

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.round.smart_police2.R

class WebViewActivity : AppCompatActivity() {

    private var browser: WebView? = null

    internal inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processDATA(data: String) {

            Log.i("processDATA",data)
            val intent = Intent()
            intent.putExtra("data",data)
            setResult(Activity.RESULT_OK, intent)
            finish()

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)

        browser = findViewById(R.id.webview) as WebView
        browser!!.settings.javaScriptEnabled = true
        browser!!.addJavascriptInterface(MyJavaScriptInterface(), "Android")

        browser!!.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {

                browser!!.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        })

        browser!!.loadUrl("http://cdn.rawgit.com/jolly73-df/DaumPostcodeExample/master/DaumPostcodeExample/app/src/main/assets/daum.html")
    }
}
