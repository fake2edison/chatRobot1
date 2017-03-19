package com.example.fake2edison.chatrobot;

import android.widget.ListView;

import com.baidu.android.common.logging.Log;
import com.google.gson.Gson;
import com.turing.androidsdk.HttpRequestListener;
import com.turing.androidsdk.TuringManager;


import java.util.List;

/**
 * Created by fake2edison on 2017/3/9.
 */

public class call{
    public List<Msg> msgList;
    private MsgAdapter adapeter;
    private ListView msgListView;
    public void backcall(TuringManager turingManager, String content){
        turingManager.setHttpRequestListener(new HttpRequestListener() {
            @Override
            public void onSuccess(String s) {
                Log.i("info",s);
                Gson gson = new Gson();
                Respone respone = gson.fromJson(s,Respone.class);
                Msg txt = new Msg(respone.text,Msg.TYPE_RECEIVED);
                Log.i("info",respone.text);
                msgList.add(txt);
                adapeter.notifyDataSetChanged();
                msgListView.setSelection(msgList.size());
                Log.i("info", String.valueOf(msgList.size()));
            }

            @Override
            public void onFail(int i, String s) {
                Log.i("info",s+i);
            }
        });
        turingManager.requestTuring(content);
    }
    public call(List<Msg> msgList,MsgAdapter adapeter,ListView msgListView){
        this.msgListView = msgListView;
        this.msgList = msgList;
        this.adapeter = adapeter;
        Log.i("info",String.valueOf(msgList.size())+"call()");
    }
}
