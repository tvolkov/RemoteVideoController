package org.tvolkov.rvc.app.network;

import android.net.http.AndroidHttpClient;
import android.util.Base64;
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

    private static final String ERROR401 = "HTTP/1.0 401 Unauthorized";

    private enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE
    }

    private SimpleHttpClient(){}

    public static String getResponse(final String url, final Map<String, String> params) {
        return getResponse(url, params, HttpMethod.GET, null, null);
    }

    public static String getResponse(final String url, final Map<String, String> params, final String login, final String password) {
        return getResponse(url, params, HttpMethod.GET, login, password);
    }

    public static String getResponse(final String url, final Map<String, String> params, final HttpMethod method, final String login, final String password) {
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

            if (login != null && password != null){
                request.addHeader("Authorization", "Basic" + Base64.encodeToString((login + ":" + password).getBytes(), Base64.DEFAULT));
            }

            String responseStr = null;

            HttpResponse response = httpClient.execute(request);

            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() != HttpStatus.SC_OK){
                throw new Exception(status.toString());
            }

            HttpEntity entity = response.getEntity();

            responseStr = convertResponse(entity.getContent());

            return responseStr;
        } catch (Exception e){
            if (e.getMessage().equals(ERROR401)){
                throw new UnauthorizedException(e);
            }
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
