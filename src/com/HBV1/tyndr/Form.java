package com.HBV1.tyndr;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.HBV1.tyndrNetwork.POST;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/*
 * @author: Tomas Karl Kjartansson<tkk4@hi.is>
 * @version: 0.1
 * @since: 2014-10-15
 */


public class Form extends Activity {


	
	private Spinner tegundirSpinner, kynSpinner, undirtegundirSpinner, feldurSpinner, litirSpinner; // spinner hlutir i vidmotinu
    ImageView Mynd;
    private boolean lost; // skrair hvort dyrid se tynt ed fundid
	private final int dp_id = 23; // id sem dagaveljarinn faer
	private final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    //ADDED
    private String filemanagerstring;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form);
		
		Mynd = (ImageView) findViewById(R.id.mynd);
		
		Mynd.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
			}
			
		});

		tegundirSpinner = (Spinner) findViewById(R.id.tegund);
		kynSpinner = (Spinner) findViewById(R.id.kyn);
		undirtegundirSpinner = (Spinner) findViewById(R.id.undirtegund);
        feldurSpinner = (Spinner) findViewById(R.id.feldur);
        litirSpinner = (Spinner) findViewById(R.id.litur);
		
		upphafsstilla();
		datepicker();
		fyllaTegundir();
	
	}
	
    //UPDATED
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String bla = getRealPathFromURI(selectedImageUri);
                Bitmap myBitmap = BitmapFactory.decodeFile(bla);

                Mynd.setImageBitmap(myBitmap);

                try {
                    ExifInterface exif = new ExifInterface(selectedImageUri.getPath());
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    if(rotation==0)
                        Mynd.setRotation(90);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public String getRealPathFromURI(Uri contentUri) {
            String[] projection = { MediaStore.MediaColumns.DATA };
            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(contentUri, projection, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else
                return null;
        }



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.form, menu);
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
	 * Fyllir a tegundir-spinnerinn og setur hlustara a hann
	 * 
	 * tekur hvorki vid ne skilar neinu.
	 */
	public void fyllaTegundir()
	{
		List<String> tegundir = new ArrayList<String>();
		tegundir.add("Veldu:");
		tegundir.add("Hundur"); //pos 1
		tegundir.add("Kisi"); // pos 2
		tegundir.add("Hestur"); // pos 3
		tegundir.add("Belja"); // pos 4
		
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.spinner_item, tegundir);
		adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tegundirSpinner.setAdapter(adapt);
		
		tegundirSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				fyllaKyn(arg2);
				fyllaUndirtegund(arg2);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	}
	
	/*
	 * Fyllir a kyn-spinnerinn
	 * 
	 * @param saetistala valkostar i tegund-spinnerhlut stjornar thvi hvada adgerd er valin
	 */
	
	public void fyllaKyn(int tegund)
	{
		List<String> kyn = new ArrayList<String>();
		
		switch(tegund){
			case 1:
				kyn.add("Rakki");
				kyn.add("Tik");
				break;
			case 2:
				kyn.add("Fress");
				kyn.add("Laeda");
				break;
			case 3:
				kyn.add("Hross");
				kyn.add("Hryssa");
				break;
			case 4:
				kyn.add("Naut");
				kyn.add("Kyr");
				break;
			default:
				kyn.add("Veldu tegund");
		}
		
		if (tegund!=0)
			kyn.add("Veit ekki");
		
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.spinner_item, kyn);
		adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		kynSpinner.setAdapter(adapt);
					
		}
	/*
	 * fyllir a undirtegund-spinnerinn
	 * 
	 * @param saetistala valkostar i tegund-spinnerhlut stjornar thvi hvada adgerd er valin
	 */
	public void fyllaUndirtegund(int tegund)
	{
		List<String> undirtegund = new ArrayList<String>();
		
		switch(tegund){
			case 1:
				undirtegund.add("Puddel");
				undirtegund.add("Great Dane");
				undirtegund.add("Blendingur");
				break;
			case 2:
				undirtegund.add("Ljon");
				undirtegund.add("American Bobtail");
				break;
			case 3:
				undirtegund.add("Islenskur");
				undirtegund.add("Utlenskur");
				break;
			case 4:
				undirtegund.add("Mjolkur Ku");
				undirtegund.add("Villtur");
				break;
			default:
				undirtegund.add("Veldu tegund");
		}
		if (tegund!=0)
			undirtegund.add("Veit ekki");
		
		ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.spinner_item, undirtegund);
		adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		undirtegundirSpinner.setAdapter(adapt);
	}
	
	/*
	 * fynnur ut hvort vidkomandi er ad skra fundid eda tynt dyr og breytir vidmoti i takt vid thad
	 * 
	 * tekur hvortki vid ne skilar neinu
	 */
	public void upphafsstilla()
	{
		String titill = getIntent().getStringExtra("titill");
		
		if (titill.equals("fundid"))
		{
			setTitle(getResources().getString(R.string.fundidTitle));
			TextView label = (TextView) findViewById(R.id.dagsetningLabel);
			label.setText(R.string.fannstLabel);
			lost=false;
		}
		else if (titill.equals("tynt"))
		{
			setTitle(getResources().getString(R.string.tyntTitle));
			TextView label = (TextView) findViewById(R.id.dagsetningLabel);
			label.setText(R.string.tyndistLabel);
			lost=true;
		}

        List<String> feldir = new ArrayList<String>();
        String[] tegundir = getResources().getStringArray(R.array.Feldir);
        for(int i=0; i<tegundir.length;i++)
        {
            feldir.add(tegundir[i]);
        }
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.spinner_item, feldir);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feldurSpinner.setAdapter(adapt);

        List<String> feldslitir = new ArrayList<String>();
        String[] litir = getResources().getStringArray(R.array.Litir);
        for (int i = 0; i<litir.length;i++)
            feldslitir.add(litir[i]);

        adapt = new ArrayAdapter<String>(this, R.layout.spinner_item, feldslitir);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        litirSpinner.setAdapter(adapt);
	}
	
	/*
	 * stillir dagsetningu i dagstening-EditText
	 * 
	 * @param dagur manadardagur
	 * @param manudur tolugildi manadar
	 * @param ar artal
	 */
	private void setDate(int dagur, int manudur, int ar)
	{
		TextView dagsetning = (TextView) findViewById(R.id.dagsetning);
		dagsetning.setText(new StringBuilder().append(dagur).append("/").append(manudur+1).append("/").append(ar));
	}
	
	/*
	 * skilar dagsetningu sem er i dagsetning-EditText
	 * 
	 * @return streng utfaersla af valdri dagsetningu
	 */
	
	private String getDate()
	{
		TextView dagsetning = (TextView) findViewById(R.id.dagsetning);
		return dagsetning.getText().toString();
	}
	
	/*
	 * stillir dagsetning-EditText a daginn i dag og setur hlustara a hann.
	 * 
	 * skilar hvortki ne tekur vid neinu
	 */
	public void datepicker()
	{
		Calendar c = Calendar.getInstance();
		
		int ar = c.get(Calendar.YEAR);
		int manudur = c.get(Calendar.MONTH);
		int dagur = c.get(Calendar.DAY_OF_MONTH);
		
		setDate(dagur,manudur,ar);
		
		TextView dagsetning = (TextView) findViewById(R.id.dagsetning);
		dagsetning.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				showDialog(dp_id);
				return false;
			}
			
		});
	}

	protected Dialog onCreateDialog(int id)
	{
		switch(id){
		case dp_id:
			Calendar c = Calendar.getInstance();
			
			int ar = c.get(Calendar.YEAR);
			int manudur = c.get(Calendar.MONTH);
			int dagur = c.get(Calendar.DAY_OF_MONTH);
			  return new DatePickerDialog(this, datePickerListener, 
                      ar, manudur,dagur);
			
		}
		return null;
		
	}
	
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			setDate(dayOfMonth,monthOfYear,year);	
		}
		
	};
	/*
	 * fall sem skra takkinn skilar. utbyr json skra sem er sidan send a bakendan okkar
	 * 
	 *	@param view hluturinn sem kallar a fallid
	 */
	public void skra(View view) throws JSONException
	{
		

		Calendar c = Calendar.getInstance();
		
		int ar = c.get(Calendar.YEAR);
		int manudurInt = c.get(Calendar.MONTH);
		int dagur = c.get(Calendar.DAY_OF_MONTH);
		
		String manudur = String.format("%02d", manudurInt+1);
		String iDag = new StringBuilder().append(ar).append("-").append(manudur).append("-").append(dagur).append("T17:40:13.467Z").toString();
		TextView nafn = (TextView) findViewById(R.id.nafn);
		JSONObject auglysing = new JSONObject();
		auglysing.accumulate("created_at", iDag);
		auglysing.accumulate("reward","Nei");
		auglysing.accumulate("description", "Tyndist/Fannst "+getDate());
		auglysing.accumulate("location","-21.89541,64.13548");
		auglysing.accumulate("resolved", false);
		auglysing.accumulate("lost", lost);
		auglysing.accumulate("name", nafn.getText().toString());
		
		new POST(auglysing).execute("http://tyndr.herokuapp.com/api/adverts");
		
		finish();

	}
	
}

