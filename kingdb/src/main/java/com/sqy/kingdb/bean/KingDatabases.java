package com.sqy.kingdb.bean;

import com.sqy.kingdb.annotion.KingField;
import com.sqy.kingdb.annotion.KingTable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@KingTable("t_king_db")
public class KingDatabases {
    @KingField("db_name")
    public String databaseName;
    @KingField("table_version")
    public Integer version;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
