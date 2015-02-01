package com.personal.bourdi_b.testmessagescommunicationwearphone;

import android.widget.Toast;

import com.google.android.gms.wearable.MessageEvent;
import com.personal.bourdi_b.communicationlibrary.BaseListenerService;

/**
 * Created by bourdi_bay on 31/01/2015.
 */
public class ListenerService extends BaseListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        showToast(messageEvent.getPath());

        // Perform actions like network requests, etc...

        // Send back an answer to the wear.
        _messageSender.sendMessage("This is a response from the phone");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
