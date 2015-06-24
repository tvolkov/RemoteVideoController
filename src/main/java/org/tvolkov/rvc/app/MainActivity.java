package org.tvolkov.rvc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import org.tvolkov.rvc.app.core.*;
import org.tvolkov.rvc.app.rest.MediaPlayerClassicRestClient;
import org.tvolkov.rvc.app.rest.PlayerRestClient;
import org.tvolkov.rvc.app.rest.RestClientProvider;
import org.tvolkov.rvc.app.util.UserSettings;

import java.util.HashMap;
import java.util.Map;
import static android.view.View.OnClickListener;

public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";

    private CommonActionServiceHelper commonActionServiceHelper;

    private boolean playbackState = false;//true = playing, false=paused

    private Object lock = new Object();

    private OnClickListener playPauseListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            synchronized (lock) {
                if (playbackState){//we want to pause it
                    commonActionServiceHelper.pause(pauseRequestHandler);
                } else {//resume playing
                    commonActionServiceHelper.play(playRequestHandler);
                }
                playbackState = !playbackState;
            }
        }
    };

    private OnClickListener stopListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            playbackState = false;
            commonActionServiceHelper.stop(stopRequestHandler);
        }
    };

    private OnClickListener prevListener = new OnClickListener(){

        @Override
        public void onClick(View view) {
            commonActionServiceHelper.playPrev(prevRequestHandler);
        }
    };

    private OnClickListener nextListener = new OnClickListener(){

        @Override
        public void onClick(View view) {
            commonActionServiceHelper.playNext(nextRequestHandler);
        }
    };


    private OnClickListener prevAudioListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            commonActionServiceHelper.prevAudio(prevAudioRequestHandler);
        }
    };

    private OnClickListener nextAudioListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            commonActionServiceHelper.nextAudio(nextAudioRequestHandler);
        }
    };

    private OnClickListener volumeUpListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            commonActionServiceHelper.volumeUp(volumeUpRequestHandler);
        }
    };

    private OnClickListener volumeDownListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            commonActionServiceHelper.volumeDown(volumeDownRequestHandler);
        }
    };

    private OnClickListener shutdownPcListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            final boolean shutdownPcOnStop = ((CheckBox) view).isChecked();
            if (shutdownPcOnStop){
                commonActionServiceHelper.shutdownPcOnStop(shutdownPcOnStopRequestHandler);
            } else {
                commonActionServiceHelper.doNothingOnStop(doNothingRequestHandler);
            }
        }
    };

    private AfterRequestHook playRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            refreshStatus(requestId, result, data);
        }
    };

    private AfterRequestHook pauseRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            refreshStatus(requestId, result, data);
        }
    };

    private AfterRequestHook stopRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            refreshStatus(requestId, result, data);
        }
    };

    private AfterRequestHook prevRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            refreshStatus(requestId, result, data);
        }
    };

    private AfterRequestHook nextRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            refreshStatus(requestId, result, data);
        }
    };

    private void refreshStatus(int requestId, int result, Bundle data){
        if (result == ServiceResult.ERROR.ordinal()){
            Toast.makeText(MainActivity.this, data.getString(BaseService.EXTRA_SERVICE_STATUS), Toast.LENGTH_SHORT).show();
        } else {
            statusRequestHandler.afterRequest(requestId, result, data);
        }
    }


    private AfterRequestHook statusRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            if (result == ServiceResult.ERROR.ordinal()){
                Toast.makeText(MainActivity.this, data.getString(BaseService.EXTRA_SERVICE_STATUS), Toast.LENGTH_SHORT).show();
            } else {
                Map<String, String> status = (HashMap)data.getSerializable(BaseService.EXTRA_RESPONSE_MAP);

                if (status == null){
                    Toast.makeText(MainActivity.this, getString(R.string.general_remote_player_unavailble), Toast.LENGTH_LONG).show();
                    return;
                }

                setStatus(status);
            }
        }
    };

    private AfterRequestHook prevAudioRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {

        }
    };

    private AfterRequestHook nextAudioRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {

        }
    };

    private AfterRequestHook volumeUpRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            //TODO show volume level
        }
    };

    private AfterRequestHook volumeDownRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            //TODO show volume level
        }
    };

    private AfterRequestHook shutdownPcOnStopRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            //TODO
        }
    };

    private AfterRequestHook doNothingRequestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            //TODO
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        commonActionServiceHelper = new CommonActionServiceHelper(this);

        initViews();

        setStatus();
    }

    private void initViews(){
        Button playPause = (Button) findViewById(R.id.main_playback_button);
        playPause.setOnClickListener(playPauseListener);

        Button stop = (Button) findViewById(R.id.main_playback_stop);
        stop.setOnClickListener(stopListener);

        Button prev = (Button) findViewById(R.id.main_prev);
        prev.setOnClickListener(prevListener);

        Button next = (Button) findViewById(R.id.main_next);
        next.setOnClickListener(nextListener);

        Button prevAudio = (Button) findViewById(R.id.main_prev_audiotrack);
        prevAudio.setOnClickListener(prevAudioListener);

        Button nextAudio = (Button) findViewById(R.id.main_next_audiotrack);
        nextAudio.setOnClickListener(nextAudioListener);

        Button volumeUp = (Button) findViewById(R.id.main_volume_up);
        volumeUp.setOnClickListener(volumeUpListener);

        Button volumeDown = (Button) findViewById(R.id.main_volume_down);
        volumeDown.setOnClickListener(volumeDownListener);

        CheckBox shutdownPc = (CheckBox) findViewById(R.id.main_shutdown_pc_on_stop);
        shutdownPc.setOnClickListener(shutdownPcListener);
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
        TextView connectedTo = (TextView) findViewById(R.id.main_connected);
        connectedTo.setText(connectedTo.getText() + UserSettings.getHost(this) + ":" + UserSettings.getPort(this));
        connectedTo.invalidate();

        setStatus(i.getStringExtra(PlayerRestClient.STATESTR), i.getStringExtra(PlayerRestClient.FILE));

        String state = i.getStringExtra(PlayerRestClient.STATE);
        playbackState = "2".equals(state);
    }

    private void setStatus(Map<String, String> status){
        setStatus(status.get(PlayerRestClient.STATESTR), status.get(PlayerRestClient.FILE));
    }

    private void setStatus(String playbackStatus, String nowPlaying){
        TextView tv = (TextView) findViewById(R.id.main_status);
        tv.setText(getString(R.string.main_activity_status) + playbackStatus);
        tv.invalidate();

        tv = (TextView) findViewById(R.id.main_current);
        tv.setText(getString(R.string.main_activity_current) + nowPlaying);
        tv.invalidate();
    }
}
