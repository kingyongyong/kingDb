package com.sqy.kingdb.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.text.TextUtils;
import android.util.Log;

import com.sqy.kingdb.KingDbHelper;
import com.sqy.kingdb.annotion.KingTable;
import com.sqy.kingdb.sql.InsertSqlSql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertManager {

    private List<String> mDataBaseFieldNames;
    private String mTableName;

    public<T> String insert(T entity){

        InsertSqlSql insertSqlSql = new InsertSqlSql();

        if(mTableName == null){
            mTableName = entity.getClass().getAnnotation(KingTable.class) == null ?
                    entity.getClass().getSimpleName() :
                    entity.getClass().getAnnotation(KingTable.class).value();
        }
        if(mDataBaseFieldNames == null){
            mDataBaseFieldNames = getDataBaseFieldNames(mTableName);
        }

        String insertSql = insertSqlSql.insertSql(entity,mDataBaseFieldNames);
        try {
            KingDbHelper.getInstance().getDatabase().execSQL(insertSql);
            return null;
        }catch (Exception e){
            return "[ king_db ] insert error : " + e.getMessage();
        }
    }

    private List<String> getDataBaseFieldNames(String tableName){
        List<String> databaseNames = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(tableName).append(" LIMIT 1,0");
        Cursor cursor = null;
        try {
            cursor = KingDbHelper.getInstance().getDatabase().rawQuery(builder.toString(),null);
            //表的列名数据
            String[] columnName = cursor.getColumnNames();
            databaseNames = Arrays.asList(columnName);
        }catch (Exception e){}
        finally {
            if(cursor != null){
                cursor.close();
            }
        }

        return databaseNames;
    }
}
