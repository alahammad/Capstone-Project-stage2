package net.ahammad.udacitycapstone;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by alahammad on 9/8/15.
 */
public class MainApp extends Application {
    private Tracker mTracker;

    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        realmConfig();
    }

    private void realmConfig (){
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                .name("db.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
