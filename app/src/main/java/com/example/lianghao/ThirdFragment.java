package com.example.lianghao;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lianghao.app.GoodsInfoActivity;
import com.example.lianghao.utils.Constants;
import com.example.lianghao.utils.ToastUtil;

public class ThirdFragment extends Fragment {

    private ThirdViewModel mViewModel;
    private ImageView mIbLogin;
    private SharedPreferences mSharedPreferences;
    private TextView mTvLogin;
    private ImageButton mBtnSetting;
    private boolean isLogined;
    private Button mBtnMyReleased;
    private Button mBtnMyServed;    // 我要做的


    public static ThirdFragment newInstance() {
        return new ThirdFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, container, false);
        mIbLogin = view.findViewById(R.id.ib_login);
        mTvLogin = view.findViewById(R.id.tv_login);
        mBtnSetting = view.findViewById(R.id.btn_setting);
        mBtnMyReleased = view.findViewById(R.id.btn_my_released_goods);
        mBtnMyServed = view.findViewById(R.id.btn_my_served_goods);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ThirdViewModel.class);
        // TODO: Use the ViewModel
        mIbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        mSharedPreferences = this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        initListeners();
    }
    // 初始化那些监听器
    public void initListeners(){
        // 如果已经登录过了
        if (!mSharedPreferences.getString("email", "null").equals("null")){
            isLogined = true;
            mIbLogin.setEnabled(false);
            mTvLogin.setText(mSharedPreferences.getString("email", "null"));
            String username = mSharedPreferences.getString("username", "");
//            String head_portrait_url = mSharedPreferences.getString("head_portrait_url", "/media/portraits/10060471_105425187390_2_J2ykXM6.jpg");
            String head_portrait_url = "/media/portraits/10060471_105425187390_2_J2ykXM6.jpg";
            Glide.with(getContext()).load(Constants.BASE_URL+head_portrait_url).into(mIbLogin);
            mTvLogin.setText(username);
            mBtnSetting.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), SettingActivity.class);
                    startActivity(intent);
//                    ToastUtil.showMsg(getContext(), "跳转到设置");
                }
            });
        }else{
            isLogined = false;
            mIbLogin.setEnabled(true);
            // 如果还没登录
            mBtnSetting.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        mBtnMyReleased.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogined) {
                    Intent intent = new Intent(getContext(), ReleasedListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email", mSharedPreferences.getString("email", "null"));   // 把邮箱传过去，作为用户标识
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        mBtnMyServed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogined) {
                    Intent intent = new Intent(getContext(), ServedListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("email", mSharedPreferences.getString("email", "null"));   // 把邮箱传过去，作为用户标识
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initListeners();
    }
}
