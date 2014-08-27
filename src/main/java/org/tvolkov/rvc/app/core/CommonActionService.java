package org.tvolkov.rvc.app.core;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import org.tvolkov.rvc.app.R;

import java.util.Formatter;

public class CommonActionService extends BaseService {

    @Override
    protected void onHandleIntent(final Intent intent) {
        final int action = intent.getIntExtra(SERVICE_ACTION, -1);
        final String host = intent.getStringExtra(EXTRA_HOST);
        final String port = intent.getStringExtra(EXTRA_PORT);

        Bundle result = new Bundle();
        try {
            switch (action) {
                case SERVICE_ACTION_GET_STATUS:
                    result = MediaPlayerClassicRestTemplates.getStatus(host, port);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_GET_PLAYLIST:
                case SERVICE_ACTION_PLAY_NEXT:
                    result = MediaPlayerClassicRestTemplates.playNext(host, port);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_PLAY_PREV:
                    result = MediaPlayerClassicRestTemplates.playPrev(host, port);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_PAUSE:
                    result = MediaPlayerClassicRestTemplates.pause(host, port);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_PLAY:
                    result = MediaPlayerClassicRestTemplates.play(host, port);
                    succeed(result, intent);
                    break;
                default:
            }
        } catch (Throwable t){
            result.putString(BaseService.EXTRA_SERVICE_STATUS, createGeneralErrorMessage(host, port));
            fail(result, intent);
        }
    }

    private String createGeneralErrorMessage(String host, String port){
        return createErrorMessage(R.string.service_general_error, host, port);
    }

    private String createErrorMessage(int stringId, String host, String port) {
        Formatter formatter = new Formatter();
        formatter.format(getString(stringId), host, port);
        return formatter.toString();
    }

    private void succeed(final Bundle result, final Intent intent) {
        handleResult(result, intent, ServiceResult.SUCCESS.ordinal());
    }

    private void fail(final Bundle result, final Intent intent){
        handleResult(result, intent, ServiceResult.ERROR.ordinal());
    }

    private void handleResult(final Bundle data, final Intent intent, final int resultCode){
        ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);
        if (receiver != null) {
            data.putInt(EXTRA_REQUEST_ID, intent.getIntExtra(EXTRA_REQUEST_ID, -1));
            receiver.send(resultCode, data);
        }
    }
}