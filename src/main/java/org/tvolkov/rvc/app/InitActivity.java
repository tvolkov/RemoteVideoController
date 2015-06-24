package org.tvolkov.rvc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import org.tvolkov.rvc.app.core.*;
import org.tvolkov.rvc.app.rest.MediaPlayerClassicRestClient;
import org.tvolkov.rvc.app.rest.PlayerRestClient;
import org.tvolkov.rvc.app.rest.RestClientProvider;
import org.tvolkov.rvc.app.rest.VlcPlayerRestClient;
import org.tvolkov.rvc.app.util.UserSettings;

import java.util.HashMap;
import java.util.Map;


public class InitActivity extends Activity {

    public static final String TAG = "InitActivity";

    private CommonActionServiceHelper commonActionServiceHelper;

    private Spinner playerTypes;

    private EditText login;

    private EditText password;

    private AdapterView.OnItemSelectedListener playerSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0){
                freezeButton();
                return;
            }
            checkPasswordVisibility(i);
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

        private void checkPasswordVisibility(final int index){
            if (index == 2){//VLC
                password.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);
            } else {
                password.setVisibility(View.GONE);
                login.setVisibility(View.GONE);
            }
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
        String login = UserSettings.getLogin(this);
        String password = UserSettings.getPassword(this);

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

        this.password = (EditText) findViewById(R.id.init_password);
        this.login = (EditText) findViewById(R.id.init_login);
        if (login != null){
            this.login.setText(login);
        }

        if (password != null){
            this.password.setText(password);
        }
    }

    private void initSpinner(){
        playerTypes = (Spinner) findViewById(R.id.init_player_type_spinner);
        ArrayAdapter<CharSequence> playerTypesAdapter = ArrayAdapter.createFromResource(this, R.array.player_types, android.R.layout.simple_spinner_item);
        playerTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerTypes.setAdapter(playerTypesAdapter);
        playerTypes.setOnItemSelectedListener(playerSelectListener);
        playerTypes.setSelection(UserSettings.getPlayerType(this));
    }

    public void connectToRemotePlayer(View view){
        setRestClientProvider();
        savePlayerType();
        if (!UserSettings.isConnectedToNetwork(this)){
            Toast.makeText(InitActivity.this, getString(R.string.init_activity_unable_to_connect), Toast.LENGTH_LONG).show();
            return;
        }
        final EditText host = (EditText) findViewById(R.id.init_host);
        final EditText port = (EditText) findViewById(R.id.init_port);


        UserSettings.setHost(this, host.getText().toString());
        UserSettings.setPort(this, port.getText().toString());
        if (login.getVisibility() == View.VISIBLE){
            UserSettings.setLogin(this, login.getText().toString());
        }
        if (password.getVisibility() == View.VISIBLE){
            UserSettings.setPassword(this, password.getText().toString());
        }

        UserSettings.setPlayerType(this, playerTypes.getSelectedItemPosition());
        Log.d(TAG, "adding after request hook");
        commonActionServiceHelper.getStatus(requestHandler);
    }

    private AfterRequestHook requestHandler = new AfterRequestHook() {
        @Override
        public void afterRequest(int requestId, int result, Bundle data) {
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
        intent.putExtra(PlayerRestClient.STATESTR, statusData.get(PlayerRestClient.STATESTR));
        intent.putExtra(PlayerRestClient.FILE, statusData.get(PlayerRestClient.FILE));
        intent.putExtra(PlayerRestClient.STATE, statusData.get(PlayerRestClient.STATE));
        startActivity(intent);
    }

    private void setRestClientProvider(){
        switch (playerTypes.getSelectedItemPosition()){
            case 0:
                Toast.makeText(this, getString(R.string.init_activity_incorrect_player_type), Toast.LENGTH_SHORT).show();
                break;
            case 1:
                RestClientProvider.getInstance().setRestClient(new MediaPlayerClassicRestClient());
                break;
            case 2:
                RestClientProvider.getInstance().setRestClient(new VlcPlayerRestClient());
            default:
        }
    }

    private void savePlayerType(){
        UserSettings.setPlayerType(this, playerTypes.getSelectedItemPosition());
    }
}
