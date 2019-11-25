//작성자: 최지희
package com.example.myapplication;

import android.app.ProgressDialog;
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


public class EditActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "13.125.120.7";
    private static String TAG = "phptest";

    public Toolbar toolbar1;

    private EditText mEditTextTitle;
    private EditText mEditTextContents;

    public static final String SCRIPT_EXTRA_Key = "script_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edite);

        toolbar1 = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setTitle("MEMO TOGETHER");


        mEditTextTitle = findViewById(R.id.txtTitle);
        mEditTextContents = findViewById(R.id.txtContent);
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

    // 작성자: 민태준
    private void onSaveScript() {
        String userid = "temp"; ///////////////
        String sharecode = "temp"; /////////////

        String title = mEditTextTitle.getText().toString();
        String contents = mEditTextContents.getText().toString();

        InsertData task = new InsertData();
        task.execute("http://" + IP_ADDRESS + "/insertmemo.php", "userid="+userid, "&title="+title, "&contents="+contents, "&sharecode="+sharecode);

        finish();
    }


    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(EditActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String s1 = (String)params[1];
            String s2 = (String)params[2];
            String s3 = (String)params[3];
            String s4 = (String)params[4];

            String serverURL = (String)params[0];
            String postParameters = s1+s2+s3+s4;

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

}

