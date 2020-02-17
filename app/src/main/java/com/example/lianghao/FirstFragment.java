package com.example.lianghao;

import androidx.core.view.MotionEventCompat;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FirstViewModel mViewModel;
    private Button mBtnSearch;
    private Button mBtnCity;    // 选择城市
    private String selectedProvince;    // 选中的省份
    private String selectedCity;        // 选中的城市
    private ViewFlipper viewFlipper;    // 主页图片轮播
    private Message message;    // 申明消息对象
    int[] images = {R.drawable.view_flipper_image_1, R.drawable.view_flipper_image_2};    // 轮播图片

    private ArrayList<CityBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private static final int MSG_LOOP_IMAGE = 0x0004;    // 轮播图片
    private Thread thread;     // 用于加载数据的线程
    private boolean isLoaded = false;    // 装载数据是否完成
    private String province;
    private String city;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        Log.i("addr", "地址数据开始解析");
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    Log.i("addr", "地址数据获取成功");
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Log.i("addr", "地址数据获取失败");
                    break;
                case MSG_LOOP_IMAGE:
                    Log.i("image", "轮播图片");
                    viewFlipper.showPrevious();    // 切换到下一张图片
                    message = mHandler.obtainMessage(MSG_LOOP_IMAGE);    // 获取Message
                    mHandler.sendMessageDelayed(message, 3000);    // 延时3秒发送消息
                    break;
            }
        }
    };

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    // 解析json数据
    private void initJsonData() {
        String CityData = new GetJsonDataUtil().getJson(getContext(), "city.json");//获取assets目录下的json文件数据
        ArrayList<CityBean> jsonBean = parseData(CityData);//用Gson转成实体

        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c);
                CityList.add(CityName);//添加城市
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }


    private ArrayList<CityBean> parseData(String result) {//Gson 解析
        ArrayList<CityBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                CityBean entity = gson.fromJson(data.optJSONObject(i).toString(), CityBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, container, false);
        mBtnSearch = view.findViewById(R.id.btn_search);
        mBtnCity = view.findViewById(R.id.btn_city);
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);    // 加载Json数据
        viewFlipper = view.findViewById(R.id.viewFlipper);
        // 设置轮播图片
        for (int image : images) {
            ImageView imageView = new ImageView(view.getContext());
            imageView.setImageResource(image);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setOnGenericMotionListener(new View.OnGenericMotionListener(){

            @Override
            public boolean onGenericMotion(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(getContext(), "ACTION_DOWN", Toast.LENGTH_LONG).show();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Toast.makeText(getContext(), "ACTION_MOVE", Toast.LENGTH_LONG).show();
                        break;
                    case MotionEvent.ACTION_UP:
                        Toast.makeText(getContext(), "ACTION_UP", Toast.LENGTH_LONG).show();
                        break;
                }
                return false;
            }
        });
        // 获取消息
//        message = mHandler.obtainMessage(MSG_LOOP_IMAGE);
//        mHandler.sendMessage(message);
        // 滑动监听器
//        viewFlipper.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getAction();
//                float startX = 0;
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        startX = (int) event.getX();
//                        //flipper.stopFlipping();// 当手指按下时，停止图片的循环播放。并记录当前x坐标
//                        Log.i("count", viewFlipper.getDisplayedChild() + "");
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Toast.makeText(getContext(), "手指提起来", Toast.LENGTH_LONG).show();
//                        if (event.getX() - startX > 100) { // 手指向右滑动，左侧滑入，右侧滑出
//                            viewFlipper.showPrevious();
////                            viewFlipper.startFlipping();
//                        } else if (startX - event.getX() > 100) {// 手指向左滑动，右侧滑入，左侧滑出
//                            viewFlipper.showNext();
////                            viewFlipper.startFlipping();
//                        } else if (startX - event.getX() < 10 || event.getX() - startX < 10) {//当左右滑动的坐标差<10时,可以确定为点击动作，解决了OnTouch和OnClick的冲突
//                            Toast.makeText(getContext(), "点击", Toast.LENGTH_LONG).show();
//                            //Toast.makeText(context, "当前索引"+flipper.getDisplayedChild(), Toast.LENGTH_SHORT).show();
////                            data.get(flipper.getDisplayedChild());
////                            Intent intent = new Intent();
////                            intent.putExtra("titleName",data.get(flipper.getDisplayedChild()).getImageName());//传递文章标题
////                            intent.putExtra("textUrl", data.get(flipper.getDisplayedChild()).getTextUrl());//传递文章的URL
////                            intent.setClass(context, WebActivity.class);
////                            context.startActivity(intent);
//                        }
//                        break;
//                }
//                return false;
//            }
//        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FirstViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(view.getContext(), SearchNumberActivity.class);
            Bundle bundle = new Bundle();
            // 传递城市和省份信息
            bundle.putString("province", selectedProvince);
            bundle.putString("city", selectedCity);
            intent.putExtras(bundle);
            startActivity(intent);
//                Toast.makeText(view.getContext(), "点击搜索按钮", Toast.LENGTH_LONG).show();
            }
        });

        mBtnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPickerView();
            }
        });

    }

    private void ShowPickerView() {// 弹出地址选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                province = options1Items.get(options1).getPickerViewText();
                city = options2Items.get(options1).get(options2);
                selectedProvince = province;
                selectedCity = city;
//                Toast.makeText(getContext(), province+city, Toast.LENGTH_LONG).show();
                mBtnCity.setText(selectedProvince+" "+selectedCity);
            }
        }).setDividerColor(Color.BLACK).setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        pvOptions.setPicker(options1Items, options2Items);//二级选择器（市区）
        pvOptions.show();
    }
}
