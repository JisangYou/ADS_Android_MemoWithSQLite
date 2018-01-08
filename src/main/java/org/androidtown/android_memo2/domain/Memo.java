package org.androidtown.android_memo2.domain;

/**
 * Created by Jisang on 2017-09-21.
 */

public class Memo {

    private int id;
    private String title;
    private String content;
    private String nDate;

    /**
     * 생성자 Overloading 을 한 경우
     * 기본 생성자도 작성을 꼭!!!해줘야 한다.
     */
    public Memo(){

    }

    /**
     * 생성자 Overloading
     *
     * @param title
     * @param content
     */
    public Memo(String title, String content){
        this.title = title;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getnDate() {
        return nDate;
    }

    public void setnDate(String nDate) {
        this.nDate = nDate;
    }

    @Override
    public String toString() {
        return "Memo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", nDate='" + nDate + '\'' +
                '}';
    }
}