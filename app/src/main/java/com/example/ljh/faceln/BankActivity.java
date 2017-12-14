package com.example.ljh.faceln;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ljh on 2017/11/29.
 */

public class BankActivity extends Camera2Activity implements View.OnClickListener {
    private LinearLayout Layout_LoginWay;
    private Button btPasswordLogin, btFaceLogin, btInsertCard, btQuitCard;
    private ImageView ivBack, ivInsertCard;
    //private AutoFitTextureView textureView;

    static final String id = "15210120222";
    /*@Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        initView();
    }*/


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btPasswordLogin:
                Intent intent1 = new Intent(this, BankPasswordLoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.btFaceLogin:
                DialogManager.showProgressDialog(this,"请稍等");
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
                //Verify();
                break;
            case R.id.btInsertCard:
                ivInsertCard.setImageResource(R.drawable.inser_card);
                Layout_LoginWay.setVisibility(View.VISIBLE);
                TakePhoto();
                break;
            case R.id.btQuitCard:
                ivInsertCard.setImageResource(R.drawable.card);
                Layout_LoginWay.setVisibility(View.GONE);
                if(bytes != null){
                    bytes = null;
                }
                break;
            case R.id.ivBack:
                finish();
                break;
        }
    }

    /**
     * 人脸认证
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public JSONObject Verify() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("ext_fields","faceliveness");
        JSONObject jsonObject = BaiDuConfig.getAipFace().verifyUser("1", Arrays.asList("group1"), getPhotoByte(), hashMap);
        Log.i("aaa", "jsonObject = " + jsonObject);
        return jsonObject;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initView() {
        setContentView(R.layout.activity_bank);
        textureView = (AutoFitTextureView) findViewById(R.id.textureView);

        btPasswordLogin = (Button) findViewById(R.id.btPasswordLogin);
        btFaceLogin = (Button) findViewById(R.id.btFaceLogin);
        btInsertCard = (Button) findViewById(R.id.btInsertCard);
        btQuitCard = (Button) findViewById(R.id.btQuitCard);
        btPasswordLogin.setOnClickListener(this);
        btFaceLogin.setOnClickListener(this);
        btInsertCard.setOnClickListener(this);
        btQuitCard.setOnClickListener(this);

        ivInsertCard = (ImageView) findViewById(R.id.ivInsertCard);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(this);
        Layout_LoginWay = (LinearLayout) findViewById(R.id.Layout_LoginWay);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    class MyAsyncTask extends AsyncTask<Void,Void,JSONObject>{


        @Override
        protected JSONObject doInBackground(Void... params) {
            Log.i("aaa","doInBackground");
            return Verify();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                DialogManager.dismissProgressDialog();
                bytes = null;
                Log.i("aaa","jsonObject = " + jsonObject);
                Log.i("aaa","jsonObject.size() = " + jsonObject.length());

                if(jsonObject.length() >= 4){
                    JSONArray jsonArray = jsonObject.getJSONArray("result");            //相似度
                    JSONObject jsonObject2 = (JSONObject) jsonObject.get("ext_info");   //活体检测
                    Double faceliveness = Double.parseDouble(jsonObject2.getString("faceliveness"));//活体值
                    int similarity = jsonArray.getInt(0);                                     //相似值
                    Log.i("aaa","similarity = " + similarity + "        " + "faceliveness = " + faceliveness);
                    if(similarity >= 90 && faceliveness >= 0.95){
                        //DialogManager.showDialog(BankActivity.this,"验证成功");
                        Intent intent = new Intent(BankActivity.this,BankMainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        DialogManager.showDialog(BankActivity.this,"验证失败,你根本不是本人" + similarity);
                    }
                }else{
                    Log.i("aaa","jsonObject2222 = " + jsonObject);
                    DialogManager.showDialog(BankActivity.this,"验证失败,找不到此人脸数据");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RecycleCamera();
    }
}
