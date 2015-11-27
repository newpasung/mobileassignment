package com.season.scut.net;

import android.content.Context;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;


/**
 * Created by gjz on 11/2/15.
 */
public abstract class JsonResponseHandler extends JsonHttpResponseHandler{
    WeakReference<Context> weakReference = null;

    public void setWeakReference(WeakReference<Context> weakReference) {
        this.weakReference = weakReference;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        try {
            int status = response.getInt("status");
            if (status == 1) {
                onSuccess(response);
            }else{
                String message = response.getString("message");
                String for_param = response.getString("for_param");
                onFailure(message, for_param);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        onFailure("请求异常", "common");
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        onFailure("请求异常", "common");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        if (throwable != null && throwable.getCause() != null) {
            if (throwable.getCause().toString().contains("Network is unreachable")) {
                onFailure("请检查网络是否正常!", "common");
                return;
            }
        }
        onFailure("请求异常", "common");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        onFailure("请求异常", "common");
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        onFailure("请求异常", "common");
    }

    public abstract void onSuccess(JSONObject response);
    public abstract void onFailure(String message, String for_param);
}
