package com.season.scut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.season.scut.net.HttpClient;
import com.season.scut.net.JsonResponseHandler;
import com.season.scut.net.RequestParamName;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/11/27.
 */
public class RegisterActivity extends Activity {

    EditText mEtname;
    EditText mEtpasswd;
    Button mBtnregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mEtname=(EditText)findViewById(R.id.et_name);
        mEtpasswd=(EditText)findViewById(R.id.et_passwd);
        mBtnregister=(Button)findViewById(R.id.btn_register);
        mBtnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netRegister();
            }
        });
    }

    public void netRegister(){
        final String name =mEtname.getText().toString();
        final String passwd =mEtpasswd.getText().toString();

        RequestParams params = new RequestParams();
        params.put(RequestParamName.USERNAME, name);
        params.put(RequestParamName.PASSWORD, passwd);
        HttpClient.post(this, "user/register", params, new JsonResponseHandler() {
            @Override
            public void onSuccess(JSONObject response) {
                setMResult(name, passwd);
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

    public void setMResult(String name,String passwd){
        Intent intent =new Intent();
        intent.putExtra("passwd",passwd);
        intent.putExtra("name",name);
        setResult(RESULT_OK, intent);
    }

}
