package com.season.scut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/11/27.
 */
public class LoginActivity extends Activity {
    final String KEY_TOKEN="HAHAHAHAHAASDFASDFAS";
    final String KEY_LOGINSTATE="SAEIOWEHROAWHE";
    final int VALUE_LOGINED=1;
    final int VALUE_NOT_LOGIN=0;
    EditText mEtname;
    EditText mEtpasswd;
    TextView mTvregister;
    Button mBtnlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (isLogined()){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        mEtname=(EditText)findViewById(R.id.et_name);
        mEtpasswd=(EditText)findViewById(R.id.et_passwd);
        mTvregister=(TextView)findViewById(R.id.tv_register);
        mBtnlogin=(Button)findViewById(R.id.btn_login);

        mBtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mEtpasswd.getText().toString())&&!TextUtils.isEmpty(mEtname.getText().toString())){
                    netLogin(mEtname.getText().toString(),mEtpasswd.getText().toString());
                }
            }
        });

        mTvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(LoginActivity.this,RegisterActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    public void netLogin(String name,String passwd){
        //TODO
        savaLoginState(12342342);
    }

    public boolean isLogined(){
        return getPreferences(MODE_PRIVATE).getInt(KEY_LOGINSTATE,VALUE_NOT_LOGIN)!=VALUE_NOT_LOGIN;
    }

    public void savaLoginState(long token){
        SharedPreferences preferences =getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor =preferences.edit();
        editor.putLong(KEY_TOKEN,token);
        editor.putInt(KEY_LOGINSTATE,VALUE_LOGINED);
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1&&resultCode==RESULT_OK){
            mEtpasswd.setText(data.getStringExtra("passwd"));
            mEtname.setText(data.getStringExtra("name"));
        }
    }
}
