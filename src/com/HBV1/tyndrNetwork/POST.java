package com.HBV1.tyndrNetwork;

import java.io.IOException;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.tyndr_server.tyndr.Tyndr;
import com.appspot.tyndr_server.tyndr.model.MessagesCreateAdvertMessage;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

/*
 * @author: Tomas Karl Kjartansson<tkk4@hi.is>
 * @version: 0.1
 * @since: 2014-10-15
 * 
 * tekur vid streng sem skilgreinir slod a serverinn sem senda skal a.
 */


public class POST extends AsyncTask<String, Void, Void>
{
	public static final JsonFactory jarvis = new AndroidJsonFactory();
	public static final HttpTransport hoppy = AndroidHttp.newCompatibleTransport();
	Tyndr.Builder tommi = new Tyndr.Builder(hoppy, jarvis, null);
	
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
		tommi.setApplicationName("Tyndr");
		Tyndr hallo = tommi.build();
		
		MessagesCreateAdvertMessage newAdd = new MessagesCreateAdvertMessage();
		

		
		try {
			newAdd.setAge((long) 10);
			newAdd.setDescription((String) msg.get("description"));
			newAdd.setName(msg.getString("name"));
			newAdd.setColor(msg.getString("color"));
			newAdd.setSpecies(msg.getString("species"));
			newAdd.setSubspecies(msg.getString("subspecies"));
			if(msg.getBoolean("lost"))
				newAdd.setLabel("lost_pets");
			else
				newAdd.setLabel("found_pets");

			hallo.advert().create(newAdd).execute();
			
			Log.d("bla", "sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	} 
}