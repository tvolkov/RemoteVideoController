package org.tvolkov.rvc.app.rest;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tvolkov.rvc.app.core.BaseService;
import org.tvolkov.rvc.app.network.SimpleHttpClient;
import org.tvolkov.rvc.app.network.UnauthorizedException;
import org.tvolkov.rvc.app.rest.exception.RestClientException;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VlcPlayerRestClient implements PlayerRestClient {

    public VlcPlayerRestClient(){}

    @Override
    public Bundle getStatus(final Endpoint player) {
        Bundle result = new Bundle();
        final String url = "http://" + player.getHost() + ":" + player.getPort() + "/requests/status.json";
        String response;
        Status status;
        try {

/*            ObjectMapper mapper = new ObjectMapper();
            Status status = mapper.readValue(new URL(url), Status.class);
            result.putParcelable(BaseService.EXTRA_STATUS_DATA, status);*/
            response = SimpleHttpClient.getResponse(url, null);

        } catch (UnauthorizedException e) {
            response = SimpleHttpClient.getResponse(url, null, player.getLogin(), player.getPassword());
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            //status = mapper.readValue(response, Status.class);
            JsonNode json  = mapper.readTree(response);
            Map<String, String> responseData = new HashMap<String, String>();
            responseData.put(STATESTR, String.valueOf(json.get("state")));
            responseData.put(TIME, String.valueOf(json.get("time")));
            responseData.put(LENGTH, String.valueOf(json.get("length")));
            responseData.put(FILE, String.valueOf(json.get("information").get("category").get("meta").get("filename")));
            result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable)responseData);
            if ("playing".equals(String.valueOf(json.get("state")))){
                responseData.put(STATE, "2");
            } else if ("paused".equals(String.valueOf(json.get("state")))){
                responseData.put(STATE, "1");
            } else if ("stopped".equals(String.valueOf(json.get("state")))){
                responseData.put(STATE, "0");
            }
        } catch (Exception e) {
            throw new RestClientException(e);
        }

        //result.putParcelable(BaseService.EXTRA_STATUS_DATA, );


        return result;
    }

    @Override
    public Bundle play(Endpoint player) {
        return null;
    }

    @Override
    public Bundle pause(Endpoint player) {
        return null;
    }

    @Override
    public Bundle stop(Endpoint player) {
        return null;
    }

    @Override
    public Bundle playPrev(Endpoint player) {
        return null;
    }

    @Override
    public Bundle playNext(Endpoint player) {
        return null;
    }

    @Override
    public Bundle volumeUp(Endpoint player) {
        return null;
    }

    @Override
    public Bundle volumeDown(Endpoint player) {
        return null;
    }

    @Override
    public Bundle prevAudio(Endpoint player) {
        return null;
    }

    @Override
    public Bundle nextAudio(Endpoint player) {
        return null;
    }

    @Override
    public Bundle exit(Endpoint player) {
        return null;
    }

    @Override
    public Bundle shutdownPcOnStop(Endpoint player) {
        return null;
    }

    @Override
    public Bundle doNothingOnStop(Endpoint player) {
        return null;
    }

/*    public Bundle getStatus1(final Endpoint player){
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/requests/status.json";
        String response = SimpleHttpClient.getResponse(url, null);
        return result;
    }*/

    public static class Status implements Parcelable {
        public int subtitledelay;
        public String version;
        public List<String> videoeffects;
        public boolean random;
        public int fullscreen;
        public boolean repeat;
        public int audiodelay;
        public int rate;
        public int apiversion;
        public String state;
        public String[] equalizer;
        public List<String> audiofilters;
        public int volume;
        public int length;
        public boolean loop;
        public int time;
        public int currentplid;
        public int position;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }
}
