package com.HBV1.tyndrNetwork;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

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
		StringEntity se = null;
		try
		{
		HttpPost httpPost = new HttpPost(urls[0]);
		
		se = new StringEntity(msg.toString(), "UTF-8");
        se.setContentType("application/json; charset=UTF-8");
		
		httpPost.setEntity(se);

		} catch (Exception e){
			Log.d("http", "error Message: " +e);
		}
		
		return null;	
	} 
}