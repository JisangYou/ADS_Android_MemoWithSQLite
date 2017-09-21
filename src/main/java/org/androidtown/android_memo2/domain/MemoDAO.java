package org.androidtown.android_memo2.domain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.androidtown.android_memo2.DBHelper;

import java.util.ArrayList;

/**
 * DAO - Data Access Object
 * <p>
 * <p>
 * 사용 예)
 * MemoDAO dao =  new DAO();                             1. DAO 객체를 생성
 * String query = "insert into...()";                    2. QUERY 생성
 * dao.create(query);                                    3. 쿼리 실행
 */

public class MemoDAO {

    DBHelper helper;



    public MemoDAO(Context context){
        helper = new DBHelper(context);
    }
    // C 생성
    public void create(String query){
        SQLiteDatabase con = helper.getWritableDatabase(); // helper에서 데이터베이스를 쓸수 있게
        con.execSQL(query);
        con.close();
    }
    // R 읽기
    public ArrayList<Memo> read(){
        String query = "select id, title, content, n_date from memo";

        // 반환할 결과타입 정의
        ArrayList<Memo> data = new ArrayList<>();
        SQLiteDatabase con = helper.getReadableDatabase();
        Cursor cursor = con.rawQuery(query, null);

        while(cursor.moveToNext()){
            Memo memo = new Memo();
            // id 이름의 컬럼이 몇번째인지 index를 가져오고
            //int index = cursor.getColumnIndex("id");
            // 위에서 가져온 index로 실제 값을 가져와서 저장
            memo.id = cursor.getInt(0);       // query 에서 가져올 컬럼이 몇번째 지정 되었는지 확인
            memo.title = cursor.getString(1);
            memo.content = cursor.getString(2);
            memo.n_date = cursor.getString(3);

            data.add(memo);
        }
        con.close();
        // 최종 데이터를 리턴
        return data;
    }
    // U 수정
    public void update(String query){
        SQLiteDatabase con = helper.getWritableDatabase();
        con.execSQL(query);
        con.close();
    }
    // D 삭제
    public void delete(String query){
        SQLiteDatabase con = helper.getWritableDatabase();
        con.execSQL(query);
        con.close();
    }
    // 사용한 DAO를 닫는다.
    public void close(){
        helper.close();
    }
}
