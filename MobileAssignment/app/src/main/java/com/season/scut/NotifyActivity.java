package com.season.scut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/11/27.
 */
public class NotifyActivity extends Activity {

    TextView mTvtitle;
    TextView mTvmatter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        mTvmatter=(TextView)findViewById(R.id.tv_matter);
        mTvtitle=(TextView)findViewById(R.id.tv_title);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
