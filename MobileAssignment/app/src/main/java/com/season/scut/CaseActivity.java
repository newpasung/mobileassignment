package com.season.scut;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.season.scut.net.HttpClient;
import com.season.scut.net.JsonResponseHandler;
import com.season.scut.net.RequestParamName;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Administrator on 2015/11/27.
 */
public class CaseActivity extends Activity {

    EditText mEttitle;
    EditText mEtmatter;
    EditText mEtAlarmTime;
    Button mBtnstarttime;
    Button mBtnendtime;
    Button mBtnOK;
    Button mbtnDelete;
    GregorianCalendar startDate=new GregorianCalendar(Locale.CHINA);
    GregorianCalendar endDate =new GregorianCalendar(Locale.CHINA);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);
        mEtmatter =(EditText)findViewById(R.id.et_matter);
        mEttitle =(EditText)findViewById(R.id.et_title);
        mEtAlarmTime=(EditText)findViewById(R.id.et_notifytime);
        mBtnstarttime=(Button)findViewById(R.id.btn_starttime);
        mBtnendtime=(Button)findViewById(R.id.btn_endtime);
        mBtnOK=(Button)findViewById(R.id.btn_ok);
        mbtnDelete = (Button)findViewById(R.id.btn_delete);

        final Case mCase =getIntent().getParcelableExtra("data");
        Log.i("schedule_id" , mCase.id+"");
        mEttitle.setText(mCase.getTitle());
        mEtmatter.setText(mCase.getMatters());
        mEtAlarmTime.setText("提前"+((mCase.starttime - (mCase.alarmtime))/60) + "分钟提醒");
        mBtnstarttime.setText(mCase.getStringStartTime());
        mBtnendtime.setText(mCase.getStringEndTime());

        final TimePickerDialog starttimedialog =new TimePickerDialog(CaseActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
                startDate.set(Calendar.MINUTE,minute);
                mBtnstarttime.setText(startDate.get(Calendar.YEAR) + "." + startDate.get(Calendar.MONTH) + "." + startDate.get(Calendar.DATE) + " " + startDate.get(Calendar.HOUR_OF_DAY) + ":" + startDate.get(Calendar.MINUTE));
            }
        },0,0,true);
        final TimePickerDialog endtimedialog =new TimePickerDialog(CaseActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
                endDate.set(Calendar.MINUTE,minute);
                mBtnendtime.setText(endDate.get(Calendar.YEAR) + "." + endDate.get(Calendar.MONTH) + "." + endDate.get(Calendar.DATE) + " " + endDate.get(Calendar.HOUR_OF_DAY) + ":" + endDate.get(Calendar.MINUTE));
            }
        },0,0,true);

        mBtnstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CaseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.set(Calendar.YEAR, year);
                                startDate.set(Calendar.MONTH, monthOfYear);
                                startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                starttimedialog.show();
                            }
                        }, 2015, 11, 28);
                datePickerDialog.show();

            }
        });

        mBtnendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(CaseActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate.set(Calendar.YEAR, year);
                        endDate.set(Calendar.MONTH, monthOfYear);
                        endDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        endtimedialog.show();
                    }
                }, 2015, 11, 28);
                datePickerDialog.show();
            }

        });

        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netUpdaeCase(mCase);
            }
        });

        mbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netDelete(mCase);
            }
        });
    }

    public void netDelete(final Case mcase) {
        Case.deletebyid(mcase.getId());
        RequestParams params = new RequestParams();
        params.put("schedule_id", mcase.getId());

        HttpClient.post(this, "schedule/delete", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
//                Case.deletebyid(mcase.getId());
                setResult(RESULT_OK, new Intent());
                finish();
            }

            @Override
            public void onFailure(String message, String for_param) {
                if (message != null) {
                    Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void netUpdaeCase(Case mcase){
        long starttime = startDate.getTime().getTime()/1000;
        long endtime =endDate.getTime().getTime()/1000;
        String title =mEttitle.getText().toString();
        String matter =mEtmatter.getText().toString();
        long notifytime=starttime - 60 * Long.valueOf(mEtAlarmTime.getText().toString());

        Case.getCaseById(mcase.getId()).starttime=starttime;
        Case.getCaseById(mcase.getId()).endtime=endtime;
        Case.getCaseById(mcase.getId()).title=title;
        Case.getCaseById(mcase.getId()).matters=matter;
        Case.getCaseById(mcase.getId()).alarmtime=notifytime;

        RequestParams params = new RequestParams();
        params.put("schedule_id", mcase.getId());
        params.put(RequestParamName.TITLE, title);
        params.put(RequestParamName.START_TIME, starttime);
        params.put(RequestParamName.END_TIME, endtime);
        params.put(RequestParamName.CONTENT, matter);
        params.put(RequestParamName.ALARM_TIME, notifytime);

        HttpClient.post(this, "schedule/update", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                setResult(RESULT_OK, new Intent());
                finish();
            }

            @Override
            public void onFailure(String message, String for_param) {
                if (message != null) {
                    Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
