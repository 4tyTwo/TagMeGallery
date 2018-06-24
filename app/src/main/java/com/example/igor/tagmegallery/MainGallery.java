package com.example.igor.tagmegallery;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainGallery extends AppCompatActivity {

  //Fields
  public static ArrayList<String> allImagePath = new ArrayList<>();
  public static ArrayList<String> allThumbsPath = new ArrayList<>();
  public static ArrayList<Integer> thumbsId = new ArrayList<>();
  public static ArrayList<Integer> imagesId = new ArrayList<>();
  GridView GalleryGrid;
  static ImageAdapter adapter;

  private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
      int realId = thumbsId.get(position);
      int pos = imagesId.indexOf(realId);
      final String path = allImagePath.get(pos);
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
    allThumbsPath = getThumbsPath();
    allImagePath = getImagesPath();
    GalleryGrid = findViewById(R.id.GalleryGrid);
    adapter = new ImageAdapter(this);
    adapter.allThumbsPath = allThumbsPath;
    adapter.allImagesPath = allImagePath;
    adapter.thumbsId = thumbsId;
    adapter.imagesId = imagesId;
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

  private ArrayList<String> getThumbsPath(){
    Uri uri;
    Cursor cursor;
    int column_index_data;
    ArrayList<String> listOfAllThumbs = new ArrayList<>();
    String absolutePathOfThumb;
    uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;

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
      int id = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID);
      thumbsId.add(Integer.parseInt(cursor.getString(id)));
      absolutePathOfThumb = cursor.getString(column_index_data);
      listOfAllThumbs.add(absolutePathOfThumb);
      while (cursor.moveToNext()) {
        thumbsId.add(0,Integer.parseInt(cursor.getString(id))); //Добавляем на 0 позицию, т.к. тамбнейлы лежат в порядке, обратном изображениям
        absolutePathOfThumb = cursor.getString(column_index_data);
        listOfAllThumbs.add(0,absolutePathOfThumb);
      }
      cursor.close();
    }
    catch (NullPointerException e){
      e.printStackTrace();
    }
    return  listOfAllThumbs;
  }

  private ArrayList<String> getImagesPath(){
    Uri uri;
    Cursor cursor;
    int column_index_data;
    ArrayList<String> listOfAllImages = new ArrayList<>();
    String absolutePathOfImage;
    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns._ID};
    final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
    try {
      //Обращаемся к ОС, чтобы получить пути к изображениям
      cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
      cursor.moveToFirst();
      column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
      int id = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
      imagesId.add(Integer.parseInt(cursor.getString(id)));
      absolutePathOfImage = cursor.getString(column_index_data);
      listOfAllImages.add(absolutePathOfImage);
      //Движимся, пока курсор курсор не дойдет до последней записи результата
      while (cursor.moveToNext()) {
        imagesId.add(Integer.parseInt(cursor.getString(id)));
        absolutePathOfImage = cursor.getString(column_index_data);
        listOfAllImages.add(absolutePathOfImage);
      }
      cursor.close();
    }
    catch (NullPointerException e){
      e.printStackTrace();
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
}
