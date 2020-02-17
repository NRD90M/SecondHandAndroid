package com.example.lianghao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

// 搜索手机号码的activity
public class SearchNumberActivity extends AppCompatActivity {
    private SearchView mSearchView;
    private Button mBtnBack;
    private String province;
    private String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_number);

        Intent intent = getIntent();    // 获取传过来的省份和城市值
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        city = bundle.getString("city");
        province = bundle.getString("province");
        Toast.makeText(getApplicationContext(), province+city, Toast.LENGTH_LONG).show();

        mBtnBack = findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSearchView = findViewById(R.id.search_view);

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            // 当点击搜索按钮时触发该方法
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}
