package com.HBV1.tyndr;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class AdvertDetailPopup extends PopupWindow implements OnClickListener {
	Activity parent;
	LinearLayout popLayout;
	FrameLayout dimmer;
	
	public AdvertDetailPopup(final Activity parent) {
		super(parent, null, R.style.MyTheme);
		dimmer = (FrameLayout) parent.findViewById(R.id.mainmenu);
		dimmer.getForeground().setAlpha(0);
		this.parent = parent;
		setAnimationStyle(R.style.PopupWindowAnimation);
		initialize();
	}
	
	private void initialize() {
		popLayout = new LinearLayout(parent);
		
		// Inflate the popup_layout.xml
		LayoutInflater inflater = (LayoutInflater) parent.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.pet_list_popup, popLayout, false);
		popLayout.addView(view);
		((Button) popLayout.findViewById(R.id.popup_skra)).setOnClickListener(this);
		setContentView(popLayout);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);	
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.popup_skra: dismiss(); dimmer.getForeground().setAlpha( 0) ; break;
		default: fyllaPopup(view); dimmer.getForeground().setAlpha(160);
		}
	}
	
	private void fyllaPopup(View view) {
		Pet pet =  (Pet) view.getTag();
		((TextView) popLayout.findViewById(R.id.popup_nafn)).setText(pet.name);
		((TextView) popLayout.findViewById(R.id.popup_stadur)).setText(pet.location);
		((TextView) popLayout.findViewById(R.id.popup_lysing)).setText(pet.description);
		showAtLocation(parent.findViewById(android.R.id.content), Gravity.CENTER, 0, 0);
	}
}
