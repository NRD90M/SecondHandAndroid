package com.example.lianghao.release.fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.lianghao.base.BaseFragment;

// 发布fragment
public class ReleaseFragment extends BaseFragment {
    private TextView mTextView;
    private String TAG = "HomeFragment";
    @Override
    public View initView() {
        Log.d(TAG, "initView");
        mTextView = new TextView(mContext);
        mTextView.setText("发布");
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextSize(25);
        return mTextView;
    }

    public void initData(){
        super.initData();
        Log.d(TAG, "initData");
    }
}
