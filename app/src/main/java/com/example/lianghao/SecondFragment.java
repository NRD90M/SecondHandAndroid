package com.example.lianghao;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.lianghao.app.GoodsInfoActivity;
import com.example.lianghao.utils.ToastUtil;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.util.Base64;
import android.widget.Toast;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class SecondFragment extends Fragment {

    private SecondViewModel mViewModel;
    private ImageButton mIbAddImage;
    private Button mBtnChooseLocation;
    private Button mBtnRelease;    // 发布
    private EditText mEtPrice;    // 输入价格
    private EditText mEtDesc;    // 商品描述

    private ArrayList<CityBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private Thread thread;     // 用于加载数据的线程
    private boolean isLoaded = false;    // 装载数据是否完成
    private String selectedProvince;    // 选中的省份
    private String selectedCity;        // 选中的城市
    private Uri imageUri;
    private SharedPreferences mSharedPreferences;
    private Bitmap bitmap;
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
            }
        }
    };

    public static final int TO_SELECT_PHOTO = 3;

    private String picPath = null;

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment, container, false);
        mIbAddImage = view.findViewById(R.id.btn_add_image);
        mBtnChooseLocation = view.findViewById(R.id.btn_location);
        mBtnRelease = view.findViewById(R.id.btn_release);
        mEtPrice = view.findViewById(R.id.et_price);
        mEtDesc = view.findViewById(R.id.et_desc);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SecondViewModel.class);

        mIbAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SelectPicActivity.class);
                startActivityForResult(intent, TO_SELECT_PHOTO);
            }
        });

        mBtnChooseLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPickerView();
            }
        });

        mBtnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = mEtDesc.getText().toString();
                String price = mEtPrice.getText().toString();
//                imageUri    图片的Uri
                //
                Double express_fee = 0.0;    // 快递费

                mSharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                // 如果已经登录过了
                String user = mSharedPreferences.getString("email", "null");

                if (user.equals("null")){
                    ToastUtil.showMsg(getContext(), "请先登录");
                }
                else if (desc.equals("")){
                    ToastUtil.showMsg(getContext(), "请输入商品描述");
                }else if (desc.length()<5){
                    ToastUtil.showMsg(getContext(), "商品描述字数太少");
                }else if (imageUri == null){
                    ToastUtil.showMsg(getContext(), "请选择商品封面");
                }else if (selectedProvince == null || selectedProvince == null){
                    ToastUtil.showMsg(getContext(), "请选择发货地点");
                }else if (price.equals("")){
                    ToastUtil.showMsg(getContext(), "请输入商品价格");
                }else{
                    ToastUtil.showMsg(getContext(), "发布成功");
                    releaseGoods(desc, imageUri, selectedProvince, selectedCity, Double.parseDouble(price), express_fee, user, new VolleyCallback() {
                        @Override
                        public void onSuccessResponse(String result) {
                            int goodsPk = Integer.valueOf(result);    // 返回商品的key
                            ToastUtil.showMsg(getContext(), "返回成功:"+result);
                            // 跳转到商品详情页面
                            Intent intent = new Intent(getContext(), GoodsInfoActivity.class);
                            Bundle bundle = new Bundle();

                            bundle.putInt("pk", goodsPk);
//                            bundle.putBoolean("isSelf", true);    // 这个东西是自己的
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);    // 加载Json数据
        // 查看是不是更新商品信息
//        parseBundle();

    }

    public void parseBundle(){
        Bundle bundle = getActivity().getIntent().getExtras();
        assert bundle != null;
        boolean isUpdate = bundle.getBoolean("isUpdate", false);
        if (isUpdate){
            Log.d("parseBundle", "编写");
        }else{
            Log.d("parseBundle", "发布新的");
        }
    }

    public void releaseGoods(final String desc, Uri imageUri, final String province, final String city, final Double price, final Double express_fee, final String user, final VolleyCallback callback){
        String url = "http://10.0.2.2:8000/trade/release_goods";
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
        }catch (IOException e){
            e.printStackTrace();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        //sending image to server
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                callback.onSuccessResponse(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());
                ToastUtil.showMsg(getContext(), "Error: " + error.toString());
            }

        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("image", imageString);
                parameters.put("desc", desc);
                parameters.put("province", province);
                parameters.put("city", city);
                parameters.put("price", String.valueOf(price));
                parameters.put("express_fee", String.valueOf(express_fee));
                parameters.put("user", user);
                return parameters;
            }
        };

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode== Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO)
        {
            String uri = data.getExtras().getString("uri");

            imageUri = Uri.parse(uri);

            ToastUtil.showMsg(getContext(), "onActivityResult"+imageUri);

            mIbAddImage.setImageURI(imageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void ShowPickerView() {// 弹出地址选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                selectedProvince = options1Items.get(options1).getPickerViewText();
                selectedCity = options2Items.get(options1).get(options2);
//                Toast.makeText(getContext(), province+city, Toast.LENGTH_LONG).show();
                mBtnChooseLocation.setText(selectedProvince+" "+selectedCity);
            }
        }).setDividerColor(Color.BLACK).setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();

        pvOptions.setPicker(options1Items, options2Items);//二级选择器（市区）
        pvOptions.show();
    }

    public interface VolleyCallback{
        void onSuccessResponse(String result);
    }
}
