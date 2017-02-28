package com.slife.whatmvp.model;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by qibin on 2015/11/15.
 */
public class MainBiz {

    public void userInfo(final Callback<String> callback) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.callback("撸神");
            }
        }, 2000);
    }

    public void data(final Callback<ArrayList<String>> callback) {
        ArrayList<String> lists = new ArrayList<String>() {
            {
                for(int i=0;i<5;i++) add("hi " + new Random().nextInt(100));
            }
        };
        callback.callback(lists);
    }

    public interface Callback<T> {
        public void callback(T data);
    }
}
