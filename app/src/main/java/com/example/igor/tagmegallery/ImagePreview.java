package com.example.igor.tagmegallery;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class ImagePreview extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.image_dialog_layout);
    ImageView im = new ImageView(null);
    int pos = getIntent().getExtras().getInt("selectedIntex");

    // ImageAdapter adapter = new ImageAdapter(imgPrevActivity.this, "image prev", null);

    long Id= MainGallery.adapter.getItemId(pos);
    im.setImageResource((int) Id);

  }

}