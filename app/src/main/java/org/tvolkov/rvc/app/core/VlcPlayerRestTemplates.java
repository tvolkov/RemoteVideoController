package org.tvolkov.rvc.app.core;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class VlcPlayerRestTemplates {

    public static Bundle getStatus(final String host, final String port){
        final String url = "http://" + host + ":" + port + "/requests/status.json";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Bundle result = new Bundle();
        result.putParcelable(CommonActionService.EXTRA_STATUS_DATA, restTemplate.getForObject(url, Status.class));
        return result;
    }

    public static class Status implements Parcelable{
        public String[] getInformation() {
            return information;
        }

        private String[] information;

        public String getTime() {
            return time;
        }

        private String time;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }
    }
}
