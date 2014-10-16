package com.HBV1.tyndrNetwork;

import java.io.InputStream;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

/*
 * @author: Tomas Karl Kjartansson<tkk4@hi.is>
 * @version: 0.1
 * @since: 2014-10-15
 * 
 * tekur vid streng sem skilgreinir slod a serverinn sem senda skal a.
 */


public class POST extends AsyncTask<String, Void, Void>
{
	private JSONObject msg; // skilabodin sem a ad senda
	/*
	 * byr til nytt instance ad POST
	 * 
	 * @param json hlutur sem inniheldur skilabodin sem a ad senda
	 */
	public POST(JSONObject message)
	{
		msg = message;
	}

	@Override
	protected Void doInBackground(String... urls) {
		InputStream inputStream = null;
		StringEntity se = null;
		try
		{
		HttpClient tenging = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(urls[0]);
		
		se = new StringEntity(msg.toString(), "UTF-8");
        se.setContentType("application/json; charset=UTF-8");
		
		httpPost.setEntity(se);
		
        HttpResponse httpResponse = tenging.execute(httpPost);
        inputStream = httpResponse.getEntity().getContent();

		} catch (Exception e){
			Log.d("http", "error Message: " +e);
		}
		
		return null;	
	} 
}