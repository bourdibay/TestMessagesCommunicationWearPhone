package com.personal.bourdi_b.communicationlibrary;

import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by bourdi_bay on 01/02/2015.
 */
public class BaseListenerService extends WearableListenerService {
    protected MessageSender _messageSender = null;

    @Override
    public void onCreate() {
        super.onCreate();
        _messageSender = new MessageSender(this);
    }

    @Override
    public void onDestroy() {
        _messageSender.waitForAllThreadsToFinish();
        super.onDestroy();
    }

}
