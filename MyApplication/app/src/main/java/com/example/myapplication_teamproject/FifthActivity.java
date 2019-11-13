package com.example.myapplication_teamproject;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FifthActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fifth);

        Button btn_cancel2=(Button) findViewById(R.id.btn_cancel2);
        btn_cancel2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

        findViewById(R.id.btn_done3).setOnClickListener(this);
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.btn_done3:
                new AlertDialog.Builder(this)
                    .setTitle("알람")
                    .setMessage("로그아웃 하셨습니다.")
                    .setNeutralButton("닫기",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dig, int sumthin){

                        }
                    })
                    .show();

                break;
        }
    }
}
