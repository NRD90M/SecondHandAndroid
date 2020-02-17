package com.example.lianghao.home.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lianghao.R;

import java.util.List;

// 分类

public class ChannelRecyclerViewAdapter extends RecyclerView.Adapter<ChannelRecyclerViewAdapter.ViewHolder> {


    private final List<String> category_list;
    private final Context mContext;

    public ChannelRecyclerViewAdapter(Context mContext, List<String> category_list) {
        this.mContext = mContext;
        this.category_list = category_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = View.inflate(mContext, R.layout.item_channel, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 根据位置进行数据绑定
        String name = category_list.get(position);
        // 绑定数据
        holder.mTvCategoryName.setText(name);
    }

    @Override
    public int getItemCount() {
        return category_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTvCategoryName;
        public ViewHolder(View itemView){
            super(itemView);

            mTvCategoryName = itemView.findViewById(R.id.tv_category_name);    // 类别名字

            // 设置点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onCategoryRecyclerView != null){
                        onCategoryRecyclerView.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

    // 点击监听器
    public interface OnCategoryRecyclerView{
        public void onItemClick(int position);
    }

    private OnCategoryRecyclerView onCategoryRecyclerView;

    public void setOnCategoryRecyclerView(OnCategoryRecyclerView onCategoryRecyclerView){
        this.onCategoryRecyclerView = onCategoryRecyclerView;
    }
}
