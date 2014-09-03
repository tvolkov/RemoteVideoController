package org.tvolkov.rvc.app.core;


import android.os.Bundle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.tvolkov.rvc.app.core.network.SimpleHttpClient;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * url format:
 * http://host:port/command.html?wm_command=<COMMAND>
 */
public class MediaPlayerClassicRestTemplates {
    public static Bundle getStatus(final String host, final String port) throws IOException {
        Bundle result = new Bundle();
        Map<String, String> responseData = getStatusData(host, port);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable)responseData);
        return result;
    }

    private static Map<String, String> getStatusData(final String host, final String port) throws IOException {
        final String url = "http://" + host + ":" + port + "/variables.html";
        Document response = Jsoup.connect(url).get();
        Map<String, String> responseData = new HashMap<String, String>();
        Elements elements = response.select("p");
        for (Element element : elements){
            responseData.put(element.attr("id"), element.text());
        }
        return responseData;
    }

    public static Bundle playPrev(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.PLAY_PREV);
    }

    public static Bundle playNext(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.PLAY_NEXT);
    }

    public static Bundle play(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.PLAY);
    }

    public static Bundle pause(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.PAUSE);
    }

    public static Bundle prevAudio(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.PREV_AUDIO);
    }

    public static Bundle nextAudio(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.NEXT_AUDIO);
    }

    public static Bundle volumeUp(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.VOLUME_UP);
    }

    public static Bundle volumeDown(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.VOLUME_DOWN);
    }

    public static Bundle shutdownPcOnStop(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.SHUTDOWN_PC_ON_STOP);
    }
    public static Bundle doNothingOnStop(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.DO_NOTHNIG_ON_STOP);
    }

    public static Bundle stop(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.STOP);
    }

    public static Bundle exit(final String host, final String port) throws IOException {
        return doRequest(host, port, MediaPlayerClassicCommandCodes.EXIT);
    }

    private static Bundle doRequest(final String host, final String port, final int command) throws IOException {
        String response = SimpleHttpClient.getResponse(generateUriString(host, port, String.valueOf(command)), null);
        Bundle result = new Bundle();
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(host, port));
        return result;
    }

    private static String generateUriString(final String host, final String port, final String command){
        return "http://" + host + ":" + port + "/command.html?wm_command=" + command;
    }

    public static class MediaPlayerClassicCommandCodes{
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

    public static class Variables{
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

        public static final String STATESTRING = "statestring";
        public static final String FILEPATH = "filepath";
        public static final String STATE = "state"; //2 = playing, 1 = paused, 0 = stopped
    }


}
