package com.example.lianghao.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.lianghao.R;
import com.example.lianghao.app.adapter.MsgAdapter;
import com.example.lianghao.app.bean.Msg;
import com.example.lianghao.utils.Constants;
import com.example.lianghao.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// 购买商品
public class PurchaseActivity extends AppCompatActivity {

    private RelativeLayout layout_title;
    private ImageView mIvBack;
    private TextView mTvUsername;
    private ImageView mIvMore;
    private Button mBtnPurchase;

    private ImageView mIvSend;
    private ImageView mIvPic;    // 展示图片
    private TextView mTvPrice;    // 价格
    private TextView mTvExpressFee;    // 快递费
    private RecyclerView mRvMessages;     // 发送的消息
    private EditText mEtInput;     // 输入发消息的内容
    private LinearLayout layout_edit;     // 用于追踪焦点

    private List<Msg> msgList = new ArrayList<>();
    private MsgAdapter adapter;
    private int type;     // 是发送方还是接收方

    // 记录有关商品的数据
    private int goodsPk;
    private String imageUrl;
    private Double price;
    private Double express_fee;
    private String username;
    private String email;
    private String desc;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mRvMessages.scrollToPosition(msgList.size()-1);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        initViews();
        initListeners();
        //初始化消息数据
        initMsgs();     // 这里应该改成从服务器进行拉取    这里还缺一个表
        // 初始化数据
        initData();
    }

    private void initData(){
        Bundle bundle = getIntent().getExtras();
        goodsPk = bundle.getInt("pk", -1);
        ToastUtil.showMsg(getApplicationContext(), "pk:"+goodsPk);
        price = bundle.getDouble("price", -1);
        express_fee = bundle.getDouble("express_fee", -1);
        username = bundle.getString("username", "");
        email = bundle.getString("email", "");     // 这是商品所拥者的邮箱
        imageUrl = bundle.getString("imageUrl", "");
        desc = bundle.getString("desc", "");
        Glide.with(getApplicationContext()).load(Constants.BASE_URL+imageUrl).into(mIvPic);
        mTvPrice.setText("￥"+price);
        mTvExpressFee.setText("含运费"+ express_fee +"元");
        mTvUsername.setText(username);
    }

    private void initMsgs() {

        Msg msg1 = new Msg("可以加点钱吗,路程挺远的",Msg.TYPE_SENT);
        Msg msg2 = new Msg("加5块钱可以吗",Msg.TYPE_RECEIVED);
        Msg msg3 = new Msg("好的",Msg.TYPE_SENT);
        Msg msg4 = new Msg("现在可以去拿吗",Msg.TYPE_RECEIVED);
//        Msg msg4 = new Msg("行吧，给你给你",Msg.TYPE_RECEIVED);
//        Msg msg5 = new Msg("成色怎么样",Msg.TYPE_SENT);
//        Msg msg6 = new Msg("成色99新",Msg.TYPE_RECEIVED);

        msgList.add(msg1);
        msgList.add(msg2);
        msgList.add(msg3);
        msgList.add(msg4);
//        msgList.add(msg5);
//        msgList.add(msg6);
    }

    public void initListeners(){
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBtnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 购买
//                ToastUtil.showMsg(getApplicationContext(), "TODO：购买");
                Intent intent = new Intent(getApplicationContext(), ConfirmPurchaseActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("goodsPk", goodsPk);
                bundle.putString("imageUrl", imageUrl);
                bundle.putString("desc", desc);
                bundle.putDouble("price", price);
                bundle.putDouble("express_fee", express_fee);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //创建默认的线性LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRvMessages.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        mRvMessages.setAdapter(adapter);
        type = Msg.TYPE_SENT;
        mIvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEtInput.getText().toString();
                if (!"".equals(content)){
                    Msg msg = new Msg(content,type);
                    msgList.add(msg);
                    //当有新消息时，刷新RecyclerView中的显示
                    adapter.notifyItemInserted(msgList.size()-1);
                    //将RecyclerView定位到最后一行
                    mRvMessages.scrollToPosition(msgList.size() - 1);
                    //清空输入框中的内容
                    mEtInput.setText("");
                    // 往服务器发送聊天消息
                    uploadMessage(msg, goodsPk);     // 获取消息和商品的pk
                    // 切换对话收发状态，形成对聊形式
                    type=type==Msg.TYPE_RECEIVED?Msg.TYPE_SENT:Msg.TYPE_RECEIVED;
                }
            }
        });

        layout_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEtInput.requestFocus();
                showSoftInput(getApplicationContext(), mEtInput);
                handler.sendEmptyMessageDelayed(0, 250);
            }
        });

        mRvMessages.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftInput(getApplicationContext(), mEtInput);
                return false;
            }
        });
    }

    private void uploadMessage(Msg msg, int goodsPk){
//        Log.d("uploadMessage", msg.getContent()+msg.getType()+","+goodsPk);
        String url = Constants.UPLOAD_MESSAGE_URL;
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            getSharedPreferences("user", MODE_PRIVATE);
            String curUserEmail = getSharedPreferences("user", MODE_PRIVATE).getString("email", "");

            jsonBody.put("pk", goodsPk);            // 商品pk
            jsonBody.put("content", msg.getContent());
            // 消息类型 0表示这是一条收到的消息
            if (msg.getType() == 0){
                jsonBody.put("sender", email);           // 这是商品拥有者的邮箱
                jsonBody.put("receiver", curUserEmail);           // 把当前用户的邮箱传获取
            }else{
                jsonBody.put("sender", curUserEmail);           // 这是商品拥有者的邮箱
                jsonBody.put("receiver", email);           // 把当前用户的邮箱传获取
            }
            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(), "onResponse: " + response.toString(), Toast.LENGTH_SHORT).show();
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
    public void initViews(){
        layout_title = findViewById(R.id.layout_title);
        mIvBack = findViewById(R.id.btn_back);
        mTvUsername = findViewById(R.id.tv_username);

        mIvPic = findViewById(R.id.iv_pic);
        mBtnPurchase = findViewById(R.id.btn_purchase);
        mEtInput = findViewById(R.id.et_input);
        mIvBack = findViewById(R.id.btn_back);
        mRvMessages = findViewById(R.id.rv_message);
        mIvSend = findViewById(R.id.btn_send);
        layout_edit = findViewById(R.id.layout_edit);
        mTvExpressFee = findViewById(R.id.tv_express_fee);
        mTvPrice = findViewById(R.id.tv_price);
    }

    public static void showSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
