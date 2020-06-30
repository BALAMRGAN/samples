package com.example.server_side;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class MyService extends Service {

    public static final String START_SERVER = "startserver";
    public static final String STOP_SERVER = "stopserver";
    public static final int SERVERPORT = 5050;

    Thread serverThread;
    ServerSocket serverSocket;

    public MyService() {

    }

    //called when the services starts
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("on start command", "server started");
        //action set by setAction() in activity
//        String action = intent.getAction();
//        if (action.equals(START_SERVER)) {
        //start your server thread from here
        this.serverThread = new Thread(new ServerThread());
        this.serverThread.start();
        Log.d("on start command", "server after startserver validation started");
//        }
//        if (action.equals(STOP_SERVER)) {
//            //stop server
//            if (serverSocket != null) {
//                try {
//                    serverSocket.close();
//                } catch (IOException ignored) {
//                }
//            }
//        }

        //configures behaviour if service is killed by system, see documentation
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("my service destroy", "serviceOnDestroy()");

        super.onDestroy();
//
//        Intent broadcastIntent = new Intent("com.example.server_side.RestartSocket");
//        sendBroadcast(broadcastIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class ServerThread implements Runnable {

        public void run() {
            Socket socket;
            try {
                serverSocket = new ServerSocket(SERVERPORT);
                Log.d("on start command", "server thread object created");
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted()) {

                try {

                    socket = serverSocket.accept();

                    CommunicationThread commThread = new CommunicationThread(socket);
                    new Thread(commThread).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        Log.i("service on create", "onCreate()");
//        Notification notification = new Notification();
//        startForeground(42, notification);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Log.d("task removed", "on task remove called");


        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(),
                1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);

//        Intent broadcastIntent = new Intent("com.example.server_side.RestartSocket");
//        sendBroadcast(broadcastIntent);
//        startService(new Intent(this, MyService.class));
//
//        Intent startServer = new Intent(this, MyService.class);
//        startServer.setAction(MyService.START_SERVER);
//        startService(startServer);
    }

    class CommunicationThread implements Runnable {

        private Socket clientSocket;

        private BufferedReader input;

        public CommunicationThread(Socket clientSocket) {

            this.clientSocket = clientSocket;

            try {

                this.input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {

            try {

                String read = input.readLine();

                Log.d("message from client ", read);
                //update ui
                //best way I found is to save the text somewhere and notify the MainActivity
                //e.g. with a Broadcast
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
