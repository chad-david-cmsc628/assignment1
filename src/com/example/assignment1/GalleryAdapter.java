package com.example.assignment1;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;

public class GalleryAdapter extends ArrayAdapter<String> {

	private ArrayList<String> stringList;
	
	public GalleryAdapter(Context context, int resource,
			int textViewResourceId, ArrayList<String> objects) {
		super(context, resource, textViewResourceId, objects);
		stringList = objects;
	}
	
	public ArrayList<String> getList() {
		return this.stringList;
	}
	
	public void setListItem(int index, String value) {
		this.stringList.set(index, value);
	}
	
	public void remove(String o) {
		super.remove(o);
	}

}
