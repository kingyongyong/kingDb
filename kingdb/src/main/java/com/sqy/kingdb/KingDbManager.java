package com.sqy.kingdb;


import com.sqy.kingdb.bean.Condition;
import com.sqy.kingdb.bean.QueryCondition;
import com.sqy.kingdb.db.DeleteManager;
import com.sqy.kingdb.db.InsertManager;
import com.sqy.kingdb.db.QueryManager;
import com.sqy.kingdb.db.UpdateManager;
import com.sqy.kingdb.sql.CreateTableSql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KingDbManager{

    public static InsertBuilder newInsertBuilder(){
        return new InsertBuilder();
    }

    public static class InsertBuilder{
        public <T> String insert(T entity){
            InsertManager insertManager = new InsertManager();
            return insertManager.insert(entity);
        }
    }

    public static UpdateBuilder newUpdateBuilder(){
        return new UpdateBuilder();
    }

    public static class UpdateBuilder{
        private UpdateManager updateManager = new UpdateManager();
        private List<Condition> setValues = new ArrayList<>();
        private QueryCondition queryCondition;
        public <T> String update(T entity){
            return updateManager.update(entity);
        }
        public UpdateBuilder updateValue(String key,Object value){
            setValues.add(new Condition(key,value));
            return this;
        }
        public UpdateBuilder setCondition(QueryCondition queryCondition){
            this.queryCondition = queryCondition;
            return this;
        }
        public <T> String update(Class<T> clazz){
            return updateManager.update(clazz,setValues,queryCondition);
        }
    }

    public static DeleteBuilder newDeleteBuilder(){
        return new DeleteBuilder();
    }

    public static class DeleteBuilder{
        private DeleteManager deleteManager = new DeleteManager();
        private QueryCondition queryCondition;
        public <T> String delete(T entity){
            return deleteManager.delete(entity);
        }

        public DeleteBuilder setCondition(QueryCondition queryCondition){
            this.queryCondition = queryCondition;
            return this;
        }

        public <T> String delete(Class<T> clazz){
            return deleteManager.delete(clazz,queryCondition);
        }
    }



    public static QueryBuilder newQueryBuilder(){
        return new QueryBuilder();
    }

    public static class QueryBuilder{
        private ConditionParams mConditionParams;
        private QueryCondition queryCondition;

        public QueryBuilder() {
            this.mConditionParams = new ConditionParams();
        }

        public QueryBuilder addEqual(String key,Object value){
            mConditionParams.equal.add(new Condition(key,value));
            return this;
        }

        public QueryBuilder addNotEqual(String key,Object value){
            mConditionParams.notEqual.add(new Condition(key,value));
            return this;
        }

        public QueryBuilder addLessThan(String key,Object value){
            mConditionParams.lessThan.add(new Condition(key,value));
            return this;
        }

        public QueryBuilder addLessThanOrEqual(String key,Object value){
            mConditionParams.lessThanOrEqual.add(new Condition(key,value));
            return this;
        }

        public QueryBuilder addMoreThan(String key,Object value){
            mConditionParams.moreThan.add(new Condition(key,value));
            return this;
        }

        public QueryBuilder addMoreThanOrEqual(String key,Object value){
            mConditionParams.moreThanOrEqual.add(new Condition(key,value));
            return this;
        }

        public QueryBuilder addOr(QueryCondition... conditions){
            mConditionParams.or.add(Arrays.asList(conditions));
            return this;
        }

        public QueryBuilder addAnd(QueryCondition... conditions){
            mConditionParams.and.add(Arrays.asList(conditions));
            return this;
        }

        public QueryBuilder addMatching(String key,Object value){
            mConditionParams.matching.add(new Condition(key,value));
            return this;
        }

        public QueryBuilder addNotMatching(String key,Object value){
            mConditionParams.notMatching.add(new Condition(key,value));
            return this;
        }

        public QueryBuilder addValueContain(String key,Object... value){
            mConditionParams.valueContain.put(key,Arrays.asList(value));
            return this;
        }

        public QueryBuilder addValueNotContain(String key,Object... value){
            mConditionParams.valueNotContain.put(key,Arrays.asList(value));
            return this;
        }

        public QueryBuilder addBetween(String key,Object start,Object end){
            mConditionParams.between.put(key,Arrays.asList(start,end));
            return this;
        }

        public QueryBuilder addLimit(int limit){
            mConditionParams.limit = limit;
            return this;
        }

        public QueryBuilder addSkip(int limit){
            mConditionParams.skip = limit;
            return this;
        }


        public QueryBuilder orderBy(String... conditions){
            mConditionParams.order.addAll(Arrays.asList(conditions));
            return this;
        }

        public QueryBuilder clearCondition(){
            mConditionParams = new ConditionParams();
            return this;
        }

        public QueryCondition buildQueryCondition(){
            queryCondition = new QueryCondition();
            queryCondition.apply(mConditionParams);
            return queryCondition;
        }

        public<T> List<T> query(Class<T> clazz){
            queryCondition = new QueryCondition();
            queryCondition.apply(mConditionParams);
            QueryManager queryManager = new QueryManager();
            return queryManager.query(clazz,queryCondition);
        }

        public class ConditionParams{
            public List<Condition> equal = new ArrayList<>();// 等于
            public List<Condition> notEqual = new ArrayList<>();// 不等于
            public List<Condition> lessThan = new ArrayList<>();// 少于
            public List<Condition> lessThanOrEqual = new ArrayList<>();// 少于或等于
            public List<Condition> moreThan = new ArrayList<>();// 多于
            public List<Condition> moreThanOrEqual = new ArrayList<>();// 多于或等于


            public List<List<QueryCondition>> or = new ArrayList<>();// 组合or查询
            public List<List<QueryCondition>> and = new ArrayList<>();// 组合and查询

            public List<Condition> matching = new ArrayList<>();//
            public Map<String,List<Object>> valueContain = new HashMap<>();//
            public List<Condition> notMatching = new ArrayList<>();//
            public Map<String,List<Object>> valueNotContain = new HashMap<>();//
            public Map<String,List<Object>> between = new HashMap<>();// 区间

            public Integer limit;//限制
            public Integer skip;//忽略

            public List<String> order = new ArrayList<>();//排序(可以多个字段排序)
        }
    }

}
