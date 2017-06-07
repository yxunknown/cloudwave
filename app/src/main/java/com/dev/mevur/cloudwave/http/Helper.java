package com.dev.mevur.cloudwave.http;

import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * Created by mevur on 5/25/2017.
 */

public class Helper {
    public static String jsonToString(@Nullable JSONObject object) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> keys = object.keys();
        System.out.println(object.toString());
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                String value = object.getString(key);
                sb.append(key + "=" + value + "&");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return sb.toString();
    }

    public static String getString(InputStream is) throws IOException {
        if (is == null) {
            return null;
        } else {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }
}
