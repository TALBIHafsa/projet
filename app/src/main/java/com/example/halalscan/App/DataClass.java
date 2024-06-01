package com.example.halalscan.App;

public class DataClass {
    private String dataTitle;
    private String dataId;
    private String dataStatut;
    private int dataImage;
    private String dataImage2;
    private boolean isFavorite;


    public DataClass(String dataId,String dataTitle, String dataStatut, int dataImage, boolean isFavorite) {
        this.dataId=dataId;
        this.dataTitle = dataTitle;
        this.dataStatut = dataStatut;
        this.dataImage = dataImage;
        this.isFavorite = isFavorite;
    }
    public DataClass(String dataId,String dataTitle, String dataStatut, String dataImage, boolean isFavorite) {
        this.dataId=dataId;
        this.dataTitle = dataTitle;
        this.dataStatut = dataStatut;
        this.dataImage2= dataImage;
        this.isFavorite = isFavorite;
    }
    public String getDataId(){ return dataId;}
    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataStatut() {
        return dataStatut;
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

