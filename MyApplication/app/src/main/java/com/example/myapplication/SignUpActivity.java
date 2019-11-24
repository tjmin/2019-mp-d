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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//민태준
public class SignUpActivity extends Activity {

    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";

    private EditText mEditTextId;
    private EditText mEditTextPw;
    private EditText mEditTextName;
    private TextView mTextViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mEditTextId = (EditText)findViewById(R.id.id_text);
        mEditTextPw = (EditText)findViewById(R.id.pw_text);
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
                String name = mEditTextName.getText().toString();

                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/mp/insert.php", id,pw,name);

                mEditTextId.setText("");
                mEditTextPw.setText("");
                mEditTextName.setText("");

                //finish();
            }
        });

        /*
        //cancel
        Button btn_cancel=(Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

         */
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
