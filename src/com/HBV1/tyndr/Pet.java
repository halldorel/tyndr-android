package com.HBV1.tyndr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/*
 * @author: Bjorn Sigurdsson
 * @version: 0.1
 * @since: 2014-10-29
 * 
 * Adeins notadur til ad geyma upplysingar. Geymir nafn, stadsetningu og lysing a dyrir.
 */


public class Pet {
	private String name;
	private String location;
	private String description;
	private String id;
	private Bitmap image;
	private String email;
	private boolean isMyAd;
	private String species;
	private String subspecies;
	private String sex;
	private String color;
	private String fur;
	private int age;
	
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
		this.image = decodeBase64(image);
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
	public static Bitmap decodeBase64(String input) {
	    byte[] decodedByte = Base64.decode(input, 0);
	    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
	}
	public String getSubspecies() {
		return subspecies;
	}
	public void setSubspecies(String subspecies) {
		this.subspecies = subspecies;
	}
	public String getSpecies() {
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getFur() {
		return fur;
	}
	public void setFur(String fur) {
		this.fur = fur;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
}
