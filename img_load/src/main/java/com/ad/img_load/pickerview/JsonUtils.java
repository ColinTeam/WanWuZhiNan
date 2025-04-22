package com.ad.img_load.pickerview;

import android.content.Context;
import android.content.res.AssetManager;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Company:
 * FileName:JsonUtils
 * Author:android
 * Mail:2898682029@qq.com
 * Date:20-2-20 下午1:41
 * Description:${DESCRIPTION}
 */
public class JsonUtils {

    public static String getJson(String fileName,Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }



}
