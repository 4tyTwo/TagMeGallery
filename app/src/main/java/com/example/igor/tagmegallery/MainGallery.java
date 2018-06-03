package com.example.igor.tagmegallery;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

public class MainGallery extends AppCompatActivity {

  //Fields
  public static ArrayList<String> allImagePath = new ArrayList<>();


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
    allImagePath = getImagesPath(this);

    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
  }

  private ArrayList<String> getImagesPath(Activity activity){
    Uri uri;
    Cursor cursor;
    int column_index_data, column_index_folder_name;
    ArrayList<String> listOfAllImages = new ArrayList<String>();
    String absolutePathOfImage = null;
    uri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
    String[] projection = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATA
    };

    cursor = activity.getContentResolver().query(uri, projection, null,
            null, null);
    column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
    column_index_folder_name = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
    while (cursor.moveToNext()) {
      absolutePathOfImage = cursor.getString(column_index_data);

      listOfAllImages.add(absolutePathOfImage);
    }
    return listOfAllImages;
  }

}
