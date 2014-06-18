package com.example.imagesearch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class FullImageViewActivity extends ActionBarActivity {
	
	Button bShare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_full_image_view);
		ImageViewResult result = (ImageViewResult) getIntent().getSerializableExtra("imageResult");
		SmartImageView view = (SmartImageView) findViewById(R.id.fvImageView);
		view.setImageUrl(result.getUrl());
		
		bShare = (Button)findViewById(R.id.bShare);
		
		ButtonListener bl = new ButtonListener(result, view);
		bShare.setOnClickListener(bl);
	}
	
	class ButtonListener implements OnClickListener{
		public ImageViewResult info;
		SmartImageView view;
		
		ButtonListener(ImageViewResult info, SmartImageView view){
			this.info = info;
			this.view = view;
		}
		
		@Override
		public void onClick(View v) {
			onShareItem(view);
		}
	}
	
	public void onShareItem(SmartImageView ivImage) {
	    // Get access to bitmap image from view
	    // Get access to the URI for the bitmap
	    Uri bmpUri = getLocalBitmapUri(ivImage);
	    if (bmpUri != null) {
	        // Construct a ShareIntent with link to image
	        Intent shareIntent = new Intent();
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
	        shareIntent.setType("image/*");
	        // Launch sharing dialog for image
	        startActivity(Intent.createChooser(shareIntent, "Share Image"));
	    } else {
	    	Toast.makeText(this, "sharing failed", Toast.LENGTH_SHORT).show();
	    }
	}
	
	public Uri getLocalBitmapUri(SmartImageView imageView) {
	    // Extract Bitmap from ImageView drawable
	    Drawable drawable = imageView.getDrawable();
	    Bitmap bmp = null;
	    if (drawable instanceof BitmapDrawable){
	       bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
	    } else {
	    	Toast.makeText(this, "sharing null", Toast.LENGTH_SHORT).show();
	       return null;
	    }
	    // Store image to default external storage directory
	    Uri bmpUri = null;
	    try {
	        File file =  new File(Environment.getExternalStoragePublicDirectory(  
		        Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
	        file.getParentFile().mkdirs();
	        FileOutputStream out = new FileOutputStream(file);
	        bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
	        out.close();
	        bmpUri = Uri.fromFile(file);
	    } catch (IOException e) {
	    	Toast.makeText(this, "sharing excep"+e.getMessage(), Toast.LENGTH_SHORT).show();
	    	e.printStackTrace();
	    }
	    return bmpUri;
	}
}
