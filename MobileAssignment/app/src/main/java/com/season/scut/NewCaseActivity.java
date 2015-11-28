package com.season.scut;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2015/11/27.
 */
public class NewCaseActivity extends Activity {

    EditText mEttitle;
    EditText mEtmatter;
    EditText mEtnotifytime;
    Button mBtnstarttime;
    Button mBtnendtime;
    GregorianCalendar startDate;
    GregorianCalendar endDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcase);
        mEttitle=(EditText)findViewById(R.id.et_title);
        mEtmatter=(EditText)findViewById(R.id.et_matter);
        mEtnotifytime=(EditText)findViewById(R.id.et_notifytime);
        mBtnstarttime=(Button)findViewById(R.id.btn_starttime);
        mBtnendtime=(Button)findViewById(R.id.btn_endtime);

        final TimePickerDialog starttimedialog =new TimePickerDialog(NewCaseActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
                startDate.set(Calendar.MINUTE,minute);
                mBtnstarttime.setText(startDate.toString());
            }
        },0,0,true);
        final TimePickerDialog endtimedialog =new TimePickerDialog(NewCaseActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endDate.set(Calendar.HOUR_OF_DAY,hourOfDay);
                endDate.set(Calendar.MINUTE,minute);
                mBtnstarttime.setText(endDate.toString());
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
                        },2015,0,0);
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
                },2015,0,0);
                datePickerDialog.show();
            }

        });
    }

    public void netNewCase(){
        long starttime = startDate.getTime().getTime();
        long endtime =endDate.getTime().getTime();
        String title =mEttitle.getText().toString();
        String matter =mEtmatter.getText().toString();
        String notifytime=mEtnotifytime.getText().toString();
    }

}
