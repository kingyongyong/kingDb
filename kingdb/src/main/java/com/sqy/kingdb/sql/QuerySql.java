package com.sqy.kingdb.sql;

import android.text.TextUtils;

import com.sqy.kingdb.annotion.KingTable;
import com.sqy.kingdb.bean.QueryCondition;
import com.sqy.kingdb.sql.iml.IQuerySql;

public class QuerySql implements IQuerySql {
    @Override
    public <T> String querySql(Class<T> clazz, QueryCondition condition) {
        StringBuilder sqlBuilder = new StringBuilder();

        ConditionSql conditionSql = new ConditionSql();

        sqlBuilder.append("SELECT * FROM ")
                .append(clazz.getAnnotation(KingTable.class) == null ?
                        clazz.getSimpleName() :
                        clazz.getAnnotation(KingTable.class).value());

        String conditionString = conditionSql.conditionSql(condition);
        if(conditionString != null && !"".equals(conditionString)){
            sqlBuilder.append(" WHERE ").append(conditionString);
        }

        return sqlBuilder.toString();
    }
}
