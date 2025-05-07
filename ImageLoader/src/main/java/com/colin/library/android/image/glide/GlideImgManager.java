package com.colin.library.android.image.glide;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.colin.library.android.image.glide.transform.GlideCircleTransform;
import com.colin.library.android.image.glide.transform.GlideRoundTransform;
import com.colin.library.android.image.glide.transform.RoundedTransform;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;


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

    public <T> Bitmap loadBitmap(T url, Application app) throws ExecutionException, InterruptedException {
        return Glide.with(app).asBitmap().load(url).submit().get();
    }

    //加载圆角矩形图片 使用默认的圆角大小 并默认全部圆角
    public <T> void loadRoundImg(T url, ImageView img) {
        loadRound(url, img, createOptions().optionalTransform(new GlideRoundTransform(CornerType.ALL)));
    }

    //加载圆角矩形图片 使用默认的圆角大小 并默认全部圆角
    public void loadRoundImg(String url, ImageView img) {
        if (TextUtils.isEmpty(url)) {
            loadRound(DEFAULT_ICON, img, createOptions().optionalTransform(new GlideRoundTransform(CornerType.ALL)));
        } else {
            loadRound(url, img, createOptions().optionalTransform(new GlideRoundTransform(CornerType.ALL)));
        }
    }


    //模糊处理
    public Bitmap blurBitmap(Bitmap srcBitmap, @FloatRange(from = 0.0f, to = 25.0f) float blurRadius) {
        // 计算图片缩小后的长宽
        int width = Math.round(srcBitmap.getWidth() * 0.4f);
        int height = Math.round(srcBitmap.getHeight() * 0.4f);
        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(srcBitmap, width, height, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(null);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);
        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);
        rs.destroy();
        return outputBitmap;
    }

    //加载圆角矩形图片 使用默认的圆角大小 设置圆角方向
    public <T> void loadRoundImg(T url, CornerType type, ImageView img) {
        loadRound(url, img, createOptions().optionalTransform(new GlideRoundTransform(type)));
    }


    //加载圆角矩形图片 使用指定的圆角大小 默认圆角方向为所有
    public <T> void loadRoundImg(T url, ImageView img, float radius) {
        loadRound(url, img, createOptions().optionalTransform(new GlideRoundTransform(radius)));
    }

    //加载圆角矩形图片 使用指定的圆角大小 默认圆角方向为所有
    public void loadRoundImg(String url, ImageView img, float radius) {
        if (TextUtils.isEmpty(url)) {
            loadRound(DEFAULT_ICON, img, createOptions().optionalTransform(new GlideRoundTransform(radius)));
        } else {
            loadRound(url, img, createOptions().optionalTransform(new GlideRoundTransform(radius)));
        }
    }

    //加载圆角矩形图片 使用指定的圆角大小 默认圆角方向为所有
//    public static <T> void loadRoundImg(T url, ImageView img, float radius,boolean isScale) {
//        if (isScale){
//            loadScaleRound(url, img, createOptions().optionalTransform(new GlideRoundTransform(radius)));
//        }else {
//            loadRound(url, img, createOptions().optionalTransform(new GlideRoundTransform(radius)));
//        }
//    }

    //加载圆角矩形图片 使用指定的圆角大小 指定圆角方向
    public <T> void loadRoundImg(T url, ImageView img, CornerType type, float radius) {
        loadRound(url, img, createOptions().optionalTransform(new GlideRoundTransform(radius, type)));
    }

    //加载圆角矩形图片 使用指定的圆角大小 指定圆角方向
    public void loadRoundImg(String url, ImageView img, CornerType type, float radius) {
        if (TextUtils.isEmpty(url)) {
            loadRound(DEFAULT_ICON, img, createOptions().optionalTransform(new GlideRoundTransform(radius, type)));
        } else {
            loadRound(url, img, createOptions().optionalTransform(new GlideRoundTransform(radius, type)));
        }
    }

    // 图片加载库采用Glide框架
    private <T> void loadScaleRound(T url, ImageView img, RequestOptions options) {
        Glide.with(img).asBitmap().load(url).apply(options).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                onScaleImage(img, resource);
            }
        });
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

    //加载圆形图片
    public <T> void loadCircleImg(T url, ImageView img) {
        WeakReference<ImageView> reference = new WeakReference<>(img);
        //RequestOptions 设置请求参数，通过apply方法设置
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
                .priority(Priority.NORMAL)//指定加载的优先级，优先级越高越优先加载，
                .placeholder(null).error(ERROR)
//                .centerCrop()
                .circleCrop().skipMemoryCache(true).transform(new GlideCircleTransform());
        // 图片加载库采用Glide框架
        Glide.with(img).load(url).apply(options).transition(new DrawableTransitionOptions().crossFade()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (reference.get() != null) {
                    reference.get().setImageDrawable(resource);
                }
            }
        });
    }

    public void loadCircleImg(String url, ImageView img) {
        WeakReference<ImageView> reference = new WeakReference(img);
        if (TextUtils.isEmpty(url)) {
            //RequestOptions 设置请求参数，通过apply方法设置
            RequestOptions options = new RequestOptions()
                    // 但不保证所有图片都按序加载
                    // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                    // 默认为Priority.NORMAL
                    // 如果没设置fallback，model为空时将显示error的Drawable，
                    // 如果error的Drawable也没设置，就显示placeholder的Drawable
                    .priority(Priority.NORMAL)//指定加载的优先级，优先级越高越优先加载，
                    .placeholder(null).error(ERROR).circleCrop().skipMemoryCache(true).transform(new GlideCircleTransform());
            // 图片加载库采用Glide框架
            Glide.with(img).load(DEFAULT_ICON).apply(options).transition(new DrawableTransitionOptions().crossFade()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    if (reference.get() != null) {
                        reference.get().setImageDrawable(resource);
                    }
                    reference.clear();
                }
            });
        } else {
            //RequestOptions 设置请求参数，通过apply方法设置
            RequestOptions options = new RequestOptions()
                    // 但不保证所有图片都按序加载
                    // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                    // 默认为Priority.NORMAL
                    // 如果没设置fallback，model为空时将显示error的Drawable，
                    // 如果error的Drawable也没设置，就显示placeholder的Drawable
                    .priority(Priority.NORMAL)//指定加载的优先级，优先级越高越优先加载，
                    .placeholder(DEFAULT_ICON).error(ERROR).circleCrop().skipMemoryCache(true).transform(new GlideCircleTransform());
            // 图片加载库采用Glide框架
            Glide.with(img).load(url).apply(options).transition(new DrawableTransitionOptions().crossFade()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    if (reference.get() != null) {
                        reference.get().setImageDrawable(resource);
                    }
                    reference.clear();
                }
            });
        }
    }

    //加载圆形图片带边框
    public <T> void loadCircleImg(T url, float borderWidth, int borderColor, ImageView img) {
        //RequestOptions 设置请求参数，通过apply方法设置
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
                .priority(Priority.NORMAL)//指定加载的优先级，优先级越高越优先加载，
                .placeholder(null).error(ERROR)
//                .centerCrop()
                .circleCrop().skipMemoryCache(false).transform(new GlideCircleTransform(borderWidth, borderColor));
        // 图片加载库采用Glide框架
        Glide.with(img).load(url).apply(options).transition(new DrawableTransitionOptions().crossFade()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (img != null) {
                    img.setImageDrawable(resource);
                }
            }
        });
    }

    //加载圆形图片带边框
    public <T> void loadCircleImg(T url, int borderColor, ImageView img) {
        //RequestOptions 设置请求参数，通过apply方法设置
        RequestOptions options = new RequestOptions()
                // 但不保证所有图片都按序加载
                // 枚举Priority.IMMEDIATE，Priority.HIGH，Priority.NORMAL，Priority.LOW
                // 默认为Priority.NORMAL
                // 如果没设置fallback，model为空时将显示error的Drawable，
                // 如果error的Drawable也没设置，就显示placeholder的Drawable
                .priority(Priority.NORMAL)//指定加载的优先级，优先级越高越优先加载，
                .placeholder(null).error(ERROR)
//                .centerCrop()
                .circleCrop().skipMemoryCache(false).transform(new GlideCircleTransform(borderColor));
        // 图片加载库采用Glide框架
        Glide.with(img).load(url).apply(options).transition(new DrawableTransitionOptions().crossFade()).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (img != null) {
                    img.setImageDrawable(resource);
                }
            }
        });
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
