package com.example.lianghao.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lianghao.R;
import com.example.lianghao.app.GoodsInfoActivity;
import com.example.lianghao.home.bean.ResultBeanData;
import com.example.lianghao.utils.GlideImageLoader;
import com.example.lianghao.utils.ToastUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentAdapter extends RecyclerView.Adapter {

    public static final int BANNER = 0;   // 轮播图或者叫做横幅
    public static final int CHANNEL = 1;    // 频道分类：比如关注、新鲜、同校、手机、数码、租房、服装
    public static final int RECOMMEND = 2;    // 当频道分类改变时，这里展示的推荐商品切换到对应的类别
    private Context mContext;
    private ResultBeanData resultBeanData;
    private final LayoutInflater mLayoutInflater;
    private ChannelRecyclerViewAdapter.ViewHolder lastModifiedViewHolder;     // 之前修改过的
    private int CurrentType = BANNER;   // 默认类型是轮播图

    public HomeFragmentAdapter(Context mContext, ResultBeanData resultBeanData){
        // result列表存了返回的列表商品信息
        this.mContext = mContext;
        this.resultBeanData = resultBeanData;
        LayoutInflater.from(mContext);
        mLayoutInflater = LayoutInflater.from(mContext);
    }
    /**
     *
     * @param parent
     * @param viewType  当前类型：是轮播图还是分类还是商品列表
     * @return
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == BANNER){
            return new BannerViewHolder(mContext, mLayoutInflater.inflate(R.layout.banner_view_pagge, null));// 往它传布局文件
        }else if (viewType == CHANNEL){
            return new ChannelViewHolder(mContext, mLayoutInflater.inflate(R.layout.channel_item, null));    //
        }else if (viewType == RECOMMEND){
            return new RecommendViewHolder(mContext, mLayoutInflater.inflate(R.layout.recommend_item, null));    //
        }
        return null;

    }

    class ChannelViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView mRvChannel;    // 切换不同分类
        private Context mContext;
        private ChannelRecyclerViewAdapter adapter;

        public ChannelViewHolder(Context mContext, View inflate) {
            super(inflate);

            mRvChannel = inflate.findViewById(R.id.rv_channel);
            this.mContext = mContext;
        }

        public void setData(List<String> category_list) {
            // 传入所有类别
            // 直接设置文本
            adapter = new ChannelRecyclerViewAdapter(mContext, category_list);


            // 设置布局管理器
            mRvChannel.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mRvChannel.setAdapter(adapter);

            // 设置item的点击事件
            adapter.setOnCategoryRecyclerView(new ChannelRecyclerViewAdapter.OnCategoryRecyclerView() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onItemClick(int position) {
                    ToastUtil.showMsg(mContext, "分类："+position);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRvChannel.getLayoutManager();
                    int firstPosition = layoutManager.findFirstVisibleItemPosition();
                    int lastPosition = layoutManager.findLastVisibleItemPosition();
                    int left = mRvChannel.getChildAt(position-firstPosition).getLeft();
                    int right = mRvChannel.getChildAt(lastPosition-position).getLeft();

                    // 一定要先边颜色之后再滚动
                    // 先回复上一次修改过的颜色
                    if (lastModifiedViewHolder != null){
                        lastModifiedViewHolder.mTvCategoryName.setTextColor(Color.rgb(145, 145, 145));    // 设置成纯黑
                        lastModifiedViewHolder.mTvCategoryName.setTextSize(16);    // 恢复成默认的大小// 记得别这样写，全都写道资源文件里//TODO
                        lastModifiedViewHolder.mTvCategoryName.setCompoundDrawables(null, null, null, null);
                    }
                    View view = mRvChannel.getChildAt(position-firstPosition);    // 获取当前点击的view
                    if(mRvChannel.getChildViewHolder(view) != null){
                        ChannelRecyclerViewAdapter.ViewHolder viewHolder = (ChannelRecyclerViewAdapter.ViewHolder) mRvChannel.getChildViewHolder(view);
                        viewHolder.mTvCategoryName.setTextSize(19);
                        viewHolder.mTvCategoryName.setTextColor(Color.rgb(0, 0, 0));    // 设置成纯黑
                        // 添加一个下横线
                        Drawable drawable = mContext.getDrawable(R.drawable.underline);
//                        Log.d("getMinimumWidth", ""+drawable.getMinimumWidth());
//                        Log.d("getMinimumWidth", ""+drawable.getMinimumWidth());
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        viewHolder.mTvCategoryName.setCompoundDrawables(null, null, null, drawable);

                        lastModifiedViewHolder = viewHolder;
                    }
                    mRvChannel.scrollBy((left-right)/2,0);
                    // 然后就是切换下面的商品列表的内容了

                }
            });
        }
    }

    class BannerViewHolder extends RecyclerView.ViewHolder{

        private Context mContext;
        private View itemView;
        private Banner banner;

        public BannerViewHolder(Context mContext, View view) {
            super(view);
            this.mContext = mContext;
            this.itemView = view;
            this.banner = view.findViewById(R.id.banner);    // 获取banner
        }

        public void setData(List<String> urls) {
            // 设置轮播图链接
            banner.setImageLoader(new GlideImageLoader());
            banner.setImages(urls);
            banner.start();

            // 设置点击事件
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    ToastUtil.showMsg(mContext, "点击banner的第"+position);
                }
            });
        }
    }
    /**
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        // 绑定视图
        if(getItemViewType(position) == BANNER){
            BannerViewHolder bannerViewHolder = (BannerViewHolder)holder;    // 先强制类型转换一下

            List<String> urls = new ArrayList<>();
            for(int i = 0; i < resultBeanData.getResult().size(); i++){
                String image_url = resultBeanData.getResult().get(i).getImage();
                urls.add(image_url);
            }
            bannerViewHolder.setData(urls);    // 传轮播图的id过去
        } else if (getItemViewType(position) == CHANNEL){
            ChannelViewHolder channelViewHolder = (ChannelViewHolder)holder;
            // 先写死吧
            List<String> channel_list = new ArrayList<>();
            channel_list.add("关注");
            channel_list.add("新鲜");
            channel_list.add("附近");
            channel_list.add("同校");
            channel_list.add("手机");
            channel_list.add("数码");
            channel_list.add("租房");
            channel_list.add("服装");
            channel_list.add("居家");
            channel_list.add("美妆");
            channel_list.add("运动");
            channel_list.add("家电");
            channel_list.add("玩具");
            channel_list.add("乐器");
            channel_list.add("短租");
            channelViewHolder.setData(channel_list);
        } else if (getItemViewType(position) == RECOMMEND){
            RecommendViewHolder recommendViewHolder = (RecommendViewHolder)holder;
            recommendViewHolder.setData(resultBeanData.getResult());     // 把会传来的商品列表送进去
        }
    }

    /**
     * 得到类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        switch (position){
            case BANNER:
                CurrentType = BANNER;
                break;
            case CHANNEL:
                CurrentType = CHANNEL;
                break;
            case RECOMMEND:
                CurrentType = RECOMMEND;
                break;
        }
        return CurrentType;
    }


    @Override
    public int getItemCount() {
        return 3;    // 轮播图+分类+每个类别下随机几个商品展示
    }

    private class RecommendViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;
        private GridView mGvRecommend;
        private RecommendGridViewAdapter adapter;

        public RecommendViewHolder(final Context mContext, View inflate) {
            super(inflate);
            this.mContext = mContext;
            mGvRecommend = inflate.findViewById(R.id.gv_recommend);

            // 动态设置高度
            ViewGroup.LayoutParams layoutParams = mGvRecommend.getLayoutParams();
            int offset = resultBeanData.getResult().size()%2;
            layoutParams.height = 730 * (resultBeanData.getResult().size()/2 + offset);
            mGvRecommend.setLayoutParams(layoutParams);

            mGvRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ToastUtil.showMsg(mContext, "position:"+position);
                    // 点击跳转到详情页面
                    Intent intent = new Intent(mContext, GoodsInfoActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putInt("pk", resultBeanData.getResult().get(position).getProduct_id());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });

        }

        public void setData(List<ResultBeanData.ResultBean> result) {
            //当有数据的时候
            // 设置GridView的适配器
            adapter = new RecommendGridViewAdapter(mContext, result);
            mGvRecommend.setAdapter(adapter);
        }
    }
}
