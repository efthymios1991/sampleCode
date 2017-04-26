
/**
 * Created by efthymioskontis on 2/3/16.
 */
import android.util.Log;

public class DebugLogger {

    public static boolean logsEnabled = true;

    private static final int STACK_TRACE_LEVEL = 4;

    public static void info(String content) {

        if(logsEnabled) {
            if(content==null) content="null";
            Log.i(getClassName()+" "+getLineNumber(),content);
        }

    }

    public static void warn(String content) {
        if(logsEnabled) {
            if(content==null) content="null";
            Log.w(getClassName()+" "+getLineNumber(),content);
        }
    }

    public static void error(String content) {
        if(true) {
            if(content==null) content="null";
            Log.e(getClassName()+" "+getLineNumber(),content);
        }
    }

    public static void error(String content, Exception e) {
        if(true) {
            if(content==null) content="null";
            Log.e(getClassName()+" "+getLineNumber(),content,e);
        }
    }

    public static void verbose(String content) {

        if(logsEnabled) {
            if(content==null) content="null";
            Log.v(getClassName()+" "+getLineNumber(), content);
        }

    }

    public static void debug(String content) {
        if(logsEnabled) {
            if(content==null) content="null";
            Log.d(getClassName()+" "+getLineNumber(),content);
        }
    }

    private static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVEL].getLineNumber();
    }

    private static String getClassName() {
        String fileName = Thread.currentThread().getStackTrace()[STACK_TRACE_LEVEL].getFileName();
        return fileName.substring(0, fileName.length() - 5);
    }

    public static void longInfo(String message, String str) {
        if(str.length() > 4000) {
            Log.i(getClassName()+" "+getLineNumber(), message+" "+str.substring(0, 4000));
            longInfo("", str.substring(4000));
        } else
            Log.i(getClassName()+" "+getLineNumber(),message+" "+str);
    }
}
