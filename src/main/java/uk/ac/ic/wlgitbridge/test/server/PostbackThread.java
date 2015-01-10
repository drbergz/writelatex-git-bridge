package uk.ac.ic.wlgitbridge.test.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ning.http.client.AsyncHttpClient;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.ExecutionException;

/**
 * Created by Winston on 10/01/15.
 */
public class PostbackThread extends Thread {

    private String url;
    private String postback;

    public PostbackThread(Reader reader, String postback) {
        if (postback != null) {
            url = new Gson().fromJson(reader, JsonObject.class).get("postbackUrl").getAsString();
            this.postback = postback;
        }
    }

    @Override
    public void run() {
        try {
            new AsyncHttpClient().preparePost(url).setBody(postback).execute().get().getResponseBody();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void startIfNotNull() {
        if (url != null && postback != null) {
            start();
        }
    }

}