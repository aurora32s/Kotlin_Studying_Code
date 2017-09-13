package kr.co.infomind.app.farmsns

import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Environment
import android.util.Log


class DownloadFile : AsyncTask<String, Int, String>() {

    private var DownFileName = ""

    override fun onPreExecute() {
        super.onPreExecute()
        ASNSActivity.mProgressDialog!!.show()
    }

    protected override fun onProgressUpdate(vararg progress: Int?) {
        super.onProgressUpdate(*progress)
        ASNSActivity.mProgressDialog!!.progress = progress[0] as Int
    }

    override fun onPostExecute(result: String) {
        super.onPostExecute(result)
        ASNSActivity.mProgressDialog!!.dismiss()
        ASNSActivity.mProgressDialog = null
    }

    override fun doInBackground(vararg sUrl: String): String? {

        // TODO Auto-generated method stub
        try {
            val url = URL(sUrl[0])
            //URLConnection connection = url.openConnection();
            val myDir = File(Environment.getExternalStorageDirectory().toString() + "/ASNS")
            // create the directory if it doesnt exist
            if (!myDir.exists()) myDir.mkdirs()

            val connection = url.openConnection() as HttpURLConnection

            //Follow redirects so as some sites redirect to the file location
            connection.instanceFollowRedirects = true
            connection.doOutput = true
            connection.connect()

            val outputFile = File(myDir, this.DownFileName)
            Log.e("outputFile", outputFile.path)
            // this will be useful so that you can show a typical 0-100%
            // progress bar
            val fileLength = connection.contentLength

            // download the file
            val input = BufferedInputStream(url.openStream())
            val output = FileOutputStream(outputFile)

            val data = ByteArray(1024)
            var total: Long = 0
            var count: Int = input.read(data)

            while (count != -1) {
                total += count.toLong()
                // publishing the progress....
                publishProgress((total * 100 / fileLength).toInt())
                output.write(data, 0, count)
                count = input.read(data)
            }

            connection.disconnect()
            output.flush()
            output.close()
            input.close()

            val mPlayer = MediaPlayer()
            try {
                mPlayer.setDataSource(outputFile.path)
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

            Log.e("File download", "complete")
        } catch (e: Exception) {
            Log.e("File download", "error: " + e.message)
        }

        return null
    }

    fun setFileName(fileName: String) {
        this.DownFileName = fileName
    }

}
