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
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/command.html?wm_command=919";
        String response = SimpleHttpClient.getResponse(url, null);
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(host, port));
        return result;
    }

    public static Bundle playNext(final String host, final String port) throws IOException {
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/command.html?wm_command=920";
        String response = SimpleHttpClient.getResponse(url, null);
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(host, port));
        return result;
    }

    public static Bundle play(final String host, final String port) throws IOException {
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/command.html?wm_command=887";
        String response = SimpleHttpClient.getResponse(url, null);
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(host, port));
        return result;
    }

    public static Bundle pause(final String host, final String port) throws IOException {
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/command.html?wm_command=888";
        String response = SimpleHttpClient.getResponse(url, null);
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(host, port));
        return result;
    }

    //953
    public static Bundle prevAudio(final String host, final String port) throws IOException {
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/command.html?wm_command=953";
        String response = SimpleHttpClient.getResponse(url, null);
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(host, port));
        return result;
    }

    //952
    public static Bundle nextAudio(final String host, final String port) throws IOException {
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/command.html?wm_command=952";
        String response = SimpleHttpClient.getResponse(url, null);
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(host, port));
        return result;
    }

    public static Bundle volumeUp(final String host, final String port) throws IOException {
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/command.html?wm_command=907";
        String response = SimpleHttpClient.getResponse(url, null);
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(host, port));
        return result;
    }

    public static Bundle volumeDown(final String host, final String port) throws IOException {
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/command.html?wm_command=908";
        String response = SimpleHttpClient.getResponse(url, null);
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(host, port));
        return result;
    }

    //915
    public static Bundle shutdownPcOnStop(final String host, final String port) throws IOException {
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/command.html?wm_command=915";
        String response = SimpleHttpClient.getResponse(url, null);
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(host, port));
        return result;
    }
    //918
    public static Bundle doNothingOnStop(final String host, final String port) throws IOException {
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/command.html?wm_command=908";
        String response = SimpleHttpClient.getResponse(url, null);
        result.putString(BaseService.EXTRA_RESPONSE_STRING, response);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable) getStatusData(host, port));
        return result;
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
