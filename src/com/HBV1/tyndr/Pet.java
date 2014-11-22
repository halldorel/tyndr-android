package com.HBV1.tyndr;

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
}
