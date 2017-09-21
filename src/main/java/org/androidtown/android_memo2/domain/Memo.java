package org.androidtown.android_memo2.domain;

/**
 * Created by Jisang on 2017-09-21.
 */

public class Memo {
    public int id;
    String title;
    String content;
    String n_date;

    @Override
    public String toString() {
        return  id+" | "+title+" | "+content+" | "+n_date+"\n";
    }
}
