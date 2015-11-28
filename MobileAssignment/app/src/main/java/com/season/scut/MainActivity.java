package com.season.scut;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.season.scut.net.HttpClient;
import com.season.scut.net.JsonResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<Case> caseList;
    ProgressDialog waitingDialog;
    MyAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waitingDialog=new ProgressDialog(this);
        waitingDialog.show();
        netRefreshData();
        caseList=new ArrayList<>();
        mRecyclerView=(RecyclerView)findViewById(R.id.recycler);
        adapter=new MyAdapter();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
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

    public void netRefreshData(){
        RequestParams params =new RequestParams();
        HttpClient.get(this, "schedule/list", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                JSONArray array = null;
                try {
                    array = response.getJSONArray("data");
                    caseList=Case.insertOrUpdate(array);
                    adapter.notifyDataSetChanged();
                    MApplication.getInstance().loadNotification();
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    waitingDialog.dismiss();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                waitingDialog.dismiss();
                Toast.makeText(MainActivity.this,"联网失败",Toast.LENGTH_SHORT).show();
            }
        });
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
