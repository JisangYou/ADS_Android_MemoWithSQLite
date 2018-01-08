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
        ArrayList<Memo> memoList = memoDAO.read();
        // 2. 결과값 출력

        String s = "";
        for(Memo memo : memoList) {
            s += "id : " + memo.getId() + " / title : " + memo.getTitle() + " / content : " + memo.getContent() + " / nDate : " + memo.getnDate() + "\n";
        }
        textResult.setText(s);

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
        ArrayList<Memo> memoList = memoDAO.read();

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


