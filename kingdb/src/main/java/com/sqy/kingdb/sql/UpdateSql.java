package com.sqy.kingdb.sql;

import com.sqy.kingdb.annotion.KingTable;
import com.sqy.kingdb.bean.Condition;
import com.sqy.kingdb.bean.QueryCondition;
import com.sqy.kingdb.sql.iml.IUpdateSql;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class UpdateSql implements IUpdateSql {
    @Override
    public <T> String updateSql(Class<T> clazz, List<Condition> setValue, QueryCondition condition) {
        if(setValue == null || setValue.size() == 0){
            return "";
        }

        StringBuilder sqlBuilder = new StringBuilder();

        ConditionSql conditionSql = new ConditionSql();

        sqlBuilder.append("UPDATE ")
                .append(clazz.getAnnotation(KingTable.class) == null ?
                        clazz.getSimpleName() :
                        clazz.getAnnotation(KingTable.class).value()).
                append(" SET ");

        Boolean isFirstSet = true;
        for(Condition setCondition : setValue){
            sqlBuilder.append(isFirstSet ? "" : ",").append(setCondition.getKey()).append(" = ").append(getConditionValue(setCondition.getValue()));
            isFirstSet = false;
        }

        String conditionString = conditionSql.conditionSql(condition);
        if(conditionString != null && !"".equals(conditionString)){
            sqlBuilder.append(" WHERE ").append(conditionString);
        }

        return sqlBuilder.toString();

    }

    private Object getConditionValue(Object obj){
        if(obj instanceof String){
            return "'" + obj + "'";
        }else if(obj instanceof Boolean){
            return (Boolean)obj ? "1" : "0";
        }else if(obj instanceof Date){
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
            return "'" + dataFormat.format((Date)obj) + "'";
        }else if(obj instanceof Time){
            SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss");
            return "'" + dataFormat.format((Time)obj) + "'";
        }else if(obj instanceof Timestamp){
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "'" + dataFormat.format((Timestamp)obj) + "'";
        }else{
            return obj;
        }
    }
}
