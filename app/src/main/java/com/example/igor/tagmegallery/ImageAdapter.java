package com.example.igor.tagmegallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.lang.reflect.InvocationHandler;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
  private Context mContext;
  public ImageController imgControl;

  public ImageAdapter(Context c) {
    mContext = c;
  }

  public int getCount() {
    return imgControl.size();
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
    String path = null;
    if (convertView == null) {
      // if it's not recycled, initialize some attributes
      imageView = new ImageView(mContext);
      imageView.setLayoutParams(new ViewGroup.LayoutParams(250, 250));
      imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
      imageView.setPadding(8, 8, 8, 8);
    } else {
      imageView = (ImageView) convertView;
    }
    path = imgControl.getThumbnailPath(position);
    if (path == null){
      //TO DO make a thumbnail of this picture
      path = imgControl.getImagePath(position);
      Drawable drawable = Drawable.createFromPath(path);
      Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
      Bitmap resized = Bitmap.createScaledBitmap(bmp,512, 384, true);
      imageView.setImageBitmap(resized);
    }
    else{
      imageView.setImageDrawable(Drawable.createFromPath(path));
    }
    return imageView;
  }



}