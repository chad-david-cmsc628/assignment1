package com.example.assignment1;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class GalleryActivity extends ListActivity implements OnClickListener, OnDismissListener {

	private String [] imageList;
	private File imageDirectory;
	private String imageDirectoryPath_;
	private Button deleteImageButton, closeImagePopup, renameImageButton, popupButtonPressed;
	private String popupButtonText;
	private LinearLayout imagePopupContainer;
	private LinearLayout buttonContainer;
	private PopupWindow imagePopupWindow;
	private ImageView selectedImage;
	private int currentImagePos;
	private String currentImagePath;
	private Object deleteImageObject;
	private File img;
	private File deleteImageFile;
	private GalleryAdapter myAdapter; 
	private ArrayList<String>images;
	private ImageView deleteMessageView;
	private Bundle extras;
	private EditText editImageText;
	private File renamedFile;
	
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
		
		myAdapter = new GalleryAdapter(GalleryActivity.this, R.layout.gallery_layout, R.id.textLabel, images);
		this.setListAdapter(myAdapter);
		
		/* Create image popup and button containers (views) */
		imagePopupContainer = new LinearLayout(this);
		imagePopupContainer.setOrientation(LinearLayout.VERTICAL);
		buttonContainer = new LinearLayout(this);
		buttonContainer.setOrientation(LinearLayout.HORIZONTAL);
		
		/* Create buttons for image popup window */
		deleteImageButton = new Button(this);
		deleteImageButton.setText("Delete Image");
		deleteImageButton.setWidth(200);
		deleteImageButton.setHeight(100);
		deleteImageButton.setOnClickListener(this);
		
		renameImageButton = new Button(this);
		renameImageButton.setText("Rename Image");
		renameImageButton.setWidth(200);
		renameImageButton.setHeight(100);
		renameImageButton.setOnClickListener(this);
		
		closeImagePopup = new Button(this);
		closeImagePopup.setText("Close");
		closeImagePopup.setWidth(200);
		closeImagePopup.setHeight(100);
		closeImagePopup.setOnClickListener(this);
		
		/* Add popup buttons to a container */
		buttonContainer.addView((View) closeImagePopup);
		buttonContainer.addView((View) renameImageButton);
		buttonContainer.addView((View) deleteImageButton);
		
		editImageText = new EditText(GalleryActivity.this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		currentImagePos = position;
		currentImagePath = imageDirectoryPath_ + File.separator + imageList[position];
		renamedFile = new File(currentImagePath);
		
		img = new File(currentImagePath);
		
		if (img.exists()) {
			Bitmap image = BitmapFactory.decodeFile(img.getAbsolutePath());
			image = scaleImage(image, 600, 600);
			
			selectedImage = new ImageView(this);
			selectedImage.setImageBitmap(image);
			
			editImageText.setText(imageList[position]);
			editImageText.setFocusable(false);
			editImageText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
			editImageText.setOnEditorActionListener(new OnEditorActionListener () {
				
				@Override
				public boolean onEditorAction(TextView view, int action,
						KeyEvent arg2event) {
					if (action == EditorInfo.IME_ACTION_DONE) {
						//myAdapter.setListItem(0, view.getText().toString());
						view.setFocusable(false);
						renamedFile.renameTo(new File(imageDirectoryPath_ + File.separator + view.getText().toString()));
						System.out.println("renaming file");
						//return true;
					}
					return false;
				}
			});
			editImageText.setImeOptions(EditorInfo.IME_ACTION_DONE);
			editImageText.setImeActionLabel("Done", EditorInfo.IME_ACTION_DONE);
			
			imagePopupContainer.addView(selectedImage);
			imagePopupContainer.addView(editImageText);
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
		popupButtonText = popupButtonPressed.getText().toString();
		
		if (popupButtonText.equals("Close")) {
			imagePopupWindow.dismiss();
		}
		else if (popupButtonText.equals("Delete Image")) {
			deleteImageObject = myAdapter.getItem(currentImagePos);			
			deleteImageFile = new File(imageDirectoryPath_ + File.separator + deleteImageObject.toString());

			/*  If image deletion is successful, remove it from the list ArrayAdapter
			 *  and show the confirmation toast view  
			 */
			if (deleteImageFile.delete() == true) {
				myAdapter.remove(deleteImageObject.toString());
				
				deleteMessageView = new ImageView(this);
				deleteMessageView.setImageResource(R.raw.delete_message);
				
				Toast deleteConfirmation = new Toast(this);
				deleteConfirmation.setDuration(Toast.LENGTH_SHORT);
				deleteConfirmation.setView(deleteMessageView);
				deleteConfirmation.show();
			}
		}
		else if (popupButtonText.equals("Rename Image")) {
			editImageText.setFocusableInTouchMode(true);
			editImageText.setFocusable(true);
		}
	}

	@Override
	public void onDismiss() {
		/*  Removing views from image popup container to avoid the exception that gets
		 *  thrown when trying to add views that already have a parent
		 */
		imagePopupContainer.removeView(selectedImage);
		imagePopupContainer.removeView(editImageText);
		imagePopupContainer.removeView(buttonContainer);
		
		/*  Refresh ArrayAdapter so we won't click on a list item that still references a previously
		 *  deleted image 
		 */
		imageList = imageDirectory.list();
		myAdapter.clear();
		myAdapter.addAll(imageList);
		myAdapter.notifyDataSetChanged();
	}
}
