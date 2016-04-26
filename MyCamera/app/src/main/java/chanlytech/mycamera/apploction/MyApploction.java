package chanlytech.mycamera.apploction;

import android.app.Application;

import chanlytech.mycamera.utils.CrashHandler;

/**
 * Created by Lyy on 2016/4/21.
 */
public class MyApploction extends Application {
    private static MyApploction mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    public static MyApploction getAppLoctin() {
        return mInstance;
    }


    public static MyApploction getApp() {
        return getAppLoctin();
    }
}
