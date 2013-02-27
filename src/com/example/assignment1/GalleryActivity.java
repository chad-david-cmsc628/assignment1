package com.example.assignment1;

import java.io.File;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class GalleryActivity extends ListActivity {

	String [] imageList;
	String imageDirectoryPath_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		imageDirectoryPath_ = extras.getString("imageDirectoryPath");
		File directory = new File(imageDirectoryPath_);
		imageList = directory.list();
		
		ArrayAdapter galleryAdapter = new ArrayAdapter(GalleryActivity.this, R.layout.gallery_layout, R.id.textLabel, imageList);
		this.setListAdapter(galleryAdapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		File imageFile = new File(imageList[position]);
		Bitmap image = BitmapFactory.decodeFile(imageDirectoryPath_ + "/" + imageFile.toString());
		
		View toastView = getLayoutInflater().inflate(R.layout.toast_layout, (ViewGroup)
		findViewById(R.id.imageView1)); // This was R.id.toast_layout
	
		ImageView view = (ImageView) toastView.findViewById(R.id.imageView1);
		view.setImageBitmap(image);

		Toast mytoast = new Toast(GalleryActivity.this);
		mytoast.setDuration(Toast.LENGTH_LONG);
		mytoast.setView(toastView);
		mytoast.show();
	}
}
