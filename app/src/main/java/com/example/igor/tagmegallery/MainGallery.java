package com.example.igor.tagmegallery;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.igor.tagmegallery.ImageAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class MainGallery extends AppCompatActivity {

  //Fields
  public static ArrayList<String> allImagePath = new ArrayList<>();
  public static ArrayList<String> allThumbsPath = new ArrayList<>();
  GridView GalleryGrid;
  static ImageAdapter adapter;
  public static ArrayList<MediaStore.Images.Thumbnails> allThumbs = new ArrayList<>();
  ArrayAdapter<ImageView>  arrayAdapter;
  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
          = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      return false;
    }
  };


  class CustomListener implements AdapterView.OnItemClickListener{
    @Override
    public void onItemClick(final AdapterView<?> parent, View view, int position, long id){
      final String path = (String) parent.getItemAtPosition(position);
      Drawable img = Drawable.createFromPath(path);
      img.setBounds(0, 0, 60, 60);
      AlertDialog.Builder alertadd = new AlertDialog.Builder(parent.getContext());
      LayoutInflater factory = LayoutInflater.from(parent.getContext());
      View view2 = factory.inflate(R.layout.image_dialog_layout, null);
      alertadd.setView(view2);
      alertadd.show();
      final AlertDialog dialog = alertadd.create();
      dialog.setOnShowListener(new DialogInterface.OnShowListener(){
        @Override
        public void onShow(DialogInterface d) {
          ImageView image = (ImageView) dialog.findViewById(R.id.imgOriginal);
          Drawable drawable = Drawable.createFromPath(path);
          image.setImageDrawable(Drawable.createFromPath(path));
          Bitmap bmp = ((BitmapDrawable)image.getBackground()).getBitmap();
          float imageWidthInPX = (float)bmp.getWidth();
          LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                  Math.round(imageWidthInPX * (float)bmp.getHeight() / (float)bmp.getWidth()));
          image.setLayoutParams(layoutParams);
        }
      });

    }
  }

  private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
      final String path = (String) parent.getItemAtPosition(position);
      Drawable img = Drawable.createFromPath(path);
      img.setBounds(0, 0, 60, 60);
      AlertDialog.Builder alertadd = new AlertDialog.Builder(parent.getContext());
      LayoutInflater factory = LayoutInflater.from(parent.getContext());
      View view2 = factory.inflate(R.layout.image_dialog_layout, null);
      alertadd.setView(view2);
      final AlertDialog dialog = alertadd.create();
      dialog.setOnShowListener(new DialogInterface.OnShowListener(){
        @Override
        public void onShow(DialogInterface d) {
          ImageView image = (ImageView) dialog.findViewById(R.id.imgOriginal);
          Drawable drawable = Drawable.createFromPath(path);
          image.setImageDrawable(Drawable.createFromPath(path));
          Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
          float imageWidthInPX = (float)bmp.getWidth();
          LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Math.round(imageWidthInPX),
                  Math.round(imageWidthInPX * (float)bmp.getHeight() / (float)bmp.getWidth()));
          image.setLayoutParams(layoutParams);
        }
    });
      dialog.show();
  }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_gallery);
    final Activity act = this;
    //allThumbsPath = getThumbsPath(act);
   // allImagePath = getImagesPath(act);
    initializePath(this,allThumbsPath,allImagePath);
    GalleryGrid = findViewById(R.id.GalleryGrid);
    //GalleryGrid.setNumColumns(3);
    //initializeImageList(imageList);
   // arrayAdapter = new ArrayAdapter(this, R.layout.activity_main_gallery,R.id.GalleryGrid,imageList);
    adapter = new ImageAdapter(this);
    adapter.allThumbsPath = allThumbsPath;
    adapter.allImagesPath = allImagePath;
    GalleryGrid.setAdapter(adapter);
    GalleryGrid.setOnItemClickListener(itemClickListener);
  }

  private boolean checkIfAlreadyhavePermission() {
    int result = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);
    if (result == PackageManager.PERMISSION_GRANTED) {
      return true;
    } else {
      return false;
    }
  }

  private void requestForSpecificPermission() {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
  }

  private ArrayList<String> getThumbsPath(final Activity activity){
    Uri uri;
    Cursor cursor;
    int column_index_data, column_index_folder_name;
    ArrayList<String> listOfAllThumbs = new ArrayList<String>();
    String absolutePathOfThumb = null;
    uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
    String[] projection = {MediaStore.Images.Thumbnails.DATA};
    final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

    int MyVersion = Build.VERSION.SDK_INT;
    if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
      if (!checkIfAlreadyhavePermission()) {
        requestForSpecificPermission();
      }
    }
    try {
      cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(getContentResolver(),uri,MediaStore.Images.Thumbnails.MINI_KIND,null);
      cursor.moveToFirst();
      column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
      //column_index_folder_name = cursor
             // .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
      while (cursor.moveToNext()) {
        absolutePathOfThumb = cursor.getString(column_index_data);
        listOfAllThumbs.add(absolutePathOfThumb);
      }
    }
    catch (NullPointerException e){
      //pass
    }
    Collections.reverse(listOfAllThumbs);
    return  listOfAllThumbs;
  }

  private void initializePath(final Activity activity,ArrayList<String> thumbs, ArrayList<String> images){
    Uri uri;
    Cursor cursor;
    int column_index_data, column_index_folder_name;
    String absolutePathOfImage = null;
    String absolutePathOfThumb = null;
    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
    final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

    int MyVersion = Build.VERSION.SDK_INT;
    if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
      if (!checkIfAlreadyhavePermission()) {
        requestForSpecificPermission();
      }
    }
    try {
      cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
      cursor.moveToFirst();
      column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
      column_index_folder_name = cursor
              .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
      while (cursor.moveToNext()) {
        absolutePathOfImage = cursor.getString(column_index_data);
        images.add(absolutePathOfImage);
      }
      cursor.close();
    }
    catch (NullPointerException e){
      //pass
    }
    try {
      uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
      cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(getContentResolver(),uri,MediaStore.Images.Thumbnails.MINI_KIND,null);
      cursor.moveToFirst();
      column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
      //column_index_folder_name = cursor
       //.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
      while (cursor.moveToNext()) {
        absolutePathOfThumb = cursor.getString(column_index_data);
        thumbs.add(absolutePathOfThumb);
      }
    }
    catch (NullPointerException e){
      //pass
    }
    Collections.reverse(thumbs);
  }

  private ArrayList<String> getImagesPath(final Activity activity){
    Uri uri;
    Cursor cursor;
    int column_index_data, column_index_folder_name;
    ArrayList<String> listOfAllImages = new ArrayList<String>();
    String absolutePathOfImage = null;
    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
    final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

    int MyVersion = Build.VERSION.SDK_INT;
    if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
      if (!checkIfAlreadyhavePermission()) {
        requestForSpecificPermission();
      }
    }
    try {
      cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
      cursor.moveToFirst();
      column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
      column_index_folder_name = cursor
              .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
      while (cursor.moveToNext()) {
        absolutePathOfImage = cursor.getString(column_index_data);
        listOfAllImages.add(absolutePathOfImage);
      }
    }
    catch (NullPointerException e){
      //pass
    }
    return listOfAllImages;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    switch (requestCode) {
      case 101:
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          //granted
        } else {
          //not granted
        }
        break;
      default:
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  private void initializeImageList(ArrayList<ImageView> imageList){
    for (int i = 0; i < allImagePath.size(); ++i){
      try {
        imageList.get(i).setImageBitmap(decodeSampledBitmap(allImagePath.get(i)));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private int calculateInSampleSize(
          BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfHeight / inSampleSize) > reqHeight
              && (halfWidth / inSampleSize) > reqWidth) {
        inSampleSize *= 2;
      }
    }

    return inSampleSize;
  }


  private Bitmap decodeSampledBitmap(String pathName,
                                     int reqWidth, int reqHeight) {

    // First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(pathName, options);

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(pathName, options);
  }

  private Bitmap decodeSampledBitmap(String pathName) {
    Display display = getWindowManager().getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);
    int width = size.x;
    int height = size.y;
    return decodeSampledBitmap(pathName, width, height);
  }

}
