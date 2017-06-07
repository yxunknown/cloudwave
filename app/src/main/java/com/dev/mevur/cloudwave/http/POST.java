package com.dev.mevur.cloudwave.http;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by mevur on 5/26/2017.
 */

public class POST extends Http implements Runnable, IRequest{
    private IHttpHander httpHander;
    private int request;
    private Handler handler;
    public POST(String host, String url, IHttpHander hander, int request) {
        super.setHost(host);
        super.setUrl(url);
        this.httpHander = hander;
        this.request = request;
        handler = new Handler(Looper.getMainLooper());
    }

    public void connect() {
        new Thread(this).start();
    }

    @Override
    public String getMethod() {
        return Http.METHOD_POST;
    }

    @Override
    public void response(final int code, final String data, final String error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                httpHander.handler(code, data, error, request);
            }
        });
    }

    @Override
    public void run() {
        try {
            getConnection(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
