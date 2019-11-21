package com.example.myapplication;
//작성자 최지희

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class EditActivity extends AppCompatActivity {
    public Toolbar toolbar1;
    public Toolbar toolbar2;
    public EditText texttitle;

    @Override //툴바 구현.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
        toolbar1 = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("Memo_To");

        toolbar2 = findViewById(R.id.mytoolbar2);

        Button btn_done2=(Button) findViewById(R.id.btn_done2);
        btn_done2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        texttitle = findViewById(R.id.txtTitle);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();
            if(texttitle.length()==0){
                texttitle.setText("{무제}");
            }
            return true;
        }
//        if (id == R.id.action_back){
//        }

        return super.onOptionsItemSelected(item);
    }
}
