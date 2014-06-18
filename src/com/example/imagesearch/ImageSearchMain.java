package com.example.imagesearch;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ImageSearchMain extends ActionBarActivity {
	
	private EditText qTextView;
	private GridView qGridView;
	private ArrayList<ImageViewResult> imageResults = new ArrayList<ImageViewResult>();
	private ImageViewerAdapter imageAdapter;
	private AsyncHttpClient client = new AsyncHttpClient();
	private ArrayList<String> options;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_search_main);
		
		loadSettings();
		
		qTextView = (EditText) findViewById(R.id.searchEditText);
		qGridView = (GridView) findViewById(R.id.searchResultsGridView);
		
		imageAdapter = new ImageViewerAdapter(this, imageResults);
		qGridView.setAdapter(imageAdapter);
		
		qGridView.setOnScrollListener(new EndlessScrollListener(){

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadMore(totalItemsCount);
			}			
		});
		
		qGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				Intent i = new Intent(getApplicationContext(), FullImageViewActivity.class);
				ImageViewResult result = imageResults.get(position);
				i.putExtra("imageResult", result);
				startActivity(i);
			}
			
		});
	}	
	
	public void onSearchClick(View v){
		imageResults.clear();
		imageAdapter.clear();
		
		String query = Uri.encode(qTextView.getText().toString());
		String url = getUrl(0)+query;
		if(isNetworkAvailable())
			makeRequest(url);
		else
			Toast.makeText(this, "Error: No network", Toast.LENGTH_LONG).show();
	}
	
	public String getUrl(int start){
		if(options!=null && !options.isEmpty()){
			String imgSize = options.get(0);
			String imgType = options.get(1);
			String imgColor = options.get(2);
			String imgSite = options.get(3);

			return "https://ajax.googleapis.com/ajax/services/search/images?"
					+"v=1.0&rsz=8&start="+start+"&imgsz="+imgSize+"&imgtype="+imgType+
					"&imgcolor="+imgColor+"&as_sitesearch="+imgSite+"&q=";
		}else{
			return "https://ajax.googleapis.com/ajax/services/search/images?"
					+"v=1.0&rsz=8&start="+start+"&q=";
		}
	}
	
	private void makeRequest(String url){
		client.get(url, new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONObject result){
				JSONArray imagesJsonResults  = null;
				try {
					imagesJsonResults = result.getJSONObject("responseData").getJSONArray("results");
					ArrayList<ImageViewResult> r = ImageViewResult.fromJsonArray(imagesJsonResults);

					imageAdapter.addAll(r);

				} catch (JSONException e) {
					Log.d("ImageSearchMain", e.getMessage());
				}				
			}

			@Override
			public void onFailure(java.lang.Throwable e, org.json.JSONObject errorResponse) {
				Toast.makeText(ImageSearchMain.this, "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
				Log.d("ImageSearchMain", "on failure msg "+e.getMessage());
			}

		});
	}
	
	private void loadSettings(){
		File fielsDir = getFilesDir();
		File todoFile = new File(fielsDir, "searchOptions.txt");

		try {
			options = new ArrayList<String>(FileUtils.readLines(todoFile));
		}catch(Exception ex){
			options = new ArrayList<String>();
			Log.d("ImageSearchMain", ex.getMessage());
		}

	}
	
	private Boolean isNetworkAvailable() {
		Toast.makeText(this, "Checking network status", Toast.LENGTH_SHORT).show();
		ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
	}
	
	public void loadMore(int start){
		String query = Uri.encode(qTextView.getText().toString());		
		String url = getUrl(start)+query;		
		makeRequest(url);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.custommenu, menu);
		return true;
	}	
	
	public void onSettingsClick(){
		Intent i = new Intent(this, SettingsActivity.class);
		startActivityForResult(i, 10);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		loadSettings();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			onSettingsClick();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
