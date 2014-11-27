package com.HBV1.tyndr;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AdvertDetailPopup extends PopupWindow implements OnClickListener {
	Activity parent;
	LinearLayout popLayout;
	FrameLayout dimmer;
	Pet pet;
	
	public AdvertDetailPopup(final Activity parent) {
		super(parent, null, R.style.MyTheme);
		dimmer = (FrameLayout) parent.findViewById(R.id.mainmenu);
		dimmer.getForeground().setAlpha(0);
		this.parent = parent;
		setAnimationStyle(R.style.PopupWindowAnimation);
		initialize();
	}
	
	/**
	 * Frumstillir popup sem synir upplysingar um dyr
	 */
	private void initialize() {
		popLayout = new LinearLayout(parent);
		// Inflate the popup_layout.xml
		LayoutInflater inflater = (LayoutInflater) parent.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pet_list_popup, popLayout, false);
		popLayout.addView(view);
		((Button) popLayout.findViewById(R.id.popup_skra)).setOnClickListener(this);
		((Button) popLayout.findViewById(R.id.popup_loka)).setOnClickListener(this);
		setContentView(popLayout);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);	
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.popup_skra: svara(); 
		case R.id.popup_loka: dismiss(); dimmer.getForeground().setAlpha( 0) ; break;
		default: fyllaPopup(view); dimmer.getForeground().setAlpha(160);
		}
	}
	
	/*
	 * Byr til popup fyrir akvedna auglysingu
	 * @param view auglysingin sem klikkad er a
	 */
	private void fyllaPopup(View view) {
		pet =  (Pet) view.getTag();
		((ImageView) popLayout.findViewById(R.id.popup_lost_pet_pic)).setImageBitmap(pet.getImage());;
		((TextView) popLayout.findViewById(R.id.popup_nafn)).setText(pet.getName());
		((TextView) popLayout.findViewById(R.id.popup_stadur)).setText(pet.getLocation());
		((TextView) popLayout.findViewById(R.id.popup_lysing)).setText(pet.getDescription());
		((TextView) popLayout.findViewById(R.id.popup_tegund)).setText(pet.getSpecies());
		((TextView) popLayout.findViewById(R.id.popup_undirtegund)).setText(pet.getSubspecies());
		((TextView) popLayout.findViewById(R.id.popup_litur)).setText(pet.getColor());
		((TextView) popLayout.findViewById(R.id.popup_kyn)).setText(pet.getSex());
		((TextView) popLayout.findViewById(R.id.popup_feldur)).setText(pet.getFur());
		showAtLocation(parent.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
	}
	
	/*
	 * Byrjar activity sem leyfir notenda ad senda skilabod til annars notenda
	 */
	public void svara() {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
	            "mailto",pet.getEmail(), null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, pet.getName()+" fannst");
		parent.startActivity(Intent.createChooser(emailIntent, "Send email..."));
	}
}
