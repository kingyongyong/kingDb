package com.sqy.kingdb.sql.iml;

public interface ITableSql {
    <T> String createSql(Class<T> entity);
}
