package com.season.scut;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Administrator on 2015/11/27.
 */
public class Case implements Parcelable{

    public long starttime;
    public long endtime;
    public String title;
    public String matters;

    public Case(long endtime, long starttime, String matters,String title) {
        this.endtime = endtime;
        this.matters = matters;
        this.starttime = starttime;
        this.title=title;
    }

    protected Case(Parcel in) {
        starttime = in.readLong();
        endtime = in.readLong();
        title = in.readString();
        matters = in.readString();
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

    public static List<Case> getDebugData(){
        List<Case> caseList =new ArrayList<>();
        for (int i=0;i<30;i++){
            caseList.add(new Case(System.currentTimeMillis(),System.currentTimeMillis()
                    ,"matter"+i,"title"+i));
        }



        return caseList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(starttime);
        dest.writeLong(endtime);
        dest.writeString(title);
        dest.writeString(matters);
    }
}
