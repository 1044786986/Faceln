package com.example.ljh.faceln;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by ljh on 2017/12/7.
 */

public class BankBalanceActivity extends AppCompatActivity{
    private TextView tvBalance;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankbalance);

        Intent intent = getIntent();
        int balance = intent.getIntExtra("balance",0);
        tvBalance = (TextView) findViewById(R.id.tvBalance);
        tvBalance.setText("当前余额为: " + balance);
    }
}
