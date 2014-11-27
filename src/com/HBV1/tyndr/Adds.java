package com.HBV1.tyndr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
	private AdvertDetailPopup advertDetailPopup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pet_list);
		drawerNavigator = new DrawerNavigator(this);
		drawerNavigator.setFinishActivity(true);
		filterSpinner = (Spinner) findViewById(R.id.filter);
		advertDetailPopup = new AdvertDetailPopup(this);
		upphafsstilla();
		getFromServerAndFillList("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.adds, menu);
		return true;
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerNavigator.openItem(item)) {
			return true;
		}
        if (item.getItemId() == R.id.action_settings) {
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
	
	public void filter(int id) {
		switch(id) {
		case 0: getFromServerAndFillList("lost_pets"); break;
		case 1: getFromServerAndFillList("found_pets");
		}
	}
	
	private void getFromServerAndFillList(String label) {
		try {
			auglysingar = new GET().execute(label).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		fyllaLista();
	}
		
	/*
	 * Fallid fyllir a og birtir listann sem auglysingarnar eru syndar a. 
	 * tekur hvorki vid ne skilar neinu.
	 */
	void fyllaLista() {
		if(auglysingar.length()<1) return;
		ViewGroup insertPoint = (ViewGroup) findViewById(R.id.lost_pet_list);
		List<Pet> pets = new ArrayList<Pet>();
		try {
			JSONArray jerry = new JSONArray(auglysingar);

			for(int i = 0; i<jerry.length();i++) {
				JSONObject newPet = (JSONObject) jerry.get(i);
				String location = null;
				try {
					location = getGeoLocation(
							newPet.getDouble("lat"),
							newPet.getDouble("lon"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Pet petty = new Pet();
				petty.setName(newPet.getString("name"));
				petty.setDescription(newPet.getString("description"));
				petty.setLocation(location);
				petty.setId(newPet.getString("id"));
				petty.setImage(newPet.getString("image_string"));
				petty.setMyAd(newPet.getBoolean("mine"));
				petty.setEmail(newPet.getString("author_email"));
				petty.setSpecies(newPet.getString("species"));
				petty.setSubspecies(newPet.getString("subspecies"));
//				petty.setSex(newPet.getString("sex"));
//				petty.setFur(newPet.getString("fur"));
//				petty.setAge(newPet.getInt("age"));
//				petty.setColor(newPet.getString("color"));
				pets.add(petty);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		insertPoint.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i=0; i<pets.size(); i++) {
			View view = inflater.inflate(R.layout.pet_list_item, insertPoint, false);
			view.setOnClickListener(advertDetailPopup);
			TextView description = (TextView) view.findViewById(R.id.lost_pet_description);
			TextView name = (TextView) view.findViewById(R.id.lost_pet_name);
			TextView location = (TextView) view.findViewById(R.id.lost_pet_location);
			MyndaLoader myndaLoader = new MyndaLoader();
			name.setText(pets.get(i).getName());
			location.setText(pets.get(i).getLocation());
			description.setText(pets.get(i).getDescription());
			myndaLoader.setImage(pets.get(i).getImage());
			view.setTag(pets.get(i));
			insertPoint.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			myndaLoader.execute(view);
		}
	}
	
	class MyndaLoader extends AsyncTask<View,Void,String> {
		View view = null;
		Bitmap image = null;
		void setImage(Bitmap image) {
			this.image = image;
		}
		@Override
		protected String doInBackground(View... params) {
			this.view = params[0];
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
        protected void onPostExecute(String woot) {
			ImageView imageView = (ImageView) view.findViewById(R.id.lost_pet_pic);
			imageView.setImageBitmap(image);
		}
	}
	
	public String getGeoLocation(double lat, double lon) {
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocation(lat, lon, 1);
		} catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // Error message to post in the log
            String errorString = "Illegal arguments " +
                    Double.toString(lat) + " , " +
                    Double.toString(lon) +
                    " passed to address service";
            Log.e("LocationSampleActivity", errorString);
            e.printStackTrace();
        }
		if (addresses != null && addresses.size() > 0) {
            // Get the first address
            Address address = addresses.get(0);
            // Format the first line of address (if available),
            // city, and country name.
            String addressText = String.format(
                    "%s, %s, %s",
                    // If there's a street address, add it
                    address.getMaxAddressLineIndex() > 0 ?
                            address.getAddressLine(0) : "",
                    // Locality is usually a city
                    address.getLocality(),
                    // The country of the address
                    address.getCountryName());
            // Return the text
            return "nalaegt " +addressText;
        } else {
            return "othekkt";
        }
	}
}
