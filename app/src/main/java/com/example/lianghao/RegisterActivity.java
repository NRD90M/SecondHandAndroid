package com.example.lianghao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lianghao.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText mEtEmail;    // 输入邮箱
    private EditText mEtCode;     // 验证码输入
    private Button mBtnGetCode;    // 获取验证码按钮
    private EditText mEtPassword;    // 输入密码
    private Button mBtnFinish;     // 完成注册按钮
    private String code;     // 记录一下验证码
    private Thread thread;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    int leftWaitTime;     // 剩余可以发送验证码的时间
    private Message message;
    private static final int MSG_COUNT_DOWN = 0x0001;    // 验证码60秒倒计时


    // 设置邮箱的正则表达式
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_COUNT_DOWN:
                    if (leftWaitTime > 0) {
                        leftWaitTime -= 1;
                        mBtnGetCode.setText(leftWaitTime + "s后重新获取");
                        message = mHandler.obtainMessage(MSG_COUNT_DOWN);
                        mHandler.sendMessageDelayed(message, 1000);
                    } else {
                        mBtnGetCode.setText("获取验证码");
                        mBtnGetCode.setEnabled(true);    // 设置按钮重新可以被点击
                    }

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEtEmail = findViewById(R.id.et_email);
        mEtCode = findViewById(R.id.et_code);
        mBtnGetCode = findViewById(R.id.btn_get_code);
        mEtPassword = findViewById(R.id.et_password);
        mBtnFinish = findViewById(R.id.btn_finish);

        mSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        // 对发送验证码按钮设置监听事件
        mBtnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEtEmail.getText().toString();    // 要先检查一下邮箱格式是否正确
                if (email.equals(""))    // 如果邮箱是空的
                    ToastUtil.showMsg(getApplicationContext(), "请输入邮箱");
//                    Toast.makeText(getApplicationContext(), "请输入邮箱", Toast.LENGTH_LONG).show();
                else {
                    // 用正则表达式检查一下邮箱是否是对的
                    boolean isValid = validate(email);
                    if (isValid) {
//                        Toast.makeText(getApplicationContext(), "邮箱格式正确", Toast.LENGTH_LONG).show();
                        // 发送验证码
                        code = generateCode(6);
                        leftWaitTime = 10;
//                        ToastUtil.showMsg(getApplicationContext(), code);
                        mBtnGetCode.setEnabled(false);    // 不可以点击
                        message = Message.obtain();
                        message.what = MSG_COUNT_DOWN;
                        mHandler.sendMessage(message);
                        // 发送邮件
                        sendCode(email, code, new VolleyCallback() {
                            @Override
                            public void onSuccessResponse(String result) {
                                try{
                                    JSONObject response = new JSONObject(result);
                                    int ret = response.getInt("ret");
                                    String desc = response.getString("desc");
                                    if (ret == 2){
                                        ToastUtil.showMsg(getApplicationContext(), desc);
                                        finish();
                                    }
                                    else{
                                        ToastUtil.showMsg(getApplicationContext(), desc);
                                    }
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        ToastUtil.showMsg(getApplicationContext(), "邮箱格式有误");
                    }
                }
            }
        });

        // 完成注册按钮
        mBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = mEtPassword.getText().toString();    // 要先检查一下密码格式是否正确
                String email = mEtEmail.getText().toString();    // 要先检查一下邮箱格式是否正确
                String inputCode = mEtCode.getText().toString();     // 验证码
                if (email.equals(""))    // 如果邮箱是空的
                    ToastUtil.showMsg(getApplicationContext(), "请输入邮箱");
                else {
                    // 如果邮箱已经输入了
                    if (inputCode.equals("")) {
                        ToastUtil.showMsg(getApplicationContext(), "请输入验证码");
                    } else {
                        // 如果验证码也输入了
                        if (password.equals("")) {
                            ToastUtil.showMsg(getApplicationContext(), "请输入密码");
                        } else {
                            // 如果密码也输入了
                            if (password.length() < 6) {
                                ToastUtil.showMsg(getApplicationContext(), "密码至少为6位");
                            } else {
                                ToastUtil.showMsg(getApplicationContext(), "密码格式正确");
                                if (inputCode.equals(code)) {
//                                    ToastUtil.showMsg(getApplicationContext(), "验证码正确");
                                    register(email, password);
                                    ToastUtil.showMsg(getApplicationContext(), "注册成功");
                                    finish();
//                                    Intent intent = new Intent(RegisterActivity.this, FirstFragment.class);
//                                    startActivity(intent);
                                    // 直接进行注册
                                } else {
                                    ToastUtil.showMsg(getApplicationContext(), "验证码错误");
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    public void login(String email){
        mEditor.putString("email", email);
        mEditor.apply();
    }
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    // 生成一个验证码
    public String generateCode(int length) {
        // length标使生成的验证码的长度
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            result.append(random.nextInt(10));
        }
        return result.toString();
    }

    public void register(String email, String password){
        String url = "http://10.0.2.2:8000/trade/register";
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("email", email);
            jsonBody.put("password", password);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("onResponse", response.toString());

//                    Toast.makeText(getApplicationContext(), "Response:  " + response.toString(), Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("onErrorResponse", error.toString());
//                    onBackPressed();    // 这是返回键
                }
            });
            queue.add(jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void sendCode(String email, String code, final VolleyCallback callback) {
        String s;
        String url = "http://10.0.2.2:8000/trade/send_auth_code";
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("email", email);
            jsonBody.put("code", code);

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
                    Toast.makeText(getApplicationContext(), "Error:  " + error.toString(), Toast.LENGTH_SHORT).show();
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
}



