package com.sqy.kingdb.sql;

import com.sqy.kingdb.annotion.KingTable;
import com.sqy.kingdb.sql.iml.ITableSql;

public class DeleteTableSql implements ITableSql {
    @Override
    public <T> String createSql(Class<T> entity) {
        StringBuilder sqlBuilder = new StringBuilder();

        String tableName = entity.getAnnotation(KingTable.class) == null ?
                entity.getSimpleName() :
                entity.getAnnotation(KingTable.class).value();
        sqlBuilder.append("DROP TABLE ")
                .append(tableName);
        return sqlBuilder.toString();
    }
}
