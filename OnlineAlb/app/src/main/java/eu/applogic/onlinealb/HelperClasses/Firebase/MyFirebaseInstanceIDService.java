package eu.applogic.onlinealb.HelperClasses.Firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import eu.applogic.onlinealb.HelperClasses.DebugLogger;

/**
 * Created by makis on 7/28/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        DebugLogger.debug("Just created - Refreshed token: " + refreshedToken);
    }
}