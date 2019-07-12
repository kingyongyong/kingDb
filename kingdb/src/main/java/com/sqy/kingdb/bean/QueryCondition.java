package com.sqy.kingdb.bean;

import com.sqy.kingdb.KingDbManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryCondition {
    private List<Condition> equal = new ArrayList<>();// 等于
    private List<Condition> notEqual = new ArrayList<>();// 不等于
    private List<Condition> lessThan = new ArrayList<>();// 少于
    private List<Condition> lessThanOrEqual = new ArrayList<>();// 少于或等于
    private List<Condition> moreThan = new ArrayList<>();// 多于
    private List<Condition> moreThanOrEqual = new ArrayList<>();// 多于或等于

    private List<Condition> matching = new ArrayList<>();// 包含
    private Map<String,List<Object>> valueContain = new HashMap<>();// 包含
    private List<Condition> notMatching = new ArrayList<>();// 不包行
    private Map<String,List<Object>> valueNotContain = new HashMap<>();// 不包含
    private Map<String,List<Object>> between = new HashMap<>();// 区间

    private List<List<QueryCondition>> or = new ArrayList<>();// 组合or查询
    private List<List<QueryCondition>> and = new ArrayList<>();// 组合and查询

    private Integer limit;//限制
    private Integer skip;//忽略

    private List<String> order = new ArrayList<>();//排序(可以多个字段排序)

    public void apply(KingDbManager.QueryBuilder.ConditionParams params)
    {
        equal=params.equal;
        notEqual=params.notEqual;
        lessThan=params.lessThan;
        lessThanOrEqual=params.lessThanOrEqual;
        moreThan=params.moreThan;
        moreThanOrEqual=params.moreThanOrEqual;
        or=params.or;
        and=params.and;
        between=params.between;
        matching=params.matching;
        valueContain=params.valueContain;
        notMatching=params.notMatching;
        valueNotContain=params.valueNotContain;
        limit=params.limit;
        skip=params.skip;
        order=params.order;
    }

    public List<Condition> getEqual() {
        return equal;
    }

    public List<Condition> getNotEqual() {
        return notEqual;
    }

    public List<Condition> getLessThan() {
        return lessThan;
    }

    public List<Condition> getLessThanOrEqual() {
        return lessThanOrEqual;
    }

    public List<Condition> getMoreThan() {
        return moreThan;
    }

    public List<Condition> getMoreThanOrEqual() {
        return moreThanOrEqual;
    }

    public List<Condition> getMatching() {
        return matching;
    }

    public Map<String, List<Object>> getValueContain() {
        return valueContain;
    }

    public List<Condition> getNotMatching() {
        return notMatching;
    }

    public Map<String, List<Object>> getValueNotContain() {
        return valueNotContain;
    }

    public List<List<QueryCondition>> getOr() {
        return or;
    }

    public List<List<QueryCondition>> getAnd() {
        return and;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getSkip() {
        return skip;
    }

    public List<String> getOrder() {
        return order;
    }

    public Map<String, List<Object>> getBetween() {
        return between;
    }
}
