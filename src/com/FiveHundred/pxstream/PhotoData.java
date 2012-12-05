package com.FiveHundred.pxstream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotoData {

	private int comments_count;
	private int times_viewed;
	private int width;
	private boolean privacy;
	private boolean store_print;
	private boolean store_download;
	private int id;
	private int favorites_count;
	private int category;
	private int height;
	private boolean nsfw;
	private String url;
	private String description;
	private String name;
	private ArrayList<ImageData> imageInfo;
	private String created_at;
	private int rating;
	private UserData userInfo;
	private int votes_count;
	private int page;
	
	public int getPage(){
		return page;
	}
	public void setPage(int _page){
		page = _page;
	}
	public int getComments_count() {
		return comments_count;
	}
	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}
	public int getTimes_viewed() {
		return times_viewed;
	}
	public void setTimes_viewed(int times_viewed) {
		this.times_viewed = times_viewed;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public boolean isPrivacy() {
		return privacy;
	}
	public void setPrivacy(boolean privacy) {
		this.privacy = privacy;
	}
	public boolean isStore_print() {
		return store_print;
	}
	public void setStore_print(boolean store_print) {
		this.store_print = store_print;
	}
	public boolean isStore_download() {
		return store_download;
	}
	public void setStore_download(boolean store_download) {
		this.store_download = store_download;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getFavorites_count() {
		return favorites_count;
	}
	public void setFavorites_count(int favorites_count) {
		this.favorites_count = favorites_count;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isNsfw() {
		return nsfw;
	}
	public void setNsfw(boolean nsfw) {
		this.nsfw = nsfw;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<ImageData> getImageInfo() {
		return imageInfo;
	}
	public void setImageInfo(ArrayList<ImageData> imageInfo) {
		this.imageInfo = imageInfo;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public UserData getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserData userInfo) {
		this.userInfo = userInfo;
	}
	public int getVotes_count() {
		return votes_count;
	}
	public void setVotes_count(int votes_count) {
		this.votes_count = votes_count;
	}
	
	public ArrayList<PhotoData> parseJSON(JSONObject jsonObject , int page) throws JSONException{
		
		try {
		    JSONArray objects = jsonObject.getJSONArray("photos");
		    
		    ArrayList<PhotoData> photos = new ArrayList<PhotoData>();
		    
		    for(int i=0; i< objects.length(); i++){
		    	ImageData image = new ImageData();
		    	UserData user = new UserData();
		    	JSONObject jphotoObj = objects.getJSONObject(i);
		    	PhotoData data = new PhotoData();
		    	data.setComments_count(jphotoObj.getInt("comments_count"));
		    	data.setTimes_viewed(jphotoObj.getInt("times_viewed"));
		    	data.setWidth(jphotoObj.getInt("width"));
		    	data.setPrivacy(jphotoObj.getBoolean("privacy"));
		    	data.setStore_print(jphotoObj.getBoolean("store_print"));
		    	data.setStore_download(jphotoObj.getBoolean("store_download"));
		    	data.setId(jphotoObj.getInt("id"));
		    	data.setFavorites_count(jphotoObj.getInt("favorites_count"));
		    	data.setCategory(jphotoObj.getInt("category"));
		    	data.setHeight(jphotoObj.getInt("height"));
		    	data.setNsfw(jphotoObj.getBoolean("nsfw"));
		    	data.setUrl(jphotoObj.getString("image_url"));
		    	data.setDescription(jphotoObj.getString("description"));
		    	data.setName(jphotoObj.getString("name"));
		    	data.setImageInfo(image.parseJSON(jphotoObj));
		    	data.setCreated_at(jphotoObj.getString("created_at"));
		    	data.setRating(jphotoObj.getInt("rating"));
		    	data.setUserInfo(user.parseJSON(jphotoObj));
		    	data.setVotes_count(jphotoObj.getInt("votes_count"));
		    	data.setPage(page);
		    	
		    	
		    	photos.add(data);
		    }
		 
		    return photos;
		} catch (JSONException e) {
		    e.printStackTrace();
		}
		
		return null;
	}
	@Override
	public String toString() {
		return "PhotoData [comments_count=" + comments_count
				+ ", times_viewed=" + times_viewed + ", width=" + width
				+ ", privacy=" + privacy + ", store_print=" + store_print
				+ ", store_download=" + store_download + ", id=" + id
				+ ", favorites_count=" + favorites_count + ", category="
				+ category + ", height=" + height + ", nsfw=" + nsfw + ", url="
				+ url + ", description=" + description + ", name=" + name
				+ ", imageInfo=" + imageInfo + ", created_at=" + created_at
				+ ", rating=" + rating + ", userInfo=" + userInfo
				+ ", votes_count=" + votes_count + "]";
	}
	
}
