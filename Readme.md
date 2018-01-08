# ADS04 Android

## 수업 내용
- SQLite를 활용한 메모장을 학습

## Code Review

### ListActiviy

```Java
/**
 * 안드로이드 SQLite 사용하기
 * <p>
 * 1. db 파일을 직접 코드로 생성
 * 2. 로컬에서 만든 파일을 assets 에 담은 후 복사/붙여넣기를 할 수 있다.
 * > 우편번호처럼 기반 데이터가 필요한 DB일 경우
 */
public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    MemoDAO memoDAO;

    EditText editTitle, editContent;
    Button btnCreate, btnRead, btnUpdate, btnDelete;
    TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initView();
        initListener();
        init();
    }


    /**
     * onClick 정의
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreate:
                createAfterRead();
                break;
            case R.id.btnRead:
                read();
                break;
            case R.id.btnUpdate:
                updateAfterRead();
                break;
            case R.id.btnDelete:
                deleteAfterRead();
                break;
        }
    }

    /**
     * 화면에 작성한 값들을 Memo 객체로 생성
     *
     * @return
     */
    private Memo getMemoFromScreen() {
        // 1. 화면에 입력된 값을 가져온다.
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        // 2. Memo 객체를 하나 생성하여 값을 담는다.
        Memo memo = new Memo(title, content);
        return memo;
    }

    /**
     * EditText 초기화
     */
    private void resetScreen() {
        editTitle.setText("");
        editContent.setText("");
        // 초기화 한 후 focus 준다.
        editTitle.requestFocus();
    }

    /**
     * Toast 출력
     *
     * @param message
     */
    private void showInfo(String message) {
        // Toast Message 출력
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * CREATE 실행
     */
    public void create(Memo memo) {
        // DB 실행
        memoDAO.create(memo);
    }

    /**
     * READ 실행
     */
    public void read() {
        // 0. 쿼리 생성
        // 1. DB 실행한 후 결과값을 받아온다.
        ArrayList<Memo> memoList = memoDAO.read(new String[]{"id", "title", "content", "nDate"}, null);
        // 2. 결과값 출력
        textResult.setText("");
        if (memoList.size() != 0) {
            for (Memo memo : memoList) {
                textResult.append(memo.toString() + "\n");
            }
        }

    }

    /**
     * UPDATE 실행
     */
    public void update(Memo memo) {
        // DB 실행
        memoDAO.update(memo);
    }

    /**
     * DELETE 실행
     */
    public void delete() {
        // DB 실행
        memoDAO.delete();
    }

    /**
     * CREATE 후 처리
     */
    private void createAfterRead() {
        Memo memo = getMemoFromScreen();
        // 1. 생성
        create(memo);
        // 2. 결과 완료
        showInfo("등록 완료!");
        // 3. 목록 갱신
        resetScreen();
        read();
    }

    /**
     * UPDATE 후 처리
     */
    private void updateAfterRead() {
        Memo memo = getMemoFromScreen();
        // 1. 생성
        update(memo);
        // 2. 결과 완료
        showInfo("수정 완료!");
        // 3. 목록 갱신
        resetScreen();
        read();
    }

    /**
     * DELETE 후 처리
     */
    private void deleteAfterRead() {
        ArrayList<Memo> memoList = memoDAO.read(new String[]{"id", "title", "content", "nDate"}, null);

        if (memoList.size() != 0) {
            delete();
            // 2. 결과 완료
            showInfo("삭제 완료!");
            // 3. 목록 갱신
            resetScreen();
            read();
        } else {
            showInfo("데이터가 없음!");
        }
    }

    /**
     * View 초기화
     */
    private void initView() {
        editTitle = (EditText) findViewById(R.id.editTitle);
        editContent = (EditText) findViewById(R.id.editContent);

        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        textResult = (TextView) findViewById(R.id.textResult);
    }

    /**
     * Listener 초기화
     */
    private void initListener() {
        btnCreate.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    /**
     * memoDAO 초기화;
     */
    private void init() {
        memoDAO = new MemoDAO(getBaseContext());
        read();
    }

    /**
     * memoDAO 를 닫아준다.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (memoDAO != null) {
            memoDAO.close();
        }
    }

}

```
### DBHelper
```Java
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

        if(oldVersion < 2){
            //version 2
            // 쿼리
        }

        if(oldVersion < 3) {
            //version 3
            // 쿼리
        }

        if(oldVersion < 4) {
            //version 4
            // 쿼리
        }

        if(oldVersion < 5) {
            //version 5
            // 쿼리
        }

        if( oldVersion < newVersion ) {
            //version 6
            // 쿼리
        }
    }
}

```

### MemoDAO

```Java
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
        SQLiteDatabase conn = getWritableConnection();

        conn.execSQL(sql);

        closeConnection(conn);
    }

    /**
     * C: 삽입에 관련된 함수
     *
     * @param memo
     */
    public void create(Memo memo) {

        String createQuery = " INSERT INTO memo(title, content, nDate)";
        createQuery += "VALUES('" + memo.getTitle() + "', '" + memo.getContent() + "', datetime('now', 'localtime'))";

        executeSql(createQuery);
    }

    /**
     * R : 읽기에 관련된 함수
     *
     * @return
     */
    public ArrayList<Memo> read(String columns[], String where) {

        String query_prefix = "SELECT ";
        String query_midfix = "";
        for (int i = 0; i < columns.length; i++) {
            query_midfix += " " + columns[i] + ((i != columns.length - 1) ? " ," : " ");
        }
        String query_suffix = "FROM memo";
        if (where != null) {
            query_suffix += " " + where;
        }

        String query = query_prefix + query_midfix + query_suffix;

        // 1. 반환할 결과 타입을 정의
        ArrayList<Memo> memoList = new ArrayList<>();

        SQLiteDatabase con = getReadableConnection();

        // 2. 조작
        Cursor cursor = con.rawQuery(query, null);
        //con.query(테이블명, columns[], selection 인자(where 절), selectionArgs 인자, groupBy 인자, having 인자, orderBy 인자);
        while (cursor.moveToNext()) {
            Memo memo = new Memo();
            for (String col : columns) {
                int index = cursor.getColumnIndex(col);
                switch (col) {
                    case "id":
                        memo.setId(cursor.getInt(index));
                        break;
                    case "title":
                        memo.setTitle(cursor.getString(index));
                        break;
                    case "content":
                        memo.setContent(cursor.getString(index));
                        break;
                    case "nDate":
                        memo.setnDate(cursor.getString(index));
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
```

### Memo

```Java
public class Memo {
    int id;
    String title;
    String content;
    String n_date;

    public Memo(){

    }
    public Memo(String title, String content){
        this.title = title;
        this.content = content;
    }
    @Override
    public String toString() {
        return id+"|"+title+"|"+content+"|"+n_date+"\n";
    }
}
```

## 보충설명

### 보충 개념 예제
#### 사용구조 

- 1 Database 열기
- 2 Table 생성
- 3 Data 추가하기
- 4 조회하기

#### 1. Database 열기

```Java
public void openDatabase(String databaseName) {

       database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
        if (database != null) {
            println("데이터베이스 오픈됨.");
        }

  }
```

- openOrCreateDatabase을 사용하여 데이터베이스 오픈

#### 2. Table 생성

```Java
public void createTable(String tableName) {
        if (database != null) {
           String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)"; // tabel 생성하는 문장(creat tabe)
           database.execSQL(sql); // sql 실행문
       } else {
           println("먼저 데이터베이스를 오픈하세요.");
       }

   }
```

- table 생성시 create

#### 3. Data 추가하기

```Java
public void insertData(String name, int age, String mobile) {

      if (database != null) {
          String sql = "insert into customer(name, age, mobile) values(?, ?, ?)";
          Object[] params = {name, age, mobile};

          database.execSQL(sql, params);

      } else {
          println("먼저 데이터베이스를 오픈하세요.");
      }
  }
```
- data 추가시 insert 문

#### 4. 조회하기

```Java
public void selectDate(String tableName) {

       if (database != null) {
           String sql = "select name, age, mobile from " + tableName;
           Cursor cursor = database.rawQuery(sql, null);
           println("조회된 데이터 개수 : " + cursor.getCount());

           for (int i = 0; i < cursor.getCount(); i++) {
               cursor.moveToNext();
               String name = cursor.getString(0);
               int age = cursor.getInt(1);
               String mobile = cursor.getString(2);

               println("#" + i + " -> " + name + ", " + age + ", " + mobile);
           }
           cursor.close();
       }
   }

```


### HelperClass

- 데이터베이스를 만들거나 열기위해 필요한 일들을 도와주는 역할
- ex) 데이터베이스를 만드는 것 이외에도 테이블의 정의가 바뀌거나 하여, 스키마를 업그레이드할 필요가 있을 때

```Java
class DatabaseHelper extends SQLiteOpenHelper {

       public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
           super(context, name, factory, version);
       }

       @Override
       public void onCreate(SQLiteDatabase db) {
           println("onCreate() 호출됨");

           String tableName = "customer";

           String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
           db.execSQL(sql);

           println("테이블 생성됨.");


       }

       @Override
       public void onUpgrade(SQLiteDatabase db, int i, int i1) {
           println("onUpgrade 호출됨 : " + i + ", " + i1);

           if (i1 > 1) {
               String tableName = "customer";
               db.execSQL("drop table if exists " + tableName);

               println("테이블 삭제함");

               String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, name text, age integer, mobile text)";
               db.execSQL(sql);

               println("테이블 생성됨.");
           }
       }
   }
```

### SQL 문법

#### 타입 :
 ```Java
 integer, real, text, blob, null
```

#### 테이블 생성 : 
```Java
 create [temp] table 테이블명 (열의 정의 [, 제약조건]);
  - temp : 임시 생성 테이블
```

#### 테이블 구조 변경 :
```Java
 alter table 테이블명 { rename to 변경할 이름 | add column 열 정의 };
``` 
#### Query
 ```Java
 select [distinct] 열 목록
 from 테이블
 where 조건식
 group by 열 목록
 having 조건식
 order by 열 목록
 limit 개수 offset 개수;
 select 열목록 from 테이블 where 조건식;
```
```Java
 select name from (select name, type_id from (select * from foods));
  - 가장 안쪽의 select 결과가 다음 select 입력으로 처리
```
#### 조인
 ```Java
 select * from A,B where A.a=B.a;
 ```
#### 삽입
 ```Java
 insert into 테이블 (열 목록) values (값 목록);
 ```
#### 변경
 ```Java
 update 테이블 set 변경내역 where 조건식;  
```
#### 삭제
 ```Java
 delete from 테이블 where 조건식;
```
### 출처

- 출처: https://github.com/Hooooong
- 출처: http://blog.naver.com/PostView.nhn?blogId=hseok74&logNo=120140244833


## TODO

- Sigleton 검색 후 디자인 패턴 정리하기
- asset이란?
- CRUD방식으로 게시판 만드는 것 연습

## Retrospect



## Output
- 생략


