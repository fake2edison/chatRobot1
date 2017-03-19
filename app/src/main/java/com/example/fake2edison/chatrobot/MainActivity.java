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

import com.baidu.android.common.logging.Log;
import com.turing.androidsdk.TuringManager;


import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ListView msgListView;
    private EditText inputText;
    private Button send;
    private MsgAdapter adapeter;
    public List<Msg> msgList = new ArrayList<Msg>();
    private String tulingKey = "4fbaa4ade654457ebde10564d7392023";
    private String secert = "f835298cf70bbc05";
    private TuringManager turingManager;
    private call call1;
//    private int i = 0;
//    private Msg []txt = new Msg[100];



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
                    turingManager = new TuringManager(MainActivity.this, tulingKey, secert);
                    Log.i("info", content);
                    call1.backcall(turingManager, content);
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
