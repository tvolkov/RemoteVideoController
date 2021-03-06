package org.tvolkov.rvc.app.core;

import android.app.IntentService;
import android.content.Intent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract public class BaseService extends IntentService {

    private static final int MAX_THREADS = 5;

    public static final String SERVICE_ACTION = "org.tvolkov.rvc.SERVICE_ACTION";

    public static final int SERVICE_ACTION_GET_STATUS = 0;
    public static final int SERVICE_ACTION_GET_PLAYLIST = 1;
    public static final int SERVICE_ACTION_PLAY_NEXT = 2;
    public static final int SERVICE_ACTION_PLAY_PREV = 3;
    public static final int SERVICE_ACTION_PAUSE = 4;
    public static final int SERVICE_ACTION_PLAY = 5;
    public static final int SERVICE_ACTION_PREV_AUDIO = 6;
    public static final int SERVICE_ACTION_NEXT_AUDIO = 7;
    public static final int SERVICE_ACTION_VOLUME_UP = 8;
    public static final int SERVICE_ACTION_VOLUME_DOWN = 9;
    public static final int SERVICE_ACTION_SHUTDOWN_PC_ON_STOP = 10;
    public static final int SERVICE_ACTION_DO_NOTHING_ON_STOP = 11;
    public static final int SERVICE_ACTION_STOP = 12;
    public static final int SERVICE_ACTION_EXIT = 13;

    public static final String EXTRA_ENDPOINT = "org.tvolkov.rvc.app.ENDPOINT";
    public static final String EXTRA_REQUEST_ID = "org.tvolkov.rvc.app.REQUEST_ID";
    public static final String EXTRA_RECEIVER = "org.tvolkov.rvc.app.RECEIVER";
    public static final String EXTRA_STATUS_DATA = "org.tvolkov.rvc.app.STATUS_DATA";
    public static final String EXTRA_RESPONSE_STRING = "org.tvolkov.rvc.app.RESPONSE";
    public static final String EXTRA_RESPONSE_MAP = "org.tvolkov.rvc.app.RESPONSE_MAP";
    public static final String EXTRA_SERVICE_STATUS = "org.tvolkov.rvc.app.SERVICE_STATUS";

    private ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

    public BaseService(){
        super("");
    }

    @Override
    public void onCreate(){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        executorService.submit(new ProcessIntentTask(intent));
        return START_STICKY;
    }

    private class ProcessIntentTask implements Runnable {
        private Intent intent;

        public ProcessIntentTask(Intent intent){
            this.intent = intent;
        }

        @Override
        public void run(){
            onHandleIntent(intent);
        }
    }
}
