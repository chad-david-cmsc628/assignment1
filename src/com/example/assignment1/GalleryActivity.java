package com.example.assignment1;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class GalleryActivity extends ListActivity implements OnClickListener, OnDismissListener {

	private String [] imageList;
	private File imageDirectory;
	private String imageDirectoryPath_;
	private Button deleteImageButton, closeImagePopup, popupButtonPressed;
	private LinearLayout imagePopupContainer;
	private LinearLayout buttonContainer;
	private PopupWindow imagePopupWindow;
	private ImageView selectedImage;
	private int deleteImagePos;
	private String deleteImagePath;
	private Object deleteImageObject;
	private File img;
	private File deleteImageFile;
	private ArrayAdapter galleryAdapter;
	private ArrayList<String>images;
	private ImageView deleteMessageView;
	private Bundle extras;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		/* Get path of the previously taken image, as well as the list of image files */
		extras = getIntent().getExtras();
		imageDirectoryPath_ = extras.getString("imageDirectoryPath");
		imageDirectory = new File(imageDirectoryPath_);
		imageList = imageDirectory.list();
		images = new ArrayList<String>();
		images.addAll(Arrays.asList(imageList));
		
		galleryAdapter = new ArrayAdapter(GalleryActivity.this, R.layout.gallery_layout, R.id.textLabel, images);
		this.setListAdapter(galleryAdapter);
		
		/* Create image popup and button containers (views) */
		imagePopupContainer = new LinearLayout(this);
		imagePopupContainer.setOrientation(LinearLayout.VERTICAL);
		buttonContainer = new LinearLayout(this);
		buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
		
		/* Create buttons for image popup window */
		deleteImageButton = new Button(this);
		deleteImageButton.setText("Delete Image");
		deleteImageButton.setWidth(300);
		deleteImageButton.setHeight(100);
		deleteImageButton.setOnClickListener(this);
		
		closeImagePopup = new Button(this);
		closeImagePopup.setText("Close");
		closeImagePopup.setWidth(300);
		closeImagePopup.setHeight(100);
		closeImagePopup.setOnClickListener(this);
		
		/* Add popup buttons to a container */
		buttonContainer.addView((View) closeImagePopup);
		buttonContainer.addView((View) deleteImageButton);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		deleteImagePos = position;
		deleteImagePath = imageDirectoryPath_ + File.separator + imageList[position];
		
		img = new File(deleteImagePath);
		
		if (img.exists()) {
			Bitmap image = BitmapFactory.decodeFile(img.getAbsolutePath());
			System.out.println("Width: " + image.getWidth() + " Height: " + image.getHeight());
			image = scaleImage(image, 600, 600);
			
			selectedImage = new ImageView(this);
			selectedImage.setImageBitmap(image);
			
			imagePopupContainer.addView(selectedImage);
			imagePopupContainer.addView(buttonContainer);
			
			imagePopupWindow = new PopupWindow(imagePopupContainer, 600, 900, true);
			imagePopupWindow.setOnDismissListener(this);
			imagePopupWindow.showAtLocation(getCurrentFocus(), Gravity.CENTER, 0, 0);
		}
	}
	
	private Bitmap scaleImage(Bitmap image_, float newWidth, float newHeight) {
		if (image_ == null) {
			return null;
		}
		
		int width = image_.getWidth();
		int height = image_.getHeight();
		
		Matrix matrix = new Matrix();
		matrix.postScale(newWidth/width, newHeight/height);
		
		return Bitmap.createBitmap(image_, 0, 0, width, height, matrix, true);
		
	}

	@Override
	public void onClick(View v) {
		popupButtonPressed = (Button) v;
		
		if (popupButtonPressed.getText().equals("Close")) {
			imagePopupWindow.dismiss();
		}
		else if (popupButtonPressed.getText().equals("Delete Image")) {
			deleteImageObject = galleryAdapter.getItem(deleteImagePos);			
			deleteImageFile = new File(imageDirectoryPath_ + File.separator + deleteImageObject.toString());

			/*  If image deletion is successful, remove it from the list ArrayAdapter
			 *  and show the confirmation toast view  
			 */
			if (deleteImageFile.delete() == true) {
				galleryAdapter.remove(deleteImageObject);
				
				deleteMessageView = new ImageView(this);
				deleteMessageView.setImageResource(R.raw.delete_message);
				
				Toast deleteConfirmation = new Toast(this);
				deleteConfirmation.setDuration(Toast.LENGTH_SHORT);
				deleteConfirmation.setView(deleteMessageView);
				deleteConfirmation.show();
			}
		}
	}

	@Override
	public void onDismiss() {
		/*  Removing views from image popup container to avoid the exception that gets
		 *  thrown when trying to add views that already have a parent
		 */
		imagePopupContainer.removeView(selectedImage);
		imagePopupContainer.removeView(buttonContainer);
		
		/*  Refresh ArrayAdapter so we won't click on a list item that still references a previously
		 *  deleted image 
		 */
		imageList = imageDirectory.list();
		galleryAdapter.clear();
		galleryAdapter.addAll(imageList);
		galleryAdapter.notifyDataSetChanged();
	}
}
