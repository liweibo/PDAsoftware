package com.crrc.pdasoftware.utils.guzhanggddata;


public class WeiwanchengDataInfo {


    private String mTitle;

    private String mContent;


    public WeiwanchengDataInfo(
            String title,
            String content
    ) {
        mTitle = title;
        mContent = content;
    }


    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }


}
