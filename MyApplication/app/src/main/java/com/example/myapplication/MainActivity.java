//작성자: 박재효
package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;

import com.example.myapplication.ScriptListener;
import com.example.myapplication.ScriptsDB;
import com.example.myapplication.ScriptsDao;

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

import static com.example.myapplication.EditActivity.SCRIPT_EXTRA_Key;


public class MainActivity extends AppCompatActivity implements ScriptListener {

    private static String IP_ADDRESS = "13.125.120.7";
    private static final String TAG = "MainActivity";

    private ArrayList<Script> mArrayList; //메모 정보가 담길 array
    private ArrayList<ClassShare> sArrayList; //공유 메모의 정보가 담길 array

    private ScriptDataAdapter mAdapter;

    SwipeController swipeController = null;

    private RecyclerView mRecyclerView;

    private String kname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MEMO TOGETHER");

        setupRecyclerView();

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_main_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mArrayList = new ArrayList<>();
        sArrayList = new ArrayList<>();
        mAdapter = new ScriptDataAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        kname = getIntent().getStringExtra("key_name"); //id를 main activity에서 전달받음

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        Button btn_plus = (Button) findViewById(R.id.btn_plus);
        btn_plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),
                        EditActivity.class);
                intent.putExtra("key_name", kname);
                startActivity(intent);
            }
        });
    }


    private void setupRecyclerView() {
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview_main_list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                Script script = mAdapter.getScript(position);
                String id = Integer.toString(script.getId());
                String sharecode = script.getScriptSharecode();
                String mid = script.getUserId(); //메모 작성자 이름

                InsertData task = new InsertData();
                if (mid.equals(kname)) { //내 메모면
                    task.execute("http://" + IP_ADDRESS + "/deletememo.php", "id=" + id);
                }
                else { //공유받은 메모면
                    task.execute("http://" + IP_ADDRESS + "/deleteshare.php", "memoid=" + sharecode + "&shareid=" + kname);
                }

                mAdapter.mList.remove(position);
                loadScripts();
                Toast.makeText(getApplicationContext(),"메모가 삭제 되었습니다.", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLeftClicked(int position){
                Script script = mAdapter.getScript(position);
                String share = script.getScriptSharecode();

                show_share(share);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_load_script:
                show_load();
                return true;
            case R.id.action_logout:
                show_logout();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    void show_share(String share){
        final String path = share;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("공유할 메모 주소");
        builder.setMessage(path);
        builder.setPositiveButton("복사",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Share_Path", path);
                        clipboardManager.setPrimaryClip(clipData);

                        Toast.makeText(getApplicationContext(),"클립보드에 주소가 복사되었습니다.",Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    void show_logout()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그아웃");
        builder.setMessage("해당 아이디에서 로그아웃 하시겠습니까?");
        builder.setPositiveButton("로그아웃",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"로그아웃 되었습니다.",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),
                                LoginActivity.class);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),"로그아웃이 취소 되었습니다..",Toast.LENGTH_LONG).show();
                    }
                });
        builder.show();
    }

    void show_load()
    {
        final EditText edittext = new EditText(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("공유 메모 추가");
        builder.setMessage("공유 받은 메모 주소를 입력해주세요.");
        builder.setView(edittext);
        builder.setPositiveButton("추가",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //EditText mEditTextShare = (EditText) findViewById(R.id.inputPath);
                        //String sharecode2 = mEditTextShare.getText().toString();
                        String sharecode = edittext.getText().toString();
                        Log.d(TAG,sharecode);

                        InsertData task = new InsertData();
                        task.execute("http://" + IP_ADDRESS + "/insertshare.php", "memoid="+sharecode,"&shareid="+kname);

                        Toast.makeText(getApplicationContext(), "메모가 추가 되었습니다." ,Toast.LENGTH_LONG).show();
                        loadScripts();

                        //Toast.makeText(getApplicationContext(), "존재하지 않거나 잘못된 주소입니다. 주소를 확인해주세요." ,Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "이미 추가된 메모입니다." ,Toast.LENGTH_LONG).show();
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }


    // 작성자: 이원구
    @Override
    protected void onResume() {
        super.onResume();
        loadScripts();
    }


    //     작성자: 이원구
    @Override
    public void onScriptClick(Script script) {
        Intent edit = new Intent(this, EditActivity.class);

        edit.putExtra("key_id", script.getId());
        Log.d(TAG,Integer.toString(script.getId()));
        edit.putExtra("key_title", script.getScriptTitle());
        edit.putExtra("key_contents", script.getScriptContents());

        startActivity(edit);
    }


    //     작성자: 이원구
    @Override
    public void onScriptLongClick(Script script) {
        Log.d(TAG, "onScriptLongClick"+ script.getId());
    }


    //     작성자: 이원구
    private void loadScripts() {
        mArrayList.clear();
        sArrayList.clear();

        mAdapter.setListener(this);

        GetShare task = new GetShare();
        task.execute( "http://" + IP_ADDRESS + "/getshare.php", "");
        GetData task2 = new GetData();
        task2.execute( "http://" + IP_ADDRESS + "/getmemo.php", "");
    }


    private String mJsonString;

    private class GetShare extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);
            if (result == null) {
                Log.d(TAG, "null error");
            } else {
                mJsonString = result;
                getShare();
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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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

    private void getShare () {
        String TAG_JSON = "results";
        String TAG_MEMOID = "memoid";
        String TAG_SHAREID = "shareid";
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                String memoid = item.getString(TAG_MEMOID);
                String shareid = item.getString(TAG_SHAREID);

                ClassShare memoScript = new ClassShare();
                if (shareid.equals(kname)) {
                    memoScript.setmemo_id(memoid);
                    memoScript.setshare_id(shareid);
                    sArrayList.add(memoScript);
                }

            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);
            if (result == null) {
                Log.d(TAG, "null error");
            } else {
                mJsonString = result;
                getMemo();
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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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

    private void getMemo () {

        String TAG_JSON = "results";
        String TAG_ID = "id";
        String TAG_USERID = "userid";
        String TAG_TITLE = "title";
        String TAG_CONTENTS = "contents";
        String TAG_SHARECODE = "sharecode";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                int id = item.getInt(TAG_ID);
                String userid = item.getString(TAG_USERID);
                String title = item.getString(TAG_TITLE);
                String contents = item.getString(TAG_CONTENTS);
                String sharecode = item.getString(TAG_SHARECODE);

                Script memoScript = new Script();
                if (userid.equals(kname)) { //자신이 만든 메모면
                    memoScript.setId(id);
                    memoScript.setUserId(userid);
                    memoScript.setScriptTitle(title);
                    memoScript.setScriptContents(contents);
                    memoScript.setScriptSharecode(sharecode);
                    mArrayList.add(memoScript);
                } //19dudt0d3t
                else { //다른 사람이 만든 메모면
                    for (int j = 0; j < sArrayList.size(); j++) {
                        ClassShare share = sArrayList.get(j);
                        String scode = share.getmemo_id();
                        String sname = share.getshare_id();
                        if (kname.equals(sname) && sharecode.equals(scode)) { //근데 share 의 name이 같고, share 번호도 같을 때
                            memoScript.setId(id);
                            memoScript.setUserId(userid);
                            memoScript.setScriptTitle(title);
                            memoScript.setScriptContents(contents);
                            memoScript.setScriptSharecode(sharecode);
                            mArrayList.add(memoScript);
                            Log.d(TAG,"Connect!" +scode + " " + sharecode + " " + kname +" " + sname);
                            break;
                        }
                        Log.d(TAG,scode + " " + sharecode + " " + kname +" " + sname);

                    }
                }

                mAdapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }



    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,"Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = (String)params[0];
            String postParameters = "";
            for (int i=1; i<params.length; i++) {
                postParameters = postParameters.concat(params[i]);
            }

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
