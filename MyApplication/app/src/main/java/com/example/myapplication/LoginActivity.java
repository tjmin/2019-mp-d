package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


//작성자 : 민태준
public class LoginActivity extends AppCompatActivity {

    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";

    private EditText mEditTextId;
    private EditText mEditTextPw;
    private ArrayList<AccountData> mArrayList;
    private String mJsonString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mArrayList = new ArrayList<>();

        Button b_login = (Button) findViewById(R.id.b_login);
        b_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                mArrayList.clear();

                GetData task = new GetData();
                task.execute( "http://" + IP_ADDRESS + "/mp/getjson.php", "");
            }
        });

        Button b_signup = (Button) findViewById(R.id.b_signup);
        b_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        SignUpActivity.class);
                startActivity(intent);
            }
        });

        Button b_temp = (Button) findViewById(R.id.button_temp_login);
        b_temp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
            }
        });
    }



    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];

            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

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
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){

        String TAG_JSON = "webnautes";
        String TAG_NUM = "num";
        String TAG_ID = "id";
        String TAG_PW ="pw";
        String TAG_NAME ="name";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String num = item.getString(TAG_NUM);
                String id = item.getString(TAG_ID);
                String pw = item.getString(TAG_PW);
                String name = item.getString(TAG_NAME);

                AccountData accountData = new AccountData();

                accountData.setMember_num(num);
                accountData.setMember_id(id);
                accountData.setMember_pw(pw);
                accountData.setMember_name(name);

                mArrayList.add(accountData);
            }

            try {
                mEditTextId = (EditText) findViewById(R.id.editText);
                mEditTextPw = (EditText) findViewById(R.id.editText2);
                String mId = mEditTextId.getText().toString();
                String mPw = mEditTextPw.getText().toString();

                for (AccountData p : mArrayList) {
                    String id = p.getMember_id();
                    String pw = p.getMember_pw();
                    String name = p.getMember_name();

                    if (id.equals(mId) && pw.equals(mPw)) {
                        Log.d(TAG,"correct");
                        Intent intent2 = new Intent(getApplicationContext(),
                                MainActivity.class);
                        startActivity(intent2);
                        break;
                    }
                }
            } catch (Exception e) {

            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}