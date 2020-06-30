package com.example.server_side;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class SocketBackgroundService extends IntentService {

    private SocketThread mSocketThread;

    public SocketBackgroundService(String name) {
        super(name);
        Log.v("going to background", "running in background");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v("going to background", "running in background");
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.v("going to background", "running in background");
    }

    @Override
    public void onCreate() {
        mSocketThread = SocketThread.getInstance();

        Log.v("going to background", "running in background");
    }


    @Override
    public void onDestroy() {
        //stop thread and socket connection here
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mSocketThread.startThread()) {
        } else {
            stopSelf();
        }

        return START_STICKY;
    }
}

class SocketThread extends Thread {

    private static SocketThread mSocketThread;
    private SocketClient mSocketClient;

    private SocketThread() {
    }

    // create single instance of socket thread class
    public static SocketThread getInstance() {
        if (mSocketThread == null)//you can use synchronized also
        {
            mSocketThread = new SocketThread();
        }
        return mSocketThread;
    }


    public boolean startThread() {
        mSocketClient = new SocketClient();
        if (mSocketClient.isConnected()) {
            mSocketThread.start();
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        super.run();
        while (mSocketClient.isConnected()) {
            // continue listen
        }
        // otherwise remove socketClient instance and stop thread
    }

    public class SocketClient {
        //write all code here regarding opening, closing sockets
        //create constructor
        public SocketClient() {
            // open socket connection here
        }

        public boolean isConnected() {
            return true;
        }
    }
}
