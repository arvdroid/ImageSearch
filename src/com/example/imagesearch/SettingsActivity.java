package com.example.imagesearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingsActivity extends ActionBarActivity {
	
	private Spinner imgTypeSpinner;
	private Spinner imgSizeSpinner;
	private Spinner imgColorSpinner;
	private EditText imgeSiteFilter;
	private Button saveButton;
	private Button defaultButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		String[] imgSizeVals = new String[]{"icon", "small", "medium", "xxlarge", "huge"};
        
		imgSizeSpinner = (Spinner)findViewById(R.id.imgSizeSpinner);
				
        ArrayAdapter<String> adapters = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, imgSizeVals);
        imgSizeSpinner.setAdapter(adapters);
        
        String[] imgTypeVals = new String[]{"face ", "photo", "clipart", "lineart"};
        
        imgTypeSpinner = (Spinner)findViewById(R.id.imgTypeSpinner);
                
        adapters = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, imgTypeVals);
        imgTypeSpinner.setAdapter(adapters);
        
        String[] imgColorVals = new String[]{"black","blue ", "brown", "red", "green"};
        
        imgColorSpinner = (Spinner)findViewById(R.id.imgColorSpinner);
        
        
        adapters = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, imgColorVals);
        imgColorSpinner.setAdapter(adapters);
        
        imgeSiteFilter = (EditText) findViewById(R.id.imgSiteFilterText);
        
        saveButton = (Button) findViewById(R.id.saveButton);
        
        saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String imgSize = imgSizeSpinner.getSelectedItem().toString();
				String imgType = imgTypeSpinner.getSelectedItem().toString();
				String imgColor = imgColorSpinner.getSelectedItem().toString();
				String siteFilter = imgeSiteFilter.getText().toString();

				ArrayList<String> options = new ArrayList<String>();
				options.add(imgSize);
				options.add(imgType);
				options.add(imgColor);
				options.add(siteFilter);
				
				writeOptions(options);
				
				Toast.makeText(getApplicationContext(), "Settings saved", Toast.LENGTH_SHORT).show();
				
				finish();
			}
		});
        
        defaultButton = (Button) findViewById(R.id.defaultButton);
        
        defaultButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<String> options = new ArrayList<String>();
				
				imgSizeSpinner.setSelection(0);
				imgTypeSpinner.setSelection(0);
				imgColorSpinner.setSelection(0);
				imgeSiteFilter.setText("");				
				
				writeOptions(options);
				
				Toast.makeText(getApplicationContext(), "Settings reset to default", Toast.LENGTH_SHORT).show();
				
				finish();				
			}
        	
        });
		
        readItems();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void readItems(){
		File fielsDir = getFilesDir();
		File todoFile = new File(fielsDir, "searchOptions.txt");
		
		try {
			ArrayList<String> options = new ArrayList<String>(FileUtils.readLines(todoFile));
			int pos = ((ArrayAdapter)imgSizeSpinner.getAdapter()).getPosition(options.get(0));
			imgSizeSpinner.setSelection(pos);
			
			pos = ((ArrayAdapter)imgTypeSpinner.getAdapter()).getPosition(options.get(1));
			imgTypeSpinner.setSelection(pos);
			
			pos = ((ArrayAdapter)imgColorSpinner.getAdapter()).getPosition(options.get(2));
			imgColorSpinner.setSelection(pos);
			
			imgeSiteFilter.setText(options.get(3));
			 
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}		
	}
	
	private void writeOptions(ArrayList<String> options){
		File fielsDir = getFilesDir();
		File todoFile = new File(fielsDir, "searchOptions.txt");
		try {
			FileUtils.writeLines(todoFile, options);
			
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	}
}
