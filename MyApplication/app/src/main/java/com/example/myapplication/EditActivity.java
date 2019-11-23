//작성자: 최지희
package com.example.myapplication;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.ScriptsDB;
import com.example.myapplication.ScriptsDao;

public class EditActivity extends AppCompatActivity {

    public Toolbar toolbar1;

    private EditText inputScriptTitle;
    private EditText inputScriptContents;

    public static final String SCRIPT_EXTRA_Key = "script_id";
    private ScriptsDao dao;
    private Script temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edite);

        toolbar1 = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("Memo_To");


        inputScriptTitle = findViewById(R.id.txtTitle);
        inputScriptContents = findViewById(R.id.txtContent);

        dao = ScriptsDB.getInstance(this).scriptsDao();
        if (getIntent().getExtras() != null) {
            int id = getIntent().getExtras().getInt(SCRIPT_EXTRA_Key, 0);
            temp = dao.getScriptById(id);
            inputScriptTitle.setText(temp.getScriptTitle());
            inputScriptContents.setText(temp.getScriptContents());
        } else
        {
            inputScriptTitle.setFocusable(true);
            inputScriptContents.setFocusable(true);
        }
    }

    private void setSupportActionBar(Toolbar toolbar1) {

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
        if (id == R.id.action_save) {
            Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();
            onSaveScript();
        }
        return super.onOptionsItemSelected(item);
    }


// 작성자: 이원구
    private void onSaveScript() {
        String textTitle = inputScriptTitle.getText().toString();
        String textContents = inputScriptContents.getText().toString();

//      If scrip is same -> update or upload a new script
        temp.setScriptTitle(textTitle);
        temp.setScriptContents(textContents);

        if(temp.getId() == -1)
            dao.insertScript(temp);
        else dao.updateScript(temp);


        finish();
        }
    }
