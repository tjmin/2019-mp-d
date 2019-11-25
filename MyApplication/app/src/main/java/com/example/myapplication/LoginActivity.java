package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

    private static String IP_ADDRESS = "13.125.120.7";
    private static String TAG = "phptest";

    private EditText mEditTextId;
    private EditText mEditTextPw;
    private ArrayList<ClassAccount> mArrayList;
    private String mJsonString;

    private ArrayList<String> iArrayList = new ArrayList<>(); //delivery id array : signup에서 아이디 중복 체크 위해 전달
    private ArrayList<String> nArrayList = new ArrayList<>(); //delivery id array : signup에서 아이디 중복 체크 위해 전달

    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setTitle("LOGIN");

        mArrayList = new ArrayList<>();

        Button b_login = (Button) findViewById(R.id.b_login);
        b_login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mArrayList.clear();
                flag = 0;
                GetData task = new GetData();
                task.execute( "http://" + IP_ADDRESS + "/getjson.php", "");
            }
        });

        Button b_signup = (Button) findViewById(R.id.b_signup);
        b_signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mArrayList.clear();
                flag = 1;

                GetData task = new GetData();
                task.execute( "http://" + IP_ADDRESS + "/getjson.php", "");
            }
        });

    }


    //php를 통해 db 데이터 가져오기
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
                Log.d(TAG,"null error");
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

    //db에서 가져온 데이터 사용
    private void showResult(){

        String TAG_JSON = "results";
        String TAG_ID = "id";
        String TAG_PW ="pw";
        String TAG_NAME ="name";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String pw = item.getString(TAG_PW);
                String name = item.getString(TAG_NAME);

                ClassAccount classAccount = new ClassAccount();

                classAccount.setMember_id(id);
                classAccount.setMember_pw(pw);
                classAccount.setMember_name(name);

                mArrayList.add(classAccount);
                iArrayList.add(id); //signup 전달용
                nArrayList.add(name); //signup 전달용
            }

            if (flag==1) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.putStringArrayListExtra("key_id", iArrayList);
                intent.putStringArrayListExtra("key_name", nArrayList);
                startActivity(intent);
            }
            else {
                int login_flag = 0;
                mEditTextId = (EditText) findViewById(R.id.editText);
                mEditTextPw = (EditText) findViewById(R.id.editText2);
                String mId = mEditTextId.getText().toString();
                String mPw = mEditTextPw.getText().toString();

                for (ClassAccount p : mArrayList) {
                    String id = p.getMember_id();
                    String pw = p.getMember_pw();
                    String name = p.getMember_name();

                    if (id.equals(mId) && pw.equals(mPw)) {
                        Toast.makeText(getApplicationContext(), "Welcome " + name, Toast.LENGTH_LONG).show();
                        Intent intent2 = new Intent(getApplicationContext(),
                                MainActivity.class);
                        startActivity(intent2);
                        login_flag = 1;
                        break;
                    }
                }
                if (login_flag == 0)
                    Toast.makeText(getApplicationContext(), "Wrong Account", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}