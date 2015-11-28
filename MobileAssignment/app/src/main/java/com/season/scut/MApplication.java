package com.season.scut;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/11/27.
 */
public class MApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        loadNotification();
    }

    public synchronized void addNotification(long time,long caseid){
        //打开notifications文件
        SharedPreferences sharedPreferences =getSharedPreferences("notifications", MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        //（包括执行过的）
        int totalnum=sharedPreferences.getInt("notification_totalcount",0);
        //这个东西用某个key+一个顺序索引来表示一个提醒任务的属性
        totalnum++;
        //存入两个数据
        editor.putLong("notification_caseid"+totalnum,caseid);
        editor.putLong("notification_time"+totalnum,time);
        //更新计数器
        editor.putInt("notification_totalcount",totalnum);
        editor.commit();
    }

    public void loadNotification(){
        SharedPreferences sharedPreferences =getSharedPreferences("notifications", MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        int totalnum=sharedPreferences.getInt("notification_totalcount", 0);
        if (totalnum<=0)return ;
        //不管效率了--全部查一遍
        int index=1;
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent =PendingIntent.getActivity(this,1
                ,new Intent
                (getApplicationContext(),NotifyActivity.class),PendingIntent.FLAG_ONE_SHOT);
        while (index<=totalnum){
            long caseid=sharedPreferences.getLong("notification_caseid",0);
            long time =sharedPreferences.getLong("notification_time",0);
            if (caseid==0||time==0){
                return ;
            }
            if (time>=System.currentTimeMillis()){
                alarmManager.set(AlarmManager.RTC_WAKEUP,time,pendingIntent);
            }
            index++;
        }
    }

}
