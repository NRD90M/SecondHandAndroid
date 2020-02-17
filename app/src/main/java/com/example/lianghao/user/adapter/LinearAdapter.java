package com.example.lianghao.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lianghao.R;
import com.example.lianghao.app.GoodsInfoActivity;
import com.example.lianghao.user.bean.UserReleasedListBean;
import com.example.lianghao.utils.Constants;

import java.util.List;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder> {

    private Context mContext;
    private List<UserReleasedListBean.InfoBean> beanList;

    public LinearAdapter(Context context, List<UserReleasedListBean.InfoBean> beanList){
        this.mContext = context;
        this.beanList = beanList;
    }

    @NonNull
    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_released_list, parent, false));    // View 表示每个item长什么样子
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder holder, final int position) {
        holder.mTvDesc.setText(beanList.get(position).getDesc());
        holder.mTvPrice.setText("￥"+beanList.get(position).getPrice());
        holder.mTvMessageCount.setText("留言"+beanList.get(position).getMessage_count());
        holder.mTvBrowseCount.setText("浏览"+beanList.get(position).getBrowse_count());
        Glide.with(mContext).load(Constants.BASE_URL+beanList.get(position).getImageUrl()).into(holder.mIvPic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pk = beanList.get(position).getPk();     // 先获取商品主键
                Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("pk", pk);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvPic;
        private TextView mTvDesc;
        private TextView mTvPrice;
        private TextView mTvMessageCount;
        private TextView mTvBrowseCount;


        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvPic = itemView.findViewById(R.id.iv_pic);
            mTvDesc = itemView.findViewById(R.id.tv_desc);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mTvMessageCount = itemView.findViewById(R.id.tv_message_count);
            mTvBrowseCount = itemView.findViewById(R.id.tv_browse_count);
        }
    }
}
