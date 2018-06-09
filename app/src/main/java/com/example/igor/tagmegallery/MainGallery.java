package com.example.igor.tagmegallery;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
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
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.igor.tagmegallery.ImageAdapter;

import java.util.ArrayList;

public class MainGallery extends AppCompatActivity {

  //Fields
  public static ArrayList<String> allImagePath = new ArrayList<>();
  GridView GalleryGrid;
  ImageAdapter adapter;
  ArrayAdapter<ImageView>  arrayAdapter;
  private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
          = new BottomNavigationView.OnNavigationItemSelectedListener() {

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      return false;
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_gallery);
    final Activity act = this;
    allImagePath = getImagesPath(act);
    GalleryGrid = findViewById(R.id.GalleryGrid);
    //GalleryGrid.setNumColumns(3);
    ArrayList<ImageView> imageList = new ArrayList<>(allImagePath.size());
    //initializeImageList(imageList);
   // arrayAdapter = new ArrayAdapter(this, R.layout.activity_main_gallery,R.id.GalleryGrid,imageList);
    adapter = new ImageAdapter(this);
    adapter.allFilesPath = allImagePath;
    GalleryGrid.setAdapter(adapter);
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
