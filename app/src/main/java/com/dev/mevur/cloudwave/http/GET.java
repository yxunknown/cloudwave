package com.dev.mevur.cloudwave.http;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by mevur on 5/25/2017.
 */

public class GET extends Http implements IRequest, Runnable {
    private IHttpHander hander;
    private int request;
    private Handler handler;

    public GET(String host, String url, IHttpHander hander, int request) {
        super.setHost(host);
        super.setUrl(url);
        this.hander = hander;
        this.request = request;
        handler = new Handler(Looper.getMainLooper());
    }

    public void connect() {
        new Thread(this).start();
    }

    @Override
    public String getMethod() {
        return Http.METHOD_GET;
    }

    @Override
    public void response(final int code, final String data, final String error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                hander.handler(code, data, error, request);
            }
        });
    }

    @Override
    public void run() {
        try {
            System.out.println("get connect");
            getConnection(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
