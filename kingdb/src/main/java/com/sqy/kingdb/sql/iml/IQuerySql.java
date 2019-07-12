package com.sqy.kingdb.sql.iml;

import com.sqy.kingdb.bean.QueryCondition;

public interface IQuerySql {
    <T> String querySql(Class<T> clazz, QueryCondition condition);
}
