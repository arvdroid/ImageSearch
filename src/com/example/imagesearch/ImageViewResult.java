package com.example.imagesearch;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageViewResult implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String url;
	private String tbUrl;
	
	public ImageViewResult(JSONObject json){
		try {
			url = json.getString("url");
			tbUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			url="";
			tbUrl="";
		}
		
	}	
	
	public String getUrl() {
		return url;
	}
	public String getTbUrl() {
		return tbUrl;
	}
	
	public String toString(){
		return tbUrl;
	}
	
	public static ArrayList<ImageViewResult> fromJsonArray(JSONArray jsonResults){
		ArrayList<ImageViewResult> imageResults = new ArrayList<ImageViewResult>();
		try {
			for(int i=0; i< jsonResults.length(); i++){
			
				imageResults.add(new ImageViewResult(jsonResults.getJSONObject(i)));
			} 
		}catch (JSONException e) {
			
		}
		
		return imageResults;
		
	}

}
