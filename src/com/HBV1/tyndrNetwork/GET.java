package com.HBV1.tyndrNetwork;

import java.io.IOException;
import java.util.ArrayList;

import android.os.AsyncTask;

import com.appspot.tyndr_server.tyndr.Tyndr;
import com.appspot.tyndr_server.tyndr.model.MessagesAdvertMessage;
import com.appspot.tyndr_server.tyndr.model.MessagesAdvertMessageCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

/**
 * @author: Tomas Karl Kjartansson<tkk4@hi.is>
 * @version: 0.1
 * @since: 2014-10-15
 * 
 * tekur vid streng sem skilgreinir umfang leitar.
 */
public class GET extends AsyncTask<String,Void,String> {
	public static final JsonFactory jarvis = new AndroidJsonFactory();
	public static final HttpTransport hoppy = AndroidHttp.newCompatibleTransport();
	Tyndr.Builder tommi = new Tyndr.Builder(hoppy, jarvis, null);
	
	/**
	 * Saekir gogn fra server og setur i json streng og skilar
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected String doInBackground(String... args) {
		tommi.setApplicationName("Tyndr");
		Tyndr hallo = tommi.build();
		MessagesAdvertMessageCollection guh = null;
		try {
			guh = hallo.advert().all(10).setLabel(args[0]).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		StringBuilder builder = new StringBuilder();
		ArrayList<MessagesAdvertMessage> heh = (ArrayList<MessagesAdvertMessage>) guh.get("items");
		for (int i=0; i<heh.size(); i++) {
			if (i!=0) {
				builder.append(","+heh.get(i).toString());
			} else {
				builder.append(heh.get(i).toString());
			}
		}
		return "["+builder.toString()+"]";
	}
}
