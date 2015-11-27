package com.season.scut;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/11/27.
 */
public class CaseActivity extends Activity {

    TextView mTvtitle;
    TextView mTvmatter;
    TextView mTvtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case);
        mTvmatter=(TextView)findViewById(R.id.tv_matter);
        mTvtitle=(TextView)findViewById(R.id.tv_title);
        mTvtime=(TextView)findViewById(R.id.tv_time);
        Case mCase =getIntent().getParcelableExtra("data");
        mTvmatter.setText(mCase.getMatters());
        mTvtime.setText(mCase.getStrTime());
        mTvtitle.setText(mCase.getTitle());
    }
}
