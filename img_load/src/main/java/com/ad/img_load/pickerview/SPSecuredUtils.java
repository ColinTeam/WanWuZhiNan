package com.ad.img_load.pickerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Company:HD
 * ProjectName: 原力数藏
 * Package: com.nft.ylsc.util
 * ClassName: SPSecuredUtils
 * Author:ShiMing Shi
 * CreateDate: 2022/4/29 20:02
 * Email:shiming024@163.com
 * Description:
 */
public final class SPSecuredUtils {

    private static volatile SPSecuredUtils instance;

    //文件名
    private static final String FILE_NAME = "sp_secured";
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    private SPSecuredUtils(Context context) {
        //name 文件名
        //mode 模式
        //MODE_PRIVATE, 默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下
        //写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中 可以使用Activity.MODE_APPEND
        //MODE_WORLD_READABLE 表示当前文件可以被其他应用读取，
        //MODE_WORLD_WRITEABLE,表示当前文件可以被其他应用写入；
        //如果希望文件被其他应用读和写，可以传入:MODE_WORLD_READABLE+Activity.MODE_WORLD_WRITEABLE
        //MODE_APPEND 该模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件
        mSharedPreferences = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
    }

    public static SPSecuredUtils newInstance(Context context) {
        if (instance == null) {
            synchronized (SPSecuredUtils.class) {
                if (instance == null) {
                    instance = new SPSecuredUtils(context);
                }
            }
        }
        return instance;
    }

    public boolean put(String key, Object value) {
        if (TextUtils.isEmpty(key) || value == null) {
            return false;
        }
        boolean result = false;
        try {
            String type = value.getClass().getSimpleName();
            switch (type) {
                default:
                case "String":
                    editor.putString(key, (String) value);
                    break;
                case "Integer":
                    editor.putInt(key, (Integer) value);
                    break;
                case "Boolean":
                    editor.putBoolean(key, (Boolean) value);
                    break;
                case "Long":
                    editor.putLong(key, (Long) value);
                    break;
                case "Float":
                    editor.putFloat(key, (Float) value);
                    break;
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferencesCompat.apply(editor);
        return result;
    }

    public Object get(String key, Object value) {
        if (TextUtils.isEmpty(key) || value == null) {
            return null;
        }
        String type = value.getClass().getSimpleName();
        switch (type) {
            case "String":
                return mSharedPreferences.getString(key, (String) value);
            case "Integer":
                return mSharedPreferences.getInt(key, (Integer) value);
            case "Boolean":
                return mSharedPreferences.getBoolean(key, (Boolean) value);
            case "Float":
                return mSharedPreferences.getFloat(key, (Float) value);
            case "Long":
                return mSharedPreferences.getLong(key, (Long) value);
            default:
                return mSharedPreferences.getString(key, value.toString());
        }
    }

    //移除某个key值已经对应的值
    public void remove(String key) {
        if (contains(key)) {
            editor.remove(key);
            SharedPreferencesCompat.apply(editor);
        }
    }

    //清除所有数据
    public  void clear() {
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }

    //查询某个key是否已经存在
    private static boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    //返回所有的键值对
    public static Map<String, ?> getAll() {
        return mSharedPreferences.getAll();
    }


    //创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
    private static class SharedPreferencesCompat {

        private static final Method sApplyMethod = findApplyMethod();

        //反射查找apply的方法
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        //如果找到则使用apply执行，否则使用commit
        private static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }


    public void clearUserInfo(){
//        remove(Constant.UID);
//        remove(Constant.USER_NICK_NAME);
//        remove(Constant.TOKEN);
//        remove(Constant.USER_MOBILE);
//        remove(Constant.USER_ALI_ACCOUNT);
//        remove(Constant.USER_TP_ADDRESS);
//        remove(Constant.USER_IS_VIP);
//        remove(Constant.IS_SET_PW);
//        remove(Constant.USER_CODE);
//
//        remove(Constant.BANK_CARD_CODE);
//        remove(Constant.BANK_CARD_NAME);
//        remove(Constant.BANK_CARD_ID);
//        remove(Constant.BANK_CARD_MOBILE);
        //remove(Constant.NOTICE_ID);
       // AdSdk.getInstance().setUserId(null);
    }

}
