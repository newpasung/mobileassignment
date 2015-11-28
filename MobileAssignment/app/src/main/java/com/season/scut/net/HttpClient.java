package com.season.scut.net;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.season.scut.XManager;

import java.lang.ref.WeakReference;

/**
 * Created by gjz on 11/2/15.
 */
public class HttpClient {
    private static final String BASE_URL = "http://121.42.160.104/manager/web/";
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(Context context, String url, RequestParams params, JsonResponseHandler handler){
        handler.setWeakReference(new WeakReference<Context>(context));
        params.put(RequestParamName.TOKEN, XManager.getToken(context));
        client.get(context, BASE_URL + url, params, handler);
    }

    public static void post(Context context, String url, RequestParams params, JsonResponseHandler handler){
        handler.setWeakReference(new WeakReference<Context>(context));
        params.put(RequestParamName.TOKEN, XManager.getToken(context));
        client.post(context, BASE_URL + url, params, handler);
        }

}
