package com.example.zxs.laundryinireland;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendRequestWithOkhttp(String address,okhttp3.Callback callback)
    {
        OkHttpClient client=new OkHttpClient();
        final Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
