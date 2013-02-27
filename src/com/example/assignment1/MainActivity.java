package com.example.assignment1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat") public class MainActivity extends Activity implements OnClickListener{
	
	/* Lecture Code Variables */
	private int CAMERA_REQUEST_CODE = 1;
	private int GALLERY_REQUEST_CODE = 2;
	
	/* Constants and Counter for Image Name */
	private static final String JPEG_FILE_SUFFIX = ".jpeg";
	private static final String JPEG_FILE_PREFIX = "image";
	private static final String TXT_FILE_PREFIX = ".txt";
	private int imageNum;
	private static final String imageCounterFile = "imageCounter" + TXT_FILE_PREFIX;
	
	/* Directory File Path to Save to and Given Image Path */
	private final File albumDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraApp");
	
	/* Buttons */
	private Button cameraButton, galleryButton;
	
	/* Text Labels */
	private ImageView logo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		try 
		{
			imageNum = getPictureNum();
			System.out.println("Image num: " + imageNum);
		}
		catch (IOException e) 
		{
			imageNum = 0;
			e.printStackTrace();
		}
		
		cameraButton = (Button) findViewById(R.id.cameraButton);
		galleryButton = (Button) findViewById(R.id.galleryButton);
		
		cameraButton.setOnClickListener(this);
		galleryButton.setOnClickListener(this);
		
		logo = (ImageView) findViewById(R.id.logo);
		logo.setImageResource(R.raw.photo_gallery_logo);
	}
	
	@Override
	public void onClick(View v_) {
		Button buttonPressed = (Button) v_;
		System.out.println(buttonPressed.getText());
		if(buttonPressed.getText().equals("Take A Picture")) 
		{
			PackageManager packageManager = getPackageManager();
			Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			List<ResolveInfo> activities = packageManager.queryIntentActivities(cameraIntent,  0);
			if(activities.size() > 0) {
				startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
			}
		}
		else if (buttonPressed.getText().equals("View Gallery"))
		{
			Intent galleryIntent = new Intent("com.example.assignment1.GalleryActivity");
			galleryIntent.putExtra("imageDirectoryPath", albumDir.getAbsolutePath().toString());
			startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == CAMERA_REQUEST_CODE)
		{
			if(resultCode == RESULT_OK)
			{
				/* Guided Lecture Code */
				Bundle extra = data.getExtras();
				Bitmap image = (Bitmap) extra.get("data");
				
				View toastView = getLayoutInflater().inflate(R.layout.toast_layout, (ViewGroup)
				findViewById(R.id.imageView1));
			
				ImageView view = (ImageView) toastView.findViewById(R.id.imageView1);
				view.setImageBitmap(image);

				Toast mytoast = new Toast(MainActivity.this);
				mytoast.setDuration(Toast.LENGTH_LONG);
				mytoast.setView(toastView);
				mytoast.show();
				
				/* Create an Image File with correct associated directory */
				try {
					File newImage = createImageFile();
					addImageToGallery(data, newImage);
				} catch (IOException e) {
					System.out.println("IO Exception");
					e.printStackTrace();
				}
			}
		}
		else if(requestCode == GALLERY_REQUEST_CODE) {
			
		}
	}

	private File createImageFile() throws IOException {
	    // Create an image file instance
	    String imageFileName = albumDir + File.separator + JPEG_FILE_PREFIX + "_" + String.format("%03d", imageNum) + JPEG_FILE_SUFFIX;
	    File image = new File(imageFileName);
	    incrementPictureNum();
	    return image;
	}
	
	private void incrementPictureNum() throws FileNotFoundException, IOException {
		FileOutputStream imgCounterOutStream = openFileOutput(imageCounterFile, Context.MODE_PRIVATE);
		imageNum++;
		imgCounterOutStream.write(imageNum);
	}
	
	private int getPictureNum() throws IOException {
		FileInputStream imgCounterInStream = openFileInput(imageCounterFile);
		imageNum = imgCounterInStream.read();
		return imageNum;
	}
	
	private void addImageToGallery(Intent data_, File image_) throws IOException {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    mediaScanIntent.putExtra("data", data_.getExtras());
	    FileOutputStream fOut = new FileOutputStream(image_);
	    Bundle b = data_.getExtras();
	    Bitmap image = (Bitmap) b.get("data");
	    image.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
	    fOut.flush();
	    fOut.close();
	    Uri contentUri = Uri.fromFile(image_);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
