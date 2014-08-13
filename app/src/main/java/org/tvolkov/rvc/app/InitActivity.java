package org.tvolkov.rvc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import org.tvolkov.rvc.app.core.AfterRequestHook;
import org.tvolkov.rvc.app.core.CommonActionService;
import org.tvolkov.rvc.app.core.CommonActionServiceHelper;
import org.tvolkov.rvc.app.util.UserSettings;

import static org.tvolkov.rvc.app.core.BaseService.EXTRA_HOST;
import static org.tvolkov.rvc.app.core.BaseService.EXTRA_PORT;


public class InitActivity extends Activity {

    public static final String TAG = "InitActivity";

    private CommonActionServiceHelper commonActionServiceHelper;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        commonActionServiceHelper = new CommonActionServiceHelper(this);

        setContentView(R.layout.activity_init);

        String host = UserSettings.getHost(this);
        String port = UserSettings.getPort(this);
        if (host != null){
            ((EditText)findViewById(R.id.init_host)).setText(host);
        }
        if (port != null){
            ((EditText)findViewById(R.id.init_port)).setText(port);
        }
    }

    public void connectToRemotePlayer(View view){
        final EditText host = (EditText) findViewById(R.id.init_host);
        final EditText port = (EditText) findViewById(R.id.init_port);

        UserSettings.setHost(this, host.getText().toString());
        UserSettings.setPort(this, port.getText().toString());
        Log.d(TAG, "adding after request hook");
        commonActionServiceHelper.addAfterRequestHook(requestHandler);
        commonActionServiceHelper.getStatus();
    }

    private AfterRequestHook requestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            System.out.println("test");
            commonActionServiceHelper.removeAfterRequestHook(requestHandler);
        }
    };
}
