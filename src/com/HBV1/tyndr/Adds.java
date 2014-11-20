package com.HBV1.tyndr;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.HBV1.tyndrNetwork.GET;

/*
 * @author: Tomas Karl Kjartansson<tkk4@hi.is>
 * @version: 0.1
 * @since: 2014-10-15
 */
public class Adds extends Activity {

	private Spinner filterSpinner; //Spinner hlutur
	private String auglysingar; // geymir svarid fra servernum.
	private DrawerNavigator drawerNavigator;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pet_list);
		drawerNavigator = new DrawerNavigator(this);
		drawerNavigator.setFinishActivity(false);
		
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
		if (drawerNavigator.openItem(item)) {
			return true;
		}
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
		
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.spinner_item, tegundir);
		adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		filterSpinner.setAdapter(adapt);
		
		filterSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1 ,int arg2, long arg3) {
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
		List<Pet> pets = new ArrayList<Pet>();
		try {
			JSONArray jerry = new JSONArray(auglysingar);

			for(int i = 0; i<jerry.length();i++)
			{
				JSONObject newPet = (JSONObject) jerry.get(i);
				pets.add( new Pet(
						newPet.getString("name"),
						"omglol",
//						newPet.getString("location"),
						newPet.getString("description")));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ViewGroup insertPoint = (ViewGroup) findViewById(R.id.lost_pet_list);
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Random rand = new Random();
		for (int i=0; i<pets.size(); i++) {
			View view = inflater.inflate(R.layout.pet_list_item, insertPoint, false);
			TextView description = (TextView) view.findViewById(R.id.lost_pet_description);
			TextView name = (TextView) view.findViewById(R.id.lost_pet_name);
			TextView location = (TextView) view.findViewById(R.id.lost_pet_location);
			ImageView image = (ImageView) view.findViewById(R.id.lost_pet_pic);
			name.setText(pets.get(i).name);
			location.setText(pets.get(i).location);
			description.setText(pets.get(i).description);
			switch (rand.nextInt(5)) {
			case 0: image.setImageDrawable(getResources().getDrawable(R.drawable.cat)); break;
			case 1: image.setImageDrawable(getResources().getDrawable(R.drawable.dog)); break;
			case 2: image.setImageDrawable(getResources().getDrawable(R.drawable.mouse)); break;
			case 3: image.setImageDrawable(getResources().getDrawable(R.drawable.spider)); break;
			case 4: image.setImageDrawable(getResources().getDrawable(R.drawable.horse)); break;
			}
			insertPoint.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		}
	}
}
