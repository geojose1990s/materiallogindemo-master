package com.sourcey.materiallogindemo;

import android.app.Application;
import android.os.StrictMode;

import com.sourcey.materiallogindemo.network.NetworkManager;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Created by geojose1990 on 07/09/16.
 */
public class ApplicationConfig extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        enabledStrictMode();
        NetworkManager.getInstance(getApplicationContext());// Singleton initialisation for NetworkManager
    }

    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
                    //.penaltyDeath() //
                    .build());
        }
    }
}
