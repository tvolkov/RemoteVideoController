package org.tvolkov.rvc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import org.tvolkov.rvc.app.core.CommonActionService;

import static org.tvolkov.rvc.app.core.BaseService.EXTRA_HOST;
import static org.tvolkov.rvc.app.core.BaseService.EXTRA_PORT;


public class InitActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);
    }

    public void connectToRemotePlayer(View view){
        final EditText host = (EditText) findViewById(R.id.init_host);
        final EditText port = (EditText) findViewById(R.id.init_port);

        Intent intent = new Intent(this, CommonActionService.class);
        intent.putExtra(EXTRA_HOST, host.getText().toString());
        intent.putExtra(EXTRA_PORT, port.getText().toString());


        startService(intent);
    }
}
