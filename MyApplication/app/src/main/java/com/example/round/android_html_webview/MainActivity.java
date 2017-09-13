package com.example.round.android_html_webview;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.lang.annotation.Target;

public class MainActivity extends AppCompatActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String myurl = "file:///android_asset/index.html";
        WebView view = (WebView)findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        WebViewInterface webViewInterface = new WebViewInterface(this,view);

        view.addJavascriptInterface(webViewInterface,"Android");
        view.loadUrl(myurl);
    }

    public class WebViewInterface {

        private WebView mAppView;
        private Activity mContext;

        /**
         * 생성자.
         * @param activity : context
         * @param view : 적용될 웹뷰
         */
        public WebViewInterface(Activity activity, WebView view) {
            mAppView = view;
            mContext = activity;
        }
        /**
         * 안드로이드 토스트를 출력한다. Time Long.
         * @param message : 메시지
         */
        @JavascriptInterface
        public void toastLong (String message) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
        /**
         * 안드로이드 토스트를 출력한다. Time Short.
         * @param message : 메시지
         */
        @JavascriptInterface
        public void toastShort (String message) { // Show toast for a short time
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }
}
