package com.example.lianghao;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lianghao.utils.ToastUtil;

import java.io.File;

import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class SelectPicActivity extends AppCompatActivity {

    private Button mBtnTakePhoto;
    private Button mBtnSelectPic;
    private Button mBtnFinish;
    private ImageView mIvPic;
    private TextView mTvPath;
    private TextView mTvUri;
    private static final int REQUEST_CODE = 0x001;

    private LQRPhotoSelectUtils mLqrPhotoSelectUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_pic);

        mBtnTakePhoto = findViewById(R.id.btn_take_photo);
        mBtnSelectPic = findViewById(R.id.btn_select_pic);
        mTvPath = findViewById(R.id.tvPath);
        mTvUri = findViewById(R.id.tvUri);
        mIvPic = findViewById(R.id.iv_pic);
        mBtnFinish = findViewById(R.id.btn_finish);

        init();
        initListener();

    }
    private void init() {
        // 1、创建LQRPhotoSelectUtils（一个Activity对应一个LQRPhotoSelectUtils）
        mLqrPhotoSelectUtils = new LQRPhotoSelectUtils(this, new LQRPhotoSelectUtils.PhotoSelectListener() {
            @Override
            public void onFinish(File outputFile, Uri outputUri) {
                // 4、当拍照或从图库选取图片成功后回调
                mTvPath.setText(outputFile.getAbsolutePath());
                mTvUri.setText(outputUri.toString());
                Glide.with(SelectPicActivity.this).load(outputUri).into(mIvPic);
            }
        }, false);//true裁剪，false不裁剪

        if (ContextCompat.checkSelfPermission(SelectPicActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(SelectPicActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(SelectPicActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SelectPicActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE);
        }else{
            ToastUtil.showMsg(SelectPicActivity.this, "已经获得了所有权限");
        }
    }

    private void initListener(){
//        mBtnTakePhoto.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                // 3、调用拍照方法
//                PermissionGen.with(SelectPicActivity.this)
//                        .addRequestCode(LQRPhotoSelectUtils.REQ_TAKE_PHOTO)
//                        .permissions(Manifest.permission.READ_EXTERNAL_STORAGE,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.CAMERA
//                        ).request();
//            }
//        });

        mBtnSelectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 选择图片
                mLqrPhotoSelectUtils.selectPhoto();
            }
        });

        mBtnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTvPath.getText().toString().equals("图片路径:")){
                    // 如果一张图片都没选
                    ToastUtil.showMsg(getApplicationContext(), "您还没有选择图片呢!");
                }else{
                    ToastUtil.showMsg(getApplicationContext(), "选择图片成功:"+mTvPath.getText().toString());
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent();
                    bundle.putString("uri", mTvUri.getText().toString());
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initListener();
            } else {
                Toast.makeText(this, "您已拒绝授予权限", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 2、在Activity中的onActivityResult()方法里与LQRPhotoSelectUtils关联
//        ToastUtil.showMsg(SelectPicActivity.this, "返回值:"+data);
        mLqrPhotoSelectUtils.attachToActivityForResult(requestCode, resultCode, data);
    }
}
