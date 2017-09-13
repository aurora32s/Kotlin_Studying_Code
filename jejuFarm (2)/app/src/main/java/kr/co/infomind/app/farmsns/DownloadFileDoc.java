package kr.co.infomind.app.farmsns;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;


public class DownloadFileDoc extends AsyncTask<String, Integer, String> {

	private String DownFileName = "";
	public Activity act;
	@Override     
	protected void onPreExecute() {         
		super.onPreExecute();         
		ASNSActivity.Companion.getMProgressDialog().show();
	} 
	
	@Override     
	protected void onProgressUpdate(Integer... progress) {         
		super.onProgressUpdate(progress);         
		ASNSActivity.Companion.getMProgressDialog().setProgress(progress[0]);
	}
	
	@Override      
	protected void onPostExecute (String result){       
		super.onPostExecute(result);       
		ASNSActivity.Companion.getMProgressDialog().dismiss();
		ASNSActivity.Companion.setMProgressDialog(null);
	}
	 
	@Override
	protected String doInBackground(String... sUrl) {
		
		// TODO Auto-generated method stub
		try { 
			 
			
			URL url = new URL(sUrl[0]); 
			
			//URLConnection connection = url.openConnection();             
			File myDir = new File(Environment.getExternalStorageDirectory() + "/ASNS"); 
			// create the directory if it doesnt exist             
			if (!myDir.exists()) myDir.mkdirs();             
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 
			
			 //Follow redirects so as some sites redirect to the file location     
			 // connection.setRequestProperty("Accept-Charset","euc-kr");
			connection.setInstanceFollowRedirects(true);                     
			connection.setDoOutput(true);  
			connection.connect(); 
			
			File outputFile     =   new File(myDir,this.DownFileName); 
			Log.e("outputFile", outputFile.getPath());
			// this will be useful so that you can show a typical 0-100%             
			// progress bar             
			int fileLength      = connection.getContentLength(); 
			
			// download the file             
			InputStream input   = new BufferedInputStream(url.openStream());       
			 
			OutputStream output = new FileOutputStream(outputFile); 
			
			
			
			 byte data[] = new byte[1024];             
			 long total = 0;             
			 int count;
			 
			 while ((count = input.read(data)) != -1) {                  
				 total += count;                 
				 // publishing the progress....                 
				 publishProgress((int) (total * 100 / fileLength));                 
				 output.write(data, 0, count);             
			 }
			 
			 connection.disconnect();             
			 output.flush();             
			 output.close();             
			 input.close();              
			 
			 MediaPlayer mPlayer = new MediaPlayer();
			try {
				Intent intent;
				intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(outputFile), "application/pdf");
				try {
					act.startActivity(intent);
				} catch (ActivityNotFoundException e) {
					// No application to view, ask to download one
					AlertDialog.Builder builder = new AlertDialog.Builder(act);
					builder.setTitle("No Application Found");
					builder.setMessage("Download one from Android Market?");
					builder.setPositiveButton("Yes, Please", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent marketIntent = new Intent(Intent.ACTION_VIEW);
							marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
							act.startActivity(marketIntent);
						}
					});
					builder.setNegativeButton("No, Thanks", null);
					builder.create().show();
				}
				
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 
			}
				
			 Log.e("File download", "complete");
		} catch (Exception e) { 
			Log.e("File download", "error: " + e.getMessage());          
		} 
		return null;
	} 
	
	public void setFileName(String fileName){
		this.DownFileName = fileName;
	}

	public Activity getAct() {
		return act;
	}

	public void setAct(Activity act) {
		this.act = act;
	}
	

}
