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

    private AdapterView.OnItemSelectedListener playerSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0){
                freezeButton();
                view.setSelected(false);
                return;
            }
            unfreezeButton();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            freezeButton();
        }

        private void freezeButton(){
            Button button = (Button) findViewById(R.id.init_button_ok);
            button.setEnabled(false);
        }

        private void unfreezeButton(){
            Button button = (Button) findViewById(R.id.init_button_ok);
            button.setEnabled(true);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        commonActionServiceHelper = new CommonActionServiceHelper(this);

        setContentView(R.layout.activity_init);

        initFields();
    }

    private void initFields(){
        initTextFields();
        initSpinner();
    }

    private void initTextFields(){
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
    }

    private void initSpinner(){
        Spinner playerTypes = (Spinner) findViewById(R.id.init_player_type_spinner);
        ArrayAdapter<CharSequence> playerTypesAdapter = ArrayAdapter.createFromResource(this, R.array.player_types, android.R.layout.simple_spinner_item);
        playerTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerTypes.setAdapter(playerTypesAdapter);
        playerTypes.setOnItemSelectedListener(playerSelectListener);
    }

    public void connectToRemotePlayer(View view){
        if (!UserSettings.isConnectedToNetwork(this)){
            Toast.makeText(InitActivity.this, getString(R.string.init_activity_unable_to_connect), Toast.LENGTH_LONG).show();
            return;
        }
        final EditText host = (EditText) findViewById(R.id.init_host);
        final EditText port = (EditText) findViewById(R.id.init_port);

        UserSettings.setHost(this, host.getText().toString());
        UserSettings.setPort(this, port.getText().toString());
        Log.d(TAG, "adding after request hook");
        commonActionServiceHelper.getStatus(requestHandler);
    }

    private AfterRequestHook requestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
            //commonActionServiceHelper.removeAfterRequestHook(requestHandler);
            if (result == ServiceResult.ERROR.ordinal()){
                Toast.makeText(InitActivity.this, data.getString(BaseService.EXTRA_SERVICE_STATUS), Toast.LENGTH_LONG).show();
            }

            Map<String, String> status = (HashMap)data.getSerializable(BaseService.EXTRA_RESPONSE_MAP);

            if (status == null){
                Toast.makeText(InitActivity.this, getString(R.string.general_remote_player_unavailble), Toast.LENGTH_LONG).show();
                return;
            }

            startMainActivity(status);
        }
    };

    private void startMainActivity(Map<String, String> statusData){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MediaPlayerClassicRestTemplates.Variables.STATESTRING, statusData.get(MediaPlayerClassicRestTemplates.Variables.STATESTRING));
        intent.putExtra(MediaPlayerClassicRestTemplates.Variables.FILEPATH, statusData.get(MediaPlayerClassicRestTemplates.Variables.FILEPATH));
        intent.putExtra(MediaPlayerClassicRestTemplates.Variables.STATE, statusData.get(MediaPlayerClassicRestTemplates.Variables.STATE));
        startActivity(intent);
    }
}
