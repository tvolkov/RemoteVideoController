package org.tvolkov.rvc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.tvolkov.rvc.app.core.*;
import org.tvolkov.rvc.app.util.UserSettings;

import java.util.HashMap;
import java.util.Map;


public class InitActivity extends Activity {

    public static final String TAG = "InitActivity";

    private CommonActionServiceHelper commonActionServiceHelper;

    private EditText currentPlayer;

    private ListPopupWindow playerSelector;

    private String[] playerTypes = {"VLC", "MPC"};

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        commonActionServiceHelper = new CommonActionServiceHelper(this);

        setContentView(R.layout.activity_init);

        String host = UserSettings.getHost(this);
        String port = UserSettings.getPort(this);
        if (host != null){
            ((EditText)findViewById(R.id.init_host)).setText(host);
        } else {//debug
            ((EditText)findViewById(R.id.init_host)).setText("192.168.1.5");
        }
        if (port != null){
            ((EditText)findViewById(R.id.init_port)).setText(port);
        } else {//debug
            ((EditText)findViewById(R.id.init_port)).setText("13579");
        }

        initializePlayerSelector();
    }

    public void connectToRemotePlayer(View view){
        if (!UserSettings.isConnectedToNetwork(this)){
            Toast.makeText(InitActivity.this, "Network unavailable. Check your connection", Toast.LENGTH_LONG).show();
            return;
        }
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
            commonActionServiceHelper.removeAfterRequestHook(requestHandler);
            if (result == ServiceResult.ERROR.ordinal()){
                Toast.makeText(InitActivity.this, data.getString(BaseService.EXTRA_SERVICE_STATUS), Toast.LENGTH_LONG).show();
            }

            Map<String, String> status = (HashMap)data.getSerializable(BaseService.EXTRA_RESPONSE);

            if (status == null){
                Toast.makeText(InitActivity.this, getString(R.string.general_remote_player_unavailble), Toast.LENGTH_LONG).show();
                return;
            }

            startMainActivity(status);
        }
    };

    private AdapterView.OnItemClickListener playerSelectorListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            currentPlayer.setText(playerTypes[i]);
            playerSelector.dismiss();
        }
    };

    private void initializePlayerSelector(){
        currentPlayer = (EditText) findViewById(R.id.current_player);

        playerSelector = new ListPopupWindow(this);
        playerSelector.setAdapter(new ArrayAdapter<String>(this, R.layout.player_type_item, playerTypes));
        playerSelector.setAnchorView(currentPlayer);
        playerSelector.setWidth(300);
        playerSelector.setHeight(400);
        playerSelector.setModal(true);
        playerSelector.setOnItemClickListener(playerSelectorListener);

        currentPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerSelector.show();
            }
        });
    }

    private void startMainActivity(Map<String, String> statusData){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MediaPlayerClassicRestTemplates.Variables.STATESTRING, statusData.get(MediaPlayerClassicRestTemplates.Variables.STATESTRING));
        intent.putExtra(MediaPlayerClassicRestTemplates.Variables.FILEPATH, statusData.get(MediaPlayerClassicRestTemplates.Variables.FILEPATH));
        intent.putExtra(MediaPlayerClassicRestTemplates.Variables.STATE, statusData.get(MediaPlayerClassicRestTemplates.Variables.STATE));
        startActivity(intent);
    }
}
