package org.tvolkov.rvc.app.core.network;

import android.net.http.AndroidHttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Map;

public class SimpleHttpClient {

    public static final String TAG = SimpleHttpClient.class.getSimpleName();

    private static final String UTF8 = "UTF-8";

    private enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    private SimpleHttpClient(){}

    public static String getResponse(final String url, final Map<String, String> params) {
        return getResponse(url, params, HttpMethod.GET);
    }

    public static String getResponse(final String url, final Map<String, String> params, final HttpMethod method) {
        if (url == null){
            throw new IllegalArgumentException("url cannot be null");
        }

        HttpUriRequest request = null;
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance(null);

        try {
            if (method == HttpMethod.GET){
                StringBuilder sb = new StringBuilder();
                sb.append(url);

                if (params != null){
                    sb.append("?");

                    for (Map.Entry<String, String> entry : params.entrySet()){
                        sb.append(URLEncoder.encode(entry.getKey(), UTF8))
                                .append("=")
                                .append(URLEncoder.encode(entry.getValue(), UTF8))
                                .append("&");
                    }
                    //Remove tailing '&'
                    sb = new StringBuilder(sb.substring(0, sb.length() - 1));
                }

                URI uri = new URI(sb.toString());
                request = new HttpGet(uri);
            }

            String responseStr = null;

            HttpResponse response = httpClient.execute(request);

            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HttpStatus.SC_OK){
                //TODO need to handle 302 response here, i.e. to do redirect
                throw new Exception(status.toString());
            }

            HttpEntity entity = response.getEntity();

            responseStr = convertResponse(entity.getContent());

            return responseStr;
        } catch (Exception e){
            throw new RuntimeException(e.getMessage());
        } finally {
            httpClient.close();
        }
    }

    private static String convertResponse(final InputStream is) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        try {
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                sb.append(line).append("\n");
            }
            return sb.toString();
        } finally {
            is.close();
        }
    }
}
