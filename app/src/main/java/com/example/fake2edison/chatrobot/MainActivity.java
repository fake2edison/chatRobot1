package com.example.fake2edison.chatrobot;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.android.common.logging.Log;
import com.turing.androidsdk.TTSManager;
import com.turing.androidsdk.TuringManager;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapeter;
    public List<Msg> msgList = new ArrayList<Msg>();
//    private String tulingKey = "4fbaa4ade654457ebde10564d7392023";
//    private String secert = "f835298cf70bbc05";
//    private TuringManager turingManager;
    private call call1;
    private SockClient socket1;

//    百度语音的API

//    private String bdKey = "AQOF1p6FISfLgiVN6RyXACfw";
//    private String bdSecery = "5655be6a30f6556a3c9312b31576c900";
//    private Button talkTTS;


//    private TTSManager ttsManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMsgs();
        send = (Button) findViewById(R.id.send);
        inputText = (EditText) findViewById(R.id.input_text);
        adapeter = new MsgAdapter(MainActivity.this, R.layout.msg_item, msgList);
        msgListView = (ListView) findViewById(R.id.msg_list_view);
        msgListView.setAdapter(adapeter);
        call1 = new call(msgList, adapeter, msgListView);
        socket1 = new SockClient(msgList,adapeter,msgListView);
        socket1.start();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SEND);
                    msgList.add(msg);
                    adapeter.notifyDataSetChanged();
                    msgListView.setSelection(msgList.size());
                    inputText.setText("");
                    Log.i("info", content);
                    socket1.setOutput(content);
                }
            }

        });
        msgListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm.isActive()&&getCurrentFocus()!=null){
                    if (getCurrentFocus().getWindowToken()!=null) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        });
    }
    public void initMsgs(){
        Msg msg1 = new Msg("你好呀～",Msg.TYPE_RECEIVED);
        msgList.add(msg1);
    }
}
