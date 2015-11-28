package com.season.scut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
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
    SwipeRefreshLayout mRefreshLayout;
    List<Case> caseList;
    ProgressDialog waitingDialog;
    MyAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waitingDialog=new ProgressDialog(this);
        waitingDialog.show();
        caseList=new ArrayList<>();
        mRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refreshlayout);
        mRecyclerView=(RecyclerView)findViewById(R.id.recycler);
        adapter=new MyAdapter();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                netRefreshData();
            }
        });
        netRefreshData();
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
                startActivityForResult(new Intent(this,NewCaseActivity.class), 111);
            }break;
        }
        return true;
    }

    public void netRefreshData(){
        mRefreshLayout.setRefreshing(true);
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
                    mRefreshLayout.setRefreshing(false);
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
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, CaseActivity.class);
                    intent.putExtra("data", caseList.get(position));
                    startActivityForResult(intent, 222);
                }
            });
            //mHolder.mTvtime.setText(caseList.get(position).getStrTime());
            mHolder.mTvtime.setText(caseList.get(position).getStringStartTime() +"");
            mHolder.mTvtitle.setText(caseList.get(position).getTitle());
            mHolder.mTvcontent.setText(caseList.get(position).getMatters());
        }

        @Override
        public int getItemCount() {
            return caseList.size();
        }

        class MHolder extends RecyclerView.ViewHolder{

            TextView mTvtime;
            TextView mTvtitle;
            TextView mTvcontent;
            public MHolder(View itemView) {
                super(itemView);
                mTvtime=(TextView)itemView.findViewById(R.id.tv_time);
                mTvtitle=(TextView) itemView.findViewById(R.id.tv_title);
                mTvcontent=(TextView)itemView.findViewById(R.id.tv_matter);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 && resultCode == RESULT_OK){
            netRefreshData();
        }
        else if(requestCode == 222 && resultCode == RESULT_OK){
            netRefreshData();
        }
    }
}
