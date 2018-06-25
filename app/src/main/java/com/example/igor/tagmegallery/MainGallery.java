package com.example.igor.tagmegallery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class MainGallery extends AppCompatActivity {

  //Fields
  ImageController imgControl;
  GridView GalleryGrid;
  static ImageAdapter adapter;

  private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
      final String path = imgControl.getImagePath(position);
      AlertDialog.Builder alertadd = new AlertDialog.Builder(parent.getContext());
      LayoutInflater factory = LayoutInflater.from(parent.getContext());
      View view2 = factory.inflate(R.layout.image_dialog_layout, null);
      alertadd.setView(view2);
      final AlertDialog dialog = alertadd.create();
      dialog.setOnShowListener(new DialogInterface.OnShowListener(){
        @Override
        public void onShow(DialogInterface d) {
          Display display = getWindowManager().getDefaultDisplay();
          Point size = new Point();
          display.getSize(size);
          float dialogHeight =  size.y * 0.9f;
          float dialogWidth = size.x * 0.85f;
          ImageView image = dialog.findViewById(R.id.imgOriginal);
          Drawable drawable = Drawable.createFromPath(path);
          Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
          //Получаем размеры изображения в пикселях
          float imageWidthInPX = (float)bmp.getWidth();
          float imageHeightInPx = (float) bmp.getHeight();
          //Отношение размера изображения к  диалоговому окну
          float heightCoeff = imageHeightInPx/dialogHeight;
          float widthCoeff = imageWidthInPX/dialogWidth;
          //
          float compressCoeff = 1.0f;
          int finalHeight, finalWidth;
          if (heightCoeff > 1.0f || widthCoeff > 1.0f ){
            compressCoeff = heightCoeff > widthCoeff ? heightCoeff : widthCoeff;
          }
          finalWidth = Math.round(imageWidthInPX/compressCoeff);
          finalHeight = Math.round(imageHeightInPx/compressCoeff);
          Bitmap resized = Bitmap.createScaledBitmap(bmp,finalWidth, finalHeight, true);
          image.setImageBitmap(resized);

        }
    });
      dialog.show();
  }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_gallery);
    GalleryGrid = findViewById(R.id.GalleryGrid);
    adapter = new ImageAdapter(this);
    if (!checkIfAlreadyhavePermission()) {
      requestForSpecificPermission();
    }
    imgControl = new ImageController(this);
    adapter.imgControl = imgControl;
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
