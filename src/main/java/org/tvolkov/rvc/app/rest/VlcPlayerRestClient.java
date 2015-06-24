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
        Map<String, String> responseData = getStatusData(player);
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, (Serializable)responseData);
        return result;
    }

    private Map<String, String> getStatusData(final Endpoint player){

        String response = getResponse(player, null);
        Map<String, String> responseData = new HashMap<String, String>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            //status = mapper.readValue(response, Status.class);
            JsonNode json  = mapper.readTree(response);

            responseData.put(STATESTR, String.valueOf(json.get("state")));
            responseData.put(TIME, String.valueOf(json.get("time")));
            responseData.put(LENGTH, String.valueOf(json.get("length")));
            JsonNode information = json.get("information");
            if (information != null){
                responseData.put(FILE, String.valueOf(json.get("information").get("category").get("meta").get("filename")));
            } else {
                responseData.put(FILE, "");
            }


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
        return responseData;
    }

    private String getResponse(final Endpoint endpoint, final String command){
        String response;
        String url = generateUrlString(endpoint, command);
        try {
            response = SimpleHttpClient.getResponse(url, null);
        } catch (UnauthorizedException e) {
            response = SimpleHttpClient.getResponse(url, null, endpoint.getLogin(), endpoint.getPassword());
        }
        return response;
    }

    private String generateUrlString(final Endpoint endpoint, final String command){
        String url = "http://" + endpoint.getHost() + ":" + endpoint.getPort() + "/requests/status.json";
        if (command != null){
            url += "&command=" + command;
        }
        return url;
    }

    @Override
    public Bundle play(Endpoint player) {
        return doRequest(player, VlcPlayerCommandCodes.PLAY);
    }

    @Override
    public Bundle pause(Endpoint player) {
        return doRequest(player, VlcPlayerCommandCodes.PAUSE);
    }

    @Override
    public Bundle stop(Endpoint player) {
        return doRequest(player, VlcPlayerCommandCodes.STOP);
    }

    @Override
    public Bundle playPrev(Endpoint player) {
        return doRequest(player, VlcPlayerCommandCodes.PLAY_PREV);
    }

    @Override
    public Bundle playNext(Endpoint player) {
        return doRequest(player, VlcPlayerCommandCodes.PLAY_NEXT);
    }

    @Override
    public Bundle volumeUp(Endpoint player) {
        return doRequest(player, VlcPlayerCommandCodes.VOLUME_UP);
    }

    @Override
    public Bundle volumeDown(Endpoint player) {
        return doRequest(player, VlcPlayerCommandCodes.VOLUME_DOWN);
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

    private Bundle doRequest(final Endpoint endpoint, final String command){
        String response = getResponse(endpoint, command);
        Bundle result = new Bundle();
        result.putSerializable(BaseService.EXTRA_RESPONSE_MAP, response);
        return result;
    }

    private static class VlcPlayerCommandCodes {
        public static final String PLAY = "pl_play";
        public static final String PAUSE = "pl_pause";
        public static final String STOP = "pl_stop";
        public static final String PLAY_PREV = "pl_previous";
        public static final String PLAY_NEXT = "pl_next";
        public static final String FULLSCREEN = "fullscreen";
        public static final String VOLUME_UP = "volume&val=+10";
        public static final String VOLUME_DOWN = "volume&val=-10";
    }
}
