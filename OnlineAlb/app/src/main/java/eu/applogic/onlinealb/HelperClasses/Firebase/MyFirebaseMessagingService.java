package eu.applogic.onlinealb.HelperClasses.Firebase;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import eu.applogic.onlinealb.HelperClasses.DebugLogger;

/**
 * Created by makis on 7/28/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        DebugLogger.debug("From: " + remoteMessage.getFrom());
        DebugLogger.debug("Data Message: " + remoteMessage.getData());
    }
}
