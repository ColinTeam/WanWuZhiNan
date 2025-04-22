package com.wanwuzhinan.mingchang.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ad.img_load.img.RoundedImageView;
import com.bumptech.glide.Glide;
import com.comm.banner.adapter.BannerAdapter;
import com.wanwuzhinan.mingchang.R;

import java.util.List;

/**
 * USER：Administrator
 * DATE：2020/12/4
 * TIME：10:41
 * DESCRIBE：
 */
public class BannerImagerAdapter extends BannerAdapter<String, BannerImagerAdapter.BannerViewHolder> {

    private Context ctx;

    public BannerImagerAdapter(List<String> mDatas, Context context) {
        //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
        super(mDatas);
        this.ctx = context;
    }

    //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        RoundedImageView imageView = new RoundedImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setCornerRadius(8);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, String data, int position, int size) {
        Glide.with(ctx).load(data).dontAnimate().centerCrop().placeholder(R.drawable.shape_dddddd_10).error(R.drawable.shape_dddddd_10).into(holder.imageView);
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        RoundedImageView imageView;

        public BannerViewHolder(@NonNull RoundedImageView view) {
            super(view);
            this.imageView = view;
        }
    }
}
