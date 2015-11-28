package com.season.scut;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/11/1.
 * 用于管理系统生成的xml文件，多为一些基础的配置文件
 */
public class XManager {
    //关于一些与特定用户无关的资料
    public static final String FILENAME_SYSTEM = "systemconfig";
    public static SharedPreferences sysPref;

    //用于特定用户的配置信息
    public static final String FILENAME_USER = "userconfig";
    public static SharedPreferences userPref;

    //一些用户无关的参数
    public static String PARAM_OPENED = "hasopened";//是否已经打开过app，用于判断是否显示引导页
    public static String PARAM_LOGINED = "haslogined";//是否已经登录
    //一些用户相关的参数
    public static String PARAM_TOKEN = "token";//用户token
    public static String PARAM_UID = "uid";//用户uid

    public static synchronized SharedPreferences getSystemManager(Context context) {
        if (sysPref == null) {
            sysPref = context.getSharedPreferences(FILENAME_SYSTEM, Context.MODE_PRIVATE);
        }
        return sysPref;
    }

    public static synchronized SharedPreferences getUserManager(Context context) {
        if (userPref == null) {
            userPref = context.getSharedPreferences(FILENAME_USER, Context.MODE_PRIVATE);
        }
        return userPref;
    }

    //获取是否已登录
    public static boolean isLogined(Context context) {
        return getSystemManager(context).getBoolean(PARAM_LOGINED, false);
    }

    /**
     * 设置是否已登录
     *
     * @param status 登录状态是或否
     */
    public static void setLoginStatus(Context context, boolean status) {
        SharedPreferences.Editor editor = getSystemManager(context).edit();
        editor.putBoolean(PARAM_LOGINED, status);
        editor.apply();
    }

    //获取是否已打开过app
    public static boolean hasOpened(Context context) {
        return getSystemManager(context).getBoolean(PARAM_OPENED, false);
    }

    /**
     * 设置是否已经打开过app
     *
     * @param status 是否打开
     */
    public static void setOpenedStatus(Context context, boolean status) {
        SharedPreferences.Editor editor = getSystemManager(context).edit();
        editor.putBoolean(PARAM_OPENED, status);
        editor.apply();
    }

    //获取用户token，如果没有则返回空字符串
    public static String getToken(Context context) {
        return getUserManager(context).getString(PARAM_TOKEN, null);
    }

    /**
     * 设置用户token
     *
     * @param token 要设置成的token
     */
    public static void setToken(Context context, String token) {
        SharedPreferences.Editor editor = getUserManager(context).edit();
        editor.putString(PARAM_TOKEN, token);
        editor.apply();
    }

    //获取用户uid，如果没有则返回0
    public static long getUid(Context context) {
        return getUserManager(context).getLong(PARAM_UID, 0l);
    }

    /**
     * 设置用户token
     *
     * @param uid 要设置成的uid
     */
    public static void setUid(Context context, int uid) {
        SharedPreferences.Editor editor = getUserManager(context).edit();
        editor.putLong(PARAM_UID, uid);
        editor.apply();
    }
}
