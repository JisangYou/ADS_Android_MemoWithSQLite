package org.androidtown.android_memo2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.android_memo2.domain.Memo;
import org.androidtown.android_memo2.domain.MemoDAO;

import java.util.ArrayList;


/**
 * 안드로이드 sqllite 사용하기
 * 1. db파일을 직접 코드로 생성
 * 2. 로컬에서 만든 파일을 asset에 담은 후 복사 붙혀넣기
 * > 우편번호처럼 기반 데이터가 필요한 db일 경우
 */

public class ListActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnCreate,btnRead,btnUpdate,btnDelete;
    EditText editTitle,editContent;
    TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initViews();
        initListener();
        init();
    }
    MemoDAO dao = null;
    private void init(){
        dao = new MemoDAO(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCreate:
                create();
                break;
            case R.id.btnRead:
                read();
                break;
            case R.id.btnUpdate:
                break;
            case R.id.btnDelete:
                break;
        }
    }

    public void create(){
        // 1. 화면에서 입력된 값을 가져온다
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        // 2. 쿼리를 만든다
        String query = "insert into memo(title, content, n_date)" +
                " values('"+title+"','"+content+"',datetime('now','localtime'))";
        // 3. DB에 실행
        dao.create(query);
        // 4. 입력 화면을 초기화해준다
        editTitle.setText("");
        editContent.setText("");
        // 5. 결과 안내
        Toast.makeText(this, "입력되었습니다!!!", Toast.LENGTH_SHORT).show();
        // 6. 목록 갱신
        read();
    }

    public void read(){
        // 0. 쿼리 있어야 되지만 생략 > DAO 에 이미 만들어 놨음
        // 1. DB 실행한 후 결과값을 받아서 처리

        ArrayList<Memo> data = dao.read();
        textResult.setText(" ");
        for(Memo memo :data){
            textResult.append(memo.toString());
        }
    }


    private void initListener() {
        btnCreate.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    private void initViews(){
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editContent = (EditText) findViewById(R.id.editContent);
        textResult = (TextView) findViewById(R.id.textResult);
    }

    @Override
    protected void onDestroy() {
        // 사용한 Database 클래스는 닫아준다.
        if(dao != null){
            dao.close();
        }
        super.onDestroy();
    }
}
