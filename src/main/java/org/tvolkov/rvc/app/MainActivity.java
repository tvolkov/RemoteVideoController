package org.tvolkov.rvc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.tvolkov.rvc.app.core.*;
import org.tvolkov.rvc.app.util.UserSettings;


public class MainActivity extends Activity {

    public static final String TAG = "InitActivity";

    private CommonActionServiceHelper commonActionServiceHelper;

    private static boolean PLAYBACK_STATE = false;//true = playing, false=paused

    private Button.OnClickListener playPauseListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (PLAYBACK_STATE){//we want to pause it
                commonActionServiceHelper.addAfterRequestHook(pauseRequestHandler);
                commonActionServiceHelper.pause();
            } else {//resume playing
                commonActionServiceHelper.addAfterRequestHook(playRequestHandler);
                commonActionServiceHelper.play();
            }
            PLAYBACK_STATE = !PLAYBACK_STATE;
        }
    };

    private Button.OnClickListener prevListener = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            commonActionServiceHelper.addAfterRequestHook(prevRequestHandler);
            commonActionServiceHelper.playPrev();
        }
    };

    private Button.OnClickListener nextListener = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            commonActionServiceHelper.addAfterRequestHook(nextRequestHandler);
            commonActionServiceHelper.playNext();
        }
    };

    private AfterRequestHook playRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            commonActionServiceHelper.removeAfterRequestHook(playRequestHandler);
            if (result == ServiceResult.ERROR.ordinal()){
                Toast.makeText(MainActivity.this, data.getString(BaseService.EXTRA_SERVICE_STATUS), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Playing", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private AfterRequestHook pauseRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            commonActionServiceHelper.removeAfterRequestHook(pauseRequestHandler);
            if (result == ServiceResult.ERROR.ordinal()){
                Toast.makeText(MainActivity.this, data.getString(BaseService.EXTRA_SERVICE_STATUS), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Paused", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private AfterRequestHook prevRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            commonActionServiceHelper.removeAfterRequestHook(prevRequestHandler);
            if (result == ServiceResult.ERROR.ordinal()){
                Toast.makeText(MainActivity.this, data.getString(BaseService.EXTRA_SERVICE_STATUS), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Previous", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private AfterRequestHook nextRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            commonActionServiceHelper.removeAfterRequestHook(prevRequestHandler);
            if (result == ServiceResult.ERROR.ordinal()){
                Toast.makeText(MainActivity.this, data.getString(BaseService.EXTRA_SERVICE_STATUS), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Next", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        commonActionServiceHelper = new CommonActionServiceHelper(this);

        Button playPause = (Button) findViewById(R.id.main_playback_button);
        playPause.setOnClickListener(playPauseListener);

        Button prev = (Button) findViewById(R.id.main_prev);
        prev.setOnClickListener(prevListener);

        Button next = (Button) findViewById(R.id.main_next);
        next.setOnClickListener(nextListener);

        setStatus();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setStatus(){
        Intent i = getIntent();
        TextView conntectedTo = (TextView) findViewById(R.id.main_connected);
        conntectedTo.setText(conntectedTo.getText() + UserSettings.getHost(this) + ":" + UserSettings.getPort(this));
        conntectedTo.invalidate();

        TextView status = (TextView) findViewById(R.id.main_status);
        status.setText(status.getText() + i.getStringExtra(MediaPlayerClassicRestTemplates.Variables.STATESTRING));
        status.invalidate();

        TextView filePath = (TextView) findViewById(R.id.main_current);
        filePath.setText(filePath.getText() + i.getStringExtra(MediaPlayerClassicRestTemplates.Variables.FILEPATH));
        filePath.invalidate();

        String state = i.getStringExtra(MediaPlayerClassicRestTemplates.Variables.STATE);
        PLAYBACK_STATE = "2".equals(state);
    }

}
