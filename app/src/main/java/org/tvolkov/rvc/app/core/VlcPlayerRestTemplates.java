package org.tvolkov.rvc.app.core;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class VlcPlayerRestTemplates {

    public static Status getStatus(final String host, final String port){
        final String url = "http://" + host + ":" + port + "/requests/status.json";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        Status status = restTemplate.getForObject(url, Status.class);
        return status;
    }

    public static class Status{
        public String[] getInformation() {
            return information;
        }

        private String[] information;

        public String getTime() {
            return time;
        }

        private String time;
    }
}
