package com.FiveHundred.pxstream;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageData {

	private String url;
	private String size;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	public ArrayList<ImageData> parseJSON(JSONObject jsonObject) throws JSONException{
		
		ArrayList<ImageData> Images = new ArrayList<ImageData>();
		
		try{
			JSONArray objects = jsonObject.getJSONArray("images");
			for(int i=0; i< objects.length(); i++){
				ImageData image = new ImageData();
				JSONObject jimageObj = objects.getJSONObject(i);
				image.setUrl(jimageObj.getString("url"));
				image.setSize(jimageObj.getString("size"));
				Images.add(image);
			}
			
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		return Images;	
	}
}
