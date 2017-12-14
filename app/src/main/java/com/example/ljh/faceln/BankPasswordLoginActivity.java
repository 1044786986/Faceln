package com.example.ljh.faceln;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ljh on 2017/12/7.
 */

public class BankPasswordLoginActivity extends AppCompatActivity {
    private EditText etPassword;
    private Button btLogin;
    private ImageView ivBack;

    private final String id = "15210120222";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankpasswordlogin);
        initView();

    }

    private void Login(final String password) {
        ThreadManager.startThread().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(3000, TimeUnit.SECONDS)
                        .readTimeout(3000, TimeUnit.SECONDS)
                        .writeTimeout(3000, TimeUnit.SECONDS)
                        .build();
                RequestBody requestBody = new FormBody.Builder()
                        .add("type", "PasswordLogin")
                        .add("id", id)
                        .add("password", password)
                        .build();
                Request request = new Request.Builder()
                        .post(requestBody)
                        .url(MainActivity.BANK_PASSWORD_LOGIN)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        NetWorkChangeReceiver.showToast(BankPasswordLoginActivity.this);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Message message = handler.obtainMessage();
                        message.obj = "dismissProgressDialog";
                        message.sendToTarget();

                        String result = response.body().string();
                        if (result != "" && result != null) {
                            Message message1 = handler.obtainMessage();
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                String result1 = jsonObject.getString("result");
                                if (result1.equals("true")) {
                                    message1.obj = "true";
                                } else if (result1.equals("false")) {
                                    message1.obj = "false";
                                }
                                message1.sendToTarget();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = msg.obj.toString();
            if (result.equals("dismissProgressDialog")) {
                DialogManager.dismissProgressDialog();
            } else if (result.equals("false")) {
                DialogManager.showDialog(BankPasswordLoginActivity.this, "密码错误!");
            } else if (result.equals("true")) {
                Intent intent = new Intent(BankPasswordLoginActivity.this, BankMainActivity.class);
                startActivity(intent);
            }
        }
    };

    private void initView() {
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText() + "";
                DialogManager.showProgressDialog(BankPasswordLoginActivity.this, "请稍等");
                Login(password);
            }
        });
        ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

