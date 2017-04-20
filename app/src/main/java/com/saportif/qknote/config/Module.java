package com.saportif.qknote.config;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.annotations.RealmModule;

/**
 * Created by Semih on 13.09.2016.
 */
public class Module extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RealmConfiguration config = new RealmConfiguration.Builder(this)
                                            .deleteRealmIfMigrationNeeded()
                                            .build();
        Realm.setDefaultConfiguration(config);
    }
}
