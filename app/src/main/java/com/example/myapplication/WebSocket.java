package com.example.myapplication;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class WebSocket extends WebSocketClient {

    public WebSocket(URI serverUri) {
        super(serverUri, new Draft_6455());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i("WebSocket", "onOpen");
    }

    @Override
    public void onMessage(String message) {
        Log.i("WebSocket", "onMessage: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i("WebSocket", "onClose, code: " + code + " , reason: " + reason + " , remote: " + remote);
    }

    @Override
    public void onError(Exception ex) {
        Log.i("WebSocket", "onError: " + ex);
    }
}