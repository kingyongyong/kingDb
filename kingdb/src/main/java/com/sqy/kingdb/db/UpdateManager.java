package com.sqy.kingdb.db;

import android.text.TextUtils;
import android.util.Log;

import com.sqy.kingdb.KingDbHelper;
import com.sqy.kingdb.KingDbManager;
import com.sqy.kingdb.annotion.KingField;
import com.sqy.kingdb.annotion.KingTable;
import com.sqy.kingdb.bean.Condition;
import com.sqy.kingdb.bean.KingTableInfo;
import com.sqy.kingdb.bean.QueryCondition;
import com.sqy.kingdb.sql.UpdateSql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class UpdateManager {

    public<T> String update(Class<T> clazz, List<Condition> setValue, QueryCondition condition){

        UpdateSql updateSql = new UpdateSql();
        String sql = updateSql.updateSql(clazz,setValue,condition);
        try {
            KingDbHelper.getInstance().getDatabase().execSQL(sql);
            return null;
        }catch (Exception e){
            return "[ king_db ] update error : " + e.getMessage();
        }

    }

    /**
     * 通过实体类更新数据库数据
     * @param entity 需要更新的实体类
     * @param <T> 实体类类型
     * @return 错误信息（如果没有错误则为空）
     *
     * 1、查看这张表是否存在
     * 2、查看这张表是否有主键id
     * 3、构造更新sql
     */
    public<T> String update(T entity){

        String tableName = entity.getClass().getAnnotation(KingTable.class) == null ?
                entity.getClass().getSimpleName() :
                entity.getClass().getAnnotation(KingTable.class).value();

        List<KingTableInfo> tables = KingDbManager.newQueryBuilder().addEqual("table_name",tableName).query(KingTableInfo.class);

        if(tables.size() == 0){
            return "[ king_db ] update error : the table [ " + tableName + " ] is not exits";
        }

        Field[] fields = entity.getClass().getDeclaredFields();
        Boolean isExistPrimaryKey = false;
        String primaryKey = "";
        String primaryKeyValue = "";
        List<Condition> conditions = new ArrayList<>();
        for(Field field : fields){
            field.setAccessible(true);
            KingField kingField = field.getAnnotation(KingField.class);
            try {
                if(field.get(entity) == null){
                    continue;
                }

                if(kingField != null){
                    if(kingField.primaryKey()){//如果是主键
                        isExistPrimaryKey = true;
                        primaryKey = TextUtils.isEmpty(kingField.value()) ? field.getName() : kingField.value();
                        primaryKeyValue = field.get(entity).toString();
                    }else if(!TextUtils.isEmpty(kingField.value())){
                        //如果不是主键 并且 kingField 的表字段名不为空
                        conditions.add(new Condition(field.getName(),field.get(entity).toString()));
                    }
                }else{
                    conditions.add(new Condition(field.getName(),field.get(entity).toString()));
                }
            } catch (Exception e) {
                return "[ king_db ] update error : the table [ " + tableName + " ] has error " + e.getMessage();
            }
        }
        if(!isExistPrimaryKey){
            //如果不存在主键，则不能用该方法进行更新数据
            return "[ king_db ] update error : the table [ " + tableName + " ] primary key not has value";
        }

        if(TextUtils.isEmpty(primaryKey) || TextUtils.isEmpty(primaryKeyValue)){
            //主键或者主键值为空，则不能用该方法进行更新数据
            return "[ king_db ] update error : the table [ " + tableName + " ] primary key is null";
        }

        UpdateSql updateSql = new UpdateSql();
        String sql = updateSql.updateSql(entity.getClass(),conditions,
                KingDbManager.newQueryBuilder().addEqual(primaryKey,primaryKeyValue).buildQueryCondition());
        try {
            KingDbHelper.getInstance().getDatabase().execSQL(sql);
            return null;
        }catch (Exception e){
            return "[ king_db ] update error : " + e.getMessage();
        }

    }

}
