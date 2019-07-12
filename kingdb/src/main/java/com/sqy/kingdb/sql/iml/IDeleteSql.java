package com.sqy.kingdb.sql.iml;

import com.sqy.kingdb.bean.QueryCondition;

public interface IDeleteSql {
    <T> String deleteSql(Class<T> clazz, QueryCondition condition);
}
