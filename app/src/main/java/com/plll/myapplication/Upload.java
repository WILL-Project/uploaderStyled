package com.plll.myapplication;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mkey;

    public Upload(){

    }
    public Upload(String name , String imageUrl,String key){
        if (name.trim().equals("")){
            name = "No Name";
        }
        mName=name;
        mImageUrl=imageUrl;
        mkey=key;
    }

    public String getMkey() {
        return mkey;
    }

    public void setMkey(String mkey) {
        this.mkey = mkey;
    }

    public String getName() {
        return mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }
}
