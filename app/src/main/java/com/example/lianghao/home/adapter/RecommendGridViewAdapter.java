package com.example.lianghao.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lianghao.R;
import com.example.lianghao.home.bean.ResultBeanData;
import com.example.lianghao.utils.Constants;

import java.util.List;

public class RecommendGridViewAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<ResultBeanData.ResultBean> resultBeanList;


    public RecommendGridViewAdapter(Context mContext, List<ResultBeanData.ResultBean> result) {
        this.mContext = mContext;
        this.resultBeanList = result;
    }

    @Override
    public int getCount() {
        return resultBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.item_recommend_grid_view, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_recommend = convertView.findViewById(R.id.iv_recommend);
            viewHolder.tv_desc = convertView.findViewById(R.id.tv_desc);
            viewHolder.tv_price = convertView.findViewById(R.id.tv_price);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 然后要根据位置得到对应的数据
        ResultBeanData.ResultBean resultBean = resultBeanList.get(position);    // 获取对应位置的数据
        // 先装载图片
        Glide.with(mContext).load(Constants.BASE_URL+resultBean.getImage()).into(viewHolder.iv_recommend);
        viewHolder.tv_desc.setText(resultBean.getDesc());
        viewHolder.tv_price.setText(String.valueOf(resultBean.getPrice()));
        return convertView;
    }

    static class ViewHolder{
        ImageView iv_recommend;     // 展示图片
        TextView tv_desc;           // 描述
        TextView tv_price;          // 价格
    }
}
