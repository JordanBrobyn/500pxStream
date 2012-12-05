package com.FiveHundred.pxstream;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView{

	private Runnable scrollerTask;
	private int initialPosition;
	private OnScrollViewListener mOnScrollViewListener; 
	
	private int newCheck = 100;
	private static final String TAG = "CustomScrollView";

	public interface OnScrollViewListener { 
	    void onScrollChanged( CustomScrollView v, int l, int t, int oldl, int oldt ); 
	}
	
	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CustomScrollView(Context context) { 
        super(context); 
    }
	
	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
    } 
	

	public void setOnScrollViewListener(OnScrollViewListener l) { 
	    this.mOnScrollViewListener = l; 
	}
	
	protected void onScrollChanged(int l, int t, int oldl, int oldt) { 
	    mOnScrollViewListener.onScrollChanged( this, l, t, oldl, oldt ); 
	    super.onScrollChanged( l, t, oldl, oldt ); 
	}
	
	/*@Override
	public void fling(int velocityY){
		
	}*/
	

}
