package com.example.fake2edison.chatrobot;


import android.util.Log;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Created by fake2edison on 2017/7/5.
 */

public class SockClient {
    public List<Msg> msgList;
    private MsgAdapter adapeter;
    private ListView msgListView;

    private Socket socket = null;
    private BufferedReader br = null;
    private PrintWriter write = null;
    private BufferedReader in = null;
    private String temp = null;
    private String recive = null;
    private static int count = 0;
    private static int sleepcpount = 0;

    public SockClient(List<Msg> msgList, MsgAdapter adapeter, ListView msgListView){
        this.msgListView = msgListView;
        this.msgList = msgList;
        this.adapeter = adapeter;
    }

    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
            try {
                socket = new Socket("47.93.99.95", 5209);
                System.out.println("客户端启动成功");
                br = new BufferedReader(new InputStreamReader(System.in));
                write = new PrintWriter(socket.getOutputStream());
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (Exception e) {
                System.out.println("服务器启动失败");
                System.out.println(e);
            }
        }
    };




    public void start(){
        new Thread(networkTask).start();
    }

    public void close(){
        try{
            write.close();
            in.close();
            socket.close();
        }catch (IOException e){
            System.out.print("服务器关闭失败");
            System.out.print(e);
        }
    }

    Runnable returnTask = new Runnable() {

        @Override
        public void run() {
            try {
                write.println(temp);
                write.flush();
                //error
                recive = in.readLine();
                recive = recive.replace("%robotname%","Chris");
                Log.i("info","213123123"+recive);
                sleepcpount++;
            }catch (IOException e){
                System.out.print(e);
            }
        }
    };


    public void setOutput(String content){
        count++;
        try {
            temp = content;
            new Thread(returnTask).start();
            while(sleepcpount!=count){
                try {
                    Thread.sleep(300);                 //1000 毫秒，也就是1秒.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
            Msg txt = new Msg(recive, Msg.TYPE_RECEIVED);
            msgList.add(txt);
            adapeter.notifyDataSetChanged();
            msgListView.setSelection(msgList.size());
        }catch (Exception e){
            System.out.print(e);
        }
    }
}

