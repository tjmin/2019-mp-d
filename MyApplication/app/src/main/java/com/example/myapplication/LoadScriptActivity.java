//작성자: 박재효
package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class LoadScriptActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.load_script);
    }


    public void mOnAdd(View v){
        Intent intent = new Intent(getApplicationContext(),
                EditActivity.class);
        startActivity(intent);
    }

    public void mOnCancle(View v){
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
