package com.FiveHundredPX.Testpx;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.fivehundredpx.api.*;



public class StreamFlowActivity extends Activity implements OnClickListener{

	//Screen dimension variables
	int height;
	int width;
	
	//Keep track of picture quantities to place them accurately
	int horizontal = 0;
	int vertical = 0;
	int tinyBox = 0;
	int bigBox = 0;
	ArrayList<Pair> pairs;
	ArrayList<Pair> pairsLeft;
	LinearLayout primaryView;
	ArrayList<Groups> groups;
	String editor = "editors";
	String fresh = "fresh_today";
	String getPhotos = "/photos?feature=";
	int page = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stream_flow);
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		height = displaymetrics.heightPixels;
		width = displaymetrics.widthPixels;
		primaryView = (LinearLayout) findViewById(R.id.PrimaryView);
		boolean connected = CheckInternet();
		
		if(connected){
			
			PxApi api = new PxApi(null,this.getString(R.string.consumer_key), this.getString(R.string.consumer_secret));
			String url = getPhotos+editor+"&page="+page;
			JSONObject json = api.get(url);
			if(json == null){
				Toast.makeText(this, "Recieved Json Info", Toast.LENGTH_LONG).show();
				//pairs = generatePairs(100);
			}
			pairs = generatePairs(10);
		}else
			Toast.makeText(this, "Data Connection Required!",Toast.LENGTH_LONG).show();
	}
	

	public LinearLayout addBigBlock(int index){
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
	            (int)(width),
	            (int)(width));
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.setMargins(2, 2, 2, 2);
		
		ImageView image = new ImageView(this);
		image.setTag("Image"+index);
		image.setAdjustViewBounds(true);
		
		image.setImageResource(R.drawable.fox);
		
		image.setScaleType(ImageView.ScaleType.CENTER);
		
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
	public LinearLayout addTinyBlocks(int index,boolean horizontal){
		LinearLayout layout = new LinearLayout(this);
		LinearLayout.LayoutParams llp;
		
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
		image1.setTag("Image"+index);
		image1.setAdjustViewBounds(true);
		
		image1.setImageResource(R.drawable.fox);

		image1.setScaleType(ImageView.ScaleType.CENTER_CROP);
		
		image1.setOnClickListener(this);
		index++;
		//Create Image 2
		ImageView image2 = new ImageView(this);
		image2.setTag("Image"+index);
		image2.setAdjustViewBounds(true);
		
		image2.setImageResource(R.drawable.fox);

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
	
	
	public LinearLayout addVerticleBlocks(int index,boolean tinyBlocks){
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
	            (int)(width),
	            (int)(width));
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)(width*0.5), LayoutParams.MATCH_PARENT);
		lp.setMargins(2, 2, 2, 2);
		
		ImageView image = new ImageView(this);
		image.setTag("Image"+index);
		image.setAdjustViewBounds(true);
		
		image.setImageResource(R.drawable.fox);

		image.setScaleType(ImageView.ScaleType.CENTER);
		
		image.setLayoutParams(lp);
		image.setOnClickListener(this);
		index++;
		
		if(tinyBlocks){
			LinearLayout tinys = addTinyBlocks(index, false);
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
			image2.setTag("Image"+index);
			image2.setAdjustViewBounds(true);
			
			image2.setImageResource(R.drawable.fox);

			image2.setScaleType(ImageView.ScaleType.CENTER);
			
			image2.setLayoutParams(lp);
			image2.setOnClickListener(this);
			layout.addView(image);
			layout.addView(image2);
			
		}	
		
		layout.setLayoutParams(llp);
		
		return layout;
	}
	
	public LinearLayout addHorizontalBlock(int index){
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
	            (int)(width),
	            (int)(width*0.5));
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.setMargins(2, 2, 2, 2);
		
		ImageView image = new ImageView(this);
		image.setTag("Image"+index);
		image.setAdjustViewBounds(true);
		
		image.setImageResource(R.drawable.fox);

		image.setScaleType(ImageView.ScaleType.CENTER);
		
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
	
	public ArrayList<Pair> generatePairs(int amount){
		
		Random r = new Random();
		int ran1;
		int ran2;
		
		double val1;
		double val2;
		
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		int index = 0;
		int count = amount;
		while(count > 0){
			
			if(count >= 4){
				ran1 = r.nextInt()%8;
				ran2 = r.nextInt()%2;
				if(ran1 < 0 )
					ran1 = ran1*-1;
				if(ran2 < 0)
					ran2 = ran2*-1;
				
				switch(ran1){
					case 0: //Create a big Block (1 x 1)
						primaryView.addView(addBigBlock(index++));
						count--;
						break;
					case 1://Create two horizontal Blocks (1 x 0.5)
						primaryView.addView(addHorizontalBlock(index++));
						primaryView.addView(addHorizontalBlock(index++));
						count = count - 2;
						break;
					case 2://Create 1 horizontal Block(1 x 0.5) and Two Tiny Boxes(0.5 x 0.5)
						if(ran2 == 0){
							primaryView.addView(addHorizontalBlock(index++));
							primaryView.addView(addTinyBlocks(index++,true));
							index++;
						}else{
							primaryView.addView(addTinyBlocks(index++,true));
							index++;
							primaryView.addView(addHorizontalBlock(index++));
						}
						count = count - 3;
						break;
					case 3://Create 2 vertical Blocks(0.5 x 1)
						primaryView.addView(addVerticleBlocks(index++,false));
						index++;
						count = count - 2;
						break;
					case 4://Create 1 vertical Block(0.5 x 1) and Two Tiny Boxes(0.5 x 0.5)
						primaryView.addView(addVerticleBlocks(index++,true));
						index = index + 2;
						count = count - 3;
						break;
					case 5://Create 4 tiny Boxes (0.5 x 0.5)
						primaryView.addView(addTinyBlocks(index++, true));
						index++;
						primaryView.addView(addTinyBlocks(index++, true));
						index++;
						count = count - 4;
						break;
					case 6://Create 1 Horizontal Box (1 x 0.5)
						primaryView.addView(addHorizontalBlock(index++));
						count--;
						break;
					case 7: //Create 2 Tiny Blocks(0.5 x 0.5) Horizontally
						primaryView.addView(addTinyBlocks(index++, true));
						index++;
						count = count -2;
						break;
				}
			}else if(count >= 2){
				ran1 = r.nextInt()%5;
				if(ran1 < 0 )
					ran1 = ran1*-1;
				
				switch(ran1){
					case 0: //Create a Big Block
						primaryView.addView(addBigBlock(index++));
						count--;
						break;
					case 1: //Create two Horizontal Blocks
						primaryView.addView(addHorizontalBlock(index++));
						primaryView.addView(addHorizontalBlock(index++));
						count = count - 2;
						break;
					case 2: //Create two Vertical Blocks
						primaryView.addView(addVerticleBlocks(index++,false));
						index++;
						count = count - 2;
						break;
					case 3: //Create one Horizontal Block
						primaryView.addView(addHorizontalBlock(index++));
						count--;
						break;
					case 4: //Create two Tiny Boxes Horizontally
						primaryView.addView(addTinyBlocks(index++, true));
						index++;
						count = count -2;
						break;
				}
			}else{ // Only 1 Slot remaints
				ran1 = r.nextInt()%2;
				
				if(ran1 < 0 )
					ran1 = ran1*-1;
				
				switch(ran1){
					case 0: //Create a Big Block
						primaryView.addView(addBigBlock(index++));
						count--;
						break;
					case 1: // Create 1 horizontal Block
						primaryView.addView(addHorizontalBlock(index++));
						count--;
						break;
				}
			}
		}
		
		return null;
		
	}
	
	public String toString(){
		return " Big Box = "+bigBox+", Tiny Box = "+tinyBox+", Verticle = "+vertical+", horizontal = "+horizontal;
	}
	
	/*Check if there is a data connection
	 * 
	 */
	public boolean CheckInternet() 
	{
	    ConnectivityManager connec = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
	    android.net.NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    android.net.NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	    // Here if condition check for wifi and mobile network is available or not.
	    // If anyone of them is available or connected then it will return true, otherwise false;

	    if (wifi.isConnected()) {
	        return true;
	    } else if (mobile.isConnected()) {
	        return true;
	    }
	    return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String tag =(String) v.getTag();
		Toast.makeText(this,"Testing Image "+tag,Toast.LENGTH_LONG).show();
		
	}

}
