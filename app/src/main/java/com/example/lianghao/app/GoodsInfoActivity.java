package com.example.lianghao.app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.lianghao.LoginActivity;
import com.example.lianghao.R;
import com.example.lianghao.app.bean.GoodsInfoBean;
import com.example.lianghao.utils.Constants;
import com.example.lianghao.utils.DensityUtil;
import com.example.lianghao.utils.GoodsUtils;
import com.example.lianghao.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class GoodsInfoActivity extends AppCompatActivity {

    private LinearLayout layout_title;
    private ImageView mIvHeadPortrait;    // 头像
    private TextView mTvUsername;
    private TextView mTvLocation;
    private TextView mTvPrice;
    private TextView mTvDesc;
    private ImageView mIvPic;
    private Button mBtnZan;    // 点赞
    private Button mBtnComment;    // 评论
    private Button mBtnCollect;    // 收藏
    private Button mBtnCart;     // 购买或者管理
    private SharedPreferences mSharedPreferences;
    private String curUserEmail;
    private GoodsInfoBean goodsInfoBean;    // 用于存储网络返回信息
    private int goodsPk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_info);

        layout_title = findViewById(R.id.layout_title);
        mIvHeadPortrait = findViewById(R.id.iv_head_portrait);
        mTvUsername = findViewById(R.id.tv_username);
        mTvLocation = findViewById(R.id.tv_location);
        mTvPrice = findViewById(R.id.tv_price);
        mIvPic = findViewById(R.id.iv_pic);
        mTvDesc = findViewById(R.id.tv_desc);


        mBtnZan = findViewById(R.id.btn_zan);
        mBtnComment = findViewById(R.id.btn_comment);
        mBtnCollect = findViewById(R.id.btn_collect);
        mBtnCart = findViewById(R.id.btn_cart);

        mSharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        curUserEmail = mSharedPreferences.getString("email", "null");


        goodsPk = getIntent().getExtras().getInt("pk", -1);
//        ToastUtil.showMsg(GoodsInfoActivity.this, "商品pk:" + pk);
        requestGoodsInfo(goodsPk, new VolleyCallback(){
            @Override
            public void onSuccessResponse(String result) {
//                ToastUtil.showMsg(getApplicationContext(), "onSuccessResponse:"+result);
                processData(result);
            }
        });
    }

    private void processData(String json){
        // 处理网络请求返回的数据
        goodsInfoBean = JSON.parseObject(json, GoodsInfoBean.class);
        GoodsInfoBean.InfoBean info = goodsInfoBean.getInfo();
        // 商品所属用户的头像
        Glide.with(GoodsInfoActivity.this).load(Constants.BASE_URL+info.getUser_head_portrait()).into(mIvHeadPortrait);
        // 商品展示图片单张
        Glide.with(GoodsInfoActivity.this).load(Constants.BASE_URL+info.getDisplay_image()).into(mIvPic);
        // 用户名
        mTvUsername.setText(info.getUsername());
        mTvLocation.setText(info.getLocation());
        mTvPrice.setText(String.valueOf(info.getPrice()));
        mTvDesc.setText(info.getDesc());
        // 判断一下这个商品是不是属于当前登录的用户
        if (curUserEmail.equals(info.getEmail())){
            // 如果这个商品属于当前登录的用户的话
            initMyOwnListeners();
        }else{
            initOtherListeners();     // 初始化监听器
        }
    }

    public void requestGoodsInfo(int pk, final VolleyCallback callback){
        String url = Constants.GOODS_INFO_URL;
        try {
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("pk", pk);

            JsonObjectRequest jsonObject = new JsonObjectRequest(Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    callback.onSuccessResponse(response.toString());
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

    // 如果这个商品属于当前登录用户的时候
    private void initMyOwnListeners(){
        layout_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(GoodsInfoActivity.this, "点击标题栏，跳转到用户详情信息");
            }
        });

        mBtnZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(GoodsInfoActivity.this, "不能给自己点赞哦~");
            }
        });

        mBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO 评论接口

                ToastUtil.showMsg(GoodsInfoActivity.this, "评论"+goodsPk);
//                ToastUtil.showMsg(GoodsInfoActivity.this);

            }
        });

        mBtnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(GoodsInfoActivity.this, "不能收藏自己的宝贝哦~");
            }
        });

        //
        mBtnCart.setText("管理");
        mBtnCart.setTextColor((Color.rgb(0, 0, 0)));
        mBtnCart.setCompoundDrawables(null, null, null, null);     // 移除购物车按钮
        mBtnCart.setBackgroundResource(R.drawable.bg_manage_btn);
        mBtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil.showMsg(GoodsInfoActivity.this, "TODO:编辑和删除功能");
                showBottomDialog();
            }
        });
    }
    private void initOtherListeners(){
        layout_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(GoodsInfoActivity.this, "点击标题栏，跳转到用户详情信息");
            }
        });

        mBtnZan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(GoodsInfoActivity.this, "点赞");
            }
        });

        mBtnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(GoodsInfoActivity.this, "评论");
            }
        });

        mBtnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(GoodsInfoActivity.this, "收藏");
            }
        });

        mBtnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 这里要先判断一下是否登录
                ToastUtil.showMsg(GoodsInfoActivity.this, "当前用户:"+curUserEmail);
                if (curUserEmail.equals("null")){
                    Intent intent = new Intent(GoodsInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
//                ToastUtil.showMsg(GoodsInfoActivity.this, "TODO:跳转到购买界面");
                    Intent intent = new Intent(GoodsInfoActivity.this, PurchaseActivity.class);
                    Bundle bundle = new Bundle();
                    GoodsInfoBean.InfoBean info = goodsInfoBean.getInfo();
                    bundle.putInt("pk", goodsPk);
                    bundle.putString("imageUrl", info.getDisplay_image());     // 展示图片的Url
                    bundle.putDouble("price", info.getPrice());
                    bundle.putDouble("express_fee", 0.0);      // 快递费
                    bundle.putString("username", info.getUsername());      // 快递费
                    bundle.putString("email", info.getEmail());      //
                    bundle.putString("desc", info.getDesc());      // 快递费
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    public interface VolleyCallback{
        void onSuccessResponse(String result);
    }

    // 当点击管理按钮的时候，从底部弹出对话框
    private void showBottomDialog(){
        final Dialog bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_content_circle, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentView.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - DensityUtil.dp2px(this, 16f);
        params.bottomMargin = DensityUtil.dp2px(this, 8f);
        contentView.setLayoutParams(params);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

        // 设置一下点击事件
        TextView tv_cancel = contentView.findViewById(R.id.tv_cancel);
        // 取消按钮
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "取消", Toast.LENGTH_SHORT).show();
                bottomDialog.dismiss();    // 把对话框关掉
            }
        });


        TextView tv_delete = contentView.findViewById(R.id.tv_delete);
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();    // 先把底部对话框关掉先
                // 这里也要弹出一个对话框，确实是否真的要删除
                // 检查完整之后，弹出对话框, 确认是否删除
                final AlertDialog.Builder builder = new AlertDialog.Builder(GoodsInfoActivity.this);
                View view = LayoutInflater.from(GoodsInfoActivity.this).inflate(R.layout.layout_dialog, null);
                Button btn_confirm = view.findViewById(R.id.dialog_btn_confirm);     // 确定
                Button btn_cancel = view.findViewById(R.id.dialog_btn_cancel);       // 取消
                builder.setView(view);
                final AlertDialog dialog = builder.show();
                btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.showMsg(getApplicationContext(), "确定");

                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();     // 关闭对话框
                    }
                });
            }
        });

        // 编辑
        TextView tv_edit = contentView.findViewById(R.id.tv_edit);
        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "TODO:编辑还没做好", Toast.LENGTH_SHORT).show();
                // 把当前的信息携带到发布fragment
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                GoodsInfoBean.InfoBean info = goodsInfoBean.getInfo();
//
//                bundle.putBoolean("isUpdate", true);     // 告诉那个activity要对商品信息进行更新
//                bundle.putInt("pk", goodsPk);
//                bundle.putString("imageUrl", info.getDisplay_image());     // 展示图片的Url
//                bundle.putString("desc", info.getDesc());
//                bundle.putString("province", "浙江");
//                bundle.putString("city", "杭州");
//                bundle.putDouble("price", info.getPrice());
//                intent.putExtras(bundle);
//                setResult(10, intent);
//                finish();
            }
        });

    }

    // 从登录跳回这个activity的时候要弄一下
    public void updateCartButton(){
        curUserEmail = mSharedPreferences.getString("email", "null");
        GoodsInfoBean.InfoBean info = goodsInfoBean.getInfo();
        // 判断一下这个商品是不是属于当前登录的用户
        if (curUserEmail.equals(info.getEmail())){
            // 如果这个商品属于当前登录的用户的话
            //
            mBtnCart.setText("管理");
            mBtnCart.setTextColor((Color.rgb(0, 0, 0)));
            mBtnCart.setCompoundDrawables(null, null, null, null);     // 移除购物车按钮
            mBtnCart.setBackgroundResource(R.drawable.bg_manage_btn);
            mBtnCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                ToastUtil.showMsg(GoodsInfoActivity.this, "TODO:编辑和删除功能");
                    showBottomDialog();
                }
            });
        }else{
            mBtnCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 这里要先判断一下是否登录
                    ToastUtil.showMsg(GoodsInfoActivity.this, "当前用户:"+curUserEmail);
                    if (curUserEmail.equals("null")){
                        Intent intent = new Intent(GoodsInfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
//                ToastUtil.showMsg(GoodsInfoActivity.this, "TODO:跳转到购买界面");
                        Intent intent = new Intent(GoodsInfoActivity.this, PurchaseActivity.class);
                        Bundle bundle = new Bundle();
                        GoodsInfoBean.InfoBean info = goodsInfoBean.getInfo();
                        bundle.putInt("pk", goodsPk);
                        bundle.putString("imageUrl", info.getDisplay_image());     // 展示图片的Url
                        bundle.putDouble("price", info.getPrice());
                        bundle.putDouble("express_fee", 0.0);      // 快递费
                        bundle.putString("username", info.getUsername());      // 快递费
                        bundle.putString("email", info.getEmail());      //
                        bundle.putString("desc", info.getDesc());      // 快递费
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });
        }
    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        updateCartButton();
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateCartButton();
    }
}
