package org.androidtown.android_memo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jisang on 2017-09-21.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "sqlite.db";
    private static final int DB_VERSION = 1;
//    // DBHelper 인스턴스
//    private static DBHelper instance = null;
//    // DBHelper 를 메모리에 하나만 띄워서 사용한다.
//    public static DBHelper getInstance(Context context){
//        if(instance == null){
//            instance = new DBHelper(context);
//        }
//        return instance;
//    }

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        // super 에서 넘겨받은 데이터베이스가 생성되어 있는지 확인한후
        // 1. 없으면 onCreate를 호출
        // 2. 있으면 버전을 체크해서 생성되어 있는 DB보다 버전이 높으면
        //    opUpgrade를 호출해준다.
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 최초 생성할 테이블을 정의
        // 데이터베이스가 업데이트되면
        // 모든 히스토리가 쿼리에 반영되어 있어야 한다.
        // settings.config
        String createQuery = "CREATE TABLE `memo` ( \n" +
                "`id` INTEGER PRIMARY KEY AUTOINCREMENT, \n" +
                "`title` TEXT, \n" +
                "`content` TEXT, \n" +
                "`n_date` TEXT " +
                "'modified` TEXT )";
        // 쿼리를 실행해서 테이블을 생성한다.
        sqLiteDatabase.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int newVersion) {
        // revision.config
        // 변경된 버전과 현재버전을 비교해서
        // 히스토리를 모두 반영해야 한다.
        if(old < 2) {
            // version 2
            // String alterQuery2 = "ALTER TABLE `memo` ( \n" +
            //                      Add Column modifed text);
        }
        if(old < 3) {
            // version 3
            // String alterQuery3 = "ALTER TABLE `memo` ( \n" +
            //                      Add Column count text);
        }
        if(old < 4) {
            // version 4
            // String alterQuery4 = "ALTER TABLE `memo` ( \n" +
            //                      Add Column members text);
        }
        if( old < 5) {
            // version 5
            // String alterQuery5 = "ALTER TABLE `memo` ( \n" +
            //                      Add Column found text);
        }
    }
}


class Singleton {
    // 인스턴스를 한개 저장하는 저장소
    private static Singleton instance = null; // <- new Singleton();

    // 접근 가능한 통로를 한개만 열어준다
    public static Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }

    // 얘는 앱 전체에 하나만 new 가 되어야 한다.
    private Singleton(){

    }
}