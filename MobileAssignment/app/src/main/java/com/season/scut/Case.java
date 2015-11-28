package com.season.scut;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/27.
 */
public class Case implements  Parcelable ,Comparable{

    public long id;
    public long starttime;
    public long endtime;
    public long alarmtime;
    public long modifiedtime;
    public String title;
    public String matters;
    public int status;
    public static HashMap<Long ,Case> caseMap=new HashMap<>();

    public Case() {
        this.modifiedtime=0l;
        this.status=1;
    }

    protected Case(Parcel in) {
        id = in.readLong();
        starttime = in.readLong();
        endtime = in.readLong();
        alarmtime = in.readLong();
        modifiedtime = in.readLong();
        title = in.readString();
        matters = in.readString();
        status =in.readInt();
    }

    public static final Creator<Case> CREATOR = new Creator<Case>() {
        @Override
        public Case createFromParcel(Parcel in) {
            return new Case(in);
        }

        @Override
        public Case[] newArray(int size) {
            return new Case[size];
        }
    };

    public long getEndtime() {
        return endtime;
    }

    public String getMatters() {
        return matters;
    }

    public long getStarttime() {
        return starttime;
    }

    public String getTitle() {
        return title;
    }

    public long getAlarmtime() {
        return alarmtime;
    }

    public long getId() {
        return id;
    }

    public long getModifiedtime() {
        return modifiedtime;
    }

    public String getStringStartTime() {
        Date startdate =new Date(getStarttime()*1000);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(startdate);
    }

    public String getStringEndTime() {
        Date enddate =new Date(getStarttime()*1000);
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(enddate);
    }

    public String getStrTime(){
        StringBuilder builder =new StringBuilder();
        Date startdate =new Date(getStarttime());
        Date enddate=new Date(getEndtime());
        builder.append(startdate.getMonth()+1);
        builder.append("月");
        builder.append(startdate.getDay());
        builder.append("日");

        //就不考虑年啦--,取最小变化单位来显示
        if (startdate.getDay()==enddate.getDay()&&startdate.getMonth()==enddate.getMonth()){
            if (startdate.getHours()==enddate.getHours()){
                builder.append(startdate.getHours());
                builder.append("时");
                builder.append(" ");
                builder.append(startdate.getMinutes());
                builder.append("分");
                builder.append(" 至 ");
                builder.append(enddate.getMinutes());
                builder.append("分");
            }else{
                builder.append(" ");
                builder.append(startdate.getHours());
                builder.append("时");
                builder.append(" 至 ");
                builder.append(enddate.getHours());
                builder.append("时");
            }
        }else{
            builder.append(" 至 ");
            enddate=new Date(getEndtime());
            builder.append(enddate.getMonth()+1);
            builder.append("月");
            builder.append(enddate.getDay());
            builder.append("日");
        }

        return builder.toString();
    }

    public static Case insertOrUpdate(Context context,JSONObject object){
        Case mCase=null;
        try {
            long id =object.getLong("id");
            if (caseMap.containsKey(id)){
                long modifiedtime=object.getLong("modified_time");
                mCase= caseMap.get(id);
                if (mCase.modifiedtime>modifiedtime){
                    //这里我们发现我们有新数据
                    LocalBroadcastManager manager =LocalBroadcastManager.getInstance(context);
                    Intent intent =new Intent();
                    intent.setAction(MApplication.ACTION_MODIFY);
                    intent.putExtra("data",mCase);
                    manager.sendBroadcast(intent);
                    return mCase;
                }
                if (object.has("status")){
                    mCase.status=object.getInt("status");
                }
                mCase.starttime=object.getLong("time");
                mCase.endtime=object.getLong("end_time");
                mCase.modifiedtime=modifiedtime;
                mCase.alarmtime =object.getLong("alarm_time");
                mCase.matters=object.getString("content");
                mCase.title=object.getString("title");
            }else{
                mCase =new Case();
                mCase.id=id;
                mCase.starttime=object.getLong("time");
                if (object.has("status")){
                    mCase.status=object.getInt("status");
                }
                mCase.endtime=object.getLong("end_time");
                mCase.modifiedtime=object.getLong("modified_time");
                mCase.alarmtime =object.getLong("alarm_time");
                mCase.matters=object.getString("content");
                mCase.title=object.getString("title");
                caseMap.put(id,mCase);
            }
            if (mCase.alarmtime*1000>System.currentTimeMillis()){
                MApplication.getInstance().addNotification(mCase.alarmtime*1000,mCase.id);
            }
            return mCase;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }

    public static List<Case> insertOrUpdate(Context context,JSONArray array){
        try {
            for (int i=0;i<array.length();i++){
                JSONObject data =array.getJSONObject(i);
                insertOrUpdate(context,data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getListData();
    }

    /**获取可用的case数据*/
    public static List<Case> getListData(){
        List<Case> caseList = new ArrayList<>();
        Iterator<Long> iterator =caseMap.keySet().iterator();
        Case mCase ;
        while(iterator.hasNext()){
            mCase=caseMap.get(iterator.next());
            if (mCase.status==1&&mCase.starttime>=System.currentTimeMillis()/1000){
                caseList.add(mCase);
            }
        }
        Collections.sort(caseList);
        /*for (int i=caseList.size()-1;i>=0;i--){
            for (int j=0;j<i;j++){
                if (caseList.get(j).starttime>caseList.get(j+1).starttime){
                    caseList.get(j).starttime=caseList.get(j).starttime+caseList.get(j+1).starttime;
                    caseList.get(j+1).starttime=caseList.get(j).starttime-caseList.get(j+1).starttime;
                    caseList.get(j).starttime=caseList.get(j).starttime-caseList.get(j+1).starttime;
                }
            }
        }*/
        //然后把list发回去
        return caseList;
    }

    public static Case getCaseById(long id){
        return caseMap.get(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(starttime);
        dest.writeLong(endtime);
        dest.writeLong(alarmtime);
        dest.writeLong(modifiedtime);
        dest.writeString(title);
        dest.writeString(matters);
    }

    public static void deletebyid(long id) {
        caseMap.remove(id);
    }

    @Override
    public int compareTo(Object another) {
        return this.starttime>((Case)another).starttime?1:-1;
    }

}
