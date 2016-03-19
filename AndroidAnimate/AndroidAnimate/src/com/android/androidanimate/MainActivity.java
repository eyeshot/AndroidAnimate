package com.android.androidanimate;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	private VerticalProgressBar pb = null;
	private int i = 0; 
	private int pre = 0;
	private int progressbarMax = 100; 
    private Runnable progressRunnable;
    private ImageView iv;
    private Button startBtn;
    private AnimationDrawable ad;
    private FrameLayout chargingLayer;
    private boolean isAnimation;
    private  Handler handler = new Handler()
    {
    	@Override
    	public void handleMessage(Message msg)
    	{
    		stopAnimation();
    	}
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		pb = (VerticalProgressBar) findViewById(R.id.verticalRatingBar1);
		pb.setHandler(handler);
		iv = (ImageView) findViewById(R.id.outer_img);
		ad = (AnimationDrawable) iv.getBackground();
		chargingLayer = (FrameLayout) findViewById(R.id.charging_layer);
		startBtn = (Button) findViewById(R.id.start);
		startBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) 
			{
				startAnimation();
			}
		});
		initProgress();
	}
	
	  private void initProgress()
	  {
		//利用线程控制进度条的进度 
			progressRunnable = new Runnable(){ 

	            public void run(){ 
	                while(i<= progressbarMax && isAnimation)
	                { 
	                	i=doAddProgress(); 
	                    handler.post(new Runnable(){ 
	                        public void run(){ 
	                        	for(int index = pre; pre <= i; pre++ )
	                        	{
	                        		pb.setProgress(index); 
	                        	}
	                        } 
	                    }); 
	                    try { 
	                        Thread.sleep(300);
	                        if(i == progressbarMax)
	                        {
	                        	pb.setProgress(0); 
	                        	i = 0;
	                        	pre = 0;
	                        }
	                        	 
	                    } catch (Exception e) { 
	                        e.printStackTrace(); 
	                    } 
	                } 
	            } 
	        };
	  }
	
	    //开始动画
		public void startAnimation()
		{
			isAnimation = true;
			startBtn.setVisibility(View.GONE);
			chargingLayer.setVisibility(View.VISIBLE);
			ThreadPoolExecutor pools = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
	                60L, TimeUnit.SECONDS,
	                new SynchronousQueue<Runnable>());
		    pools.execute(progressRunnable);
			ad.start();
		}
		
		//停止动画
		public void stopAnimation()
		{
			isAnimation = false;
			startBtn.setVisibility(View.VISIBLE);
			chargingLayer.setVisibility(View.GONE);
			ad.stop();
		}

	//增加进度条
		public int doAddProgress()
		{
			pre = i;   
			return  i += 5;
		}

	    public void setDisabale() {
	        startBtn.setEnabled(false);
	    }

	    public void setEnabled(){
	        startBtn.setEnabled(true);
	    }

}
