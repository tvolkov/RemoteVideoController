package org.tvolkov.rvc.app.core;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class CommonActionService extends BaseService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CommonActionService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final int action = intent.getIntExtra(SERVICE_ACTION, -1);
        final String host = intent.getStringExtra(EXTRA_HOST);
        final String port = intent.getStringExtra(EXTRA_PORT);

        switch (action) {
            case SERVICE_ACTION_GET_STATUS:
                Bundle status = VlcPlayerRestTemplates.getStatus(host, port);
                handleSuccess(status, intent);
                break;
        }
    }

    private void handleSuccess(final Bundle result, final Intent intent) {

    }

    private void handleError(final Bundle result, final Intent intent){

    }

    private void handleResult(final Bundle data, final Intent intent, final int resultCode){
        ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra(EXTRA_RECEIVER);
        if (receiver != null) {


            data.putInt(EXTRA_REQUEST_ID, intent.getIntExtra(EXTRA_REQUEST_ID, -1));

            receiver.send(resultCode, data);
        }
    }
}