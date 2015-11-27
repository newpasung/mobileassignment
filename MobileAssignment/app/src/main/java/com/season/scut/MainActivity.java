package com.season.scut;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<Case> caseList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        caseList=Case.getDebugData();
        mRecyclerView=(RecyclerView)findViewById(R.id.recycler);
        mRecyclerView.setAdapter(new MyAdapter());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        AlarmManager alarmManager =(AlarmManager)getSystemService(ALARM_SERVICE);
        PendingIntent pendingIntent =PendingIntent.getActivity(this,1
                ,new Intent(this,NotifyActivity.class),PendingIntent.FLAG_ONE_SHOT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,caseList.get(0).getStarttime()+1000*10,pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_newcase:{
                startActivity(new Intent(this,NewCaseActivity.class));
            }break;
        }
        return true;
    }

    class MyAdapter extends RecyclerView.Adapter{
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.listitem_case,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            MHolder mHolder =(MHolder)holder;
            mHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent();
                    intent.setClass(MainActivity.this,CaseActivity.class);
                    intent.putExtra("data",caseList.get(position));
                    startActivity(intent);
                }
            });
            mHolder.mTvtime.setText(caseList.get(position).getStrTime());
            mHolder.mTvtitle.setText(caseList.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return caseList.size();
        }

        class MHolder extends RecyclerView.ViewHolder{

            TextView mTvtime;
            TextView mTvtitle;
            public MHolder(View itemView) {
                super(itemView);
                mTvtime=(TextView)itemView.findViewById(R.id.tv_time);
                mTvtitle=(TextView) itemView.findViewById(R.id.tv_title);
            }
        }

    }

}
