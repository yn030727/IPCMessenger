package com.example.ipcmessenger;

import static android.icu.text.UnicodeSet.CASE;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import androidx.annotation.NonNull;

public class MessengerService extends Service {

    private static class MessengerHandler extends Handler{
        //处理服务端发送过来的信息
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:{
                    Messenger client = msg.replyTo;
                    Message replyMessage = Message.obtain(null,1);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply","嗯，你的消息我收到了");
                    replyMessage.setData(bundle);
                    try {
                        client.send(replyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                default:{
                    super.handleMessage(msg);
                }
            }
        }
    }


    public MessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}