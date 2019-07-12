package com.sqy.kingdb.db;

import android.text.TextUtils;

import com.sqy.kingdb.KingDbHelper;
import com.sqy.kingdb.KingDbManager;
import com.sqy.kingdb.annotion.KingField;
import com.sqy.kingdb.annotion.KingTable;
import com.sqy.kingdb.bean.QueryCondition;
import com.sqy.kingdb.sql.DeleteSql;

import java.lang.reflect.Field;

public class DeleteManager {

    public <T> String delete(Class<T> clazz, QueryCondition condition) {
        DeleteSql deleteSql = new DeleteSql();
        String sql = deleteSql.deleteSql(clazz, condition);
        try {
            KingDbHelper.getInstance().getDatabase().execSQL(sql);
            return null;
        } catch (Exception e) {
            return "[ king_db ] delete error : " + e.getMessage();
        }
    }

    public <T> String delete(T entity) {

        String tableName = entity.getClass().getAnnotation(KingTable.class) == null ?
                entity.getClass().getSimpleName() :
                entity.getClass().getAnnotation(KingTable.class).value();

        Field[] fields = entity.getClass().getDeclaredFields();
        Boolean isExistPrimaryKey = false;
        String primaryKey = "";
        String primaryKeyValue = "";
        for (Field field : fields) {
            field.setAccessible(true);
            KingField kingField = field.getAnnotation(KingField.class);
            try {
                if (kingField != null && kingField.primaryKey() && field.get(entity) != null) {
                    isExistPrimaryKey = true;
                    primaryKey = TextUtils.isEmpty(kingField.value()) ? field.getName() : kingField.value();
                    primaryKeyValue = field.get(entity).toString();
                }
            } catch (Exception e) {
                return "[ king_db ] delete error : the table [ " + tableName + " ] has error " + e.getMessage();
            }
        }
        if (!isExistPrimaryKey) {
            //如果不存在主键，则不能用该方法进行更新删除
            return "[ king_db ] delete error : the table [ " + tableName + " ] primary key not has value";
        }

        if (TextUtils.isEmpty(primaryKey) || TextUtils.isEmpty(primaryKeyValue)) {
            //主键或者主键值为空，则不能用该方法进行删除数据
            return "[ king_db ] delete error : the table [ " + tableName + " ] primary key is null";
        }

        DeleteSql deleteSql = new DeleteSql();
        String sql = deleteSql.deleteSql(entity.getClass(),
                KingDbManager.newQueryBuilder().addEqual(primaryKey, primaryKeyValue).buildQueryCondition());
        try {
            KingDbHelper.getInstance().getDatabase().execSQL(sql);
            return null;
        } catch (Exception e) {
            return "[ king_db ] delete error : " + e.getMessage();
        }

    }

}
