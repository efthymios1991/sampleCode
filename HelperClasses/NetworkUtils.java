
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by makis on 1/10/2017.
 */

public class NetworkUtils {

    /**
     * Method checks if network connection is available.
     *
     * @param context Current context.
     * @return True if network connection is available, false otherwise.
     */
    public static boolean isNetworkAvailable(Context context) {
        boolean result = false;

        if (context != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                result = activeNetworkInfo != null && activeNetworkInfo.isConnected();
            }
        }

        return result;
    }
}
