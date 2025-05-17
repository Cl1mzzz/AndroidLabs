package com.example.lab4.audio.model;

public class AudioModel {

    public String audioName;
    public String audioDuration;
    public String path;

    public AudioModel(String audioName, String audioDuration, String path) {
        this.audioName = audioName;
        this.audioDuration = audioDuration;
        this.path = path;
    }

    public String getAudioName() {
        return audioName;
    }

    public String getAudioDuration() {
        long durationMillis = Long.parseLong(audioDuration);
        int minutes = (int) (durationMillis / 1000) / 60;
        int seconds = (int) (durationMillis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public String getPath() {
        return path;
    }

}
