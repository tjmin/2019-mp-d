//작성자: 박재효
package com.example.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;



public class MainActivity extends AppCompatActivity{


    private ScriptDataAdapter mAdapter;

    SwipeController swipeController = null;


    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MEMO TOGETHER");

        setupRecyclerView();


        mRecyclerView = findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));



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
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview_main_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                mAdapter.mList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mAdapter.getItemCount());


                Toast.makeText(getApplicationContext(),"메모가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLeftClicked(int position){
                show_share();
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
                show_load();
                return true;
            case R.id.action_logout:
                show_logout();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    void show_share(){
        final String path = "(공유용 주소)";

        //path 얻어오는 코드 or 함수 들어있어야함

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("공유할 메모 주소");
        builder.setMessage(path);
        builder.setPositiveButton("복사",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Share_Path", path);
                        clipboardManager.setPrimaryClip(clipData);

                        Toast.makeText(getApplicationContext(),"클립보드에 주소가 복사되었습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    void show_logout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그아웃");
        builder.setMessage("해당 아이디에서 로그아웃 하시겠습니까?");
        builder.setPositiveButton("로그아웃",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"로그아웃 되었습니다.",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"로그아웃이 취소 되었습니다..",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    void show_load()
    {
        final EditText edittext = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("공유 메모 추가");
        builder.setMessage("공유 받은 메모 주소를 입력해주세요.");
        builder.setView(edittext);
        builder.setPositiveButton("추가",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "메모가 추가 되었습니다." ,Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "존재하지 않거나 잘못된 주소입니다. 주소를 확인해주세요." ,Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "이미 추가된 메모입니다." ,Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }
}
