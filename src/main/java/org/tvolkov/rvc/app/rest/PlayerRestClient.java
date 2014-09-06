package org.tvolkov.rvc.app.rest;

import android.os.Bundle;

public interface PlayerRestClient {

    /*public static final String STATESTRING = "statestring";
    public static final String FILEPATH = "filepath";
    public static final String STATE = "state";*/

    public static final String STATE = "org.tvolkov.rvc.STATE";
    public static final String FILE = "org.tvolkov.rvc.FILE";
    public static final String STATESTR = "org.tvolkov.rvc.STATESTR";
    public static final String LENGTH = "org.tvolkov.rvc.LENGTH";
    public static final String TIME = "org.tvolkov.rvc.TIME";


    Bundle getStatus(final Endpoint player);
    Bundle play(final Endpoint player);
    Bundle pause(final Endpoint player);
    Bundle stop(final Endpoint player);
    Bundle playPrev(final Endpoint player);
    Bundle playNext(final Endpoint player);
    Bundle volumeUp(final Endpoint player);
    Bundle volumeDown(final Endpoint player);
    Bundle prevAudio(final Endpoint player);
    Bundle nextAudio(final Endpoint player);
    Bundle exit(final Endpoint player);
    Bundle shutdownPcOnStop(final Endpoint player);
    Bundle doNothingOnStop(final Endpoint player);
}
