package com.HBV1.tyndr;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.HBV1.tyndrNetwork.GET;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

/*
 * @author: Tomas Karl Kjartansson<tkk4@hi.is>
 * @version: 0.1
 * @since: 2014-10-15
 */
public class Adds extends Activity {

	private Spinner filterSpinner; //Spinner hlutur
	private String auglysingar; // geymir svarid fra servernum.
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adds);
		
		filterSpinner = (Spinner) findViewById(R.id.filter);
		upphafsstilla();
		
		
		try {
			auglysingar = new GET().execute("").get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fyllaLista();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.adds, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * uphafsstillir spinner hlut og setur hlustara a hann.
	 * 
	 * tekur hvorki vid ne skilar neinu.
	 */
	
	public void upphafsstilla()
	{
		List<String> tegundir = new ArrayList<String>();
		tegundir.add("Allar");
		tegundir.add("Tynd");
		tegundir.add("fundin"); 
		
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tegundir);
		adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		filterSpinner.setAdapter(adapt);
		
		filterSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				filter(arg2);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}
	
	/*
	 * Gerir mismunandi koll a serverinn eftir gildi id.
	 * 
	 * @param: id saetistala valkostar i spinner hlut stjornar thvi hvada adgerd er valin.
	 * 
	 */
	
	public void filter(int id)
	{
		switch(id)
		{
		case 0:
			try {
				auglysingar = new GET().execute("").get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fyllaLista();
				break;
				
		case 1:
			try {
				auglysingar = new GET().execute("[lost]=true").get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fyllaLista();
			break;
			
		case 2:
			try {
				auglysingar = new GET().execute("[lost]=false").get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fyllaLista();
			break;
		}
	}
		
	/*
	 * Fallid fyllir a og birtir listann sem auglysingarnar eru syndar a. 
	 * tekur hvorki vid ne skilar neinu.
	 */
	void fyllaLista() 
	{
		List<String> Nofn = new ArrayList<String>();
		JSONArray jerry;
		try {
			jerry = new JSONArray(auglysingar);
			for(int i = 0; i<jerry.length();i++)
			{
				JSONObject temp = (JSONObject) jerry.get(i);
				Nofn.add(temp.getString("name").toString());
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, Nofn);
		ListView list = (ListView) findViewById(R.id.Listi);
		list.setAdapter(adapter); 
	}
}
