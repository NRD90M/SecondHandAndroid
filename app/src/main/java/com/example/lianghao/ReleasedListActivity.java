package com.example.lianghao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lianghao.user.adapter.LinearAdapter;
import com.example.lianghao.user.bean.UserReleasedListBean;
import com.example.lianghao.utils.Constants;
import com.example.lianghao.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.example.lianghao.utils.Constants.RELEASED_GOODS_LIST_URL;

// 展示每个人发布了的商品的明细
public class ReleasedListActivity extends AppCompatActivity {
    private RecyclerView mRvMain;    // 用于展示每个人发布的商品
    private String email;    // 用户邮箱
    private UserReleasedListBean userReleasedListBean;     // 用户发布的商品列表信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_released_list);

        email = getIntent().getExtras().getString("email", "");
        ToastUtil.showMsg(ReleasedListActivity.this, email);

        mRvMain = findViewById(R.id.rv_main);
        // 然后就是根据email来获取这个用户所有发布的商品了
        requestReleasedGoodsList(email, new VolleyCallback(){
            @Override
            public void onSuccessResponse(String result) {
                processData(result);
            }
        });
    }

    private void processData(String json){
        userReleasedListBean = JSON.parseObject(json, UserReleasedListBean.class);
        if (userReleasedListBean.getRet_code() == 0){
            // 请求成功
            mRvMain.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRvMain.setAdapter(new LinearAdapter(ReleasedListActivity.this, userReleasedListBean.getInfo()));
        }else{
            ToastUtil.showMsg(getApplicationContext(), userReleasedListBean.getUser_info());
        }
    }
    private void requestReleasedGoodsList(String email, final VolleyCallback volleyCallback) {
        if (email.equals("")){
            ToastUtil.showMsg(getApplicationContext(), "没有接收到传入的邮箱信息");
        }else{
            // 如果有邮箱
            String url = Constants.RELEASED_GOODS_LIST_URL;
            try{
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                JSONObject jsonBody = new JSONObject();

                jsonBody.put("email", email);

                JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        volleyCallback.onSuccessResponse(response.toString());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("onErrorResponse", error.toString());
                        Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                queue.add(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public interface VolleyCallback{
        void onSuccessResponse(String result);
    }
}
