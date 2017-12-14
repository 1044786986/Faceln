package com.example.ljh.faceln;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ljh on 2017/11/30.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername, etPassword, etPassword2, etEmail;
    private ImageView ivFace, ivDelete, ivTip, ivBack;
    private Button btUpLoad, btSubmit;

    private String username, password, password2, email;
    private Bitmap bitmap;
    private Uri uri;

    private String USERNAME_ERROR; //= getResources().getString(R.string.UsernameError);
    private String PASSWORD_ERROR; //= getResources().getString(R.string.PasswordError);
    private String PASSWORD_ERROR2;// = getResources().getString(R.string.PasswordError2);
    private String EMAIL_ERROR;// = getResources().getString(R.string.EmailError);
    private String FACE_ERROR;// = getResources().getString(R.string.FaceError);

    private static final int TAKE_PHOTO = 1;
    private static final int DISMISS = 2;
    private static final int FALSE = 3;
    private static final int TRUE = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initTip();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btUpLoad:
                if (bitmap == null) {
                    openCamera();
                }
                break;
            case R.id.btSubmit:
                DialogManager.showProgressDialog(this,"正在注册,请稍候");
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
                break;
            case R.id.ivDelete:
                bitmap = null;
                hideImageView();
                break;
            case R.id.ivBack:
                finish();
                if (bitmap != null) {
                    bitmap.recycle();
                }
                break;
        }
    }

    /*public void test(){
        ThreadManager.startThread().execute(new Runnable() {
            @Override
            public void run() {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                JSONObject jsonObject = BaiDuConfig.getAipFace().detect(TransformationManager.BitmapToByte(bitmap),hashMap);
                Log.i("aaa","JsonObject = " + jsonObject);
            }
        });
    }*/

    public JSONObject SendRegister(){
        String username = etUsername.getText() + "";
        String password = etPassword.getText() + "";
        HashMap<String,String> map = new HashMap<String,String>();
        //map.put("action_type","replace");
        JSONObject jsonObject
                = BaiDuConfig.getAipFace().addUser(password,username, Arrays.asList("neusoft"),TransformationManager.BitmapToByte(bitmap),map);
        return jsonObject;
    }

    public void Register() {
        String username = etUsername.getText() + "";
        String password = etPassword.getText() + "";
        String password2 = etPassword2.getText() + "";
        String email = etEmail.getText() + "";

        if (username == null || username == "") {
            showTipToast(USERNAME_ERROR);
        } else if (password == null || password == "") {
            showTipToast(PASSWORD_ERROR);
        } else if (password2 == null || password2 == "") {
            showTipToast(PASSWORD_ERROR);
        } else if (!password.equals(password2)) {
            showTipToast(PASSWORD_ERROR2);
        } else if (email == null || email == "") {
            showTipToast(EMAIL_ERROR);
        } else if (bitmap == null) {
            showTipToast(FACE_ERROR);
        } else {
            Register();
        }
    }

    public void showTipToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    /**
     * 打开相机
     */
    public void openCamera() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Faceln" + getTimeMillis() + ".jpg");
        if (Build.VERSION.SDK_INT >= 23) {
            uri = FileProvider.getUriForFile(this, "com.example.ljh.faceln.fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("camerasensortype",2);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            bitmap = CompressManager.CompressFromCamera(this, uri);
            ivFace.setImageBitmap(bitmap);
            showImageView();
        }
    }

    /**
     * 获取当前的时间戳
     */
    public String getTimeMillis() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * 显示删除按钮和上传的图片
     */
    public void showImageView() {
        if (bitmap != null) {
            ivFace.setVisibility(View.VISIBLE);
            ivDelete.setVisibility(View.VISIBLE);
            ivTip.setImageResource(R.drawable.ic_check_box_blue_24dp);
        }
    }

    /**
     * 隐藏删除按钮和上传的图片
     */
    public void hideImageView() {
        if (bitmap == null) {
            ivFace.setVisibility(View.GONE);
            ivDelete.setVisibility(View.GONE);
            ivTip.setImageResource(R.drawable.ic_error_red_24dp);
        }
    }

    public void initTip() {
         USERNAME_ERROR = getResources().getString(R.string.UsernameError);
         PASSWORD_ERROR = getResources().getString(R.string.PasswordError);
         PASSWORD_ERROR2 = getResources().getString(R.string.PasswordError2);
         EMAIL_ERROR = getResources().getString(R.string.EmailError);
         FACE_ERROR = getResources().getString(R.string.FaceError);
    }

    public void initView() {
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etPassword2 = (EditText) findViewById(R.id.etPassword2);
        etEmail = (EditText) findViewById(R.id.etEmail);

        ivFace = (ImageView) findViewById(R.id.ivFace);
        ivDelete = (ImageView) findViewById(R.id.ivDelete);
        ivTip = (ImageView) findViewById(R.id.ivTip);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivDelete.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        btUpLoad = (Button) findViewById(R.id.btUpLoad);
        btSubmit = (Button) findViewById(R.id.btSubmit);
        btUpLoad.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(bitmap != null){
            bitmap.recycle();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TRUE:
                    Intent intent = new Intent(RegisterActivity.this,RegisterSuccessActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case FALSE:
                    DialogManager.showDialog(RegisterActivity.this,"注册失败");
                    break;
                case DISMISS:
                    DialogManager.dismissProgressDialog();
                    break;
            }
        }
    };

    class MyAsyncTask extends AsyncTask<Void,Void,JSONObject>{

        @Override
        protected JSONObject doInBackground(Void... params) {
            return SendRegister();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                handler.sendEmptyMessage(DISMISS);
                Log.i("aaa","JSONObject = " + jsonObject);
                String log_id = jsonObject.getString("log_id");
                if(log_id == "" || log_id == null){
                    handler.sendEmptyMessage(FALSE);
                }else{
                    handler.sendEmptyMessage(TRUE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
