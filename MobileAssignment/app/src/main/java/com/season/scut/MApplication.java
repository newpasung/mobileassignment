package com.season.scut;

import android.app.AlarmManager;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.season.scut.net.HttpClient;
import com.season.scut.net.JsonResponseHandler;
import com.season.scut.net.RequestParamName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/27.
 */
public class MApplication extends Application {

    public static final String ACTION_MODIFY ="modifyexistingdata";
    PendingIntent pendingIntent;
    public static MApplication mApplication;

    BroadcastReceiver receiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_MODIFY)){
                Case mCase=intent.getParcelableExtra("data");
                netUpdaeCase(mCase);
            }
        }
    };

    public static MApplication getInstance(){
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication=this;
        loadNotification();
        LocalBroadcastManager manager =LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(receiver,new IntentFilter());
        pendingIntent =PendingIntent.getActivity(this,1
                ,new Intent
                (getApplicationContext(),NotifyActivity.class),PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LocalBroadcastManager manager =LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(receiver);
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
        editor.putLong("notification_caseid" + totalnum, caseid);
        editor.putLong("notification_time" + totalnum, time);
        //更新计数器
        editor.putInt("notification_totalcount", totalnum);
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
        long need_time=-1;
        long need_caseid=0;
        while (index<=totalnum){
            long caseid=sharedPreferences.getLong("notification_caseid"+index,0);
            long time =sharedPreferences.getLong("notification_time"+index,0);
            if (caseid==0||time==0){
                return ;
            }
            if (need_time<System.currentTimeMillis()&&time>System.currentTimeMillis()){
                need_time=time;
            }else{
                index++;
                continue;
            }
            if (time>System.currentTimeMillis()){
                if (need_time<time){
                    need_time=time;
                    need_caseid =caseid;
                }
            }
            index++;
        }
        if (Case.getCaseById(need_caseid)!=null)
        alarmManager.set(AlarmManager.RTC_WAKEUP, Case.getCaseById(need_caseid).getAlarmtime() * 1000, pendingIntent);
    }

    public void netUpdaeCase(Case mcase){
        long starttime = mcase.getStarttime();
        long endtime =mcase.getEndtime();
        String title =mcase.getTitle();
        String matter =mcase.getMatters();
        long notifytime=mcase.getAlarmtime();

        Case.getCaseById(mcase.getId()).starttime=starttime;
        Case.getCaseById(mcase.getId()).endtime=endtime;
        Case.getCaseById(mcase.getId()).title=title;
        Case.getCaseById(mcase.getId()).matters=matter;
        Case.getCaseById(mcase.getId()).alarmtime=notifytime;

        RequestParams params = new RequestParams();
        params.put("id", mcase.getId());
        params.put(RequestParamName.TITLE, title);
        params.put(RequestParamName.START_TIME, starttime);
        params.put(RequestParamName.END_TIME, endtime);
        params.put(RequestParamName.CONTENT, matter);
        params.put(RequestParamName.ALARM_TIME, notifytime);

        HttpClient.post(this, "schedule/update", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Case.insertOrUpdate(getInstance(),response.getJSONObject("data").getJSONObject("schedule"));
                    loadNotification();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
            }
        });
    }

}
