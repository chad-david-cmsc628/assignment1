package com.example.assignment1;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private int CAMERA_REQUEST_CODE = 1;
	private int GALLERY_REQUEST_CODE = 2;
	String [] classes = {"Camera", "View"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main);

		ArrayAdapter myAdapter = new ArrayAdapter<String>(MainActivity.this,
				android.R.layout.simple_list_item_1, classes);
				
		//ArrayAdapter myAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_main, R.id.textLabel, classes);
		
		this.setListAdapter(myAdapter);
		
	}
	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		String myClass = classes[position];
		if(myClass.compareTo("Camera") == 0) 
		{
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
		}
		else if (myClass.compareTo("View") == 0)
		{
			Intent galleryIntent = new Intent("com.example.assignment1.GalleryActivity");
			startActivity(galleryIntent);
		}
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == CAMERA_REQUEST_CODE)
		{
			if(resultCode == RESULT_OK)
			{
				Bundle extra = data.getExtras();
				Bitmap image = (Bitmap) extra.get("data");
				
				View toastView = getLayoutInflater().inflate(R.layout.toast_layout, (ViewGroup) 
						findViewById(R.id.imageView1));
				
				ImageView view = (ImageView) findViewById(R.id.imageView1);
				view.setImageBitmap(image);
				
				Toast myToast = new Toast(MainActivity.this);
				myToast.setDuration(Toast.LENGTH_LONG);
				myToast.setView(toastView);
				myToast.show();
			}
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
