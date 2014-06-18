package com.example.imagesearch;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageViewerAdapter extends ArrayAdapter<ImageViewResult> {

	ImageLoaderConfiguration config = null;
	ImageLoader loader = null;
	
	public ImageViewerAdapter(Context context, ArrayList<ImageViewResult> images) {
		super(context,R.layout.image_view_main, images);
		config = new ImageLoaderConfiguration.Builder(context).build();
		ImageLoader.getInstance().init(config);
		loader = ImageLoader.getInstance();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageViewResult imageInfo = this.getItem(position);
		SmartImageView view;
		if(convertView == null){
			LayoutInflater inflator = LayoutInflater.from(getContext());
			
			view = (SmartImageView) inflator.inflate(R.layout.image_view_main, parent, false);			
			view.setImageResource(android.R.color.transparent);
		}else{
			view = (SmartImageView)convertView;			
			view.setImageResource(android.R.color.transparent);
		}
		loader.displayImage(imageInfo.getTbUrl(), view);
		return view;
	}
}
