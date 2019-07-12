package com.sqy.kingdbdemo;

import android.app.Application;

import com.sqy.kingdb.KingDbHelper;
import com.sqy.kingdb.enums.SavePathType;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        KingDbHelper.withThis(this).
                setSavePathType(SavePathType.INNER_CARD).
                setDatabaseName("king_db_demo").
                setVersion(2).
                complete();
    }
}
