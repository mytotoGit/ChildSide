package com.ishuinzu.childside.notification;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationSender {
    private String fcm_token;
    private String title;
    private String body;
    private Context context;
    private RequestQueue requestQueue;
    private final String post_url = "https://fcm.googleapis.com/fcm/send";
    private final String server_key = "AAAAZsSWYmU:APA91bF3gz03El4EizYc2UTwIOzgIALsWjRCKQvPi1KlnJgikcQS1fZmR1bN7LS2owljLqfweGtS15k3_AiSa2q8VHjl4ZsN2LZh9Kah4flIVaLe8DL9xO3ZmLS8J3V-vlZFAhiKC2Xt";

    public NotificationSender(Context context, String fcm_token, String title, String body) {
        this.context = context;
        this.fcm_token = fcm_token;
        this.title = title;
        this.body = body;

        sendNotification();
    }

    public void sendNotification() {
        requestQueue = Volley.newRequestQueue(context);
        JSONObject main = new JSONObject();

        try {
            main.put("to", fcm_token);
            JSONObject notification = new JSONObject();
            notification.put("title", title);
            notification.put("body", body);
            main.put("notification", notification);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, post_url, main, response -> {
            }, error -> {
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + server_key);
                    return header;
                }
            };
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}