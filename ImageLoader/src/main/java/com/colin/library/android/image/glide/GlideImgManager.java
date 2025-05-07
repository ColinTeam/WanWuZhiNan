package com.colin.library.android.image.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.colin.library.android.image.glide.transform.RoundedTransform;


public class GlideImgManager {

    //站位图
    public static final int ERROR = -1;
    //站位图
    private static final int DEFAULT_ICON = -1;
    //默认宽高
    private static final int IMG_WIDTH = 100;
    private static final int IMG_HEIGHT = 100;


    private static final class InstanceHolder {
        static final GlideImgManager instance = new GlideImgManager();
    }

    public static GlideImgManager get() {
        return InstanceHolder.instance;
    }

    //加载图片
    public <T> void loadImg(T url, ImageView img) {
        loadImg(url, img, IMG_WIDTH, IMG_HEIGHT);
    }

    //加载图片
    public void loadImg(String url, ImageView img) {
        if (TextUtils.isEmpty(url)) {
            loadImg(DEFAULT_ICON, img, IMG_WIDTH, IMG_HEIGHT);
        } else {
            loadImg(url, img, IMG_WIDTH, IMG_HEIGHT);
        }
    }

    public static void loadGif(@NonNull AppCompatImageView view, int res) {
        Glide.with(view).asGif().load(res).into(view);
    }

    //加载图片
    public void loadDefaultImg(String url, ImageView img, int error) {
        Glide.with(img).load(url).dontAnimate().error(error).placeholder(error).into(img);
    }

    public void loadBg(String url, ImageView img) {
        Glide.with(img).load(url).placeholder(img.getDrawable()).dontAnimate().centerCrop().into(img);
    }

    //加载图片
    public void loadImg(String url, ImageView img, int error) {
        Glide.with(img).load(url).dontAnimate().error(error).placeholder(error).centerCrop().into(img);
    }

    //加载图片
    public void loadFitCenterImg(String url, ImageView img, int error) {
        Glide.with(img).load(url).dontAnimate().error(error).placeholder(error).fitCenter().into(img);
    }


    public void loadImgFitCenter(String url, ImageView img, int error) {
        Glide.with(img).load(url).dontAnimate().error(error).placeholder(error).fitCenter().into(img);
    }

    //加载图片
    public void loadImg(String url, ImageView img, float topLeft, float topRight, float bottomRight, float bottomLeft, int error) {
        Glide.with(img).load(url).dontAnimate().error(error).placeholder(error).transform(new RoundedTransform(topLeft, topRight, bottomRight, bottomLeft)).into(img);
    }


    //加载图片 指定宽高
    public <T> void loadImg(T url, ImageView img, int width, int height) {
        loadRound(url, img, createOptions().override(width, height));
    }


    // 图片加载库采用Glide框架
    private <T> void loadRound(T url, ImageView img, RequestOptions options) {
        Glide.with(img).load(url).dontAnimate().error(DEFAULT_ICON).placeholder(DEFAULT_ICON).apply(options).centerCrop()
//                    .listener(new RequestListener<Bitmap>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourc6eReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//                            // 根据图片的实际长宽判断，如果宽大于长则拉伸，如果宽小于等于长则居中显示
//                            int width = resource.getWidth();
//                            int height = resource.getHeight();
//                            if(width > height){
//                                img.setScaleType(ImageView.ScaleType.FIT_XY);
//                            }else {
//                                img.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                            }
//                            return false;
//                        }
//                    })
                .into(img);
    }

    //缩放图片
    private void onScaleImage(ImageView img, @NonNull Bitmap resource) {
        if (img == null) {
            return;
        }
        img.setImageBitmap(resource);
        //屏幕宽度
        float width = getScreenWidth(img.getContext());
        //屏幕高度
        float height = getScreenHeight(img.getContext());
        //缩放比例
        float scale = width / resource.getWidth();
        //缩放后的宽度和高度
        int afterWidth = (int) (resource.getWidth() * scale);
        int afterHeight = (int) (resource.getHeight() * scale);
        ViewGroup.LayoutParams lp = img.getLayoutParams();
        lp.width = afterWidth;
        lp.height = afterHeight;
        img.setLayoutParams(lp);
    }

    //获取屏幕宽度
    private int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    //获取屏幕高度
    private int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public void load(ImageView img, String url, int width, int height) {
        if (TextUtils.isEmpty(url)) {
            Glide.with(img).load(DEFAULT_ICON).apply(createOptions().override(width, height)).into(img);
        } else {
            Glide.with(img).load(url).apply(createOptions().override(width, height)).into(img);
        }
    }

    public void load(ImageView img, int url, int width, int height) {
        if (url <= 0) {
            Glide.with(img).load(DEFAULT_ICON).apply(createOptions().override(width, height)).into(img);
        } else {
            Glide.with(img).load(url).apply(createOptions().override(width, height)).into(img);
        }
    }

    //createOptions
    private RequestOptions createOptions() {
        //RequestOptions 设置请求参数，通过apply方法设置
        return new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
                .priority(Priority.NORMAL)//指定加载的优先级，优先级越高越优先加载，
                .error(ERROR).centerCrop().skipMemoryCache(false).placeholder(ERROR);
        // 缓存原始数据
//               .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//               .optionalTransform(new GlideRoundTransform(CornerType.ALL));
    }



    //暂停加载
    public void clear(View view) {
        //Glide.with(view).clear(view);
    }

    //暂停加载
    public void pauseRequests(View view) {
        Glide.with(view).pauseRequests();
    }

    //恢复加载
    public void resumeRequests(View view) {
        Glide.with(view).resumeRequests();
    }
}
