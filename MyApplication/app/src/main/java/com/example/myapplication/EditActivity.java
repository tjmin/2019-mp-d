//작성자: 최지희
package com.example.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    public Toolbar toolbar1;

    private EditText inputScriptTitle;
    private EditText inputScriptContents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edite);

        toolbar1 = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("MEMO TOGETHER");


        inputScriptTitle = findViewById(R.id.txtTitle);
        inputScriptContents = findViewById(R.id.txtContent);

    }

    private void setSupportActionBar(Toolbar toolbar1) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

public EditText txttitle ;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        txttitle = findViewById(R.id.txtTitle);
        if (id == R.id.action_save) {
            if(txttitle.length() == 0) {
                Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_LONG).show();
            }
            else Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();

            }
        return super.onOptionsItemSelected(item);
        }

}
