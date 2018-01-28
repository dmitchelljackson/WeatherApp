package com.example.danieljackson.weatherapp.data.network;

import java.util.Observable;

public class NetworkingClient {

    private static NetworkingClient sNetworkingClient;

    public synchronized static NetworkingClient getInstance() {
        if(sNetworkingClient == null) {
            sNetworkingClient = new NetworkingClient();
        }
        return sNetworkingClient;
    }

}
