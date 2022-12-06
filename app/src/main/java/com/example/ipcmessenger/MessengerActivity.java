package com.example.ipcmessenger;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.PersistableBundle;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//客户端
public class MessengerActivity extends Activity {

    private static final String TAG = " MessengerActivity" ;

    private Messenger messenger;

    //建立连接
    private ServiceConnection mConncetin = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //绑定完后通过服务端返回的binder对象创建Messenger对象
            messenger = new Messenger(iBinder);
            //使用此对象向服务端发送信息
            Message msg = Message.obtain(null , 1);
            Bundle data = new Bundle();
            data.putString("msg","hello , this is client");
            msg.setData(data);
            //连接部分多了一句，发送replyTo参数给服务端
            msg.replyTo = mGetReplyMessenger;
            try {
                messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    private Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());
    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:{
                    msg.getData().getString("reply");
                    break;
                }
                default:{
                    super.handleMessage(msg);
                }
            }
        }
    }

    //Activity创建的时候申请绑定
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Intent intent = new Intent(this , MessengerService.class);
        bindService(intent,mConncetin, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mConncetin);
        super.onDestroy();
    }
}
