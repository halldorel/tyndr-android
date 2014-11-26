package com.HBV1.tyndr;

import android.graphics.Bitmap;

/*
 * @author: Bjorn Sigurdsson
 * @version: 0.1
 * @since: 2014-10-29
 * 
 * Adeins notadur til ad geyma upplysingar. Geymir nafn, stadsetningu og lysing a dyrir.
 * Mun verda mun staerri
 * 
 * @params: name nafn dyrs
 * @params: location stadsetning dyrs
 * @params: description lysing dyrs
 */


public class Pet {
	private String name;
	private String location;
	private String description;
	private String id;
	private Bitmap image;
	private String email;
	private boolean isMyAd;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(String image) {
		//fall sem tommi er med
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isMyAd() {
		return isMyAd;
	}
	public void setMyAd(boolean isMyAd) {
		this.isMyAd = isMyAd;
	}

	
}
