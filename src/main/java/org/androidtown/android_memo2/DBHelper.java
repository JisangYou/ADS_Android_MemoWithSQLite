package org.androidtown.android_memo2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // SQLite 는
    // /data/data/패키지명/database/데이터베이스명 에 생성된다.

    // DB name
    private static final String DB_NAME = "sqlite.db";
    // DB version
    private static final int DB_VERSION = 1;

    // 매개변수가 없는 Default 생성자가 없는 Class 를 상속받기 위해서는
    // Overloading 한 생성자를 호출해야 한다.
    // super(매개변수...)
    public DBHelper(Context context) {
        // factory 는 ~~~
        super(context, DB_NAME, null, DB_VERSION);

        // super 에서 넘겨받은 데이터베이스가 생성되어 있는지 확인한 후
        // 1. 없으면 onCreate 를 호출
        // 2. 있으면 version 을 체크해서 생성되어 있는 DB 보다 version 이 높으면 onUpgrade 를 호출한다.
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 최초 생성할 테이블 상의
        // settings.config

        // DB 가 업데이트 되면
        // 모든 히스토리가 쿼리에 반영되어 있어야 한다.

        String createDB = "CREATE TABLE `memo`                                \n" +
                "( `id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
                "  `title` TEXT, \n" +
                "  `content` TEXT, \n" +
                "  `nDate` TEXT \n" +
                ")";
        // DB Query 실행
        db.execSQL(createDB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // revision.config
        // App 을 update 를 하게 되면 코드들이 새로 엎어써지고, DB Version 을 확인해서

        // onUpgrade 를 실행하기위해 Alter Table 칼럼추가 를 하게 되면
        // onCreate 에도 반영이 되어야 하고

        // version check 를 통해 version 별로 업데이트 되는 내역 또한 반영이 되어야 한다.

        if (oldVersion < 2) {
            //version 2
            // 쿼리
        }

        if (oldVersion < 3) {
            //version 3
            // 쿼리
        }

    }
}
