package com.sqy.kingdb.sql;

import com.sqy.kingdb.annotion.KingTable;
import com.sqy.kingdb.bean.QueryCondition;
import com.sqy.kingdb.sql.iml.IDeleteSql;

public class DeleteSql implements IDeleteSql {
    @Override
    public <T> String deleteSql(Class<T> clazz, QueryCondition condition) {
        StringBuilder sqlBuilder = new StringBuilder();

        ConditionSql conditionSql = new ConditionSql();

        sqlBuilder.append("DELETE FROM ")
                .append(clazz.getAnnotation(KingTable.class) == null ?
                        clazz.getSimpleName() :
                        clazz.getAnnotation(KingTable.class).value());

        String conditionString = conditionSql.conditionSql(condition);
        if (conditionString != null && !"".equals(conditionString)) {
            sqlBuilder.append(" WHERE ").append(conditionString);
        }

        return sqlBuilder.toString();
    }
}
