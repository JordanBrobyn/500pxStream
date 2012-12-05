package com.FiveHundred.pxstream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UserData {

	private int id;
	private String userpic_url;
	private String username;
	private int upgrade_status;
	private String lastname;
	private String firstname;
	private int followers_count;
	private String fullname;
	private String country;
	private String city;
	
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserpic_url() {
		return userpic_url;
	}
	public void setUserpic_url(String userpic_url) {
		this.userpic_url = userpic_url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getUpgrade_status() {
		return upgrade_status;
	}
	public void setUpgrade_status(int upgrade_status) {
		this.upgrade_status = upgrade_status;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public int getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public UserData parseJSON(JSONObject jsonObject) throws JSONException{
		
		
		try{
			JSONObject juserObj = jsonObject.getJSONObject("user");
			UserData user = new UserData();

			user.setId(juserObj.getInt("id"));
			user.setUserpic_url(juserObj.getString("userpic_url"));
			user.setUsername(juserObj.getString("username"));
			user.setUpgrade_status(juserObj.getInt("upgrade_status"));
			user.setLastname(juserObj.getString("lastname"));
			user.setFirstname(juserObj.getString("firstname"));
			user.setFollowers_count(juserObj.getInt("followers_count"));
			user.setFullname(juserObj.getString("fullname"));
			user.setCountry(juserObj.getString("country"));
			user.setCity(juserObj.getString("city"));
			
			return user;
			
		} catch (JSONException e) {
		    e.printStackTrace();
		    return null;
		}
	}
	
	
}
