package com.example.igor.tagmegallery;

import java.util.ArrayList;

public class TaggedImageList extends ArrayList<TaggedImage> {

  public int indexOfPath(String path){
    for (int i = 0; i < size(); ++i)
      if (get(i).getPath().equals(path))
        return i;
    return -1;
  }

  public int indexOfId(int id){
    for (int i = 0; i < size(); ++i)
      if (get(i).getId() == id)
        return i;
    return -1;
  }

  public TaggedImageList searchByTag(ArrayList<String> tagList){
    TaggedImageList result = new TaggedImageList();
    boolean allTagsPresent;
    for (int i = 0; i < size(); ++i){
      allTagsPresent = true;
      for (int j = 0; j < tagList.size(); ++j){
        if (!get(i).tagExists(tagList.get(j)))
          allTagsPresent = false;
      }
      if (allTagsPresent)
        result.add(get(i));
    }
    return result;
  }

  public TaggedImageList searchByTag(String tag){
    TaggedImageList result = new TaggedImageList();
    for (int i = 0; i < size(); ++i){
      if (get(i).tagExists(tag))
        result.add(get(i));
    }
    return result;
  }

}
