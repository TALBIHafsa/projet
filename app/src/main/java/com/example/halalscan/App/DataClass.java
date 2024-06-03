package com.example.halalscan.App;

public class DataClass {
    private String dataTitle;
    private String dataId;
    private int dataImage;
    private String dataImage2;
    private boolean isFavorite;


    public DataClass(String dataId,String dataTitle, int dataImage, boolean isFavorite) {
        this.dataId=dataId;
        this.dataTitle = dataTitle;
        this.dataImage = dataImage;
        this.isFavorite = isFavorite;
    }
    public DataClass(String dataId,String dataTitle, String dataImage, boolean isFavorite) {
        this.dataId=dataId;
        this.dataTitle = dataTitle;
        this.dataImage2= dataImage;
        this.isFavorite = isFavorite;
    }
    public String getDataId(){ return dataId;}
    public String getDataTitle() {
        return dataTitle;
    }
    public int getDataImage() {
        return dataImage;
    }
    public String getDataImage2(){
        return dataImage2;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite=isFavorite;
    }
}

