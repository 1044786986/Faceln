package com.example.ljh.faceln;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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
 * Created by ljh on 2017/12/6.
 */

public class BankMainActivity extends AppCompatActivity{
    private Button btBalance,btQuit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankmain);
        initView();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = msg.obj+"";
            if(result.equals("balance")){
                int balance = msg.arg1;
                Intent intent = new Intent(BankMainActivity.this,BankBalanceActivity.class);
                intent.putExtra("balance",balance);
                startActivity(intent);
            }
        }
    };

    public void getBalance(){
        ThreadManager.startThread().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(3000, TimeUnit.SECONDS)
                        .readTimeout(3000, TimeUnit.SECONDS)
                        .writeTimeout(3000, TimeUnit.SECONDS)
                        .build();
                RequestBody requestBody = new FormBody.Builder()
                        .add("type", "GetBalance")
                        .add("id", BankActivity.id)
                        .build();
                Request request = new Request.Builder()
                        .post(requestBody)
                        .url(MainActivity.BANK_PASSWORD_LOGIN)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        NetWorkChangeReceiver.showToast(BankMainActivity.this);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String result = response.body().string();
                        if (result != "" && result != null) {
                                Message message = handler.obtainMessage();
                                message.obj = "balance";
                                message.arg1 = Integer.parseInt(result);
                                message.sendToTarget();
                        }
                    }
                });
            }
        });
    }



    public void initView(){
        btBalance = (Button) findViewById(R.id.btBalance);
        btQuit = (Button) findViewById(R.id.btQuit);
        btBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBalance();
            }
        });
        btQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
