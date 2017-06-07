package com.dev.mevur.cloudwave.http;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mevur on 5/24/2017.
 */

public class Http {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final int READ_TIMEOUT = 4000;
    public static final int CONNECT_TIMEOUT = 4000;
    private String method;
    private String host;
    private String url;
    private JSONObject parameter;
    private JSONObject data;
    private HttpURLConnection connection;

    public Http() {
    }

    private void init(IRequest request) {
        this.method = request.getMethod();
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public void setParameter(JSONObject parameter) {
        this.parameter = parameter;
    }

    protected void setHost(String host) {
        this.host = host;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    final protected void getConnection(final IRequest request) throws Exception {
        init(request);
        URL conUrl = new URL(getUrl());
        System.out.println(conUrl);
        connection = (HttpURLConnection) conUrl.openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        connection.setDoInput(true);
        if (method.equals(METHOD_POST)) {
            connection.setDoOutput(true);
            if (data != null) {
                writeData();
            }
        }
        connection.connect();
        final int code = connection.getResponseCode();
        final String data = Helper.getString(connection.getInputStream());
        final String error = Helper.getString(connection.getErrorStream());
        request.response(code, data, error);
        if (connection != null) {
            connection.disconnect();
        }
    }

    private void writeData() {
        try {
            DataOutputStream dos = new DataOutputStream(
                    connection.getOutputStream());
            if (data != null) {
                String content = Helper.jsonToString(data);
                dos.write(content.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(host + "/" + url);
        if (parameter != null) {
            //添加参数
            String params = Helper.jsonToString(parameter);
            if (params != null) {
                sb.append("?" + params);
            }
        }
        return sb.toString();
    }


}
