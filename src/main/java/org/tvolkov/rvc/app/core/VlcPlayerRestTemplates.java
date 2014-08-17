package org.tvolkov.rvc.app.core;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.tvolkov.rvc.app.core.network.SimpleHttpClient;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class VlcPlayerRestTemplates {

    public static Bundle getStatus(final String host, final String port) throws IOException {
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/requests/status.json";
        ObjectMapper mapper = new ObjectMapper();
        Status status = mapper.readValue(new URL(url), Status.class);
        result.putParcelable(BaseService.EXTRA_STATUS_DATA, status);
        return result;
    }

    public static Bundle getStatus1(final String host, final String port){
        Bundle result = new Bundle();
        final String url = "http://" + host + ":" + port + "/requests/status.json";
        String response = SimpleHttpClient.getResponse(url, null);
        return result;
    }

    public static class Status implements Parcelable{
        private String version;
        private List<String> videoeffects;
        private String random;
        private String fullscreen;
        private String repeat;
        private String audiodelay;
        private String rate;
        private String apiversion;
        private String state;
        private String equalizer;
        private List<String> audiofilters;
        private String volume;
        private String length;
        private String loop;
        private String time;
        private String currentplid;
        private String position;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    }
}
