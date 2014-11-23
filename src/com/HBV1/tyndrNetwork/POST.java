package com.HBV1.tyndrNetwork;

import java.io.IOException;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.tyndr_server.tyndr.Tyndr;
import com.appspot.tyndr_server.tyndr.model.MessagesAdvertMessage;
import com.appspot.tyndr_server.tyndr.model.MessagesCreateAdvertMessage;
import com.appspot.tyndr_server.tyndr.model.MessagesStatusMessage;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
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
	Tyndr.Builder tommi;
	
	private MessagesCreateAdvertMessage msg; // skilabodin sem a ad senda
	/*
	 * byr til nytt instance ad POST
	 * MessagesCreateAdvertMessage message,
	 * @param json hlutur sem inniheldur skilabodin sem a ad senda
	 */
	public POST(MessagesCreateAdvertMessage message, GoogleAccountCredential cred)
	{
		msg = message;
		tommi = new Tyndr.Builder(hoppy, jarvis, cred);
	}

	@Override
	protected Void doInBackground(String... urls) {
		Tyndr hallo = tommi.build();
		
		
		try {
			hallo.advert().create(msg).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("blabla", "virkar ekki");
		}
			
		
		return null;
	} 
}