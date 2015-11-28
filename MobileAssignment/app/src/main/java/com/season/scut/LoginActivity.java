package com.season.scut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.season.scut.net.HttpClient;
import com.season.scut.net.JsonResponseHandler;
import com.season.scut.net.RequestParamName;

import org.json.JSONException;
import org.json.JSONObject;

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
                if (!TextUtils.isEmpty(mEtpasswd.getText().toString()) && !TextUtils.isEmpty(mEtname.getText().toString())) {
                    netLogin(mEtname.getText().toString(), mEtpasswd.getText().toString());
                }
            }
        });

        mTvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void netLogin(String name,String passwd){
        RequestParams params = new RequestParams();
        params.put(RequestParamName.USERNAME, name);
        params.put(RequestParamName.PASSWORD, passwd);
        HttpClient.post(this, "user/login", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    savaLoginState(response.getJSONObject("data").getJSONObject("user").getString("token"));
                    SharedPreferences preferences =getPreferences(MODE_PRIVATE);
                    String token = preferences.getString(KEY_TOKEN, null);
                    Log.i("token", token);
                    startActivity(new Intent(getApplication(), MainActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String message, String for_param) {
                if (message != null) {
                    Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isLogined(){
        return getPreferences(MODE_PRIVATE).getInt(KEY_LOGINSTATE,VALUE_NOT_LOGIN)!=VALUE_NOT_LOGIN;
    }

    public void savaLoginState(String token){
        SharedPreferences preferences =getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor =preferences.edit();
        editor.putString(KEY_TOKEN, token);
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
