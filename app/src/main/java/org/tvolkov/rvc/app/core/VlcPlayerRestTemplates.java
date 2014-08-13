package org.tvolkov.rvc.app.core;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class VlcPlayerRestTemplates {

    public static Bundle getStatus(final String host, final String port){
        final String url = "http://" + host + ":" + port + "/requests/status.json";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        Bundle result = new Bundle();
        result.putParcelable(CommonActionService.EXTRA_STATUS_DATA, restTemplate.getForObject(url, Status.class));
        return result;
    }

    public static class Status implements Parcelable{
        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCurrentplid() {
            return currentplid;
        }

        public void setCurrentplid(String currentplid) {
            this.currentplid = currentplid;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLoop() {
            return loop;
        }

        public void setLoop(String loop) {
            this.loop = loop;
        }

        public String getLength() {
            return length;
        }

        public void setLength(String length) {
            this.length = length;
        }

        public String getVolume() {
            return volume;
        }

        public void setVolume(String volume) {
            this.volume = volume;
        }

        public List<String> getAudiofilters() {
            return audiofilters;
        }

        public void setAudiofilters(List<String> audiofilters) {
            this.audiofilters = audiofilters;
        }

        public String getEqualizer() {
            return equalizer;
        }

        public void setEqualizer(String equalizer) {
            this.equalizer = equalizer;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getApiversion() {
            return apiversion;
        }

        public void setApiversion(String apiversion) {
            this.apiversion = apiversion;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getAudiodelay() {
            return audiodelay;
        }

        public void setAudiodelay(String audiodelay) {
            this.audiodelay = audiodelay;
        }

        public String getFullscreen() {
            return fullscreen;
        }

        public void setFullscreen(String fullscreen) {
            this.fullscreen = fullscreen;
        }

        public String getRandom() {
            return random;
        }

        public void setRandom(String random) {
            this.random = random;
        }

        public List<String> getVideoeffects() {
            return videoeffects;
        }

        public void setVideoeffects(List<String> videoeffects) {
            this.videoeffects = videoeffects;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        private String version;
        private List<String> videoeffects;
        private String random;
        private String fullscreen;

        public String getRepeat() {
            return repeat;
        }

        public void setRepeat(String repeat) {
            this.repeat = repeat;
        }

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
