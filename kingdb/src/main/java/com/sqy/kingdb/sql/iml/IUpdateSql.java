package com.sqy.kingdb.sql.iml;

import com.sqy.kingdb.bean.Condition;
import com.sqy.kingdb.bean.QueryCondition;

import java.util.List;

public interface IUpdateSql {
    <T> String updateSql(Class<T> clazz, List<Condition> setValue, QueryCondition condition);
}
