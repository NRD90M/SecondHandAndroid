//package com.example.lianghao.app.view;
//
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.support.v7.widget.AppCompatImageView;
//import android.util.AttributeSet;
//import android.widget.ImageView;
//
//public class ResizableImageView extends ImageView {
//
//    public ResizableImageView(Context context) {
//        super(context);
//    }
//
//    public ResizableImageView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
//        Drawable d = getDrawable();
//
//        if(d!=null){
//            // ceil not round - avoid thin vertical gaps along the left/right edges
//            int width = MeasureSpec.getSize(widthMeasureSpec);
//            //高度根据使得图片的宽度充满屏幕计算而得
//            int height = (int) Math.ceil((float) width * (float) d.getIntrinsicHeight() / (float) d.getIntrinsicWidth());
//            setMeasuredDimension(width, height);
//        }else{
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
//    }
//
//}
