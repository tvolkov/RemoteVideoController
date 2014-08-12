package org.tvolkov.rvc.app.core;

import android.content.Intent;

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
    protected void onHandleIntent(Intent intent) {
        final int action = intent.getIntExtra(SERVICE_ACTION, -1);
        final String host = intent.getStringExtra(EXTRA_HOST);
        final String port = intent.getStringExtra(EXTRA_PORT);

        switch (action) {
            case SERVICE_ACTION_GET_STATUS:
                VlcPlayerRestTemplates.Status status = VlcPlayerRestTemplates.getStatus(host, port);
                
        }
    }
}
