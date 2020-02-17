package com.example.lianghao.message.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lianghao.R;
import com.example.lianghao.app.PurchaseActivity;
import com.example.lianghao.message.bean.MessageListBean;
import com.example.lianghao.utils.Constants;


public class LinearAdapter extends RecyclerView.Adapter<com.example.lianghao.message.adapter.LinearAdapter.LinearViewHolder> {

    private Context mContext;
    private MessageListBean messageListBean;
    public LinearAdapter(Context context, MessageListBean messageListBean) {
        this.mContext = context;
        this.messageListBean = messageListBean;
    }

    @NonNull
    @Override
    public com.example.lianghao.message.adapter.LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_message_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder holder, int position) {
        MessageListBean.InfoBean infoBean = messageListBean.getInfo().get(position);
        holder.mTvLastMessage.setText(infoBean.getLast_message());
        holder.mTvUsername.setText(infoBean.getOwner_username());
        Glide.with(mContext).load(Constants.BASE_URL+infoBean.getImageUrl()).into(holder.mIvPic);
        Glide.with(mContext).load(Constants.BASE_URL+infoBean.getOwner_head_portrait()).into(holder.mIvHeadPortrait);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PurchaseActivity.class);

            }
        });
    }

    @Override
    public int getItemCount() {
        return messageListBean.getInfo().size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

        private ImageView mIvHeadPortrait;
        private ImageView mIvPic;
        private TextView mTvUsername;
        private TextView mTvLastMessage;
        private TextView mTvLastTime;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);

            mIvHeadPortrait = itemView.findViewById(R.id.iv_head_portrait);
            mIvPic = itemView.findViewById(R.id.iv_pic);
            mTvUsername = itemView.findViewById(R.id.tv_username);
            mTvLastMessage = itemView.findViewById(R.id.tv_last_message);
            mTvLastTime = itemView.findViewById(R.id.tv_last_time);
        }
    }
}
