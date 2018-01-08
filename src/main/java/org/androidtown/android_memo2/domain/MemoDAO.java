package org.androidtown.android_memo2.domain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.androidtown.android_memo2.DBHelper;

import java.util.ArrayList;

/**
 * DAO : Data Access Object
 * Data 조작을 담당
 * <p>
 * 사용 예)
 * <p>
 * MemoDAO dao = new MemoDAO();                 1. DAO 객체 생성
 * String insertQuery = "insert into ~~"        2. Query 생성
 * dao.create(query);                           3. Query 실행
 */
public class MemoDAO {

    DBHelper dbHelper;

    public MemoDAO(Context context) { // MemoDAO가 호출되면 자동적으로 DB가 연결되면서 DATA를 조작할 준비를 함.
        // 1. 데이터베이스에 연결
        this.dbHelper = new DBHelper(context);
    }

    // 연결된 것을 토대로, CRUD방식으로 메소드 정의

    private SQLiteDatabase getReadableConnection() {

        return dbHelper.getReadableDatabase();
    }

    private SQLiteDatabase getWritableConnection() {

        return dbHelper.getWritableDatabase();
    }

    private void closeConnection(SQLiteDatabase db) {
        db.close();
    }

    private void executeSql(String sql) {
        SQLiteDatabase con = getWritableConnection();
        con.execSQL(sql);
        closeConnection(con);
    }

    /**
     * C: 삽입에 관련된 함수
     *
     * @param memo
     */
    public void create(Memo memo) {

        String createQuery = "INSERT INTO memo(title, content, nDate) VALUES('" + memo.getTitle() + "', '" + memo.getContent() + "', datetime('now', 'localtime'))";


        executeSql(createQuery);
    }

    /**
     * R : 읽기에 관련된 함수
     *
     * @return
     */
    public ArrayList<Memo> read() {

        String query = "select * from memo";

        // 1. 반환할 결과 타입을 정의
        ArrayList<Memo> memoList = new ArrayList<>();
        SQLiteDatabase con = getReadableConnection();

        // 2. 조작
        Cursor cursor = con.rawQuery(query, null);
        //con.query(테이블명, columns[], selection 인자(where 절), selectionArgs 인자, groupBy 인자, having 인자, orderBy 인자);
        while (cursor.moveToNext()) {
            Memo memo = new Memo();
            for (int i = 0; i < cursor.getColumnCount(); i++) {

                switch (cursor.getColumnName(i)) {
                    case "id":
                        memo.setId(cursor.getInt(i));
                        break;
                    case "title":
                        memo.setTitle(cursor.getString(i));
                        break;
                    case "content":
                        memo.setContent(cursor.getString(i));
                        break;
                    case "nDate":
                        memo.setnDate(cursor.getString(i));
                        break;
                }
            }
            memoList.add(memo);
        }

        // 3. 연결 해제
        closeConnection(con);

        // 데이터를 리턴
        return memoList;
    }

    /**
     * U : 수정에 관련된 함수
     *
     * @param memo
     */
    public void update(Memo memo) {
        String updateQuery = " UPDATE memo " +
                " SET title = '" + memo.getTitle() + "', " +
                " content = '" + memo.getContent() + "', " +
                " nDate = datetime('now', 'localtime')  " +
                " WHERE id = (SELECT max(id) from Memo) ";

        executeSql(updateQuery);
    }

    /**
     * D: 삭제에 관련된 함수
     */
    public void delete() {
        String deleteQuery = " DELETE FROM memo " +
                " WHERE id = (SELECT max(id) from Memo)";

        executeSql(deleteQuery);
    }

    /**
     * DB Helper 를 닫는 메소드
     */
    public void close() {
        dbHelper.close();
    }
}