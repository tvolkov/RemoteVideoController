package org.tvolkov.rvc.app.core;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;

public class VlcPlayerRestTemplates {
    /**
     *
     * The following method wont work since huawey jerks didn't fix this annoying bug
    08-14 19:59:46.331    3351-3370/org.tvolkov.rvc.app W/System.err﹕ java.lang.NumberFormatException: Invalid int: ""
            08-14 19:59:46.331    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.lang.Integer.invalidInt(Integer.java:138)
            08-14 19:59:46.331    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.lang.Integer.parseInt(Integer.java:359)
            08-14 19:59:46.341    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.lang.Integer.parseInt(Integer.java:332)
            08-14 19:59:46.341    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.util.Calendar.getHwFirstDayOfWeek(Calendar.java:807)
            08-14 19:59:46.341    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.util.Calendar.<init>(Calendar.java:745)
            08-14 19:59:46.341    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.util.GregorianCalendar.<init>(GregorianCalendar.java:338)
            08-14 19:59:46.341    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.util.GregorianCalendar.<init>(GregorianCalendar.java:314)
            08-14 19:59:46.351    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.text.SimpleDateFormat.<init>(SimpleDateFormat.java:378)
            08-14 19:59:46.351    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.text.SimpleDateFormat.<init>(SimpleDateFormat.java:368)
            08-14 19:59:46.351    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.text.SimpleDateFormat.<init>(SimpleDateFormat.java:253)
            08-14 19:59:46.351    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at org.codehaus.jackson.map.util.StdDateFormat.<clinit>(StdDateFormat.java:79)
            08-14 19:59:46.351    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at org.codehaus.jackson.map.MapperConfig.<clinit>(MapperConfig.java:53)
            08-14 19:59:46.351    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at org.codehaus.jackson.map.ObjectMapper.<init>(ObjectMapper.java:391)
            08-14 19:59:46.361    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at org.codehaus.jackson.map.ObjectMapper.<init>(ObjectMapper.java:358)
            08-14 19:59:46.361    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at org.codehaus.jackson.map.ObjectMapper.<init>(ObjectMapper.java:328)
            08-14 19:59:46.361    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at org.springframework.http.converter.json.MappingJacksonHttpMessageConverter.<init>(MappingJacksonHttpMessageConverter.java:56)
            08-14 19:59:46.361    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at org.tvolkov.rvc.app.core.VlcPlayerRestTemplates.getStatus(VlcPlayerRestTemplates.java:17)
            08-14 19:59:46.361    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at org.tvolkov.rvc.app.core.CommonActionService.onHandleIntent(CommonActionService.java:29)
            08-14 19:59:46.371    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at org.tvolkov.rvc.app.core.BaseService.access$000(BaseService.java:9)
            08-14 19:59:46.371    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at org.tvolkov.rvc.app.core.BaseService$ProcessIntentTask.run(BaseService.java:62)
            08-14 19:59:46.371    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:442)
            08-14 19:59:46.371    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.util.concurrent.FutureTask$Sync.innerRun(FutureTask.java:305)
            08-14 19:59:46.371    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.util.concurrent.FutureTask.run(FutureTask.java:137)
            08-14 19:59:46.381    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1076)
            08-14 19:59:46.381    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:569)
            08-14 19:59:46.381    3351-3370/org.tvolkov.rvc.app W/System.err﹕ at java.lang.Thread.run(Thread.java:856)

    public static Bundle getStatus(final String host, final String port){
        final String url = "http://" + host + ":" + port + "/requests/status.json";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
        Bundle result = new Bundle();
        result.putParcelable(CommonActionService.EXTRA_STATUS_DATA, restTemplate.getForObject(url, Status.class));
        return result;
    }*/

    public static Bundle getStatus(final String host, final String port){
        try {
            final String url = "http://" + host + ":" + port + "/requests/status.json";
            ObjectMapper mapper = new ObjectMapper();
            Status status = mapper.readValue(new URL(url), Status.class);
            Bundle result = new Bundle();
            result.putParcelable(BaseService.EXTRA_STATUS_DATA, status);
            return result;
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

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
