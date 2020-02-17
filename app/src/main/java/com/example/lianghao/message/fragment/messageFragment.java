package com.example.lianghao.message.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.lianghao.R;
import com.example.lianghao.ReleasedListActivity;
import com.example.lianghao.message.bean.MessageListBean;
import com.example.lianghao.message.adapter.LinearAdapter;
import com.example.lianghao.user.bean.UserReleasedListBean;
import com.example.lianghao.utils.Constants;
import com.example.lianghao.utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class messageFragment extends Fragment {
    private RecyclerView mRvMain;
    private String email;   // 当前登录用户的邮箱
    private MessageListBean messageListBean;        // bean

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment, container, false);
        mRvMain = view.findViewById(R.id.rv_main);

        email = view.getContext().getSharedPreferences("user", Context.MODE_PRIVATE).getString("email", "");
        ToastUtil.showMsg(getContext(), email);
        // 然后就是根据email来获取这个用户所有发布的商品了
        requestMessageList(email, new VolleyCallback(){
            @Override
            public void onSuccessResponse(String result) {
                processData(result);
//                ToastUtil.showMsg(getContext(), result);
            }
        });

        return view;
    }

    private void processData(String json){
        messageListBean = JSON.parseObject(json, MessageListBean.class);
        mRvMain.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvMain.setAdapter(new LinearAdapter(getContext(), messageListBean));
    }

    private void requestMessageList(String email, final VolleyCallback volleyCallback) {
        if (email.equals("")) {
            ToastUtil.showMsg(getContext(), "没有接收到传入的邮箱信息");
        }
        else{
            // 如果有邮箱
            String url = Constants.GET_MESSAGE_LIST_URL;
            try {
                RequestQueue queue = Volley.newRequestQueue(getContext());
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
                        Toast.makeText(getContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
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
