//작성자: 최지희
package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class EditActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "13.125.120.7";
    private static String TAG = "edittest";


    public Toolbar toolbar1;

    private EditText inputScriptTitle;
    private EditText inputScriptContents;

    public static final String SCRIPT_EXTRA_Key = "script_id";

    private int kid;
    private String kname, ktitle, kcontents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edite);

        toolbar1 = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("MEMO TOGETHER");

        inputScriptTitle = findViewById(R.id.txtTitle);
        inputScriptContents = findViewById(R.id.txtContent);

        Intent intent = getIntent();
        kid = intent.getIntExtra("key_id",0); //메모를 "생성"할 때, id를 main activity에서 전달받음
        kname = intent.getStringExtra("key_name"); //메모를 "생성"할 때, id를 main activity에서 전달받음
        ktitle = intent.getStringExtra("key_title"); //메모를 "수정"할 때, id를 main activity에서 전달받음
        kcontents = intent.getStringExtra("key_contents"); //메모를 "수정"할 때, id를 main activity에서 전달받음

        inputScriptTitle.setText(ktitle);
        inputScriptContents.setText(kcontents);
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
        EditText editText_title, editText_contents;

        editText_title = findViewById(R.id.txtTitle);
        editText_contents = findViewById(R.id.txtContent);
        if(editText_title.length() == 0) {
            Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else if (editText_contents.length() == 0) {
            Toast.makeText(getApplicationContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_SHORT).show();
            onSaveScript();
        }

        return super.onOptionsItemSelected(item);
    }




// 작성자: 이원구
    private void onSaveScript() {

        String id = Integer.toString(kid);
        String title = inputScriptTitle.getText().toString();
        String contents = inputScriptContents.getText().toString();
        String sharecode = randomNumber().toString();

        InsertData task = new InsertData();

        if (kname == null) { //수정인 경우
            task.execute("http://" + IP_ADDRESS + "/modifymemo.php", "id=" + id, "&title="+title, "&contents="+contents);
        }
        else { //추가인 경우
            task.execute("http://" + IP_ADDRESS + "/insertmemo.php", "userid="+kname, "&title="+title,"&contents="+contents, "&sharecode="+sharecode);
        }
        finish();
    }



    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //progressDialog = ProgressDialog.show(EditActivity.this,                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = (String)params[0];
            String postParameters = "";
            for (int i=1; i<params.length; i++) {
                postParameters = postParameters.concat(params[i]);
                Log.d(TAG,params[i]);
            }
            Log.d(TAG,"tttttttttt" + postParameters);

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }


    private StringBuffer randomNumber(){
        //10자리 영문+숫자 랜덤코드 만들기
        Random rnd =new Random();
        StringBuffer buf =new StringBuffer();
        for(int i=0;i<10;i++){
            // rnd.nextBoolean() 는 랜덤으로 true, false 를 리턴. true일 시 랜덤 한 소문자를, false 일 시 랜덤 한 숫자를 StringBuffer 에 append 한다.
            if(rnd.nextBoolean()){
                buf.append((char)((int)(rnd.nextInt(26))+97));
            }else{
                buf.append((rnd.nextInt(10)));
            }
        }
        return buf;
    }
}

