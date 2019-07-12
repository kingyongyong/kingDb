package com.sqy.kingdb.sql;

import android.text.TextUtils;

import com.sqy.kingdb.annotion.KingField;
import com.sqy.kingdb.annotion.KingTable;
import com.sqy.kingdb.bean.KingTableInfo;
import com.sqy.kingdb.sql.iml.ITableSql;

import java.lang.reflect.Field;

public class CreateTableSql implements ITableSql {
    private KingTableInfo mKingTableInfo;

    @Override
    public<T> String createSql(Class<T> entity) {
        StringBuilder sqlBuilder = new StringBuilder();

        String tableName = entity.getAnnotation(KingTable.class) == null ?
                entity.getSimpleName() :
                entity.getAnnotation(KingTable.class).value();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS ")
                .append(tableName)
                .append(" (");

        mKingTableInfo = new KingTableInfo();
        mKingTableInfo.setName(tableName);
        mKingTableInfo.getFields().clear();
        Field[] fields = entity.getDeclaredFields();

        boolean isFirst = true;
        for(Field field : fields){
            field.setAccessible(true);

            String dataType = getDbDataType(field.getType().getSimpleName());

            KingField kingField = field.getAnnotation(KingField.class);

            String fieldName = kingField == null ?
                    field.getName() : kingField.value();

            sqlBuilder.append(isFirst ? "" : ",").append(fieldName).
                    append(" ").append(dataType);

            StringBuilder constraint = new StringBuilder();

            if(kingField != null){
                constraint.append(kingField.primaryKey() ? " PRIMARY KEY" : "")
                        .append(kingField.notNull() ? " NOT NULL" : "")
                        .append(kingField.unique() ? " UNIQUE" : "");
            }

            sqlBuilder.append(constraint);

            if("".equals(constraint.toString())){
                mKingTableInfo.getFields().add(fieldName+"-"+dataType);
            }else{
                mKingTableInfo.getFields().add(fieldName+"-"+dataType+"-"+constraint.toString());
            }

            isFirst = false;
        }
        sqlBuilder.append(")");

        return sqlBuilder.toString();
    }

    /**
     * 将以下的java数据类型转换成数据库类型
     * Byte
     * Short
     * Integer
     * Long
     * Float
     * Double
     * Boolean
     * String
     * Date
     * Time
     * Timestamp
     * List
     *
     * @param javaType 输入的java数据类型
     * @return 数据库数据类型
     */
    private String getDbDataType(String javaType){
        String dbType = "";
        switch (javaType){
            case "Byte":
                dbType = "SMALLINT";
                break;
            case "Short":
                dbType = "SMALLINT";
                break;
            case "Integer":
                dbType = "INTEGER";
                break;
            case "Long":
                dbType = "BIGINT";
                break;
            case "Float":
                dbType = "FLOAT";
                break;
            case "Double":
                dbType = "DOUBLE";
                break;
            case "Boolean":
                dbType = "BOOLEAN";
                break;
            case "String":
                dbType = "TEXT";
                break;
            case "Date":
                dbType = "DATE";
                break;
            case "Time":
                dbType = "TIME";
                break;
            case "Timestamp":
                dbType = "TIMESTAMP";
                break;
            case "List":
                dbType = "TEXT";
                break;
        }
        return dbType;
    }

    public KingTableInfo getKingTableInfo() {
        return mKingTableInfo;
    }
}
