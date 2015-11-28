package com.season.scut;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Administrator on 2015/11/27.
 */
public class NewCaseActivity extends Activity {

    EditText mEttitle;
    EditText mEtmatter;
    EditText mEtnotifytime;
    Button mBtnstarttime;
    Button mBtnendtime;
    Button mBtnOK;
    GregorianCalendar startDate=new GregorianCalendar(Locale.CHINA);
    GregorianCalendar endDate =new GregorianCalendar(Locale.CHINA);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcase);
        mEttitle=(EditText)findViewById(R.id.et_title);
        mEtmatter=(EditText)findViewById(R.id.et_matter);
        mEtnotifytime=(EditText)findViewById(R.id.et_notifytime);
        mBtnstarttime=(Button)findViewById(R.id.btn_starttime);
        mBtnendtime=(Button)findViewById(R.id.btn_endtime);
        mBtnOK=(Button)findViewById(R.id.btn_ok);

        final TimePickerDialog starttimedialog =new TimePickerDialog(NewCaseActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
                startDate.set(Calendar.MINUTE,minute);
                mBtnstarttime.setText(startDate.get(Calendar.YEAR) + "." + startDate.get(Calendar.MONTH)+1 + "." + startDate.get(Calendar.DATE) + " " + startDate.get(Calendar.HOUR_OF_DAY) + ":" + startDate.get(Calendar.MINUTE));
            }
        },0,0,true);
        final TimePickerDialog endtimedialog =new TimePickerDialog(NewCaseActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
                endDate.set(Calendar.MINUTE,minute);
                mBtnendtime.setText(endDate.get(Calendar.YEAR) + "." + endDate.get(Calendar.MONTH)+1 + "." + endDate.get(Calendar.DATE) + " " + endDate.get(Calendar.HOUR_OF_DAY) + ":" + endDate.get(Calendar.MINUTE));
            }
        },0,0,true);

        mBtnstarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =new DatePickerDialog(NewCaseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDate.set(Calendar.YEAR,year);
                                startDate.set(Calendar.MONTH,monthOfYear);
                                startDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                starttimedialog.show();
                            }
                        },2015,10,28);
                datePickerDialog.show();

            }
        });

        mBtnendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =new DatePickerDialog(NewCaseActivity.this
                        , new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate.set(Calendar.YEAR,year);
                        endDate.set(Calendar.MONTH,monthOfYear);
                        endDate.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        endtimedialog.show();
                    }
                },2015,10,28);
                datePickerDialog.show();
            }

        });

        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netNewCase();
            }
        });
    }

    public void netNewCase(){
        long starttime = startDate.getTime().getTime()/1000;
        long endtime =endDate.getTime().getTime()/1000;
        String title =mEttitle.getText().toString();
        String matter =mEtmatter.getText().toString();
        if (TextUtils.isEmpty(title)||TextUtils.isEmpty(matter)||TextUtils.isEmpty(mEtnotifytime.getText().toString())){
            Toast.makeText(this,"请完整输入",Toast.LENGTH_SHORT).show();
            return ;
        }
        long notifytime=starttime - 60 * Long.valueOf(mEtnotifytime.getText().toString());



        RequestParams params = new RequestParams();
        params.put(RequestParamName.TITLE, title);
        params.put(RequestParamName.START_TIME, starttime);
        params.put(RequestParamName.END_TIME, endtime);
        params.put(RequestParamName.CONTENT, matter);
        params.put(RequestParamName.ALARM_TIME, notifytime);

        HttpClient.post(this, "schedule/create", params, new JsonResponseHandler() {
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
