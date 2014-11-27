package com.HBV1.tyndr;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.json.JSONException;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import com.appspot.tyndr_server.tyndr.Tyndr;
import com.appspot.tyndr_server.tyndr.model.MessagesCreateAdvertMessage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

/*
 * @author: Tomas Karl Kjartansson<tkk4@hi.is>
 * @version: 0.1
 * @since: 2014-10-15
 */


public class Form extends Activity implements GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {


	
	private Spinner tegundirSpinner, kynSpinner, undirtegundirSpinner, feldurSpinner, litirSpinner; // spinner hlutir i vidmotinu
    ImageView Mynd;
    TextView Nafn, Aldur, Lysing;
    private boolean lost; // skrair hvort dyrid se tynt ed fundid
	private final int dp_id = 23; // id sem dagaveljarinn faer
	private final int SELECT_PICTURE = 1;
	LocationClient mLocationClient;
	GoogleAccountCredential credential;
	Tyndr hallo;
	public static final JsonFactory jarvis = new AndroidJsonFactory();
	public static final HttpTransport hoppy = AndroidHttp.newCompatibleTransport();
	
	String accountName, BaseMynd;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form);
		
		Mynd = (ImageView) findViewById(R.id.mynd);
		Nafn = (TextView) findViewById(R.id.nafn);
		Aldur = (TextView) findViewById(R.id.aldur);
		Lysing = (TextView) findViewById(R.id.lysing);
		credential = GoogleAccountCredential.usingAudience(this,
				   "server:client_id:259192441078-gmov6a7cj5dbg8ikdgkdalht3vuevs00.apps.googleusercontent.com");
		
		

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
		mLocationClient = new LocationClient(this, this, this);
		
		Tyndr.Builder tommi = new Tyndr.Builder(hoppy, jarvis, credential);
		hallo = tommi.build();
	}
	
	@Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
 
    	mLocationClient.connect();
    }
	
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }
	
	void isConnectable()
	{
		
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates","Google Play services is available.");
            }
        else
        	Log.d("Location", "tengist ekki");
        	
	}
	
    //UPDATED
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
        	switch (requestCode) {
	            case REQUEST_ACCOUNT_PICKER:
	               if (data != null && data.getExtras() != null) {
	
	               accountName = data.getExtras().getString(AccountManager.KEY_ACCOUNT_NAME);
	               if (accountName != null) {
	                  credential.setSelectedAccountName(accountName);
	               }
	            }
	            break;
	            
	            case SELECT_PICTURE:
	            	Uri selectedImageUri = data.getData();
	                String bla = getRealPathFromURI(selectedImageUri);
	                BitmapFactory.Options options = new BitmapFactory.Options();
	                options.inSampleSize = 4;
	                Bitmap mBitmap = BitmapFactory.decodeFile(bla,options);

	                
	                int rotation = 0;
	                try {
	                    ExifInterface exif = new ExifInterface(bla);
	                    rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
	                    Log.d("bla",Integer.toString(rotation));
	                    if(rotation==0)
	                        Mynd.setRotation(90);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	                mBitmap = rotateBitmap(mBitmap,rotation);
	                mBitmap =  scaleDown(mBitmap,300,true);
	                Log.d("size",mBitmap.getByteCount() + "");
	                BaseMynd = encodeTobase64(mBitmap);
	                Mynd.setImageBitmap(mBitmap);
	                break;
	         }
                
            }
        }
    
    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    
    /*
     * Fengið að láni frá stackoverflow, snýr mynndum rétt.
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
		switch (orientation) {
		    case ExifInterface.ORIENTATION_NORMAL:
		        return bitmap;
		    case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
		        matrix.setScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_ROTATE_180:
		        matrix.setRotate(180);
		        break;
		    case ExifInterface.ORIENTATION_FLIP_VERTICAL:
		        matrix.setRotate(180);
		        matrix.postScale(-1, 1);
		        break;
		    case ExifInterface.ORIENTATION_TRANSPOSE:
		        matrix.setRotate(90);
		        matrix.postScale(-1, 1);
		        break;
		   case ExifInterface.ORIENTATION_ROTATE_90:
		       matrix.setRotate(90);
		       break;
		   case ExifInterface.ORIENTATION_TRANSVERSE:
		       matrix.setRotate(-90);
		       matrix.postScale(-1, 1);
		       break;
		   case ExifInterface.ORIENTATION_ROTATE_270:
		       matrix.setRotate(-90);
		       break;
		   default:
		       return bitmap;
		}
		try {
		    Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		    bitmap.recycle();
		    return bmRotated;
		}
		catch (OutOfMemoryError e) {
		    e.printStackTrace();
		    return null;
		}
    }
    
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
            boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
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
		tegundir.add("Veldu tegund");
        String[] tegund = getResources().getStringArray(R.array.Dyr);
        for(int i=0; i<tegund.length;i++)
        {
            tegundir.add(tegund[i]);
        }
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
		String[] kynid = null;
		switch(tegund){
			case 1:
				kynid = getResources().getStringArray(R.array.HundaKyn);
				break;
			case 2:
				kynid = getResources().getStringArray(R.array.KattarKyn);
				break;
			case 3:
				kynid = getResources().getStringArray(R.array.HestaKyn);
				break;
			case 4:
				kynid = getResources().getStringArray(R.array.KuaKyn);
				break;
			case 5:
				kynid = getResources().getStringArray(R.array.KindaKyn);
				break;
			default:
				kynid = new String[1];
				kynid[0] = "Veldu tegund";
		}
		
		for(int i = 0; i<kynid.length;i++)
			kyn.add(kynid[i]);
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
		String[] UT = new String[3];
		
		switch(tegund){
			case 1:
				UT = getResources().getStringArray(R.array.HundaUndirtegundir);
				break;
			case 2:
				UT = getResources().getStringArray(R.array.KattaUndirtegundir);
				break;
			case 3:

			case 4:

			case 5:
				UT = getResources().getStringArray(R.array.AdrarUndirtegundir);
				break;
			default:
				UT = new String[1];
				UT[0] = "Veldu Tegund";
		}
		for(int i=0; i<UT.length;i++)
			undirtegund.add(UT[i]);
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
		chooseAccount();
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
	static final int REQUEST_ACCOUNT_PICKER = 2;
	
	void chooseAccount() {
	  startActivityForResult(credential.newChooseAccountIntent(),
	    REQUEST_ACCOUNT_PICKER);
	}
	
	public void skra(View view) throws JSONException
	{
		Location loc = mLocationClient.getLastLocation();
		MessagesCreateAdvertMessage newAdd = new MessagesCreateAdvertMessage();
		newAdd.setAge((long) Integer.parseInt(Aldur.getText().toString()));
		newAdd.setDescription(Lysing.getText().toString());
		newAdd.setName(Nafn.getText().toString());
		newAdd.setColor(litirSpinner.getSelectedItem().toString());
		newAdd.setSpecies(tegundirSpinner.getSelectedItem().toString());
		newAdd.setSubspecies(undirtegundirSpinner.getSelectedItem().toString());
		newAdd.setLat(loc.getLatitude());
		newAdd.setLon(loc.getLongitude());
		newAdd.setImageString(BaseMynd);
		if(lost)
			newAdd.setLabel("lost_pets");
		else
			newAdd.setLabel("found_pets");

		Calendar c = Calendar.getInstance();
		
		int ar = c.get(Calendar.YEAR);
		int manudurInt = c.get(Calendar.MONTH);
		int dagur = c.get(Calendar.DAY_OF_MONTH);
		
		String manudur = String.format("%02d", manudurInt+1);
		String iDag = new StringBuilder().append(ar).append("-").append(manudur).append("-").append(dagur).append("T17:40:13.467Z").toString();
		
		
		new POST(newAdd, credential).execute();
		finish();

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		Log.d("tenging", "Galli");
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		Log.d("tenging", "Tengdist");
		
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Log.d("tenging", "aftengdist");
	}
	
}

