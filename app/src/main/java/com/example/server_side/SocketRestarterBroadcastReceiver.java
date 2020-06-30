package com.example.server_side;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.Handler;

public class SocketRestarterBroadcastReceiver extends BroadcastReceiver {

//    private Context context;
//
//    public SocketRestarterBroadcastReceiver(Context context) {
//        this.context = context;
//    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SocketRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
//        context.startService(new Intent(context, MyService.class));

        Intent startServer = new Intent(context, MyService.class);
        startServer.setAction(MyService.START_SERVER);
        context.startService(startServer);
    }
}
