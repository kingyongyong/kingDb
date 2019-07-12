package com.sqy.kingdb.sql;

import android.text.TextUtils;

import com.sqy.kingdb.bean.Condition;
import com.sqy.kingdb.bean.QueryCondition;
import com.sqy.kingdb.sql.iml.IConditionSql;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class ConditionSql implements IConditionSql {
    @Override
    public String conditionSql(QueryCondition condition) {
        if(condition == null){
            return "";
        }

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(simpleCondition(condition));

        if(condition.getLimit() != null){
            sqlBuilder.append(" LIMIT ").append(condition.getLimit());
        }

        if(condition.getSkip() != null){
            sqlBuilder.append(" OFFSET ").append(condition.getSkip());
        }

        if(condition.getOrder() != null && condition.getOrder().size() > 0){
            sqlBuilder.append(" ORDER BY ");
            Boolean isFirst = true;
            for(String orderCondition : condition.getOrder()){
                if(TextUtils.isEmpty(orderCondition)){
                    continue;
                }
                String tempOrder;
                String order;
                if(orderCondition.startsWith("-")){
                    tempOrder = orderCondition.substring(1,orderCondition.length());
                    order = " DESC ";
                }else{
                    tempOrder = orderCondition;
                    order = " ASC ";
                }
                sqlBuilder.append(isFirst ? "" : ",").
                        append(tempOrder).
                        append(order);
                isFirst = false;
            }
        }


        return sqlBuilder.toString();
    }


    private Object getConditionValue(Object obj){
        if(obj instanceof String){
            return "'" + obj + "'";
        }else if(obj instanceof Boolean){
            return (Boolean)obj ? "1" : "0";
        }else if(obj instanceof Date){
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
            return "'" + dataFormat.format((Date)obj) + "'";
        }else if(obj instanceof Time){
            SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss");
            return "'" + dataFormat.format((Time)obj) + "'";
        }else if(obj instanceof Timestamp){
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return "'" + dataFormat.format((Timestamp)obj) + "'";
        }else{
            return obj;
        }
    }

    private StringBuilder simpleCondition(QueryCondition condition){
        StringBuilder sqlBuilder = new StringBuilder();
        Boolean isFirst = true;
        Boolean isNeedAndOr = false;


        if(condition.getEqual() != null){
            for(Condition conditionItem : condition.getEqual()){
                sqlBuilder.append(isNeedAndOr ? " AND " : "").append(conditionItem.getKey()).append(" = ").append(getConditionValue(conditionItem.getValue()));
                isNeedAndOr = true;
            }
        }

        if(condition.getNotEqual() != null){
            for(Condition conditionItem : condition.getNotEqual()){
                sqlBuilder.append(isNeedAndOr ? " AND " : "").append(conditionItem.getKey()).append(" != ").append(getConditionValue(conditionItem.getValue()));
                isNeedAndOr = true;
            }
        }

        if(condition.getLessThan() != null){
            for(Condition conditionItem : condition.getLessThan()){
                sqlBuilder.append(isNeedAndOr ? " AND " : "").append(conditionItem.getKey()).append(" < ").append(getConditionValue(conditionItem.getValue()));
                isNeedAndOr = true;
            }
        }

        if(condition.getLessThanOrEqual() != null){
            for(Condition conditionItem : condition.getLessThanOrEqual()){
                sqlBuilder.append(isNeedAndOr ? " AND " : "").append(conditionItem.getKey()).append(" <= ").append(getConditionValue(conditionItem.getValue()));
                isNeedAndOr = true;
            }
        }

        if(condition.getMoreThan() != null){
            for(Condition conditionItem : condition.getMoreThan()){
                sqlBuilder.append(isNeedAndOr ? " AND " : "").append(conditionItem.getKey()).append(" > ").append(getConditionValue(conditionItem.getValue()));
                isNeedAndOr = true;
            }
        }

        if(condition.getMoreThanOrEqual() != null){
            for(Condition conditionItem : condition.getMoreThanOrEqual()){
                sqlBuilder.append(isNeedAndOr ? " AND " : "").append(conditionItem.getKey()).append(" >= ").append(getConditionValue(conditionItem.getValue()));
                isNeedAndOr = true;
            }
        }

        if(condition.getMatching() != null){
            for(Condition conditionItem : condition.getMatching()){
                sqlBuilder.append(isNeedAndOr ? " AND " : "").append(conditionItem.getKey()).append(" LIKE ").append(getConditionValue(conditionItem.getValue()));
                isNeedAndOr = true;
            }
        }

        if(condition.getNotMatching() != null){
            for(Condition conditionItem : condition.getNotMatching()){
                sqlBuilder.append(isNeedAndOr ? " AND " : "").append(conditionItem.getKey()).append(" NOT LIKE ").append(getConditionValue(conditionItem.getValue()));
                isNeedAndOr = true;
            }
        }

        if(condition.getValueContain() != null && condition.getValueContain().keySet().size() > 0){
            for(String key : condition.getValueContain().keySet()){
                List<Object> values = condition.getValueContain().get(key);
                if(values != null && values.size() > 0){
                    sqlBuilder.append(isNeedAndOr ? " AND " : "").append(key).append(" IN ( ");
                    isNeedAndOr = true;
                    for(Object obj : values){
                        sqlBuilder.append(obj).append(" ");
                    }
                    sqlBuilder.append(")");
                }
            }
        }

        if(condition.getValueNotContain() != null && condition.getValueNotContain().keySet().size() > 0){
            for(String key : condition.getValueNotContain().keySet()){
                List<Object> values = condition.getValueNotContain().get(key);
                if(values != null && values.size() > 0){
                    sqlBuilder.append(isNeedAndOr ? " AND " : "").append(key).append(" IN ( ");
                    isNeedAndOr = true;
                    for(Object obj : values){
                        sqlBuilder.append(obj).append(" ");
                    }
                    sqlBuilder.append(")");
                }
            }
        }

        if(condition.getBetween() != null && condition.getBetween().keySet().size() > 0){
            for(String key : condition.getBetween().keySet()){
                List<Object> values = condition.getBetween().get(key);
                if(values != null && values.size() == 2){
                    sqlBuilder.append(isNeedAndOr ? " AND " : "")
                            .append(key)
                            .append(" BETWEEN ")
                            .append(values.get(0))
                            .append(" AND ")
                            .append(values.get(1));
                    isNeedAndOr = true;
                }
            }
        }

        if(condition.getAnd() != null && condition.getAnd().size() > 0){
            sqlBuilder.append(isNeedAndOr ? " AND " : "");
            isNeedAndOr = true;
            for(List<QueryCondition> conditions : condition.getAnd()){
                if(conditions != null && conditions.size() > 0){
                    if(isFirst){
                        isFirst = false;
                        sqlBuilder.append("(");
                    }
                    Boolean isConditionFirst = true;
                    for(QueryCondition conditionItem : conditions){
                        if(isConditionFirst){
                            sqlBuilder.append("(").append(simpleCondition(conditionItem).append(")"));
                            isConditionFirst = false;
                        }else{
                            sqlBuilder.append(" AND (").append(simpleCondition(conditionItem).append(")"));
                        }

                    }
                }
            }
            if(!isFirst){
                sqlBuilder.append(")");
                isFirst = false;
            }
        }


        if(condition.getOr() != null && condition.getOr().size() > 0){
            sqlBuilder.append(isNeedAndOr ? " AND " : "");
            for(List<QueryCondition> conditions : condition.getOr()){
                if(conditions != null && conditions.size() > 0){
                    if(isFirst){
                        isFirst = false;
                        sqlBuilder.append("(");
                    }
                    Boolean isConditionFirst = true;
                    for(QueryCondition conditionItem : conditions){
                        if(isConditionFirst){
                            sqlBuilder.append("(").append(simpleCondition(conditionItem).append(")"));
                            isConditionFirst = false;
                        }else{
                            sqlBuilder.append(" OR (").append(simpleCondition(conditionItem).append(")"));
                        }

                    }
                }
            }
            if(!isFirst){
                sqlBuilder.append(")");
            }
        }

        return sqlBuilder;
    }


}
