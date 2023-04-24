package com.example.blooddonation;

public class fileinfomodel
{
  String fileurl,filename;

    public fileinfomodel(String fileurl) {
        this.fileurl = fileurl;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileurl() {
        return fileurl;
    }

    public void setFileurl(String fileurl) {
        this.fileurl = fileurl;
    }

}
