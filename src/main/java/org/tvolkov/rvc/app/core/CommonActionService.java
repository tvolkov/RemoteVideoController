package org.tvolkov.rvc.app.core;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import org.tvolkov.rvc.app.R;
import org.tvolkov.rvc.app.rest.Endpoint;
import org.tvolkov.rvc.app.rest.PlayerRestClient;
import org.tvolkov.rvc.app.rest.RestClientProvider;

import java.util.Formatter;

public class CommonActionService extends BaseService {

    @Override
    protected void onHandleIntent(final Intent intent) {
        final int action = intent.getIntExtra(SERVICE_ACTION, -1);
        final Endpoint player = intent.getParcelableExtra(EXTRA_ENDPOINT);

        Bundle result = new Bundle();
        PlayerRestClient restClient = RestClientProvider.getInstance().getClient();
        try {
            switch (action) {
                case SERVICE_ACTION_GET_STATUS:
                    result = restClient.getStatus(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_GET_PLAYLIST:
                case SERVICE_ACTION_PLAY_NEXT:
                    result = restClient.playNext(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_PLAY_PREV:
                    result = restClient.playPrev(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_PAUSE:
                    result = restClient.pause(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_PLAY:
                    result = restClient.play(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_PREV_AUDIO:
                    result = restClient.prevAudio(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_NEXT_AUDIO:
                    result = restClient.nextAudio(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_VOLUME_UP:
                    result = restClient.volumeUp(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_VOLUME_DOWN:
                    result = restClient.volumeDown(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_SHUTDOWN_PC_ON_STOP:
                    result = restClient.shutdownPcOnStop(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_DO_NOTHING_ON_STOP:
                    result = restClient.doNothingOnStop(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_STOP:
                    result = restClient.stop(player);
                    succeed(result, intent);
                    break;
                case SERVICE_ACTION_EXIT:
                    result = restClient.exit(player);
                    succeed(result, intent);
                    break;
                default:
            }
        } catch (Throwable t){
            result.putString(BaseService.EXTRA_SERVICE_STATUS, createGeneralErrorMessage(player));
            fail(result, intent);
        }
    }

    private String createGeneralErrorMessage(final Endpoint player){
        return createErrorMessage(R.string.service_general_error, player);
    }

    private String createErrorMessage(int stringId, final Endpoint player) {
        Formatter formatter = new Formatter();
        formatter.format(getString(stringId), player.getHost(), player.getPort());
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