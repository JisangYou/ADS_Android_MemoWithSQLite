# Database
## 사용구조

- 1- Database 열기
- 2- Table 생성
- 3- Data 추가하기
- 4- 조회하기

### 1. Database 열기

```Java
public void openDatabase(String databaseName) {

       database = openOrCreateDatabase(databaseName, MODE_PRIVATE, null);
        if (database != null) {
            println("데이터베이스 오픈됨.");
        }

  }
```

- openOrCreateDatabase을 사용하여 데이터베이스 오픈

### 2. Table 생성

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

### 3. Data 추가하기

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

### 4. 조회하기

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

- 조회 시 select 문

## HelperClass

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
