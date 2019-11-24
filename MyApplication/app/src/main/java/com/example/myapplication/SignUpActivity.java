package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.myapplication.LoginActivity;

//민태준
public class SignUpActivity extends Activity {

    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";

    private EditText mEditTextId;
    private EditText mEditTextPw;
    private EditText mEditTextPw2;
    private EditText mEditTextName;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mEditTextId = (EditText)findViewById(R.id.id_text);
        mEditTextPw = (EditText)findViewById(R.id.pw_text);
        mEditTextPw2 = (EditText)findViewById(R.id.pw_text2);
        mEditTextName = (EditText)findViewById(R.id.name_text);
        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);

        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());

        //signup
        Button buttonInsert = (Button)findViewById(R.id.btn_done);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = mEditTextId.getText().toString();
                String pw = mEditTextPw.getText().toString();
                String pw2 = mEditTextPw2.getText().toString();
                String name = mEditTextName.getText().toString();

                //id check
                if (id.length() < 4 || id.length() > 20) {
                    Toast.makeText(getApplicationContext(), "아이디는 4~20bytes만 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (!(pw.equals(pw2)) || pw.length() < 4 || pw.length() > 20) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG,pw + pw2 + pw.length());
                    return;
                }

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/mp/insert.php", id,pw,name);

                mEditTextId.setText("");
                mEditTextPw.setText("");
                mEditTextName.setText("");

                Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();

                finish();
            }
        });

        //id check
        Button btn_check=(Button) findViewById(R.id.buttonidcheck);
        btn_check.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                
            }
        });

        //cancel
        Button btn_cancel=(Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });


    }



    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String pw = (String)params[2];
            String name = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id + "&pw=" + pw + "&name=" + name;

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
