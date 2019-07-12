package com.sqy.kingdb.test;

import com.sqy.kingdb.KingDbManager;
import com.sqy.kingdb.bean.Condition;
import com.sqy.kingdb.sql.CreateTableSql;
import com.sqy.kingdb.sql.InsertSqlSql;
import com.sqy.kingdb.sql.QuerySql;
import com.sqy.kingdb.sql.UpdateSql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class Main {
    public static void main(String[] args) {

        CreateTableSql carEntityCreateSql = new CreateTableSql();
        System.out.println(carEntityCreateSql.createSql(UserInfo.class));

        /*UpdateSql updateSql = new UpdateSql();

        List<Condition> conditions = new ArrayList<>();
        conditions.add(new Condition("age",13));
        conditions.add(new Condition("rrr","afsdgag"));

        System.out.println(updateSql.updateSql(UserInfo.class,conditions,
                new KingDbManager.QueryBuilder().addMoreThan("id","13241234")
                        .buildQueryCondition()));*/









        /*QuerySql querySql = new QuerySql();

        System.out.println(querySql.querySql(UserInfo.class,
                new KingDbManager.QueryBuilder()
                        .addEqual("物种","人类")
                        .addOr(new KingDbManager.QueryBuilder()
                                .addBetween("身高",160,170)
                                .addEqual("性别","男")
                                .buildQueryCondition(),
                                new KingDbManager.QueryBuilder()
                                        .addEqual("性别","女")
                                        .buildQueryCondition())
                        .buildQueryCondition()));

        System.out.println(querySql.querySql(UserInfo.class,
                new KingDbManager.QueryBuilder()
                        .buildQueryCondition()));*/

        //between
        /*System.out.println(querySql.querySql(UserInfo.class,
                new KingDbManager.QueryBuilder()
                        .addEqual("addEqual",23)
                        .addNotEqual("addNotEqual","sdfiduh")
                        .addMoreThan("addMoreThan",23)
                        .addOr(new KingDbManager.QueryBuilder()
                                        .addEqual("addEqual",23)
                                        .addNotEqual("addNotEqual","sdfiduh")
                                        .addMoreThan("addMoreThan",23)
                                        .addMoreThanOrEqual("addMoreThanOrEqual",99)
                                        .addLessThan("addLessThan",8)
                                        .addLessThanOrEqual("addLessThanOrEqual",43)
                                        .addMatching("addMatching","uu%")
                                        .addNotMatching("addNotMatching","_88%")
                                        .addValueContain("addValueContain1",23,43,55)
                                        .addValueContain("addValueContain2","sdf","sdf",55)
                                        .addValueContain("addValueContain3","sdf")
                                        .addValueNotContain("addValueNotContain1",23,43,55)
                                        .addValueNotContain("addValueNotContain2","sdf","sdf",55)
                                        .addValueNotContain("addValueNotContain3","sdf")
                                        .buildQueryCondition(),
                                new KingDbManager.QueryBuilder()
                                        .addEqual("addEqual",23)
                                        .addNotEqual("addNotEqual","sdfiduh")
                                        .addMoreThan("addMoreThan",23)
                                        .addMoreThanOrEqual("addMoreThanOrEqual",99)
                                        .addLessThan("addLessThan",8)
                                        .addLessThanOrEqual("addLessThanOrEqual",43)
                                        .addMatching("addMatching","uu%")
                                        .addNotMatching("addNotMatching","_88%")
                                        .addValueContain("addValueContain1",23,43,55)
                                        .addValueContain("addValueContain2","sdf","sdf",55)
                                        .addValueContain("addValueContain3","sdf")
                                        .addValueNotContain("addValueNotContain1",23,43,55)
                                        .addValueNotContain("addValueNotContain2","sdf","sdf",55)
                                        .addValueNotContain("addValueNotContain3","sdf")
                                        .buildQueryCondition())
                        .addMoreThanOrEqual("addMoreThanOrEqual",99)
                        .addLessThan("addLessThan",8)
                        .addLessThanOrEqual("addLessThanOrEqual",43)
                        .addMatching("addMatching","uu%")
                        .addNotMatching("addNotMatching","_88%")
                        .addValueContain("addValueContain1",23,43,55)
                        .addValueContain("addValueContain2","sdf","sdf",55)
                        .addValueContain("addValueContain3","sdf")
                        .addValueNotContain("addValueNotContain1",23,43,55)
                        .addValueNotContain("addValueNotContain2","sdf","sdf",55)
                        .addValueNotContain("addValueNotContain3","sdf")
                        .buildQueryCondition()));*/











































    }

    public static void init(){
        UserInfo userinfo = new UserInfo();
        userinfo.setName("4444");
        userinfo.setAge(23);
        userinfo.setMoney(66.8f);
        InsertSqlSql insertSql = new InsertSqlSql();
        List<String> dataBaseNames = new ArrayList<>();
        dataBaseNames.add("f_name");
        dataBaseNames.add("f_age");
        dataBaseNames.add("f_money");

        CarEntity carEntity = new CarEntity();
        InsertSqlSql carInsertSql = new InsertSqlSql();
        List<String> carDataBaseNames = new ArrayList<>();
        carDataBaseNames.add("aa");
        carDataBaseNames.add("aaa");
        carDataBaseNames.add("bb");
        carDataBaseNames.add("cc");
        carDataBaseNames.add("dd");
        carDataBaseNames.add("ee");
        carDataBaseNames.add("ff");
        carDataBaseNames.add("gg");
        carDataBaseNames.add("hh");
        carDataBaseNames.add("aas");
        carDataBaseNames.add("bbs");
        carDataBaseNames.add("ccs");
        carDataBaseNames.add("dds");
        carDataBaseNames.add("ees");
        carDataBaseNames.add("ffs");
        carDataBaseNames.add("ggs");
        carDataBaseNames.add("hhs");
        carDataBaseNames.add("date");
        carDataBaseNames.add("time");
        carDataBaseNames.add("timestamp");

        //System.out.println(carInsertSql.insertSql(carEntity,carDataBaseNames));

        CreateTableSql carEntityCreateSql = new CreateTableSql();
        System.out.println(carEntityCreateSql.createSql(CarEntity.class));

        //  java      sqLite
        //  Byte     smallint
        //  Short     smallint
        //  Integer     integer
        //    Long     bigint
        //  Float     FLOAT
        //  Double     DOUBLE
        //  Boolean     VARCHAR(1)
        //  String     text
    }
}
