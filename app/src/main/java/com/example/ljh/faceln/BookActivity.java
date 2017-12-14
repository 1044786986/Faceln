package com.example.ljh.faceln;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ljh on 2017/11/27.
 */

public class BookActivity extends Camera2Activity{
    private EditText etPassword;
    private Button btLogin;

    static List<String> bookList;

    private static final int TAKE_PHOTO = 0;
    private static final int FACE_LOGIN = 1;
    private static final int DISMISS = 2;
    private static final int TRUE = 3;
    private static final int FALSE = 4;
    private static final int CONNECT_ERROR = 5;

    /**
     * 发送人脸
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JSONObject FaceLogin(){
        HashMap<String,Object> hashMap = new HashMap<>();
        JSONObject jsonObject = BaiDuConfig.getAipFace().identifyUser(Arrays.asList("neusoft"),getPhotoByte(),hashMap);
        return jsonObject;
    }

    /**
     * 拍照
     */
    public void takePhoto(){
        handler.sendEmptyMessageDelayed(TAKE_PHOTO,1000);
    }

    /**
     * 执行AsyncTask
     */
    private void ExecuteAsyncTask(){
        handler.sendEmptyMessageDelayed(FACE_LOGIN,2000);
    }

    private Handler handler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TAKE_PHOTO:
                    TakePhoto();
                    DialogManager.showProgressDialog(BookActivity.this,"请稍等");
                    break;
                case FACE_LOGIN:
                    BookAsyncTask bookAsyncTask = new BookAsyncTask();
                    bookAsyncTask.execute();
                    break;
                case DISMISS:
                    DialogManager.dismissProgressDialog();
                    break;
                case TRUE:
                    DialogManager.dismissProgressDialog();
                    Intent intent = new Intent(BookActivity.this,BookMainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case FALSE:
                    DialogManager.dismissProgressDialog();
                    Toast.makeText(BookActivity.this,"找不到记录!",Toast.LENGTH_LONG).show();
                    break;
                case CONNECT_ERROR:
                    NetWorkChangeReceiver.showToast(BookActivity.this);
                    break;
            }
        }
    };

    public void GetBook(final String uid){
        ThreadManager.startThread().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(3000, TimeUnit.SECONDS)
                        .writeTimeout(3000,TimeUnit.SECONDS)
                        .connectTimeout(3000,TimeUnit.SECONDS)
                        .build();
                RequestBody requestBody = new FormBody.Builder()
                        .add("uid",uid)
                        .add("type","GetBook")
                        .build();
                 Request request = new Request.Builder()
                        .post(requestBody)
                        .url(MainActivity.BOOKSERVLET)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(CONNECT_ERROR);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        if(result != "" && result != null){
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                Log.i("aaa","jsonObjcet2 = " + jsonObject);
                                for(int i = 0;i<jsonArray.length();i++){
                                    String bookName = jsonArray.getString(i);
                                    bookList.add(bookName);
                                }
                                if(bookList.size() != 0 && bookList != null){
                                    handler.sendEmptyMessage(TRUE);
                                }else{
                                    handler.sendEmptyMessage(FALSE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        bookList = new ArrayList<String>();
        setContentView(R.layout.activity_book);

        textureView = (AutoFitTextureView) findViewById(R.id.textureViewBook);
        takePhoto();        //延迟一秒拍照
        ExecuteAsyncTask(); //延迟两秒发送数据

        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = etPassword.getText()+"";
                if(uid != "" && uid != null){
                    GetBook(uid);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //RecycleCamera();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    class BookAsyncTask extends AsyncTask<Void,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... voids) {
            return FaceLogin();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            bytes = null;   //释放资源
            Log.i("aaa","JSONObject = " + jsonObject);
            Log.i("aaa","JSONObject.length = " + jsonObject.length());
            //handler.sendEmptyMessage(DISMISS);

            try {
                String result = jsonObject+"";
                if(!result.contains("error_code")){
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    jsonObject = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject.getJSONArray("scores");
                    List<Integer> list = new ArrayList<Integer>();
                    list.add(jsonArray1.getInt(0));
                    int similarity = list.get(0);
                    if(similarity >= 88){
                        String uid = jsonObject.getString("uid");
                        GetBook(uid);
                    }else{
                        handler.sendEmptyMessage(FALSE);
                    }
                }else{
                    handler.sendEmptyMessage(FALSE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
