package com.FiveHundred.pxstream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.FiveHundred.pxstream.CustomScrollView.OnScrollViewListener;
import com.fivehundredpx.api.*;



public class StreamFlowActivity extends Activity implements OnClickListener{

	//Screen dimension variables
	int height;
	int width;
	
	//Used to determine if the scrollView bottom has been reached or if we need to reload old images
	int press = 0;
	int recentPoint = 0;
	int recentScroll = 0;
	
	boolean released = true;
	boolean isLoading = false;
	
	//Keep track of picture quantities to place them accurately
	int horizontal = 0;
	int vertical = 0;
	int tinyBox = 0;
	int bigBox = 0;
	
	//Resources Available
	double currentMemory;
	double recentMemoryChange;
	double resourceAmount;
	
	ArrayList<PhotoData> totalPhotos;
	
	ArrayList<Integer> pageSizes;
	int removeCount = 0;
	
	LinearLayout primaryView;
	String feature;
	String editor = "editors";
	String fresh = "fresh_today";
	int image_size = 3;
	
	//Manipulate search query
	String getPhotos = "/photos?include_store=store_download&include_states=voted&feature=";
	int currentPage = 1;
	int lowestPage = 1;
	int totalPages = 0;
	int totalItems = 0;
	int old_y = 0;
	
	CustomScrollView scrollBar;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stream_flow);
		
		//Get screen dimensions
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		
		//Create global variables to alter display or keep valuable info
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;
		primaryView = (LinearLayout) findViewById(R.id.PrimaryView);
		totalPhotos = new ArrayList<PhotoData>();
		scrollBar = (CustomScrollView) findViewById(R.id.ScrollView);
		
		scrollBar.setOnScrollViewListener(new OnScrollViewListener() {
			public void onScrollChanged(CustomScrollView v, int x, int y, int oldx, int oldy){
				if(!isLoading)
					checkScroll(y, oldy);
			}
		});

		pageSizes = new ArrayList<Integer>();
		double max = Runtime.getRuntime().maxMemory();
		
		System.out.println("Max memory = "+(max/1024/1024)+"MB");
		isLoading = true;
		boolean connected = CheckInternet();
		if(connected){
			currentMemory = getMemoryResources();
			LoadImages loader = new LoadImages();
			feature = editor;
			String url = getPhotos+feature+"&page="+currentPage+"&image_size="+image_size;
			loader.execute(url,"down");
		}else
			Toast.makeText(this, "Data Connection Required!",Toast.LENGTH_LONG).show();
	}
	
	public Bitmap loadBitmap(String url) {
		
		Bitmap bitmap;
		
		//The json returned sometimes has escaped backslashes
		String cleanUrl = url.replace("\\", "");
		try {
			bitmap = BitmapFactory.decodeStream((InputStream)new URL(cleanUrl).getContent());
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public LinearLayout addBigBlock(PhotoData photo){
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
	            (int)(width),
	            (int)(width));
		
		layout.setTag("big");
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.setMargins(2, 2, 2, 2);
		
		ImageView image = new ImageView(this);
		image.setTag("Image"+photo.getId());
		image.setAdjustViewBounds(true);
		
		Bitmap bitmap = loadBitmap(photo.getUrl());
		
		if(bitmap == null)
			image.setImageResource(R.drawable.deleted);
		else
			image.setImageBitmap(bitmap);
		
		image.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		image.setLayoutParams(lp);
		image.setOnClickListener(this);
		
		layout.addView(image);
		layout.setLayoutParams(llp);
		
		return layout;
	}
	
	/*
	 * Returns either a horizontal Layout or Vertical Layout
	 * To Display two 0.5 x 0.5 Images
	 */
	public LinearLayout addTinyBlocks(PhotoData photo1,PhotoData photo2,boolean horizontal){
		LinearLayout layout = new LinearLayout(this);
		LinearLayout.LayoutParams llp;
		
		layout.setTag("tiny");
		
		if(horizontal){
			layout.setOrientation(LinearLayout.HORIZONTAL);
			llp = new LinearLayout.LayoutParams(
		            (int)(width),
		            (int)(width*0.5));
		}
		else{
			layout.setOrientation(LinearLayout.VERTICAL);
			llp = new LinearLayout.LayoutParams(
		            (int)(width*0.5),
		            (int)(width));
		}
		
		LinearLayout.LayoutParams lp;
		
		
		//Create Image 1
		ImageView image1 = new ImageView(this);
		image1.setTag("Image"+photo1.getId());
		image1.setAdjustViewBounds(true);
		
		Bitmap bitmap = loadBitmap(photo1.getUrl());
		
		if(bitmap == null)
			image1.setImageResource(R.drawable.deleted);
		else
			image1.setImageBitmap(bitmap);

		image1.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		image1.setOnClickListener(this);
		//Create Image 2
		ImageView image2 = new ImageView(this);
		image2.setTag("Image"+photo2.getId());
		image2.setAdjustViewBounds(true);
		
		bitmap = loadBitmap(photo2.getUrl());
		if(bitmap == null)
			image2.setImageResource(R.drawable.deleted);
		else
			image2.setImageBitmap(bitmap);

		image2.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		
		image2.setOnClickListener(this);
		
		if(horizontal){
			lp = new LinearLayout.LayoutParams((int)(width*0.5), LayoutParams.MATCH_PARENT);
			lp.setMargins(2, 2, 2, 2);
			image1.setLayoutParams(lp);
			image2.setLayoutParams(lp);
		}else{
			lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int)(width*0.5));
			lp.setMargins(2, 2, 2, 2);
			image1.setLayoutParams(lp);
			image2.setLayoutParams(lp);
		}
		
		layout.addView(image1);
		layout.addView(image2);
		layout.setLayoutParams(llp);
		
		
		return layout;
	}
	
	
	public LinearLayout addVerticleBlocks(PhotoData photo1,PhotoData photo2,PhotoData photo3,boolean tinyBlocks){
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
	            (int)(width),
	            (int)(width));
		
		layout.setTag("big");
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)(width*0.5), LayoutParams.MATCH_PARENT);
		lp.setMargins(2, 2, 2, 2);
		
		ImageView image = new ImageView(this);
		image.setTag("Image"+photo1.getId());
		image.setAdjustViewBounds(true);
		Bitmap bitmap = loadBitmap(photo1.getUrl());
		
		if(bitmap == null)
			image.setImageResource(R.drawable.deleted);
		else
			image.setImageBitmap(bitmap);

		image.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		image.setLayoutParams(lp);
		image.setOnClickListener(this);
		
		if(tinyBlocks){
			LinearLayout tinys = addTinyBlocks(photo2,photo3, false);
			Random rand = new Random();
			int value = rand.nextInt()%2;
			if(value > 0){
				layout.addView(tinys);
				layout.addView(image);
			}else{
				layout.addView(image);
				layout.addView(tinys);
			}
		}else{
			ImageView image2 = new ImageView(this);
			image2.setTag("Image"+photo2.getId());
			image2.setAdjustViewBounds(true);
			bitmap = loadBitmap(photo2.getUrl());
			
			if(bitmap == null)
				image2.setImageResource(R.drawable.deleted);
			else
				image2.setImageBitmap(bitmap);

			image2.setScaleType(ImageView.ScaleType.CENTER_CROP);
			
			image2.setLayoutParams(lp);
			image2.setOnClickListener(this);
			layout.addView(image);
			layout.addView(image2);
			
		}	
		
		layout.setLayoutParams(llp);
		
		return layout;
	}
	
	public LinearLayout addPageDivider(int page){
		LinearLayout layout = new LinearLayout(this);
		
		Resources r = getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());
		
		layout.setTag(""+(int)px);
		
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
	            (int)(width),
	            (int)(px));
		llp.setMargins(2, 2, 2, 2);
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		TextView text = new TextView(this);
		text.setText("Page "+page);
		text.setTextColor(0xffffffff);
		text.setGravity(Gravity.CENTER);

		
		text.setLayoutParams(lp);
		layout.addView(text);
		layout.setBackgroundResource(R.color.divider_color);
		
		return layout;
	}
	
	public LinearLayout addHorizontalBlock(PhotoData photo){
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
	            (int)(width),
	            (int)(width*0.5));
		
		layout.setTag("tiny");
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.setMargins(2, 2, 2, 2);
		
		ImageView image = new ImageView(this);
		image.setTag("Image"+photo.getId());
		image.setAdjustViewBounds(true);
		
		Bitmap bitmap = loadBitmap(photo.getUrl());
		
		if(bitmap == null)
			image.setImageResource(R.drawable.deleted);
		else
			image.setImageBitmap(bitmap);

		image.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		image.setLayoutParams(lp);
		image.setOnClickListener(this);
		
		layout.addView(image);
		layout.setLayoutParams(llp);
		
		return layout;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_stream_flow, menu);
		return true;
	}
	
	//Returns the amount of memory currently in use
	public double getMemoryResources(){
		Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
		Debug.getMemoryInfo(memoryInfo);
		
		String memMessage = String.format(
			    "Memory: Pss=%.2f MB, Private=%.2f MB, Shared=%.2f MB",
			    memoryInfo.getTotalPss() / 1024.0,
			    memoryInfo.getTotalPrivateDirty() / 1024.0,
			    memoryInfo.getTotalSharedDirty() / 1024.0);
		
		return memoryInfo.getTotalPss();
	}
	
	/*
	 * Async task to prepare images for the primary display.
	 * Loads one page at a time.
	 * If memory limits are being reached, old pages should be released.
	 */
	private class LoadImages extends AsyncTask<String, LinearLayout, String> {
	    
		int direction = 0;
		boolean removed = false;
		
		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			int pageDifference = 0;
			if(!params[1].equals("down")){
				direction = 1;
			}
			
			isLoading = true;
			PxApi api = new PxApi(null, getApplication().getString(R.string.consumer_key), getApplication().getString(R.string.consumer_secret));
			JSONObject json = api.get(url);
			if(json != null){
				try {
					//Keep track of browsing information
					totalPages = json.getInt("total_pages");
					totalItems = json.getInt("total_items");
					ArrayList<PhotoData> photos = null;
					
					if(direction == 0){
						currentPage = json.getInt("current_page");
						pageDifference = currentPage - lowestPage;
						
						//Gather a page of 500px images and information
						photos = new PhotoData().parseJSON(json,currentPage);
						totalPhotos.addAll(photos);
						if(pageDifference >= 2){
							publishProgress((LinearLayout)null);
						}
						generatePairs(photos.size(),photos,direction);
						
					}else{
						lowestPage = json.getInt("current_page");
						pageDifference = currentPage - lowestPage;
						
						
						photos = new PhotoData().parseJSON(json,lowestPage);
						if(pageDifference >= 2){
							publishProgress((LinearLayout)null);
						}
						totalPhotos.addAll(0,photos);
						generatePairs(photos.size(),photos,direction);
						
						
					}
					//Add the photos to the global set incase we need them again
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
				return "OK";
			}
			return null;
		}
		

		@Override
		protected void onPostExecute(String result) {
			
			
			if(result != null){
				System.gc();
				recentMemoryChange = getMemoryResources();
				resourceAmount =  recentMemoryChange - currentMemory;
				System.out.println("Memory increased by "+resourceAmount);
				if(removed){
					if(direction == 0)
						lowestPage = lowestPage + 1;
					else
						currentPage--;
				}
				isLoading = false;
			}
			else
				Toast.makeText(getApplicationContext(),"Failed to fetch images from 500px",Toast.LENGTH_LONG).show();
		}

	     protected void onProgressUpdate(LinearLayout... layout) {
	    	 super.onProgressUpdate(layout);
	    	 int height = 0;
	    	 
	    	 if(layout[0] == null){
	    		 System.out.println("We are removing a page");
	    		 if(direction == 0){
	    			 //Moving Down, remove the Top page
	    			 int pageCount = pageSizes.get(0);
	    			 System.out.println("There are "+pageCount+" children");
	    			 height = 0;
	    			 for(int i=0; i< pageCount; i++){
	    				 height = height + primaryView.getChildAt(i).getHeight();
	    			 }
	    			 primaryView.removeViews(0, pageCount);
	    			 scrollBar.scrollTo(0, scrollBar.getScrollY()-height);
	    			 for(int i = 0; i< pageCount-1; i++){
							totalPhotos.remove(0);
					 }
	    			 pageSizes.remove(0);
	    			 removed = true;
	    			 System.gc();
	    		 }else{
	    			 //Moving up, remove the bottom page
	    			 int pageCount = pageSizes.get(pageSizes.size()-1);
	    			 int viewCount = primaryView.getChildCount();
	    			 int offset = viewCount - pageCount;
	    			 primaryView.removeViews(offset, pageCount);
	    			 for(int i = 0; i< pageCount; i++){
							totalPhotos.remove(0);
					 }
	    			 pageSizes.remove(pageSizes.size()-1);
	    			 removed = true;
	    			 System.gc();
	    		 }
	    		 return;
	    	 }
	    	 
	    	 if(direction == 0)
	    		 //Moving down
	    		 primaryView.addView(layout[0]);
	    	 else{
	    		 //Moving up
	    		 scrollBar.scrollTo(0, scrollBar.getScrollY());
	    		 primaryView.addView(layout[0],0);
	    		 if(layout[0].getTag().equals("tiny"))
	    			 height = (int) (width*0.5);
	    		 else if(layout[0].getTag().equals("big"))
	    			 height = width;
	    		 else{
	    			 height = Integer.parseInt((String) layout[0].getTag());
	    		 }
	    		 scrollBar.scrollTo(0, scrollBar.getScrollY()+height);
	    		 
	    		 
	    		 
	    	 }
	     }
	     
	     public void generatePairs(int amount, ArrayList<PhotoData> photos,int direction) throws InterruptedException{
	 		
	    	/*Direction: Up = 1
	    	 * 			 Down = 0;
	    	 */
	    	 
	 		Random r = new Random();
	 		int ran1;
	 		int ran2;
	 		Integer pageCount = 0;
	 		
	 		int index = 0;
	 		int increase = 1;
	 		int page = currentPage;
	 		
	 		if(direction == 1){
	 			index = photos.size()-1;
	 			increase = -1;
	 			page = lowestPage;
	 		}else{
	 			publishProgress(addPageDivider(page));
	 			pageCount = pageCount + 1;
	 		}
	 		
	 		int count = amount;
	 		

	 		while(count > 0){
	 			if(count >= 4){
	 				ran1 = r.nextInt()%7;
	 				ran2 = r.nextInt()%2;
	 				if(ran1 < 0 )
	 					ran1 = ran1*-1;
	 				if(ran2 < 0)
	 					ran2 = ran2*-1;
	 				
	 				switch(ran1){
	 					case 0: //Create a big Block (1 x 1)
	 						publishProgress(addBigBlock(photos.get(index)));
	 						index = index + increase;
	 						pageCount = pageCount + 1;
	 						count--;
	 						break;
	 					case 1://Create two horizontal Blocks (1 x 0.5)
	 						publishProgress(addHorizontalBlock(photos.get(index)));
	 						index = index + increase;
	 						publishProgress(addHorizontalBlock(photos.get(index)));
	 						index = index + increase;
	 						pageCount = pageCount + 2;
	 						count = count - 2;
	 						break;
	 					case 2://Create 1 horizontal Block(1 x 0.5) and Two Tiny Boxes(0.5 x 0.5)
	 						if(ran2 == 0){
	 							publishProgress(addHorizontalBlock(photos.get(index)));
	 							index = index + increase;
	 							publishProgress(addTinyBlocks(photos.get(index),photos.get(index+increase),true));
	 							index = index + increase*2;
	 							pageCount = pageCount + 2;
	 						}else{
	 							publishProgress(addTinyBlocks(photos.get(index),photos.get(index+increase),true));
	 							index = index + increase*2;
	 							publishProgress(addHorizontalBlock(photos.get(index)));
	 							index = index + increase;
	 							pageCount = pageCount + 2;
	 						}
	 						count = count - 3;
	 						break;
	 					case 3://Create 2 vertical Blocks(0.5 x 1)
	 						publishProgress(addVerticleBlocks(photos.get(index),photos.get(index+increase),null,false));
	 						index = index + increase*2;
	 						pageCount = pageCount + 1;
	 						count = count - 2;
	 						break;
	 					case 4://Create 1 vertical Block(0.5 x 1) and Two Tiny Boxes(0.5 x 0.5)
	 						publishProgress(addVerticleBlocks(photos.get(index),photos.get(index+increase),photos.get(index+increase*2),true));
	 						pageCount = pageCount + 1;
	 						index = index + increase*3;
	 						count = count - 3;
	 						break;
	 					/*case 5://Create 4 tiny Boxes (0.5 x 0.5)
	 						publishProgress(addTinyBlocks(photos.get(index),photos.get(index+increase),index++,true));
	 						index++;
	 						publishProgress(addTinyBlocks(photos.get(index),photos.get(index+1),index++,true));
	 						pageCount = pageCount + 2;
	 						index++;
	 						count = count - 4;
	 						break;*/
	 					case 5://Create 1 Horizontal Box (1 x 0.5)
	 						publishProgress(addHorizontalBlock(photos.get(index)));
	 						index = index + increase;
	 						pageCount = pageCount + 1;
	 						count--;
	 						break;
	 					case 6: //Create 2 Tiny Blocks(0.5 x 0.5) Horizontally
	 						publishProgress(addTinyBlocks(photos.get(index),photos.get(index+increase),true));
	 						pageCount = pageCount + 1;
	 						index = index + increase*2;
	 						count = count -2;
	 						break;
	 				}
	 			}else if(count >= 2){
	 				ran1 = r.nextInt()%5;
	 				if(ran1 < 0 )
	 					ran1 = ran1*-1;
	 				
	 				switch(ran1){
	 					case 0: //Create a Big Block
	 						publishProgress(addBigBlock(photos.get(index)));
	 						pageCount = pageCount + 1;
	 						index = index + increase;
	 						count--;
	 						break;
	 					case 1: //Create two Horizontal Blocks
	 						publishProgress(addHorizontalBlock(photos.get(index)));
	 						index = index + increase;
	 						publishProgress(addHorizontalBlock(photos.get(index)));
	 						index = index + increase;
	 						pageCount = pageCount + 2;
	 						count = count - 2;
	 						break;
	 					case 2: //Create two Vertical Blocks
	 						publishProgress(addVerticleBlocks(photos.get(index),photos.get(index+increase),null,false));
	 						index = index + increase*2;
	 						pageCount = pageCount + 1;
	 						count = count - 2;
	 						break;
	 					case 3: //Create one Horizontal Block
	 						publishProgress(addHorizontalBlock(photos.get(index)));
	 						index = index + increase;
	 						pageCount = pageCount + 1;
	 						count--;
	 						break;
	 					case 4: //Create two Tiny Boxes Horizontally
	 						publishProgress(addTinyBlocks(photos.get(index),photos.get(index+increase),true));
	 						pageCount = pageCount + 1;
	 						index = index + increase*2;
	 						count = count -2;
	 						break;
	 				}
	 			}else{ // Only 1 Slot remains
	 				ran1 = r.nextInt()%2;
	 				
	 				if(ran1 < 0 )
	 					ran1 = ran1*-1;
	 				
	 				switch(ran1){
	 					case 0: //Create a Big Block
	 						publishProgress(addBigBlock(photos.get(index)));
	 						index = index + increase;
	 						pageCount = pageCount + 1;
	 						count--;
	 						break;
	 					case 1: // Create 1 horizontal Block
	 						publishProgress(addHorizontalBlock(photos.get(index)));
	 						index = index + increase;
	 						pageCount = pageCount + 1;
	 						count--;
	 						break;
	 				}
	 			}
	 		}
	 		
	 		if(direction == 1){
	 			publishProgress(addPageDivider(page));
	 			pageCount = pageCount + 1;
	 			pageSizes.add(0,pageCount);
	 		}else
	 			pageSizes.add(pageCount);
	 		//Keep track of how many LinearLayouts were added to the primary ScrollView
	 		//We can then remove full pages at a time instead of having left overs
	 		
	 		
	 	}
	 }
	
	/*
	 * Check if there is a data connection
	 * between the wifi or mobile connection
	 */
	public boolean CheckInternet() 
	{
	    getApplicationContext();
		ConnectivityManager connec = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	    android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	    // Here if condition check for wifi and mobile network is available or not.
	    // If anyone of them is available or connected then it will return true, otherwise we cannot continue;

	    if (wifi.isConnected()) {
	        return true;
	    } else if (mobile.isConnected()) {
	        return true;
	    }
	    return false;
	}
	

	@Override
	public void onClick(View v) {

		/*
		 * Display data on click or perform other actions
		 * on the selected image
		 */
		
		String tag =(String) v.getTag();
		String value = tag.replace("Image", "");
		int id = Integer.parseInt(value);
		
		for(PhotoData data: totalPhotos){
			if(data.getId() == id){
				int size = Integer.parseInt(data.getImageInfo().get(0).getSize());
				Toast.makeText(this,"Image Name: "+data.getName()+"\n  Description: "+data.getDescription(),Toast.LENGTH_LONG).show();
			}
		}
		
		
	}

	
	public boolean checkScroll(int y, int oldy) {

		//Check if one of the last three views have come into range
		//Start loading more data if near the bottom
				
		if(!isLoading){
			Rect scrollBounds = new Rect();
			scrollBar.getHitRect(scrollBounds);
			int pointerPos = scrollBar.getScrollY();
			
			//Moving Down
			if((y - oldy) >= 0){
				if (primaryView.getChildAt(primaryView.getChildCount()-2).getLocalVisibleRect(scrollBounds) ||
						primaryView.getChildAt(primaryView.getChildCount()-1).getLocalVisibleRect(scrollBounds)){
					
					
					boolean connected = CheckInternet();
					if(connected){
						currentMemory = getMemoryResources();
						LoadImages loader = new LoadImages();
						currentPage++;
						String url = getPhotos+feature+"&page="+currentPage+"&image_size="+image_size;
						removeCount = 0;
						loader.execute(url,"down");
						System.out.println("Loading next images");
					}else
						Toast.makeText(this, "Data Connection Required!",Toast.LENGTH_LONG).show();
					
				} else {
				    // imageView is not within the visible window
				}
			}//Moving up
			else{
				if (primaryView.getChildAt(1).getLocalVisibleRect(scrollBounds) ||
						primaryView.getChildAt(2).getLocalVisibleRect(scrollBounds)){
					
					if(lowestPage > 1){
						
						//Loading previous pages
						boolean connected = CheckInternet();
						if(connected){
							old_y = oldy;
							currentMemory = getMemoryResources();
							LoadImages loader = new LoadImages();
							String url = getPhotos+feature+"&page="+(lowestPage-1)+"&image_size="+image_size;
							removeCount = 0;
							loader.execute(url,"up");
							System.out.println("Loading next images");
						}else
							Toast.makeText(this, "Data Connection Required!",Toast.LENGTH_LONG).show();
					}
				}
			}
			recentPoint = pointerPos;
			
		}
		return false;
	}

}
