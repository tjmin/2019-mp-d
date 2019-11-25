//작성자: 박재효
package com.example.myapplication;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ScriptListener;
import com.example.myapplication.ScriptsDB;
import com.example.myapplication.ScriptsDao;

import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.EditActivity.SCRIPT_EXTRA_Key;


public class MainActivity extends AppCompatActivity implements ScriptListener {

    private static final String TAG = "MainActivity";

    private ArrayList<Script> mArrayList;
    private ScriptDataAdapter mAdapter;
    private int count = -1;

    SwipeController swipeController = null;


//    localDB
//    private ScriptsDao dao;

    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupRecyclerView();

        mRecyclerView = findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mArrayList = new ArrayList<>();

        mAdapter = new ScriptDataAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

    Button btn_plus = (Button) findViewById(R.id.btn_plus);
        btn_plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        EditActivity.class);
                startActivity(intent);
            }
        });
//        dao = ScriptsDB.getInstance(this).scriptsDao();
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview_main_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {

//                로컬db
//                1줄 바꿈
//                dao.deleteScript(mAdapter.mList.remove(position));

                mAdapter.mList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());


                    // refresh Scripts
                    loadScripts();
                Toast.makeText(getApplicationContext(),"메모가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLeftClicked(int position){
                Intent intent_share = new Intent(getApplicationContext(),
                        ShareScriptActivity.class);
                startActivity(intent_share);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
//        1줄 추가
//        dao = ScriptsDB.getInstance(this).scriptsDao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_load_script:
                Intent intent_loadScript = new Intent(getApplicationContext(),
                        LoadScriptActivity.class);
                startActivity(intent_loadScript);
                return true;
            case R.id.action_logout:
                Intent intent_logout = new Intent(getApplicationContext(),
                        LogoutActivity.class);
                startActivity(intent_logout);
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

//  Script 내용 불러오기 실행
    // 작성자: 이원구
    @Override
    protected void onResume() {
        super.onResume();
        loadScripts();
    }


    //     작성자: 이원구
    @Override
    public void onScriptClick(Script script) {
        Intent edit = new Intent(this, EditActivity.class);
        edit.putExtra(SCRIPT_EXTRA_Key, script.getId());
        startActivity(edit);
    }


    //     작성자: 이원구
    @Override
    public void onScriptLongClick(Script script) {
        Log.d(TAG, "onScriptLongClick"+ script.getId());
    }


//  Script 내용 불러오기
    private void loadScripts() {



//        로컬db
//        this.mArrayList = new ArrayList<>();
//        List<Script> list = dao.getScripts(); // get All scripts from DataBase
//        this.mArrayList.addAll(list);
//        this.mAdapter = new ScriptDataAdapter(mArrayList);
//        // set listener to adapter
//        this.mAdapter.setListener(this);
//        this.mRecyclerView.setAdapter(mAdapter);
    }
}
