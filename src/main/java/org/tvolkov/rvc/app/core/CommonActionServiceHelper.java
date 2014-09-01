package org.tvolkov.rvc.app.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.util.SparseArray;
import org.tvolkov.rvc.app.util.UserSettings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CommonActionServiceHelper {
    private static final String EXTRA_REQUEST_ID = "org.tvolkov.rvc.EXTRA_REQUEST_ID";
    private static final int MAX_REQUEST_ID = 1000;
    private Random random = new Random();
    private SparseArray<Intent> processingIntents = new SparseArray<Intent>();
    private final List<WeakReference<AfterRequestHook>> listeners = Collections.synchronizedList(new ArrayList<WeakReference<AfterRequestHook>>());
    private Context context;
    private CommonResultReceiver receiver;
    private static final String TAG = "CommonActionServiceHelper";

    public CommonActionServiceHelper(Context c) {
        this.context = c;
        this.receiver = new CommonResultReceiver(new Handler());
    }

    private int getStatus(){
        return doAction(CommonActionService.SERVICE_ACTION_GET_STATUS);
    }

    public int getStatus(AfterRequestHook hook){
        addAfterRequestHook(hook);
        return getStatus();
    }

    public int pause(AfterRequestHook hook){
        addAfterRequestHook(hook);
        return pause();
    }

    private int pause(){
        return doAction(CommonActionService.SERVICE_ACTION_PAUSE);
    }

    private int play(){
        return doAction(CommonActionService.SERVICE_ACTION_PLAY);
    }

    public int play(AfterRequestHook hook){
        addAfterRequestHook(hook);
        return play();
    }

    private int playPrev(){
        return doAction(CommonActionService.SERVICE_ACTION_PLAY_PREV);
    }

    public int playPrev(AfterRequestHook hook){
        addAfterRequestHook(hook);
        return playPrev();
    }

    private int playNext(){
        return doAction(CommonActionService.SERVICE_ACTION_PLAY_NEXT);
    }

    public int playNext(AfterRequestHook hook){
        addAfterRequestHook(hook);
        return playNext();
    }

    public int prevAudio(AfterRequestHook hook){
        addAfterRequestHook(hook);
        return prevAudio();
    }

    private int prevAudio(){
        return doAction(CommonActionService.SERVICE_ACTION_PREV_AUDIO);
    }

    public int nextAudio(AfterRequestHook hook){
        return nextAudio();
    }

    private int nextAudio(){
        return doAction(CommonActionService.SERVICE_ACTION_NEXT_AUDIO);
    }

    public int volumeUp(AfterRequestHook hook){
        addAfterRequestHook(hook);
        return volumeUp();
    }

    private int volumeUp(){
        return doAction(CommonActionService.SERVICE_ACTION_VOLUME_UP);
    }

    public int volumeDown(AfterRequestHook hook){
        addAfterRequestHook(hook);
        return volumeDown();
    }

    private int volumeDown(){
        return doAction(CommonActionService.SERVICE_ACTION_VOLUME_DOWN);
    }

    public int shutdownPcOnStop(AfterRequestHook hook){
        addAfterRequestHook(hook);
        return shutdownPcOnStop();
    }

    private int shutdownPcOnStop(){
        return doAction(CommonActionService.SERVICE_ACTION_SHUTDOWN_PC_ON_STOP);
    }

    public int doNothingOnStop(AfterRequestHook hook){
        addAfterRequestHook(hook);
        return doNotingOnStop();
    }

    private int doNotingOnStop(){
        return doAction(CommonActionService.SERVICE_ACTION_DO_NOTHING_ON_STOP);
    }

    private int doAction(int action){
        final int requestId = random.nextInt(MAX_REQUEST_ID);
        Intent intent = prepareIntent();
        intent.putExtra(BaseService.SERVICE_ACTION, action);
        intent.putExtra(BaseService.EXTRA_REQUEST_ID, requestId);
        context.startService(intent);
        processingIntents.append(requestId, intent);
        return requestId;
    }

    private Intent prepareIntent(){
        Intent intent = new Intent(context, CommonActionService.class);
        intent.putExtra(BaseService.EXTRA_HOST, UserSettings.getHost(context));
        intent.putExtra(BaseService.EXTRA_PORT, UserSettings.getPort(context));
        intent.putExtra(BaseService.EXTRA_RECEIVER, receiver);
        return intent;
    }

    public class CommonResultReceiver extends ResultReceiver {
        /**
         * Create a new ResultReceive to receive results.  Your
         * {@link #onReceiveResult} method will be called from the thread running
         * <var>handler</var> if given, or from an arbitrary thread if null.
         *
         * @param handler
         */
        public CommonResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        public void onReceiveResult(final int resultCode, final Bundle data){
            processResult(resultCode, data);
        }
    }

    private void addAfterRequestHook(final AfterRequestHook hook) {
        WeakReference<AfterRequestHook> weakRef = new WeakReference<AfterRequestHook>(hook);
        boolean match = false;
        for (WeakReference<AfterRequestHook> ref : listeners) {
            if (ref.get() != null) {
                if (ref.get().getClass().getName().equals(hook.getClass().getName())) {
                    match = true;
                    break;
                }
            }
        }
        if (!listeners.contains(weakRef) && !match) {
            Log.e(TAG, "adding afterRequestHook");
            listeners.add(weakRef);
        }
    }

    private void processResult(final int resultCode, final Bundle resultData) {
        final int requestId = resultData.getInt(EXTRA_REQUEST_ID);
        processingIntents.remove(requestId);
        for (WeakReference<AfterRequestHook> weakRef : listeners) {
            if (weakRef != null) {
                AfterRequestHook listener = weakRef.get();
                if (listener != null) {
                    Log.e(TAG, "notify listeners");
                    listener.afterRequest(requestId, resultCode, resultData);
                    listeners.remove(listener);
                }
            }
        }
    }
}

