package com.sqy.kingdb.bean;

import com.sqy.kingdb.annotion.KingField;
import com.sqy.kingdb.annotion.KingTable;

import java.util.ArrayList;
import java.util.List;

@KingTable("t_king_db_table")
public class KingTableInfo {
    @KingField("table_name")
    private String name;
    @KingField("package_name")
    private String packageName;
    @KingField("table_fields")
    private List<String> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getFields() {
        if(fields == null){
            fields = new ArrayList<>();
        }
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
