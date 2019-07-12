package com.sqy.kingdb.sql.iml;

import com.sqy.kingdb.bean.QueryCondition;

public interface IConditionSql {//删除
    String conditionSql(QueryCondition condition);
}
