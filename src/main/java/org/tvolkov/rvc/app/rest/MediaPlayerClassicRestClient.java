package org.tvolkov.rvc.app.rest;


import android.os.Bundle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tvolkov.rvc.app.core.BaseService;
import org.tvolkov.rvc.app.network.SimpleHttpClient;
import org.tvolkov.rvc.app.rest.exception.RestClientException;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * url format:
 * http://host:port/command.html?wm_command=<COMMAND>
 */
public class MediaPlayerClassicRestClient implements PlayerRestClient {

    public MediaPlayerClassicRestClient(){}

    @Override
    public Bundle getStatus(final Endpoint player) {
        Bundle result = new Bundle();
        Map<String, String> responseData = getStatusData(player);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable)responseData);
        return result;
    }

    private Map<String, String> getStatusData(final Endpoint player) {
        try {
            final String url = "http://" + player.getHost() + ":" + player.getPort() + "/variables.html";
            Document response = Jsoup.connect(url).get();
            Map<String, String> responseData = new HashMap<String, String>();
            Elements elements = response.select("p");
            for (Element element : elements){
//                responseData.put(element.attr("id"), element.text());
                if (element.attr("id").equals("statestring")) {
                    responseData.put(STATESTR, element.text());
                } else if (element.attr("id").equals("filepath")) {
                    responseData.put(FILE, element.text());
                } else if (element.attr("id").equals("state")){
                    responseData.put(STATE, element.text());
                }
            }
            return responseData;
        } catch (IOException e){
            throw new RestClientException(e.getMessage());
        }

    }

    @Override
    public Bundle playPrev(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.PLAY_PREV);
    }

    @Override
    public Bundle playNext(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.PLAY_NEXT);
    }

    @Override
    public Bundle play(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.PLAY);
    }

    @Override
    public Bundle pause(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.PAUSE);
    }

    @Override
    public Bundle prevAudio(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.PREV_AUDIO);
    }

    @Override
    public Bundle nextAudio(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.NEXT_AUDIO);
    }

    @Override
    public Bundle volumeUp(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.VOLUME_UP);
    }

    @Override
    public Bundle volumeDown(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.VOLUME_DOWN);
    }

    @Override
    public Bundle shutdownPcOnStop(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.SHUTDOWN_PC_ON_STOP);
    }

    @Override
    public Bundle doNothingOnStop(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.DO_NOTHNIG_ON_STOP);
    }

    @Override
    public Bundle stop(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.STOP);
    }

    @Override
    public Bundle exit(final Endpoint player) {
        return doRequest(player, MediaPlayerClassicCommandCodes.EXIT);
    }


    private Bundle doRequest(final Endpoint player, final int command) {
        String response = SimpleHttpClient.getResponse(generateUriString(player, String.valueOf(command)), null);
        Bundle result = new Bundle();
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(player));
        return result;
    }

    private String generateUriString(final Endpoint player, final String command){
        return "http://" + player.getHost() + ":" + player.getPort() + "/command.html?wm_command=" + command;
    }

    public class MediaPlayerClassicCommandCodes{
        public static final int PLAY_PREV = 919;
        public static final int PLAY_NEXT = 920;
        public static final int PLAY = 887;
        public static final int PAUSE = 888;
        public static final int STOP = 890;
        public static final int PREV_AUDIO = 953;
        public static final int NEXT_AUDIO = 952;
        public static final int VOLUME_UP = 907;
        public static final int VOLUME_DOWN = 908;
        public static final int SHUTDOWN_PC_ON_STOP = 915;
        public static final int DO_NOTHNIG_ON_STOP = 918;
        public static final int EXIT = 816;
    }

    public class Variables{
/*        public String filepatharg;
        public String filepath;
        public String filedirarg;
        public String filedir;
        public String state;
        public String statestring;
        public String position;
        public String positionstring;
        public String duration;
        public String durationstring;
        public String volumelevel;
        public String muted;
        public String playbackrate;
        public String reloadtime;*/

        public final String STATESTRING = "statestring";
        public final String FILEPATH = "filepath";
        public final String STATE = "state"; //2 = playing, 1 = paused, 0 = stopped
    }


}
