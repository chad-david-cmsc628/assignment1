package com.example.assignment1;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class GalleryActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		String [] imageList = {"img01", "img02", "img03"};
		
		ArrayAdapter galleryAdapter = new ArrayAdapter(GalleryActivity.this, R.layout.gallery_layout, R.id.textLabel, imageList);
		this.setListAdapter(galleryAdapter);
	}
	
}
