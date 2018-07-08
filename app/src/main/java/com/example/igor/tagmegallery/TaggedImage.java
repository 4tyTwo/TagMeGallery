package com.example.igor.tagmegallery;

import java.util.ArrayList;

public class TaggedImage {
  final private String path;
  final private int id;
  private ArrayList<String> tags; //Все тэги должны быть в нижнем регистре
  final static  int MAX_TAGS = 16;

  TaggedImage(String path,int id, ArrayList<String> tags){
    this.id = id;
    this.path = path;
    this.tags = new ArrayList<>(tags);
  }

  TaggedImage(String path,int id){
    this.id = id;
    this.path = path;
    this.tags = new ArrayList<>();
  }

  TaggedImage(TaggedImage copied){
    this.tags = new ArrayList<>(copied.tags);
    this.id = copied.id;
    this.path = copied.path;
  }

  public String getPath() {
    return path;
  }

  public int getId() {
    return id;
  }

  public ArrayList<String> getTags() {
    return new ArrayList<>(tags); //Не нужно допускать изменения списка тегов
  }

  public void addTag(String tag){
    String insertedTag = tag.toLowerCase();
    if (tags.size() < MAX_TAGS)
      if (tags.indexOf(insertedTag) == -1)
        tags.add(insertedTag);
  }

  public boolean tagExists(String tag){
    String searchedTag = tag.toLowerCase();
    if (tags.indexOf(searchedTag) != -1)
      return true;
    return false;
  }

  public void deleteTag(String tag){
    String deletedTag = tag.toLowerCase();
    tags.remove(deletedTag);
  }


}
