package com.ivtolkachev.fbfriendslistapp.net;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

public class FacebookLoader {
	
	private static final String TAG = "FacebookLoaderTag";
	
	private static Context context;
	private static Exception exception;
	private static List<AsyncTask<?,?,?>> tasksList = new ArrayList<AsyncTask<?,?,?>>();
	
	public static void loadMeProfilePicture(Context ctx, Session session, Request.Callback callback){
		context = ctx;
		int size = context.getResources().getDimensionPixelSize(
				com.facebook.android.R.dimen.com_facebook_profilepictureview_preset_size_large);
		Bundle parameters = new Bundle();
		parameters.putInt("width", size);
		parameters.putInt("height", size);
		String graphPath= "me/picture";
		Request request = new Request(session, graphPath, parameters, HttpMethod.GET, callback);
		Request.executeBatchAsync(request);
	}
	
	public static String parsMeProfilePictureResponce(Response response){
		String imageUrl = null;
		GraphObject graphObject = response.getGraphObject();
		if (graphObject == null) {
			exception = new Exception("Response doesn't contain url.");
		} else {
			JSONObject jsonObject = graphObject.getInnerJSONObject();
			if (jsonObject.has(Response.NON_JSON_RESPONSE_PROPERTY)){
				try {
					//byte[] urlInByteArray = jsonObject.getString(Response.NON_JSON_RESPONSE_PROPERTY).getBytes("UTF-8");
					//imageUrl = new String(urlInByteArray);
					imageUrl = jsonObject.getString(Response.NON_JSON_RESPONSE_PROPERTY);
					Log.d(TAG, "url = " + imageUrl);
				} catch (JSONException e) {
					exception = e;
				} /*catch (UnsupportedEncodingException e) {
					exception = e;
				}*/
			}
		}
		return imageUrl;
	}
	
	public static void loadImage(Context ctx, Response response, final Callback callback){
		String urlString = parsMeProfilePictureResponce(response);
		if (urlString == null && exception != null){
			callback.onError(exception);
		} else {
			loadImage(ctx, urlString, callback);
		}
	}
	
	public static void loadImage(Context ctx, String urlString, final Callback callback){
		context = ctx;
		tasksList.add(new AsyncTask<String, Void, Bitmap>(){

			private Exception ex;
			
			@Override
			protected Bitmap doInBackground(String... params) {
				Bitmap bmp = null;
				try {
					bmp = loadImageByUrl(params[0]);
				} catch (Exception e) {
					ex = e;
				}
				return bmp;
			}
			
			@Override
			protected void onPostExecute(Bitmap result) {
				if (context != null){
					if (ex != null) {
						callback.onError(ex);
					} else {
						callback.onComplate(result);
					}
				}
			};
			
			@Override
			protected void onCancelled() {
				if (context != null) {
					callback.onCancel();
				}
			};
			
		}.execute(urlString));
	}
	
	private static Bitmap loadImageByUrl(String urlString) throws Exception {
		Bitmap bitmap = null;
		InputStream in = null;
		in = openHttpConnection(urlString);
		if (in != null){
			BitmapFactory.Options bmOptions;
		    bmOptions = new BitmapFactory.Options(); 
			bitmap = BitmapFactory.decodeStream(in, null, bmOptions);
	        in.close();
		}
		return bitmap;
	}
	
	private static InputStream openHttpConnection(String urlStr) throws Exception {
		InputStream inputStream = null;
		URL url = new URL(urlStr);
		URLConnection conn = url.openConnection();
		HttpURLConnection httpConn = (HttpURLConnection)conn;
		httpConn.setRequestMethod("GET");
		httpConn.connect();
		if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			inputStream = httpConn.getInputStream();
		}
		return inputStream;
	}
	
	public static void cancelAllTasks(){
		for (AsyncTask<?, ?, ?> task : tasksList) {
			if (!task.isCancelled()){
				task.cancel(true);
			}
		}
	}
	
	public interface Callback {
		
		public void onComplate(Bitmap bitmap);
		
		public void onError(Exception exception);
		
		public void onCancel();
		
	}
	
}
