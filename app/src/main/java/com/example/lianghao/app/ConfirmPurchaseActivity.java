package com.example.lianghao.app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lianghao.R;
import com.example.lianghao.utils.Constants;
import com.example.lianghao.utils.ToastUtil;

// 选择收货地址界面
public class ConfirmPurchaseActivity extends AppCompatActivity {
    private ImageView mIvPic;
    private TextView mTvDesc;
    private TextView mTvPrice;
    private TextView mTvExpressFee;
    private TextView mTvTotalPrice;
    private Button mBtnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_purchase);

        mIvPic = findViewById(R.id.iv_pic);
        mTvDesc = findViewById(R.id.tv_desc);
        mTvPrice = findViewById(R.id.tv_price);
        mTvExpressFee = findViewById(R.id.tv_express_fee);
        mTvTotalPrice = findViewById(R.id.tv_total_price);
        mBtnConfirm = findViewById(R.id.btn_confirm);

        Bundle bundle = getIntent().getExtras();
        String imageUrl = bundle.getString("imageUrl", "");
        Glide.with(getApplicationContext()).load(Constants.BASE_URL+imageUrl).into(mIvPic);
        mTvDesc.setText(bundle.getString("desc", ""));
        Double price = bundle.getDouble("price", 0.00);
        mTvPrice.setText("￥"+price);
        Double express_fee = bundle.getDouble("express_fee", 0.00);
        mTvExpressFee.setText("￥"+express_fee);
        Double total_price = price+express_fee;
        mTvTotalPrice.setText("￥"+total_price);

        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(getApplicationContext(), "调用支付接口");
            }
        });
    }
}
