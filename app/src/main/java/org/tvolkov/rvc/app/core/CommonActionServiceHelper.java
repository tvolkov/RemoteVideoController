package org.tvolkov.rvc.app.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.util.SparseArray;
import org.tvolkov.rvc.app.util.UserSettings;

import java.util.*;

public class CommonActionServiceHelper {
    private static final String EXTRA_REQUEST_ID = "org.tvolkov.rvc.EXTRA_REQUEST_ID";
    private static final int MAX_REQUEST_ID = 1000;
    private static CommonActionServiceHelper INSTANCE = null;
    private Random random = new Random();
    private SparseArray<Intent> processingIntents = new SparseArray<Intent>();
    private Set<AfterRequestHook> listeners = Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<AfterRequestHook, Boolean>()));
    private Context context;
    private CommonResultReceiver receiver;
    private static final String TAG = "CommonActionServiceHelper";

    public CommonActionServiceHelper(Context c) {
        this.context = c;
        this.receiver = new CommonResultReceiver(new Handler());
    }

    public static CommonActionServiceHelper getInstance(Context c){
        Log.d(TAG, "get INstance");
        if (INSTANCE == null){
            synchronized (INSTANCE) {
                if (INSTANCE == null){
                    INSTANCE = new CommonActionServiceHelper(c);
                }
            }
        }
        return INSTANCE;
    }

    public int getStatus(){
        final int requestId = random.nextInt(MAX_REQUEST_ID);
        final Intent intent = new Intent(context, CommonActionService.class);
        intent.putExtra(CommonActionService.EXTRA_HOST, UserSettings.getHost(context));
        intent.putExtra(CommonActionService.EXTRA_PORT, UserSettings.getPort(context));
        intent.putExtra(CommonActionService.SERVICE_ACTION, CommonActionService.SERVICE_ACTION_GET_STATUS);
        intent.putExtra(CommonActionService.EXTRA_REQUEST_ID, requestId);
        intent.putExtra(CommonActionService.EXTRA_RECEIVER, receiver);
        context.startService(intent);
        processingIntents.append(requestId, intent);
        return requestId;
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

    public void addAfterRequestHook(final AfterRequestHook hook){
        listeners.add(hook);
    }

    public void removeAfterRequestHook(final AfterRequestHook hook){
        listeners.remove(hook);
    }

    private void processResult(final int resultCode, final Bundle data){
        final int requestId = data.getInt(EXTRA_REQUEST_ID);
        processingIntents.remove(requestId);
        for (AfterRequestHook hook : listeners){
            if (hook != null){
                hook.afterRequest(requestId, resultCode, data);
            }
        }
    }
}

