package com.example.assignment1;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;

public class GalleryAdapter extends ArrayAdapter<String> {

	// Declare private members
	private ArrayList<String> stringList;
	
	// Constructor that will call the parent constructor with the parameters passed to it
	public GalleryAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<String> objects) {
		super(context, resource, textViewResourceId, objects);
		
		// Sets the private member ArrayList so it can be manipulated for file renaming purposes
		stringList = objects;
	}
	
	// Accessor function that returns the private member ArrayList
	public ArrayList<String> getList() {
		return this.stringList;
	}
	
	// Mutator function that sets an element in the private member ArrayList
	public void setListItem(int index, String value) {
		this.stringList.set(index, value);
	}
	
	// Wrapper function that calls the parent class' remove() function
	public void remove(String o) {
		super.remove(o);
	}

}
