package com.example.ljh.faceln;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ljh on 2017/12/8.
 */

public class BookMainActivity extends AppCompatActivity{
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private Button btBook,btBack;

    private List<String> bookList;
    private List<String> positionList;
    private List<String> list;  //要还的书籍

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmain);
        bookList = BookActivity.bookList;
        positionList = new ArrayList<String>();
        initView();
    }

    /**
     * 还书
     */
    public void Book(final JSONObject jsonObject){
        ThreadManager.startThread().execute(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient.Builder()
                        .readTimeout(3000, TimeUnit.SECONDS)
                        .writeTimeout(3000, TimeUnit.SECONDS)
                        .connectTimeout(3000, TimeUnit.SECONDS)
                        .build();
                RequestBody requestBody = new FormBody.Builder()
                         .add("book",jsonObject+"")
                        .add("type", "Book")
                        .build();
                Request request = new Request.Builder()
                        .post(requestBody)
                        .url(MainActivity.BOOKSERVLET)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();

                    }
                });
            }
    });
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewBook);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        final BookMainAdapter adapter = new BookMainAdapter();
        recyclerView.setAdapter(adapter);

        btBook = (Button) findViewById(R.id.btBook);
        btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positionList.size() == 0 || positionList == null){
                    Toast.makeText(BookMainActivity.this,"没有选择书籍",Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(BookMainActivity.this);
                    builder.setMessage("你选择了" + positionList.size() + "本书");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for(int i=0;i<positionList.size();i++){
                                //int position = Integer.parseInt(positionList.get(i));
                                String name = positionList.get(i);
                                bookList.remove(name);
                            }
                            adapter.notifyDataSetChanged();
                            Map<String,List<String>> map = new HashMap<String,List<String>>();
                            map.put("data",positionList);
                            JSONObject jsonObject = new JSONObject(map);
                            Book(jsonObject);
                            Toast.makeText(BookMainActivity.this,"还书成功",Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("取消",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });

        btBack = (Button) findViewById(R.id.btBack);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class BookMainAdapter extends RecyclerView.Adapter<BookMainAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BookMainActivity.this).inflate(R.layout.item_bookmain,null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.tvBookName.setText(bookList.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.checkBox.isChecked()){
                        holder.checkBox.setChecked(false);
                        positionList.remove(bookList.get(position));
                    }else{
                        holder.checkBox.setChecked(true);
                        positionList.add(bookList.get(position));
                    }
                }
            });

            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.checkBox.isChecked()){
                        //holder.checkBox.setChecked(false);
                        positionList.add(bookList.get(position));
                    }else{
                        //holder.checkBox.setChecked(true);
                        positionList.remove(bookList.get(position));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return bookList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tvBookName;
            CheckBox checkBox;
            public ViewHolder(View itemView) {
                super(itemView);
                tvBookName = itemView.findViewById(R.id.tvBookName);
                checkBox = itemView.findViewById(R.id.cbBook);
            }
        }
    }
}
