package org.tvolkov.rvc.app.rest;

public class RestClientProvider {

    private static RestClientProvider INSTANCE;

    private PlayerRestClient restClient;

    public static RestClientProvider getInstance(){
        if (INSTANCE == null){
            INSTANCE = new RestClientProvider();
        }
        return INSTANCE;
    }

    public RestClientProvider(){}

    public PlayerRestClient getClient(){
        return restClient;
    }

    public void setRestClient(PlayerRestClient restClient){
        this.restClient = restClient;
    }
}
