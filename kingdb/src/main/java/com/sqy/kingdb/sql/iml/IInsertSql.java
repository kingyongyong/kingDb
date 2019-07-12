package com.sqy.kingdb.sql.iml;


import java.util.List;

public interface IInsertSql {
    <T> String insertSql(T entity,List<String> dataBaseNames);
}
