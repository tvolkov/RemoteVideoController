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
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CommonActionServiceHelper {
    private static final String EXTRA_REQUEST_ID = "org.tvolkov.rvc.EXTRA_REQUEST_ID";
    private static final int MAX_REQUEST_ID = 1000;
    private Random random = new Random();
    private SparseArray<Intent> processingIntents = new SparseArray<Intent>();
    private List<WeakReference<AfterRequestHook>> listeners = new ArrayList<WeakReference<AfterRequestHook>>();
    private Context context;
    private CommonResultReceiver receiver;
    private static final String TAG = "CommonActionServiceHelper";

    public CommonActionServiceHelper(Context c) {
        this.context = c;
        this.receiver = new CommonResultReceiver(new Handler());
    }

    public int getStatus(){
        return doAction(CommonActionService.SERVICE_ACTION_GET_STATUS);
    }

    public int pause(){
        return doAction(CommonActionService.SERVICE_ACTION_PAUSE);
    }

    public int play(){
        return doAction(CommonActionService.SERVICE_ACTION_PLAY);
    }

    public int playPrev(){
        return doAction(CommonActionService.SERVICE_ACTION_PLAY_PREV);
    }

    public int playNext(){
        return doAction(CommonActionService.SERVICE_ACTION_PLAY_NEXT);
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

    public void addAfterRequestHook(final AfterRequestHook hook) {
        WeakReference<AfterRequestHook> weakRef = new WeakReference<AfterRequestHook>(hook);
        synchronized (listeners) {
            boolean match = false;
            for (WeakReference<AfterRequestHook> ref : listeners){
                if (ref.get() != null) {
                    if (ref.get().getClass().getName().equals(hook.getClass().getName())) {
                        match = true;
                        break;
                    }
                }
            }
            if (!listeners.contains(weakRef) && !match) {
                listeners.add(weakRef);
            }
        }
    }

    public void removeAfterRequestHook(final AfterRequestHook hook) {
        synchronized (listeners) {
            for (WeakReference<AfterRequestHook> ref : listeners){
                if (ref.get() != null){
                    if (ref.get().getClass().getName().equals(hook.getClass().getName())){
                        listeners.remove(ref);
                        break;
                    }
                }
            }
        }
    }

    private void processResult(final int resultCode, final Bundle resultData) {
        final int requestId = resultData.getInt(EXTRA_REQUEST_ID);
        processingIntents.remove(requestId);

        synchronized (listeners) {
            for (WeakReference<AfterRequestHook> weakRef : listeners) {
                if (weakRef != null) {
                    AfterRequestHook listener = weakRef.get();
                    if (listener != null) {
                        listener.afterRequest(requestId, resultCode, resultData);
                    }
                }
            }
        }
    }
}

