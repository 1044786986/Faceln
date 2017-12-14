package com.example.ljh.faceln;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends PermissionManager implements View.OnClickListener{
    private Button btBook,btHospital,btBank;
    private TextView tvRegister;

    static final String IP = "192.168.155.1";
    static final String BANK_PASSWORD_LOGIN = "http://" + IP + ":8080/Faceln/BankLogin";
    static final String BOOKSERVLET = "http://" + IP + ":8080/Faceln/BookServlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckPermission(MainActivity.this);
        initView();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btBook:
                intent = new Intent(this,BookActivity.class);
                startActivity(intent);
                break;
            case R.id.btHospital:
                break;
            case R.id.btBank:
                intent = new Intent(this,BankActivity.class);
                startActivity(intent);
                break;
            case R.id.tvRegister:
                intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void initView(){
        btBook = (Button) findViewById(R.id.btBook);
        btHospital = (Button) findViewById(R.id.btHospital);
        btBank = (Button) findViewById(R.id.btBank);
        btBook.setOnClickListener(this);
        btHospital.setOnClickListener(this);
        btBank.setOnClickListener(this);

        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(this);
    }

}
