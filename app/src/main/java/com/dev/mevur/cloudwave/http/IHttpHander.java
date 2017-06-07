package com.dev.mevur.cloudwave.http;

/**
 * Created by mevur on 5/24/2017.
 */

public interface IHttpHander {
    void handler(int response, String data, String error, int request);
}
