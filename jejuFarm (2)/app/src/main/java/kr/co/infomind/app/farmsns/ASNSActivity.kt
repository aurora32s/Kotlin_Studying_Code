package kr.co.infomind.app.farmsns

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.StrictMode
import android.provider.MediaStore
import android.provider.MediaStore.Images
import android.telephony.TelephonyManager
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.KeyEvent
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.GeolocationPermissions.Callback
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebStorage
import android.webkit.WebView

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.gcm.GoogleCloudMessaging

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.NameValuePair
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.protocol.HTTP

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.UUID

import kr.co.infomind.app.farmsns.util.MarketVersionChecker

import kr.co.infomind.app.farmsns.R.id.webview

class ASNSActivity : DroidGap() {

    private var uploadMessage: ValueCallback<Uri>? = null

    private val handler = Handler()
    private var backHandler: Handler? = null
    private var mFlag = false

    lateinit var act: Activity
    //private static final String TAG = "ICELANCER";

    //// 구글서버 ID
    internal var SENDER_ID = "1043130844065"

    internal var gcm: GoogleCloudMessaging? = null
    internal var prefs: SharedPreferences? = null
    internal lateinit var context: Context

    internal lateinit var regid: String


    private var mBackgroundThread: BackgroundThread? = null
    internal lateinit var deviceVersion: String
    internal var storeVersion: String? = null
    /** http://dexx.tistory.com/124
     * Called when the activity is first created.
     */

    private val VOICE_RESULTCODE = 2
    private val FILECHOOSER_RESULTCODE = 1
    private val PICK_FROM_CAMERA = 0
    protected val HTTP_STATUS_OK = 0
    private var phoneNumber: String? = null
    private val authToken: String? = null
    private var phoneVer: String? = null

    private val MAIN_URL = "file:///android_asset/www/html/index.html"
    private val SNS_URL = "http://e-ati-1.jeju.go.kr/"
    private val PUSH_URL = "http://e-ati-2.jeju.go.kr/sns/c2dm/pushCheck.do"

    lateinit var dbHelper: DBHelper
    val dbName = "SNS_USER.db"
    val dbVersion = 1


    private val TAG = "ASNSActivity"


    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

    // SharedPreferences에 저장할 때 key 값으로 사용됨.
    val PROPERTY_REG_ID = "INFO_PUSH_FARMSNS"

    private var appView: WebView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        //        super.onCreate(savedInstanceState);
        //        super.setIntegerProperty("splashscreen", R.drawable.loading);
        //        //super.setStringProperty("loadingDialog", "Wait,Loading .....");
        //        super.setBooleanProperty("loadInWebView", true);


        //        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        //      int  currentVersion  = getAppVersion(context);


        // mBackgroundThread = new BackgroundThread();
        //mBackgroundThread.start();


        //    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        출처@ http@ //fimtrus.tistory.com/entry/Android-Hybrid-JavascriptInterface-사용법 [Lv.Max 를 꿈꾸는 개발자 블로그]
        try {
            appView = findViewById(webview) as WebView
            appView!!.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            appView!!.loadUrl(MAIN_URL)
        } catch (e: Exception) {

        }


        //super.loadUrl(MAIN_URL, 6000);
        //        super.loadUrl(MAIN_URL, 6000);
        //super.lo
        this.act = this
        this.context = this

        this.dbHelper = DBHelper(this, dbName, null, dbVersion)
        //System.out.println("Build.VERSION_CODES.FROYO= 프로그램버전>>>"+Build.VERSION_CODES.FROYO);

        if (Build.VERSION_CODES.FROYO < Build.VERSION.SDK_INT) {
            //	System.out.println("버전체크 진저부터 가능");
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        // C2DM으로부터 Registration ID와 AuthToken을 발급받는다.
        try {
            //requestRegistrationId();

            if (checkPlayServices()) {
                gcm = GoogleCloudMessaging.getInstance(this)
                regid = getRegistrationId(act)

                if (regid.isEmpty()) {

                    println("regid==>" + regid)
                    registerInBackground()
                }
            } else {
                Log.i(TAG, "No valid Google Play Services APK found.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        backHandler = object : Handler() {
            fun handlerMessage(msg: Message) {
                if (msg.what == 0) {
                    mFlag = false
                }
            }
        }


        // Bridge 인스턴스 등록 //웹뷰 설정 제일 상단에 위치해야 정상 동작하는 것으로 보임.
        appView!!.addJavascriptInterface(AndroidBridge(), "ASNS")

        appView!!.settings.javaScriptEnabled = true
        appView!!.settings.javaScriptCanOpenWindowsAutomatically = true
        appView!!.settings.defaultTextEncodingName = "UTF-8"
        appView!!.settings.allowFileAccess = true
        //appView.getZoomControls();
        appView!!.settings.setSupportZoom(true)
        appView!!.settings.builtInZoomControls = true
        appView!!.settings.saveFormData = true

        appView!!.settings.domStorageEnabled = true
        appView!!.settings.databaseEnabled = true
        val databasePath = this.applicationContext.getDir("database",
                Context.MODE_PRIVATE).path
        appView!!.settings.databasePath = databasePath
        //        appView.setWebChromeClient(new WebChromeClient() {
        //            @Override
        //            public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize,
        //                                                long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
        //                quotaUpdater.updateQuota(estimatedSize * 2);
        //            }
        //        });


        appView!!.isFocusable = true


        //
        //        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        //        {
        //            if (0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE))
        //            { appView.setWebContentsDebuggingEnabled(true); }
        //        }

        // 전화번호 가져오기
        val mTelephonyMgr = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        phoneNumber = mTelephonyMgr.line1Number
        if (phoneNumber != null) {
            phoneNumber = phoneNumber!!.replace("+82", "0")
        }

        phoneVer = Build.VERSION.RELEASE
        Log.e("systemVersion", phoneVer)

        /** input type='file'  */
        appView!!.setWebChromeClient(MyWebChromeClient(this@ASNSActivity))


        mBackgroundThread = BackgroundThread()
        mBackgroundThread!!.start()


        //   this.setInstallInfomation();
    }

    //    @Override
    //    public boolean onKeyUp(int keyCode, KeyEvent event) {
    //
    //        Log.i("32sTag", appView.getUrl());
    //        if ((keyCode == KeyEvent.KEYCODE_BACK) && appView.getUrl().equals("file:///android_asset/www/html/index.html")) {
    //
    //            setDialog();
    //            return false;
    //
    //        }
    //        return super.onKeyUp(keyCode, event);
    //    }


    internal fun setDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.dialog_message)).setCancelable(false)
                .setPositiveButton(getString(R.string.dialog_positivebtn)) { dialog, id -> act.finish() }
                .setNegativeButton(getString(R.string.dialog_negativebtn), null)
        builder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        var result: Uri? = null

        if (null == uploadMessage) return

        if (requestCode == FILECHOOSER_RESULTCODE) {
            result = if (intent == null || resultCode != Activity.RESULT_OK) null else imageDevide(intent.data)
        } else if (requestCode == PICK_FROM_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {

                result = lastCaptureImageUri
            }
        } else if (requestCode == VOICE_RESULTCODE) {
            result = if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data

        }
        uploadMessage!!.onReceiveValue(result)
        uploadMessage = null

    }


    private fun imageDevide(data: Uri): Uri? {

        var uri: Uri? = null

        try {
            val bitmap = Images.Media.getBitmap(contentResolver, data)
            Log.e("32sTag", "(1)width, height = " + bitmap.width + " , " + bitmap.height)
            val height = bitmap.height / 2
            val width = bitmap.width / 2
            Log.e("32sTag", "(2)width, height = $width , $height")

            var resized: Bitmap? = null
            resized = Bitmap.createScaledBitmap(bitmap, width, height, true)

            val bytes = ByteArrayOutputStream()
            resized!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = Images.Media.insertImage(contentResolver, resized, "Title", null)
            uri = Uri.parse(path)

        } catch (e: Exception) {

        }


        return uri
    }

    private fun setInstallInfomation() {
        try {
            val _installation = Installation()
            val client = DefaultHttpClient()
            val postURL = PUSH_URL
            val post = HttpPost(postURL)
            val params = ArrayList<NameValuePair>()
            params.add(BasicNameValuePair("phoneNumber", phoneNumber))
            params.add(BasicNameValuePair("uuid", _installation.id(act.applicationContext)))
            params.add(BasicNameValuePair("phoneGubun", "android"))
            params.add(BasicNameValuePair("pushCode", regid))
            params.add(BasicNameValuePair("ver", Build.VERSION.RELEASE))
            val ent = UrlEncodedFormEntity(params, HTTP.UTF_8)
            post.entity = ent
            val responsePOST = client.execute(post)
            val resEntity = responsePOST.entity

            println("KEY:" + regid)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private //사진 회전정보 겟
            //카메라를 세로로 들고 찍었을 때 오른쪽으로 90도 회전시킴
    val lastCaptureImageUri: Uri?
        get() {
            var uri: Uri? = null
            val IMAGE_PROJECTION = arrayOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.ImageColumns._ID)

            try {
                val cursorImages = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, null)

                if (cursorImages != null && cursorImages.moveToLast()) {

                    val fileStr = "file://" + cursorImages.getString(0)
                    val imageUrl = cursorImages.getString(0)
                    cursorImages.close()

                    val fileUri = Uri.parse(fileStr)
                    val filePath = fileUri.path

                    val c = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data = '$filePath'", null, null)
                    c!!.moveToNext()

                    val id = c.getInt(0)

                    uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toLong())
                    val exif = ExifInterface(imageUrl)
                    val exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
                    Log.e("32sTag", "exifOrientation = " + exifOrientation)

                    val bitmap = Images.Media.getBitmap(contentResolver, uri)
                    Log.e("32sTag", "(1)width, height = " + bitmap.width + " , " + bitmap.height)
                    val height = bitmap.height / 2
                    val width = bitmap.width / 2
                    Log.e("32sTag", "(2)width, height = $width , $height")

                    var resized: Bitmap? = null
                    resized = Bitmap.createScaledBitmap(bitmap, width, height, true)
                    if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
                        resized = rotate(resized, 90)
                    }


                    val bytes = ByteArrayOutputStream()
                    resized!!.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                    val path = Images.Media.insertImage(contentResolver, resized, "Title", null)
                    uri = Uri.parse(path)


                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return uri
        }

    private fun rotate(bitmap: Bitmap?, degrees: Int): Bitmap? {
        var bitmap = bitmap
        if (degrees != 0 && bitmap != null) {
            val m = Matrix()
            m.setRotate(degrees.toFloat(), bitmap.width.toFloat() / 2,
                    bitmap.height.toFloat() / 2)

            try {
                val converted = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.width, bitmap.height, m, true)
                if (bitmap != converted) {
                    bitmap.recycle()
                    bitmap = converted
                }
            } catch (ex: OutOfMemoryError) {
                // 메모리가 부족하여 회전을 시키지 못할 경우 그냥 원본을 반환합니다.
            }

        }
        return bitmap
    }

    private fun getUriToPath(_uri: Uri): Uri? {
        var uri: Uri? = null
        val c = contentResolver.query(_uri, null, null, null, null)
        if (c!!.moveToNext()) {
            val path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA))
            uri = Uri.fromFile(File(path))
        }
        c.close()

        return uri
    }

    /*
     @Override
	 public boolean onKeyUp(int keyCode, KeyEvent event) {
		 if(appView.getUrl().indexOf(SNS_URL)>-1){
			 if (keyCode == KeyEvent.KEYCODE_BACK) {
				 return true;
			 }else{
				 return super.onKeyUp(keyCode, event);
			 }
		 }

		 return super.onKeyUp(keyCode, event);
	 }
	 */

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {

        val webUrl = appView!!.url
        //file:///android_asset/www/html/index.html

        var goBack = true

        if ("file:///android_asset/www/html/index.html" == webUrl) {
            goBack = false
        }


        Log.d(TAG, "onKeyDown: " + webUrl)
        if (keyCode == KeyEvent.KEYCODE_BACK && appView!!.canGoBack() && goBack) {
            appView!!.goBack()
            return true
        } else {


            setDialog()
            return false
        }

        //		return super.onKeyDown(keyCode, event);
    }


    inner class MyWebChromeClient(ctx: Context) : WebChromeClient() {
        private val ctx: DroidGap
        private val MAX_QUOTA = (100 * 1024 * 1024).toLong()


        init {
            this.ctx = ctx as DroidGap
        }

        fun openFileChooser(uploadMsg: ValueCallback<Uri>, acceptType: String, capture: String) {
            openFileChooser(uploadMsg, "")
        }

        fun openFileChooser(uploadFile: ValueCallback<Uri>, acceptType: String) {
            openFileChooser(uploadFile)
        }

        fun openFileChooser(uploadMsg: ValueCallback<Uri>) {
            uploadMessage = uploadMsg

            //final String items[] = { "카메라로 촬영하기", "갤러리에서 가져오기", "음성 녹음하기", "녹음파일 가져오기" };
            val items = arrayOf("카메라로 촬영하기", "갤러리에서 가져오기")
            AlertDialog.Builder(this@ASNSActivity).setIcon(R.drawable.ic_launcher).setSingleChoiceItems(items, -1) { dialog, item ->
                if (item == 0) {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    //intent.setComponent(new ComponentName("com.sec.android.app.camera", "com.sec.android.app.camera.Camera"));
                    startActivityForResult(intent, PICK_FROM_CAMERA)
                } else if (item == 1) {
                    val uri = Uri.parse("content://media/external/images/media")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    intent.action = Intent.ACTION_GET_CONTENT
                    intent.type = "image/*"
                    startActivityForResult(intent, FILECHOOSER_RESULTCODE)

                }

                /*
                    else if (item == 2) {
						Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
						startActivityForResult(intent, VOICE_RESULTCODE);
					} else if (item == 3) {
						Uri uri = Uri.parse("content://media/external/audios/media");
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						intent.setAction(Intent.ACTION_GET_CONTENT);
						intent.setType("audio/3gp");
						startActivityForResult(intent, VOICE_RESULTCODE);
					}
					*/

                dialog.dismiss()
            }
                    .setTitle("첨부파일 선택")
                    .setIcon(R.drawable.s_icon)
                    .setCancelable(false)
                    .show()
        }

        override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
            val dlg = AlertDialog.Builder(this.ctx)
            dlg.setMessage(message)
            dlg.setTitle("Alert")
            dlg.setCancelable(true)
            dlg.setPositiveButton(android.R.string.ok, {dialog,which->{result.confirm()}})

            dlg.setOnCancelListener { result.confirm() }

            dlg.setOnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    result.confirm()
                    false
                } else {
                    true
                }
            }

            dlg.create()
            dlg.show()

            return true
        }

        override fun onJsConfirm(view: WebView, url: String, message: String, result: JsResult): Boolean {
            val dlg = AlertDialog.Builder(this.ctx)
            dlg.setMessage(message)
            //dlg.setTitle("Confirm");
            dlg.setCancelable(true)
            dlg.setPositiveButton(android.R.string.ok) { dialog, which -> result.confirm() }
            dlg.setNegativeButton(android.R.string.cancel) { dialog, which -> result.cancel() }
            dlg.setOnCancelListener { result.cancel() }
            dlg.setOnKeyListener { dialog, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    result.cancel()
                    false
                } else {
                    true
                }
            }
            dlg.create()
            dlg.show()

            return true
        }

        override fun onJsPrompt(view: WebView, url: String, message: String, defaultValue: String, result: JsPromptResult): Boolean {
            val reqOk = false

            //            if (url.startsWith("file://") || isUrlWhiteListed(url)) {
            //                reqOk = true;
            //            }
            //
            //            if (reqOk && defaultValue != null && defaultValue.length() > 3 && defaultValue.substring(0, 4).equals("gap:")) {
            //                JSONArray array;
            //                try {
            //                    array = new JSONArray(defaultValue.substring(4));
            //                    String service = array.getString(0);
            //                    String action = array.getString(1);
            //                    String callbackId = array.getString(2);
            //                    boolean async = array.getBoolean(3);
            //                    String r = pluginManager.exec(service, action, callbackId, message, async);
            //                    result.confirm(r);
            //                } catch (JSONException e) {
            //                    e.printStackTrace();
            //                }
            //            } else if (reqOk && defaultValue != null && defaultValue.equals("gap_poll:")) {
            //                String r = callbackServer.getJavascript();
            //                result.confirm(r);
            //            } else if (reqOk && defaultValue != null && defaultValue.equals("gap_callbackServer:")) {
            //                String r = "";
            //                if (message.equals("usePolling")) {
            //                    r = "" + callbackServer.usePolling();
            //                } else if (message.equals("restartServer")) {
            //                    callbackServer.restartServer();
            //                } else if (message.equals("getPort")) {
            //                    r = Integer.toString(callbackServer.getPort());
            //                } else if (message.equals("getToken")) {
            //                    r = callbackServer.getToken();
            //                }
            //                result.confirm(r);
            //            } else if (reqOk && defaultValue != null && defaultValue.equals("gap_init:")) {
            //                appView.setVisibility(View.VISIBLE);
            //                ctx.spinnerStop();
            //                result.confirm("OK");
            //            } else {
            //                final JsPromptResult res = result;
            //                AlertDialog.Builder dlg = new AlertDialog.Builder(this.ctx);
            //                dlg.setMessage(message);
            //                final EditText input = new EditText(this.ctx);
            //                if (defaultValue != null) {
            //                    input.setText(defaultValue);
            //                }
            //                dlg.setView(input);
            //                dlg.setCancelable(false);
            //                dlg.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            //                    public void onClick(DialogInterface dialog, int which) {
            //                        String usertext = input.getText().toString();
            //                        res.confirm(usertext);
            //                    }
            //                });
            //
            //                dlg.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            //                    public void onClick(DialogInterface dialog, int which) {
            //                        res.cancel();
            //                    }
            //                });
            //                dlg.create();
            //                dlg.show();
            //            }

            return true
        }

        override fun onExceededDatabaseQuota(url: String, databaseIdentifier: String, currentQuota: Long, estimatedSize: Long, totalUsedQuota: Long, quotaUpdater: WebStorage.QuotaUpdater) {
            if (estimatedSize < MAX_QUOTA) {
                quotaUpdater.updateQuota(estimatedSize)
            } else {
                quotaUpdater.updateQuota(currentQuota)
            }
        }

        override fun onConsoleMessage(message: String, lineNumber: Int, sourceID: String) {
            super.onConsoleMessage(message, lineNumber, sourceID)
        }

        override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
            return super.onConsoleMessage(consoleMessage)
        }

        override fun onGeolocationPermissionsShowPrompt(origin: String, callback: Callback) {
            super.onGeolocationPermissionsShowPrompt(origin, callback)
            callback.invoke(origin, true, false)
        }
    }

    private inner class Installation {
        private var sID: String? = null
        private val INSTALLRATION = "INSTALLRATION"

        fun id(context: Context): String? {
            if (sID == null) {
                val installation = File(context.filesDir, INSTALLRATION)
                try {
                    if (!installation.exists()) {
                        this.writeInstallationFile(installation)
                    } else {
                        sID = this.readInstallationFile(installation)
                    }
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }

            }

            return sID
        }

        @Throws(IOException::class)
        private fun readInstallationFile(installation: File): String {
            val f = RandomAccessFile(installation, "r")
            val bytes = ByteArray(f.length().toInt())
            f.readFully(bytes)
            f.close()
            return String(bytes)
        }

        @Throws(IOException::class)
        private fun writeInstallationFile(installation: File) {
            val out = FileOutputStream(installation)
            val id = UUID.randomUUID().toString()
            out.write(id.toByteArray())
            out.close()
        }
    }

    // /////////////////////////////

    private inner class AndroidBridge {
        fun setMessage(arg: String) { // must be final
            handler.post { }
        }

        fun setFileDownloadUrl(file: String, fileName: String) {
            handler.post {
                // TODO Auto-generated method stub
                mProgressDialog = ProgressDialog(this@ASNSActivity)
                mProgressDialog!!.setMessage("File download")
                mProgressDialog!!.isIndeterminate = false
                mProgressDialog!!.max = 100
                mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)

                if (fileName.indexOf("3gp") > -1 || fileName.indexOf("m4a") > -1 || fileName.indexOf("amr") > -1 || fileName.indexOf("wav") > -1) {
                    val items = arrayOf("다운로드 후 재생", "바로 재생")
                    AlertDialog.Builder(this@ASNSActivity).setIcon(R.drawable.icon).setSingleChoiceItems(items, -1) { dialog, item ->
                        dialog.dismiss()
                        if (item == 0) {
                            // start a new download
                            val downloadFile = DownloadFile()
                            downloadFile.setFileName(fileName)
                            downloadFile.execute(SNS_URL + "upload/" + file)
                        } else if (item == 1) {
                            val mPlayer = MediaPlayer()
                            try {
                                mPlayer.setDataSource(SNS_URL + "upload/" + file)
                                mPlayer.prepare()
                                mPlayer.start()

                            } catch (e: IllegalArgumentException) {
                                // TODO Auto-generated catch block
                                e.printStackTrace()
                            } catch (e: SecurityException) {
                                // TODO Auto-generated catch block
                                e.printStackTrace()
                            } catch (e: IllegalStateException) {
                                // TODO Auto-generated catch block
                                e.printStackTrace()
                            } catch (e: IOException) {
                                // TODO Auto-generated catch block
                                e.printStackTrace()
                            }

                        }
                    }
                            .setTitle("음성파일")
                            .setIcon(R.drawable.s_icon)
                            .setCancelable(false)
                            .show()
                }
            }
        }

        fun getStartData() {
            handler.post {
                // TODO Auto-generated method stub
                appView!!.loadUrl("javascript:fn_setStartData('$phoneNumber', '$regid')")
            }
        }

        fun getPhoneNumber() {
            handler.post {
                // TODO Auto-generated method stub
                appView!!.loadUrl("javascript:fn_setPhoneNumber('$phoneNumber')")
            }
        }

        val phoneVer: String?
            get() = phoneVer

        fun getRegistrationId() {
            handler.post {
                // TODO Auto-generated method stub
                appView!!.loadUrl("javascript:fn_setRegistrationId('$regid')")
            }
        }

        fun getUUID() {
            handler.post {
                // TODO Auto-generated method stub
                val _installation = Installation()
                appView!!.loadUrl("javascript:fn_setUUID('" + _installation.id(act.applicationContext) + "')")
            }
        }

        fun getRegistInfo() {
            handler.post {
                // TODO Auto-generated method stub
                val _installation = Installation()
                appView!!.loadUrl("javascript:fn_setRegistInfo('" + regid + "', '" + _installation.id(act.applicationContext) + "')")
            }
        }

        fun getInstallInfo() {
            handler.post {
                // TODO Auto-generated method stub
                val _installation = Installation()
                appView!!.loadUrl("javascript:fn_setInstallInfo('" + _installation.id(act.applicationContext) + "', '" + regid + "', '" + phoneNumber + "', 'android', '" + Build.VERSION.SDK + "')")
            }
        }

        // url 이동
        fun setLoadUrl(url: String) {
            handler.post {
                // TODO Auto-generated method stub
                appView!!.loadUrl(url)
                // super.loadUrl(url);
            }
        }

        // login info save
        fun setLoginInfo(id: String, pw: String) {
            handler.post {
                val db: SQLiteDatabase
                var sql: String

                db = dbHelper.writableDatabase

                sql = "DELETE FROM SNS_USER"
                db.execSQL(sql)

                sql = String.format("INSERT INTO SNS_USER VALUES(NULL,'%S','%S')", id, pw)
                db.execSQL(sql)

                Log.d("SNSActivity", "id:" + id)
                Log.d("SNSActivity", "pw:" + pw)
            }
        }

        fun getLoginInfo() {
            handler.post {
                // TODO Auto-generated method stub

                Log.d("SNSActivity", "getLoginInfo")
                val db: SQLiteDatabase
                val sql: String

                var id = ""
                var pw = ""

                db = dbHelper.writableDatabase
                sql = "SELECT SNS_ID, SNS_PW FROM SNS_USER"

                val cursor = db.rawQuery(sql, null)

                if (cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        id = cursor.getString(0)
                        pw = cursor.getString(1)
                    }
                }

                cursor.close()

                Log.d("SNSActivity", "id:" + id)
                Log.d("SNSActivity", "pw:" + pw)
                appView!!.loadUrl("javascript:fn_setLoginInfo('$id', '$pw')")
            }
        }

        // public void viewPDF(final String url) {
        // handler.post(new Runnable() {
        // public void run() {
        // // TODO Auto-generated method stub
        // //appView.loadUrl(url);
        //
        // String tmpUrl ="" ;
        //
        // if (url.endsWith(".pdf")) {
        //
        // try {
        // tmpUrl = URLEncoder.encode(url, "utf-8");
        // } catch (UnsupportedEncodingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // String googleDocs = "https://docs.google.com/viewer?url=";
        // Intent intent = new Intent(Intent.ACTION_VIEW);
        // intent.setDataAndType(Uri.parse(url), "application/pdf");
        // // startActivity(intent);
        //
        // appView.loadUrl(googleDocs + tmpUrl);
        // }
        // // super.loadUrl(url);
        // }
        //
        // });
        // }

        fun viewInfoPDF(url: String) {

            mProgressDialog = ProgressDialog(this@ASNSActivity)
            mProgressDialog!!.setMessage("File download")
            mProgressDialog!!.isIndeterminate = false
            mProgressDialog!!.max = 100
            mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)

//String url2 ="http://www.agri.jeju.kr/common/board/result/download.wiz?pkCode=207724";
            val downloadFile = DownloadFileDoc()
            downloadFile.setFileName(url)
            downloadFile.setAct(act)
            downloadFile.execute(url)


            val intent: Intent
            intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.parse(url), "application/pdf")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // No application to view, ask to download one
                val builder = AlertDialog.Builder(act)
                builder.setTitle("No Application Found")
                builder.setMessage("Download one from Android Market?")
                builder.setPositiveButton("Yes, Please") { dialog, which ->
                    val marketIntent = Intent(Intent.ACTION_VIEW)
                    marketIntent.data = Uri.parse("market://details?id=com.adobe.reader")
                    startActivity(marketIntent)
                }
                builder.setNegativeButton("No, Thanks", null)
                builder.create().show()
            }

        }

        fun viewPDF(url: String) {

            mProgressDialog = ProgressDialog(this@ASNSActivity)
            mProgressDialog!!.setMessage("File download")
            mProgressDialog!!.isIndeterminate = false
            mProgressDialog!!.max = 100
            mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)

            val fileName = "1.pdf"
            //String url2 ="http://www.agri.jeju.kr/common/board/result/download.wiz?pkCode=207724";
            val downloadFile = DownloadFileDoc()
            downloadFile.setFileName(fileName)
            downloadFile.setAct(act)
            downloadFile.execute(url)


            //			Intent intent;
            //			intent = new Intent(Intent.ACTION_VIEW);
            //			intent.setDataAndType(Uri.parse(url), "application/pdf");
            //			try {
            //				startActivity(intent);
            //			} catch (ActivityNotFoundException e) {
            //				// No application to view, ask to download one
            //				AlertDialog.Builder builder = new AlertDialog.Builder(act);
            //				builder.setTitle("No Application Found");
            //				builder.setMessage("Download one from Android Market?");
            //				builder.setPositiveButton("Yes, Please", new DialogInterface.OnClickListener() {
            //					@Override
            //					public void onClick(DialogInterface dialog, int which) {
            //						Intent marketIntent = new Intent(Intent.ACTION_VIEW);
            //						marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
            //						startActivity(marketIntent);
            //					}
            //				});
            //				builder.setNegativeButton("No, Thanks", null);
            //				builder.create().show();
            //			}
        }

        fun callPopUp(anounceMention: String) {
            handler.post {
                val alt_bld = AlertDialog.Builder(act)
                alt_bld.setMessage(anounceMention).setCancelable(
                        false).setPositiveButton("확인"
                ) { dialog, id ->
                    // Action for 'Yes' Button
                }
                val alert = alt_bld.create()
                // Title for AlertDialog
                alert.setTitle("공지사항")
                // Icon for AlertDialog
                //alert.setIcon(R.drawable.icon);
                alert.show()
            }
        }

    }


    class DBHelper(context: Context, name: String, factory: CursorFactory?,
                                  version: Int) : SQLiteOpenHelper(context, name, factory, version) {

        override fun onCreate(db: SQLiteDatabase) {
            val SQL = "CREATE TABLE SNS_USER (_ID INTEGER PRIMARY KEY AUTOINCREMENT, SNS_ID TEXT, SNS_PW TEXT)"
            db.execSQL(SQL)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS SNS_USER")
            onCreate(db)
        }

    }


    private fun checkPlayServices(): Boolean {
        println("** checkPlayServices **")

        val resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show()

                Log.i(TAG, "This device go ")

            } else {
                Log.i(TAG, "This device is not supported.")
                finish()
            }
            return false
        }
        return true
    }


    private fun sendRegistrationIdToBackend() {
        println("** sendRegistrationIdToBackend **")
        setInstallInfomation()
    }

    private fun getRegistrationId(context: Context): String {
        val prefs = getGCMPreferences(context)
        val registrationId = prefs.getString(PROPERTY_REG_ID, "")

        Log.i(TAG, "registrationId:" + registrationId!!)

        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.")
            return ""
        }

        // 앱이 업데이트 되었는지 확인하고, 업데이트 되었다면 기존 등록 아이디를 제거한다.
        // 새로운 버전에서도 기존 등록 아이디가 정상적으로 동작하는지를 보장할 수 없기 때문이다.
        val registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE)
        val currentVersion = getAppVersion(context)

        println("** registeredVersion - currentVersion:$registeredVersion/$currentVersion")


        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.")
            return ""
        }
        return registrationId
    }

    private fun getGCMPreferences(context: Context): SharedPreferences {
        return getSharedPreferences(ASNSActivity::class.java.simpleName, Context.MODE_PRIVATE)
    }

    private fun storeRegistrationId(context: Context, regid: String) {


        println("** storeRegistrationId **")


        val prefs = getGCMPreferences(context)
        val appVersion = getAppVersion(context)
        //   int appVersion = 1;
        Log.i(TAG, "Saving regId on app version " + appVersion)
        val editor = prefs.edit()
        editor.putString(PROPERTY_REG_ID, regid)
        editor.putInt(PROPERTY_APP_VERSION, appVersion)
        editor.commit()
    }


    override fun onResume() {
        super.onResume()
        checkPlayServices()
    }


    private fun registerInBackground() {
        object : AsyncTask<Void, Void, String>() {


            override fun doInBackground(vararg params: Void): String {
                var msg = ""


                println("registerInBackground: restart")
                println("SENDER_ID:" + SENDER_ID)


                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context)
                    }
                    regid = gcm!!.register(SENDER_ID)
                    msg = "Device registered, registration ID=" + regid

                    // 서버에 발급받은 등록 아이디를 전송한다.
                    // 등록 아이디는 서버에서 앱에 푸쉬 메시지를 전송할 때 사용된다.
                    sendRegistrationIdToBackend()


                    println("PUSH:" + regid)
                    // 등록 아이디를 저장해 등록 아이디를 매번 받지 않도록 한다.
                    storeRegistrationId(context, regid)
                } catch (ex: IOException) {
                    msg = "Error :" + ex.message
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }

                return msg
            }

            override fun onPostExecute(msg: String) {
                // progressDialog.dismiss();
            }


        }.execute(null, null, null)
    }


    inner class BackgroundThread : Thread() {
        override fun run() {

            // 패키지 네임 전달
            storeVersion = MarketVersionChecker.getMarketVersion(packageName)

            println("storeVersion:" + storeVersion!!)


            // 디바이스 버전 가져옴
            try {
                deviceVersion = packageManager.getPackageInfo(packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            deviceVersionCheckHandler.sendMessage(deviceVersionCheckHandler.obtainMessage())
            // 핸들러로 메세지 전달
        }
    }

    private val deviceVersionCheckHandler = DeviceVersionCheckHandler(this)

    // 핸들러 객체 만들기
    private class DeviceVersionCheckHandler(mainActivity: ASNSActivity) : Handler() {
        private val mainActivityWeakReference: WeakReference<ASNSActivity>

        init {
            mainActivityWeakReference = WeakReference(mainActivity)
        }

        override fun handleMessage(msg: Message) {
            val activity = mainActivityWeakReference.get()
            activity?.handleMessage(msg)
        }
    }

    private fun handleMessage(msg: Message) {
        //핸들러에서 넘어온 값

        println("storeVersion:$storeVersion,deviceVersion:$deviceVersion")

        if (storeVersion!!.compareTo(deviceVersion) > 0) {
            // 업데이트 필요

            val alertDialogBuilder = AlertDialog.Builder(ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light))
            alertDialogBuilder.setTitle("업데이트")
            alertDialogBuilder
                    .setMessage("새로운 버전이 있습니다.\n보다 나은 사용을 위해 업데이트 해 주세요.")
                    .setPositiveButton("업데이트 바로가기") { dialog, which ->
                        // 구글플레이 업데이트 링크


                        // 버튼 이벤트 내 Intent를 생성하여 Intent.ACTION_VIEW 액션을 취해준 뒤, url을 넣어줌

                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=kr.co.infomind.app.farmsns&hl=ko"))
                        startActivity(intent)
                    }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.setCanceledOnTouchOutside(true)
            alertDialog.show()

        } else {
            // 업데이트 불필요

        }
    }

    companion object {

        var mProgressDialog: ProgressDialog? = null

        // SharedPreferences에 저장할 때 key 값으로 사용됨.
        private val PROPERTY_APP_VERSION = "INFO_PUSH_FARMSNS_ver"

        private fun getAppVersion(context: Context): Int {
            try {
                val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                return packageInfo.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                // should never happen
                throw RuntimeException("Could not get package name: " + e)
            }

        }
    }


}