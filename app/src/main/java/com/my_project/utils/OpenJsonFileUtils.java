package com.my_project.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017\11\20 0020.
 */

public class OpenJsonFileUtils {
    public static String getJson(Context ctx, String fileName) {
        StringBuilder sb = new StringBuilder();
        AssetManager assetManager = ctx.getAssets();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(assetManager.open(fileName)));
            String next = "";
            while ((next = br.readLine()) != null) {
                sb.append(next);
            }
        } catch (Exception e) {
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }

}
