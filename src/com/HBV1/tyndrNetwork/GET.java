package com.HBV1.tyndrNetwork;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.util.Log;

/*
 * @author: Tomas Karl Kjartansson<tkk4@hi.is>
 * @version: 0.1
 * @since: 2014-10-15
 * 
 * tekur vid streng sem skilgreinir umfang leitar.
 */

public class GET extends AsyncTask<String,Void,String>
{

	@Override
	protected String doInBackground(String... arg0) {
		StringBuilder builder = new StringBuilder();

		HttpClient tenging = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet("http://tyndr.herokuapp.com/api/adverts?filter[where]"+arg0[0]);
		
		
		try {
		      HttpResponse response = tenging.execute(httpGet);
		      StatusLine statusLine = response.getStatusLine();
		      int statusCode = statusLine.getStatusCode();
		      if (statusCode == 200) {
		        HttpEntity entity = response.getEntity();
		        InputStream content = entity.getContent();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
		        String line;
		        while ((line = reader.readLine()) != null) {
		          builder.append(line);
		        }
		      } else {
		        Log.d("GET", "Failed to download file");
		      }
		    } catch (ClientProtocolException e) {
		      e.printStackTrace();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
			//Adds.setText(builder.toString());
		    return builder.toString();
		  }
}
