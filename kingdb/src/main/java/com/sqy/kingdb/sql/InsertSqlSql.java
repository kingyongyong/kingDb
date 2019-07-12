package com.sqy.kingdb.sql;

import com.sqy.kingdb.annotion.KingField;
import com.sqy.kingdb.annotion.KingTable;
import com.sqy.kingdb.sql.iml.IInsertSql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class InsertSqlSql implements IInsertSql {

    @Override
    public <T> String insertSql(T entity,List<String> dataBaseNames) {
        if(dataBaseNames == null){
            return "";
        }

        StringBuilder sqlBuilder = new StringBuilder();

        String mTableName = entity.getClass().getAnnotation(KingTable.class) == null ?
                entity.getClass().getSimpleName() :
                entity.getClass().getAnnotation(KingTable.class).value();

        sqlBuilder.append("INSERT INTO ")
                .append(mTableName)
                .append(" ( ");

        Field[] fields = entity.getClass().getDeclaredFields();
        StringBuilder valueBuilder = new StringBuilder();
        Boolean isHasValue = false;//是否具有值
        Boolean isFirst = true;
        for(Field field : fields){
            field.setAccessible(true);

            String fieldName = field.getAnnotation(KingField.class) == null ?
                    field.getName() : field.getAnnotation(KingField.class).value();

            if(dataBaseNames.contains(fieldName)){
                try {
                    if(field.get(entity) == null){
                        continue;
                    }else{
                        String fieldValue = field.get(entity).toString();

                        if("Boolean".equals(field.getType().getSimpleName())){
                            valueBuilder.append(isFirst ? "" : ",").append("true".equals(fieldValue) ? "1" : "0");
                        }else{
                            List<String> textType = new ArrayList<>();
                            textType.add("String");
                            textType.add("Date");
                            textType.add("Time");
                            textType.add("Timestamp");
                            textType.add("List");
                            if(textType.contains(field.getType().getSimpleName())){
                                valueBuilder.append(isFirst ? "" : ",")
                                        .append("\"").append(fieldValue).append("\"");
                            }else{
                                valueBuilder.append(isFirst ? "" : ",").append(fieldValue);
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return "";
                }
                sqlBuilder.append(isFirst ?
                        fieldName : " , " + fieldName);
                isHasValue = true;
                isFirst = false;
            }
        }
        sqlBuilder.append(" ) values ( ").append(valueBuilder).append(" )");

        return isHasValue ? sqlBuilder.toString() : "";
    }

}
