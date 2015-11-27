package com.season.scut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        String name =mEtname.getText().toString();
        String passwd =mEtpasswd.getText().toString();
        //TODO
        setMResult(name,passwd);
    }

    public void setMResult(String name,String passwd){
        Intent intent =new Intent();
        intent.putExtra("passwd",passwd);
        intent.putExtra("name",name);
        setResult(RESULT_OK, intent);
    }

}
