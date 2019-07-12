package com.sqy.kingdb.enums;

import android.os.Environment;

public enum SavePathType {
    //内部存储
    INNER_CARD(Environment.getDataDirectory().getAbsolutePath()),
    //外部存储
    EXTERNAL_CARD(Environment.getExternalStorageDirectory().getAbsolutePath());

    private String basePath;
    SavePathType(String path) {
        basePath = path;
    }

    public String getBasePath() {
        return basePath;
    }
}
