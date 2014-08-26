package org.tvolkov.rvc.app.core;

/**
 * An interface describes basic player interactions
 */
public interface PlayerRestTemplate {
    void playPrev();

    void playNext();

    void pause();

    void play();
}
