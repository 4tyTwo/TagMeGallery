import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

public class ImageController {
  //Данный класс обеспечивает инициализацию, хранение и доступ к путям исходных изображений и их тамбнейлов
  //Он же будет запрашивать разрешение у пользователя

  public class ImagePathId{
    //Класс, обеспечивающий хранение и доступ к паре значений путь, Id для изображения
    //Можно использовать как для исходников так и для тамбнейлов
    final public String path; //Должны быть неизменны вне объекта
    final public Integer id;

    public ImagePathId(String path, int id){
      this.path = path;
      this.id = id;
    }
    @Override
    public boolean equals(Object other){
      if( ((ImagePathId) other).path.equals(this.path) && ((ImagePathId) other).id.equals(this.id))
        return true;
      return false;
    }
  }

  private ArrayList<ImagePathId> images = new ArrayList<>();
  private ArrayList<ImagePathId> thumbs = new ArrayList<>();

  public ImageController(Activity activity){
    //Внутри конструктора сразу получаем все необходимые для дальнейшей работы пути
    initializeImages(activity);
    initializeThumbs(activity);
  }

  private void initializeThumbs(Activity activity){ //Извлечение из памяти списка тамнейлов
    Uri uri;
    Cursor cursor;
    int column_index_data,id;
    String absolutePath;
    uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
    //Требуется заранее получить разрешение на доступ к файлам, это делается в MainGallery
    try {
      cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(activity.getContentResolver(),uri,MediaStore.Images.Thumbnails.MINI_KIND,null);
      cursor.moveToFirst();
      column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
      id = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.IMAGE_ID);
      absolutePath = cursor.getString(column_index_data);
      thumbs.add(0,new ImagePathId(absolutePath,Integer.parseInt(cursor.getString(id))));
      while (cursor.moveToNext()) {
        absolutePath = cursor.getString(column_index_data);
        thumbs.add(0,new ImagePathId(absolutePath,Integer.parseInt(cursor.getString(id))));
      }
      cursor.close();
    }
    catch (NullPointerException e){
      e.printStackTrace();
    }
  }

  private void initializeImages(Activity activity){
    Uri uri;
    Cursor cursor;
    int column_index_data,id;
    String absolutePath;
    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.MediaColumns._ID};
    final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
    try {
      //Обращаемся к ОС, чтобы получить пути к изображениям
      cursor = activity.getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");
      cursor.moveToFirst();
      column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
      absolutePath = cursor.getString(column_index_data);
      id = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID);
      images.add(0,new ImagePathId(absolutePath,Integer.parseInt(cursor.getString(id))));
      //Движимся, пока курсор курсор не дойдет до последней записи результата
      while (cursor.moveToNext()) {
        absolutePath = cursor.getString(column_index_data);
        images.add(0,new ImagePathId(absolutePath,Integer.parseInt(cursor.getString(id))));
      }
      cursor.close();
    }
    catch (NullPointerException e){
      e.printStackTrace();
    }
  }

  public String getThumbnailPath(int index){
    //Вызывается из адаптера
    //Проверяет есть ли тамбнейл для изображения с индексом index
    //Возвращает null если нет, адаптер разберется с этим
    String returnPath = null;
    int originId = images.indexOf()


    return returnPath;
  }



}
