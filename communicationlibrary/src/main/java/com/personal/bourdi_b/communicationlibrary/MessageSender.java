package com.personal.bourdi_b.communicationlibrary;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by bourdi_bay on 31/01/2015.
 */
public class MessageSender {
    private static final long CONNECTION_TIME_OUT = 1; // seconds

    private GoogleApiClient _client;
    private String _nodeId;
    private Thread _retrieveDeviceNodeThread = null;
    private Thread _messageThread = null;

    /**
     * Initialize the GoogleApiClient and get the Node ID of the connected device.
     */
    public MessageSender(Context context) {
        _client = getGoogleApiClient(context);
        retrieveDeviceNode();
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        return new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .build();
    }

    /**
     * Connect to the GoogleApiClient and retrieve the connected device's Node ID. If there are
     * multiple connected devices, the first Node ID is returned.
     */
    private void retrieveDeviceNode() {
        _retrieveDeviceNodeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                ConnectionResult connectionResult = _client
                        .blockingConnect(CONNECTION_TIME_OUT, TimeUnit.SECONDS);
                if (connectionResult.isSuccess() == true) {
                    NodeApi.GetConnectedNodesResult result =
                            Wearable.NodeApi.getConnectedNodes(_client).await();
                    List<Node> nodes = result.getNodes();
                    if (nodes.size() > 0) {
                        _nodeId = nodes.get(0).getId();
                    }
                    _client.disconnect();
                }
            }
        });
        _retrieveDeviceNodeThread.start();
    }

    /**
     * Send a message to the connected mobile device.
     */
    public void sendMessage(final String message) {

        // Ensure we finished to try to get the nodeID first.
        if (_retrieveDeviceNodeThread != null) {
            try {
                _retrieveDeviceNodeThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (_nodeId != null) {
            _messageThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    _client.blockingConnect(CONNECTION_TIME_OUT, TimeUnit.SECONDS);
                    Wearable.MessageApi.sendMessage(_client, _nodeId, message, null);
                    _client.disconnect();
                }
            });
            _messageThread.start();
        }
    }

    public void waitForAllThreadsToFinish() {
        try {
            if (_retrieveDeviceNodeThread != null) {
                _retrieveDeviceNodeThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            if (_messageThread != null) {
                _messageThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
