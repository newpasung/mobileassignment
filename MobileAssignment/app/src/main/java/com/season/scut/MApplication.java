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

    PendingIntent pendingIntent;
    public static MApplication mApplication;

    public static MApplication getInstance(){
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication=this;
        loadNotification();
        pendingIntent =PendingIntent.getActivity(this,1
                ,new Intent
                (getApplicationContext(),NotifyActivity.class),PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public synchronized void addNotification(long time,long caseid){
        //打开notifications文件
        SharedPreferences sharedPreferences =getSharedPreferences("notifications", MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();
        //（包括执行过的）
        int totalnum=sharedPreferences.getInt("notification_totalcount",0);
        /*这个东西用某个key+一个顺序索引来表示一个提醒任务的属性
        ，事实上那个提醒时间改变了，这里是会有冗余数据的*/
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
        int totalnum=sharedPreferences.getInt("notification_totalcount", 0);
        if (totalnum<=0)return ;
        //不管效率了--全部查一遍
        int index=1;
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        long need_time=Long.MAX_VALUE;
        long need_caseid=0;
        while (index<=totalnum){
            long caseid=sharedPreferences.getLong("notification_caseid",0);
            long time =sharedPreferences.getLong("notification_time",0);
            if (caseid==0||time==0){
                return ;
            }
            if (need_time>time&&time>System.currentTimeMillis()/1000){
                need_time=time;
                need_caseid =caseid;
            }
            index++;
        }
        alarmManager.set(AlarmManager.RTC_WAKEUP,Case.getCaseById(need_caseid).getAlarmtime(),pendingIntent);
    }
}
