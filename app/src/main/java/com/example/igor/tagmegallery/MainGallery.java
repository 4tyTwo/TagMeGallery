package com.example.igor.tagmegallery;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MainGallery extends AppCompatActivity {

  //Fields
  public static ArrayList<String> allImagePath = new ArrayList<>();
  final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 101;

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

    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
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
    cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
    cursor.moveToFirst();
    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
    column_index_folder_name = cursor
              .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
    while (cursor.moveToNext()) {
      absolutePathOfImage = cursor.getString(column_index_data);
      listOfAllImages.add(absolutePathOfImage);
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
}
