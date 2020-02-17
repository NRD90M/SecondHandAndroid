package com.example.lianghao.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static Toast mToast;
    public static void showMsg(Context context, String msg)
    {
        if(mToast == null){
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        }else{
            mToast.setText(msg);
        }
        mToast.show();
    }
}
