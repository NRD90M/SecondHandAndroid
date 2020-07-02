package com.example.lianghao.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lianghao.R;
import com.example.lianghao.utils.Constants;
import com.example.lianghao.utils.ToastUtil;

import static com.example.lianghao.utils.GoodsUtils.requestGiveService;

// 选择收货地址界面
public class ConfirmPurchaseActivity extends AppCompatActivity {
    private ImageView mIvPic;
    private TextView mTvDesc;
    private TextView mTvPrice;
    private TextView mTvExpressFee;
    private TextView mTvTotalPrice;
    private Button mBtnConfirm;
    private SharedPreferences mSharedPreferences;
    private String imageUrl;
    private String desc;
    private Double price;
    private Double express_fee;
    private int goodsPk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_purchase);

        mSharedPreferences = getSharedPreferences("user",Context.MODE_PRIVATE);


        mIvPic = findViewById(R.id.iv_pic);
        mTvDesc = findViewById(R.id.tv_desc);
        mTvPrice = findViewById(R.id.tv_price);
        mTvExpressFee = findViewById(R.id.tv_express_fee);
        mTvTotalPrice = findViewById(R.id.tv_total_price);
        mBtnConfirm = findViewById(R.id.btn_confirm);

        Bundle bundle = getIntent().getExtras();
        goodsPk = bundle.getInt("goodsPk", 0);
        imageUrl = bundle.getString("imageUrl", "");
        desc = bundle.getString("desc", "");
        Glide.with(getApplicationContext()).load(Constants.BASE_URL+imageUrl).into(mIvPic);
        mTvDesc.setText(this.desc);
        price = bundle.getDouble("price", 0.00);
        mTvPrice.setText("￥"+price);

        express_fee = bundle.getDouble("express_fee", 0.00);
        mTvExpressFee.setText("￥"+express_fee);
        double total_price = price+express_fee;
        mTvTotalPrice.setText("￥"+total_price);

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtil.showMsg(getApplicationContext(), "调用支付接口");
                // 跳转到订单详情页面
                // 请求服务器,告诉这个商品被人做了
                requestGiveService(goodsPk, mSharedPreferences.getString("email", "null"), new GoodsInfoActivity.VolleyCallback(){
                    @Override
                    public void onSuccessResponse(String result) {
                        Toast.makeText(ConfirmPurchaseActivity.this, result, Toast.LENGTH_LONG).show();
                    }
                }, getApplicationContext());

                // 这块代码
                Intent intent = new Intent(getApplicationContext(), OrderInfoActivity.class);
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
    }
}
