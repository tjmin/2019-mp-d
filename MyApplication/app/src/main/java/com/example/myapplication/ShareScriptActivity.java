package com.example.myapplication;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


public class ShareScriptActivity extends Activity {

    String path = "";

    TextView sharePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_script);

        sharePath  = (TextView) findViewById(R.id.sharePath);

        //path  얻어오는 코드 들어와야함

        sharePath.setText(path);

    }


    public void mOnCopy(View v){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Share_Path", path);
        clipboardManager.setPrimaryClip(clipData);

        Toast.makeText(getApplicationContext(),"주소가 복사되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void mOnOk(View v){
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    public void onBackPerssed(){
        return;
    }
}
