package com.example.igor.tagmegallery;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
  private Context mContext;
  ArrayList<String> allFilesPath;

  public ImageAdapter(Context c) {
    mContext = c;
  }

  public int getCount() {
    return allFilesPath.size();
  }

  public Object getItem(int position) {
    return null;
  }

  public long getItemId(int position) {
    return 0;
  }

  // create a new ImageView for each item referenced by the Adapter
  public View getView(int position, View convertView, ViewGroup parent) {
    ImageView imageView;
    if (convertView == null) {
      // if it's not recycled, initialize some attributes
      imageView = new ImageView(mContext);
      imageView.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      imageView.setPadding(8, 8, 8, 8);
    } else {
      imageView = (ImageView) convertView;
    }
    imageView.setImageDrawable(Drawable.createFromPath(allFilesPath.get(position)));
    return imageView;
  }
}