package com.expenselog.adapterPackage;

/**
 * Created by siddhant on 11/16/16.
 */
public class ItemData {

    String text;
    Integer imageId;

    public ItemData() {
    }

    public ItemData(String text, Integer imageId){
        this.text=text;
        this.imageId=imageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }
}
