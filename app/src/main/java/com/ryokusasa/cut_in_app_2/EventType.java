package com.ryokusasa.cut_in_app_2;

public enum EventType{
        SCREEN_ON("画面ON"),
        LOW_BATTERY("電池残量低下"),
        APP_NOTIFICATION("アプリ通知");

    private final String text;

    EventType(final String text) {
        this.text = text;
    }

    public String getString() {
        return this.text;
    }
}
