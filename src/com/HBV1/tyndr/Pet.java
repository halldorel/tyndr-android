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
 * Mun verda mun staerri
 * 
 * @params: name nafn dyrs
 * @params: location stadsetning dyrs
 * @params: description lysing dyrs
 */


public class Pet {
	final public String name;
	final public String location;
	final public String description;
	final public String id;
	
	public Pet(String name, String location, String description, String id) {
		this.name = name;
		this.location = location;
		this.description = description;
		this.id = id;
	}
	public static Bitmap decodeBase64(String input) 
	{
	    byte[] decodedByte = Base64.decode(input, 0);
	    return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length); 
	}
}
