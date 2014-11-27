package com.HBV1.tyndrNetwork;

import java.io.IOException;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.util.Log;

import com.appspot.tyndr_server.tyndr.Tyndr;
import com.appspot.tyndr_server.tyndr.model.MessagesAdvertMessage;
import com.appspot.tyndr_server.tyndr.model.MessagesAdvertMessageCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

public class MINE extends AsyncTask<String,Void,String>
{
	public static final JsonFactory jarvis = new AndroidJsonFactory();
	public static final HttpTransport hoppy = AndroidHttp.newCompatibleTransport();
	Tyndr.Builder tommi;
	public MINE(GoogleAccountCredential cred)
	{
		tommi = new Tyndr.Builder(hoppy, jarvis, cred);
	}


	@SuppressWarnings("unchecked")
	@Override
	protected String doInBackground(String... args) {
		Tyndr hallo = tommi.build();
		MessagesAdvertMessageCollection guh = null;
		Log.d("bla","1");
		try {
			guh = hallo.advert().all(10).setLabel("lost_pets").execute();
			Log.d("bla","2");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder builder = new StringBuilder();
		Log.d("bla","3");

		ArrayList<MessagesAdvertMessage> heh = (ArrayList<MessagesAdvertMessage>) guh.get("items");
		for (int i=0; i<heh.size(); i++) {
			if(heh.get(i).getMine()){
				Log.d("bla","4");
				if (i!=0) {
					builder.append(","+heh.get(i).toString());
				} else {
					builder.append(heh.get(i).toString());
				}
			}
		}
		String jo = "["+builder.toString()+"]";

		return jo;
	}
}

