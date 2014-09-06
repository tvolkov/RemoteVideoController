package org.tvolkov.rvc.app.rest;

import android.os.Parcel;
import android.os.Parcelable;

public class Endpoint implements Parcelable {
    private String host;
    private String port;
    private String login;
    private String password;

    public Endpoint(final String host, final String port, final String login, final String password){
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.host, this.port, this.login, this.password});
    }

    public static final Parcelable.Creator<Endpoint> CREATOR = new Parcelable.Creator<Endpoint>() {
        public Endpoint createFromParcel(Parcel in) {
            return new Endpoint(in);
        }

        public Endpoint[] newArray(int size) {
            return new Endpoint[size];
        }
    };

    private Endpoint(Parcel in){
        String[] data = new String[4];
        in.readStringArray(data);
        this.host = data[0];
        this.port = data[1];
        this.login = data[2];
        this.password = data[3];
    }

}
