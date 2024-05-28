package com.example.halalscan.App;

public class DataClass {
    private String dataTitle;
    private String dataDesc;
    private int dataImage;
    private boolean isFavorite;


    public DataClass(String dataTitle, String dataDesc, int dataImage, boolean isFavorite) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataImage = dataImage;
        this.isFavorite = isFavorite;
    }
    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public int getDataImage() {
        return dataImage;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite=isFavorite;
    }
}
