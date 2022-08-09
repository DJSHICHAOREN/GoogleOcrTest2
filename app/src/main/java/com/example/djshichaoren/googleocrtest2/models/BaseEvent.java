package com.example.djshichaoren.googleocrtest2.models;

import android.os.Bundle;

public class BaseEvent {
    protected String eventType;
    protected String message;
    protected Bundle bundle;

    public BaseEvent() {
    }

    public BaseEvent(String type) {
        this.eventType = type;
    }

    public BaseEvent(String type, String msg) {
        this.eventType = type;
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }

    public String getType() {
        return this.eventType;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}