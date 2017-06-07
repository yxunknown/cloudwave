package com.dev.mevur.cloudwave.http;

/**
 * Created by mevur on 5/25/2017.
 */

public interface IRequest {
    String getMethod();
    void response(int code, String data, String error);
}
