package com.personal.bourdi_b.testmessagescommunicationwearphone;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;

import com.personal.bourdi_b.communicationlibrary.MessageSender;

/**
 * Created by bourdi_bay on 31/01/2015.
 */
public class MainActivity extends Activity {

    private static final String MESSAGE = "This is from the Wear";
    private MessageSender _messageSender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _messageSender = new MessageSender(this);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupWidgets();
            }
        });
    }

    /**
     * Set up the button for handling click events.
     */
    private void setupWidgets() {
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessageToPhone();
            }
        });
    }

    private void sendMessageToPhone() {
        _messageSender.sendMessage(MESSAGE);
    }
}
