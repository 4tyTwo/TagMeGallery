package com.example.igor.tagmegallery;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Currency;

public class ImageAdapter extends BaseAdapter {
  private Context mContext;
  public Cursor mCursor;
  ArrayList<String> allThumbsPath;
  ArrayList<String> allImagesPath;
  public ArrayList<Integer> thumbsId;
  public ArrayList<Integer> imagesId;

  public ImageAdapter(Context c) {
    mContext = c;
  }

  public int getCount() {
    return allThumbsPath.size();
  }

  public Object getItem(int position) {
    return allImagesPath.get(position);
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
    imageView.setImageDrawable(Drawable.createFromPath(allThumbsPath.get(position)));
    return imageView;
  }
  AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      //Получаем настоящее изображение по id тамбнейла
      int realId = thumbsId.get((int)id);
      int pos = imagesId.indexOf(realId);
      Drawable img = parent.getResources().getDrawable(pos, null);
      img.setBounds(0, 0, 60, 60);
      AlertDialog.Builder alertadd = new AlertDialog.Builder(parent.getContext());
      LayoutInflater factory = LayoutInflater.from(parent.getContext());
      final View view2 = factory.inflate(R.layout.image_dialog_layout, null);
      alertadd.setView(view2);
      alertadd.show();
    }
  };
}