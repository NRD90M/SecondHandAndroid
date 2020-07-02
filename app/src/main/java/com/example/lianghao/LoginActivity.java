package com.example.lianghao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.lianghao.utils.ToastUtil;
import com.example.lianghao.utils.ValidateUtils;


import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Button mBtnRegister;
    private EditText mEtEmail;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtEmail = findViewById(R.id.et_email);
        mEtPassword = findViewById(R.id.et_password);
        mBtnLogin = findViewById(R.id.btn_login);

        mBtnRegister = findViewById(R.id.btn_register);
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();
                ToastUtil.showMsg(getApplicationContext(), email+"|"+password);
                if (email.equals("")){
                    ToastUtil.showMsg(getApplicationContext(), "请输入邮箱");
                }else if (password.equals("")){
                    ToastUtil.showMsg(getApplicationContext(), "请输入密码");
                }else if (!ValidateUtils.validate(email)){
                    ToastUtil.showMsg(getApplicationContext(), "邮箱格式不正确");
                }else if (password.length() < 6){
                    ToastUtil.showMsg(getApplicationContext(), "密码至少为6位");
                }else{
                    // 进行登录
                    login(email, password, new VolleyCallback(){
                        @Override
                        public void onSuccessResponse(String result) {
                            try{
                                JSONObject response = new JSONObject(result);
                                int ret = response.getInt("ret");
                                String desc = response.getString("desc");
                                if (ret == 1){
                                    // 密码错误或者这个用户没有注册过
                                    ToastUtil.showMsg(getApplicationContext(), desc);
//                                    finish();
                                }
                                else{
                                    String username = response.getString("username");
                                    String head_portrait_url = response.getString("head_portrait");
                                    // 密码正确
                                    ToastUtil.showMsg(getApplicationContext(), desc);
                                    mEditor.putString("email", email);
                                    mEditor.putString("username", username);
                                    mEditor.putString("head_portrait_url", head_portrait_url);
                                    // 把头像、用户名、都传回来
                                    mEditor.apply();
                                    finish();
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                }

            }
        });
    }

    public void login(String email, String password, final VolleyCallback callback) {
        String url = "http://10.0.2.2:8000/trade/login";
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("email", email);
            jsonBody.put("password", password);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccessResponse(response.toString());
//                    Log.d("onResponse", response.toString());
//                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("onErrorResponse", error.toString());
                    Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
//                    onBackPressed();    // 这是返回键
                }
            });
            queue.add(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public interface VolleyCallback{
        void onSuccessResponse(String result);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//        intent.putExtra("fragment_id", 2);    // 跳回到第2个fragment, 从0开始
//        startActivity(intent);
    }
}
