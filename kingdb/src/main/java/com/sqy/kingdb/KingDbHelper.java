package com.sqy.kingdb;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.sqy.kingdb.annotion.KingTable;
import com.sqy.kingdb.bean.KingDatabases;
import com.sqy.kingdb.bean.KingTableInfo;
import com.sqy.kingdb.enums.SavePathType;
import com.sqy.kingdb.exception.KingDbException;
import com.sqy.kingdb.sql.CreateTableSql;
import com.sqy.kingdb.sql.DeleteTableSql;
import com.sqy.kingdb.util.KingDbUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KingDbHelper {

    private Context mContext;

    private KingDbHelper() {}

    private SQLiteDatabase mDatabase;
    private static KingDbHelper mInstance;

    private SavePathType mSavePathType = SavePathType.INNER_CARD;
    private String databasePath = "/kingdb";
    private String packageName = "";
    private String databaseName = "kingdb.db";
    private Integer version = 1;

    public SQLiteDatabase getDatabase() {
        if (mDatabase == null) {
            synchronized (KingDbHelper.class) {
                if (mDatabase == null) {
                    String finalDbPath = "";
                    File dbFile = null;
                    switch (mSavePathType){
                        case EXTERNAL_CARD:
                            finalDbPath = mSavePathType.getBasePath() + databasePath ;
                            break;
                        case INNER_CARD:
                            finalDbPath = mSavePathType.getBasePath() + "/data/" + packageName + "/databases";
                            break;
                    }

                    dbFile = new File(finalDbPath);
                    if(!dbFile.exists()){
                        dbFile.mkdirs();
                    }
                    finalDbPath += ("/" + databaseName);
                    mDatabase = SQLiteDatabase.openOrCreateDatabase(finalDbPath, null);
                }
            }
        }
        return mDatabase;
    }

    public KingDbHelper setSavePathType(SavePathType mSavePathType) {
        this.mSavePathType = mSavePathType;
        return this;
    }



    public KingDbHelper setDatabasePath(String... databasePath) {
        StringBuilder builder = new StringBuilder();
        for(String name : databasePath){
            builder.append("/").append(name);
        }
        this.databasePath = builder.toString();
        return this;
    }

    private void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    private void setContext(Context context) {
        this.mContext = context;
    }

    public KingDbHelper setDatabaseName(String databaseName) {
        if(databaseName == null || "".equals(databaseName)){
            return this;
        }
        if(!databaseName.endsWith(".db")){
            databaseName += ".db";
        }
        this.databaseName = databaseName;
        return this;
    }

    public KingDbHelper setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public static KingDbHelper withThis(Context context) {
        KingDbHelper.getInstance().setContext(context);
        KingDbHelper.getInstance().setPackageName(context.getPackageName());
        KingDbHelper.getInstance().setDatabasePath("/" + context.getPackageName() + "/databases");
        return KingDbHelper.getInstance();
    }

    public static KingDbHelper getInstance() {
        if (mInstance == null) {
            synchronized (KingDbHelper.class) {
                if (mInstance == null) {
                    mInstance = new KingDbHelper();
                }
            }
        }
        return mInstance;
    }

    /**
     * 1、如果版本号表里没有数据，则表示是第一次，则直接把配置文件里的表更新到系统数据库中去
     *
     * 2、获取版本号，查看当前是否是最新版本，如果是则直接完成
     * 3、如果不是最新版本，刷新系统版本号，然后更新表
     *
     * 更新：
     *  把配置文件里的表 和 系统数据库中的表 进行对比，然后完成
     *  新增表 ： 系统数据库中不存在A表，配置文件中存在A表，则表示该表要新增A表
     *  删除表 ： 系统数据库中存在A表，配置文件中不存在A表，则表示该表要删除A表
     *  更改表 ：
     *          （1）新增字段
     *          （2）更改字段
     *          （3）删除字段
     */
    public void complete() {
        CreateTableSql createTableSql = new CreateTableSql();
        KingDbHelper.getInstance().getDatabase().execSQL(createTableSql.createSql(KingDatabases.class));
        KingDbHelper.getInstance().getDatabase().execSQL(createTableSql.createSql(KingTableInfo.class));

        //1、如果版本号表里没有数据，则表示是第一次，则直接把配置文件里的表更新到系统数据库中去
        List<KingDatabases> databases = KingDbManager.newQueryBuilder().query(KingDatabases.class);

        //拿到注册过的表
        List<String> tablePackageNameList = new ArrayList<>();
        try {
            ApplicationInfo appInfo = mContext.getPackageManager()
                    .getApplicationInfo(mContext.getPackageName(),
                            PackageManager.GET_META_DATA);
            String dbBeanInfo = appInfo.metaData.getString("king_db");
            dbBeanInfo = KingDbUtil.replaceAllBlank(dbBeanInfo);
            if(!TextUtils.isEmpty(dbBeanInfo)){
                if(dbBeanInfo.contains(",")){
                    tablePackageNameList.addAll(Arrays.asList(dbBeanInfo.split(",")));
                }else{
                    tablePackageNameList.add(dbBeanInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(databases.size() == 0) {
            //第一次初始化数据库
            KingDatabases kingDatabases = new KingDatabases();
            kingDatabases.setDatabaseName(databaseName);
            kingDatabases.setVersion(version);
            KingDbManager.newInsertBuilder().insert(kingDatabases);
        }

        List<KingTableInfo> tableInfos = KingDbManager.newQueryBuilder().query(KingTableInfo.class);

        if(tableInfos.size() == 0){
            //第一次初始化数据库

            //拿到manifest里注册的表，然后新建
            for(String packageName : tablePackageNameList){
                Class clazz = null;
                try {
                    clazz = Class.forName(packageName);
                    //创建新表
                    KingDbHelper.getInstance().getDatabase().execSQL(createTableSql.createSql(clazz));
                    //把创表信息放入系统表中
                    KingTableInfo kingTableInfo = createTableSql.getKingTableInfo();
                    kingTableInfo.setPackageName(packageName);
                    KingDbManager.newInsertBuilder().insert(kingTableInfo);
                } catch (Exception e) {
                    // 如果发生异常，返回错误信息，同时清空表,所以下次重启APP还会走这里
                    KingDbManager.newDeleteBuilder().delete(KingDatabases.class);
                    throw new KingDbException("king db error : 表" + packageName + "不规范，详细错误信息" + e.getMessage());
                }
            }
        }else{
            //如果不是第一次初始化数据库，则比较版本号
            KingDatabases kingDatabases = databases.get(0);
            /*
             *  只有这种情况下才进行数据库更新：
             *  把配置文件里的表 和 系统数据库中的表 进行对比，然后完成
             *  新增表 ： 系统数据库中不存在A表，配置文件中存在A表，则表示该表要新增A表
             *  删除表 ： 系统数据库中存在A表，配置文件中不存在A表，则表示该表要删除A表
             *  更改表 ：
             *          （1）新增字段
             *          （2）更改字段
             *          （3）删除字段
             */
            if(version > kingDatabases.getVersion()){
                //首先获取上个版本（数据库中）的表信息
                List<KingTableInfo> oldTables = KingDbManager.newQueryBuilder().query(KingTableInfo.class);
                //获取manifest你们注册的表信息
                List<KingTableInfo> newTables = new ArrayList<>();
                for(String packageName : tablePackageNameList){
                    Class clazz = null;
                    try {
                        clazz = Class.forName(packageName);
                        createTableSql.createSql(clazz);
                        //把创表信息放入系统表中
                        KingTableInfo kingTableInfo = createTableSql.getKingTableInfo();
                        kingTableInfo.setPackageName(packageName);
                        newTables.add(kingTableInfo);
                    } catch (ClassNotFoundException e) {
                        throw new KingDbException("king db error : 表" + packageName + "不规范，详细错误信息" + e.getMessage());
                    }
                }

                //用于比较的老表的集合
                List<KingTableInfo> compareOldTableList = new ArrayList<>();
                //用于比较的新表的集合
                List<KingTableInfo> compareNewTableList = new ArrayList<>();

                for(KingTableInfo oldT : oldTables){
                    for(KingTableInfo newT : newTables){
                        if(oldT.getName().equals(newT.getName())){
                            compareOldTableList.add(oldT);
                            compareNewTableList.add(newT);
                            newTables.remove(newT);
                            break;
                        }
                    }
                }

                //1、新增表 (此时 newTables 是新增表的集合)
                newTables.removeAll(compareNewTableList);
                for(KingTableInfo addTable : newTables){
                    Class clazz = null;
                    try {
                        clazz = Class.forName(addTable.getPackageName());
                        KingDbHelper.getInstance().getDatabase().execSQL(createTableSql.createSql(clazz));
                        KingDbManager.newInsertBuilder().insert(addTable);
                    } catch (Exception e) {
                        throw new KingDbException("king db error : 表升级时，新增表" + addTable.getPackageName() + "发生异常，详细错误信息" + e.getMessage());
                    }
                }

                //2、删除表 (此时 oldTables 删除表的集合)
                oldTables.removeAll(compareOldTableList);
                for(KingTableInfo deleteTable : oldTables){
                    Class clazz = null;
                    try {
                        clazz = Class.forName(deleteTable.getPackageName());
                        DeleteTableSql deleteTableSql = new DeleteTableSql();
                        KingDbHelper.getInstance().getDatabase().execSQL(deleteTableSql.createSql(clazz));
                        KingDbManager.newDeleteBuilder().setCondition(
                                KingDbManager.newQueryBuilder().addEqual("table_name",deleteTable.getName()).buildQueryCondition()
                        ).delete(KingTableInfo.class);
                    } catch (Exception e) {
                        throw new KingDbException("king db error : 表升级时，删除表" + deleteTable.getPackageName() + "发生异常，详细错误信息" + e.getMessage());
                    }
                }


                /* 3、更改表 ：
                 *    （1）新增字段
                 *    （2）更改字段
                 *    （3）删除字段
                 */

                for(KingTableInfo oldTable : compareOldTableList){
                    for(KingTableInfo newTable : compareNewTableList){
                        //找到相对应的一张表
                        if(oldTable.getName().equals(newTable.getName())){
                            if(oldTable.getFields().size() == newTable.getFields().size()){
                                Boolean isEqual = true;//用来判断是否每一个字段都相等
                                for(int i = 0;i< oldTable.getFields().size();i ++){
                                    if(!KingDbUtil.replaceAllBlank(oldTable.getFields().get(i)).equals(KingDbUtil.replaceAllBlank(newTable.getFields().get(i)))){
                                        isEqual = false;
                                        break;
                                    }
                                }
                                /*
                                 * 如果字段全部都相等，则说明该表没变化，不需要升级
                                 * 反之则说明需要升级
                                 */
                                if(!isEqual){
                                    updateSqLite(oldTable,newTable);
                                }
                            }else if(oldTable.getFields().size() < newTable.getFields().size()){
                                /*
                                 * 还有一种情况，就是原有字段没有变化，只是新增了字段
                                 * 这种情况就是旧表的每一个字段在新表都能找到相等的字段
                                 */
                                Boolean isAllEqual = true;//用来判断时候所有字段都相等
                                for(String oldField : oldTable.getFields()){
                                    Boolean isEqual = false;
                                    for(String newField : newTable.getFields()){
                                        if(KingDbUtil.replaceAllBlank(oldField).equals(KingDbUtil.replaceAllBlank(newField))){
                                            isEqual = true;
                                        }
                                    }
                                    if(!isEqual){
                                        //说明存在一个字段在新表找不到相同的字段
                                        isAllEqual = false;
                                        break;
                                    }
                                }
                                if(isAllEqual){
                                    //旧表如果所有字段在新表都能找到相同的字段，那么就说明该表存是新增字段
                                    updateSqLiteAddCol(oldTable,newTable);
                                }else{
                                    //旧表如果存在一个字段在新表找不到相同的字段，那么就说明该表存在字段更新
                                    updateSqLite(oldTable,newTable);
                                }
                            }else{
                                /*
                                 * 说明旧表字段列表长度小于新表，最起码是有的字段被删除了，需要更新
                                 */
                                updateSqLite(oldTable,newTable);
                            }
                        }
                    }
                }

                KingDbManager.newUpdateBuilder().updateValue("table_version",version).update(KingDatabases.class);
            }
        }
    }

    /**
     * 数据库表需要升级
     * 比如要升级表A
     * 1、给原来的表重命名A_king_db_temporary
     * 2、根据最新的实体类，新建表A
     * 3、把A_king_db_temporary数据放到A中
     * 4、删除A_king_db_temporary表
     *
     * @param oldT 旧表
     * @param newT 新表
     */
    private void updateSqLite(KingTableInfo oldT,KingTableInfo newT){
        for(String newCol : newT.getFields()){
            if(newCol.split("-").length < 2){
                throw new KingDbException("[king db error] : 升级表["+newT.getName()+"]时，字段" + newCol + "不合法,大概率是使用了基本数据类型，请使用封装数据类型");
            }
        }

        /*
         * 1、给原来的表重命名A_king_db_temporary
         * ALTER TABLE 旧表名 RENAME TO 新表名
         */
        StringBuilder sqlBuilder = new StringBuilder("ALTER TABLE ");
        sqlBuilder.append(newT.getName()).append(" RENAME TO ").append(newT.getName()).append("_king_db_temporary");
        try {
            //执行改表操作
            KingDbHelper.getInstance().getDatabase().execSQL(sqlBuilder.toString());
        }catch (Exception e){
            throw new KingDbException("[king db error] : 升级表["+newT.getName()+"]时，创建临时表失败,详细信息：" + e.getMessage());
        }
        //删除系统表记录
        KingDbManager.newDeleteBuilder().setCondition(
                KingDbManager.newQueryBuilder().addEqual("table_name",oldT.getName()).buildQueryCondition()
        ).delete(KingTableInfo.class);
        /*
         * 2、根据最新的实体类，新建表A
         */
        Class clazz = null;
        try {
            clazz = Class.forName(newT.getPackageName());
            CreateTableSql createTableSql = new CreateTableSql();
            KingDbHelper.getInstance().getDatabase().execSQL(createTableSql.createSql(clazz));
        } catch (Exception e) {
            //恢复旧表在系统表中的数据记录
            KingDbManager.newInsertBuilder().insert(oldT);
            //把临时表名字改回去
            StringBuilder temBuilder = new StringBuilder("ALTER TABLE ");
            temBuilder.append(newT.getName()).append("_king_db_temporary").append(" RENAME TO ").append(newT.getName());
            //执行改表操作
            KingDbHelper.getInstance().getDatabase().execSQL(temBuilder.toString());
            throw new KingDbException("[ king db error ] : 升级更新表时，新增新表" + newT.getPackageName() + "发生异常，详细错误信息" + e.getMessage());
        }
        //在系统表中插入新表数据记录
        KingDbManager.newInsertBuilder().insert(newT);
        /*
         * 3、把A_king_db_temporary数据放到A中
         *  demo : Insert Into sn_info(author,tutor) select author,tutor from A1.title_info
         */
        StringBuilder insertSql = new StringBuilder();
        StringBuilder fieldSql = new StringBuilder();
        Boolean isFirst = true;
        for(String newF : newT.getFields()){
            boolean isExit = false;
            for(String oldF : oldT.getFields()){
                if(KingDbUtil.replaceAllBlank(newF.split("-")[0]).
                        equals(KingDbUtil.replaceAllBlank(oldF.split("-")[0]))){
                    fieldSql.append(isFirst ? "" : ",").append(newF.split("-")[0]);
                    isFirst = false;
                    isExit = true;
                }
            }
            if(!isExit){
                fieldSql.append(isFirst ? "''" : ",''");
                isFirst = false;
            }
        }
        insertSql.append("INSERT INTO ").
                append(newT.getName()).
                //append("(").
                //append(fieldSql).
                append(" SELECT ").
                append(fieldSql).
                append(" FROM ").
                append(newT.getName()).
                append("_king_db_temporary");

        try {
            //执行数据转移操作
            KingDbHelper.getInstance().getDatabase().execSQL(insertSql.toString());
        }catch (Exception e){

            //删除新表系统表记录
            KingDbManager.newDeleteBuilder().setCondition(
                    KingDbManager.newQueryBuilder().addEqual("table_name",oldT.getName()).buildQueryCondition()
            ).delete(KingTableInfo.class);

            //恢复旧表在系统表中的数据记录
            KingDbManager.newInsertBuilder().insert(oldT);

            //删除新建的表
            StringBuilder deleteSql = new StringBuilder();
            deleteSql.append("DROP TABLE ").append(newT.getName());
            KingDbHelper.getInstance().getDatabase().execSQL(deleteSql.toString());

            //把临时表名字改回去
            StringBuilder temBuilder = new StringBuilder("ALTER TABLE ");
            temBuilder.append(newT.getName()).append("_king_db_temporary").append(" RENAME TO ").append(newT.getName());
            //执行改表操作
            KingDbHelper.getInstance().getDatabase().execSQL(temBuilder.toString());
            throw new KingDbException("[king db error] : 升级表["+newT.getName()+"]时，备份表向新表转移数据失败,详细信息：" + e.getMessage());
        }

        /*
         * 4、删除A_king_db_temporary表
         *  demo : DROP TABLE xxxxxx;
         */
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("DROP TABLE ").append(newT.getName()).append("_king_db_temporary");

        try {
            //执行删除备份表操作
            KingDbHelper.getInstance().getDatabase().execSQL(deleteSql.toString());
        }catch (Exception e){

            //删除新表系统表记录
            KingDbManager.newDeleteBuilder().setCondition(
                    KingDbManager.newQueryBuilder().addEqual("table_name",oldT.getName()).buildQueryCondition()
            ).delete(KingTableInfo.class);

            //恢复旧表在系统表中的数据记录
            KingDbManager.newInsertBuilder().insert(oldT);

            //删除新建的表
            StringBuilder deleteNewSql = new StringBuilder();
            deleteNewSql.append("DROP TABLE ").append(newT.getName());
            KingDbHelper.getInstance().getDatabase().execSQL(deleteNewSql.toString());

            //把临时表名字改回去
            StringBuilder temBuilder = new StringBuilder("ALTER TABLE ");
            temBuilder.append(newT.getName()).append("_king_db_temporary").append(" RENAME TO ").append(newT.getName());
            //执行改表操作
            KingDbHelper.getInstance().getDatabase().execSQL(temBuilder.toString());
            throw new KingDbException("[king db error] : 升级表["+newT.getName()+"]时，删除备份表失败,详细信息：" + e.getMessage());
        }

    }

    /**
     * 数据库表需要升级,且新表仅需要新增字段
     * //ALTER TABLE OLD_COMPANY ADD COLUMN SEX char(1);
     * @param oldT 旧表
     * @param newT 新表
     */
    private void updateSqLiteAddCol(KingTableInfo oldT,KingTableInfo newT){
        for(String newCol : newT.getFields()){
            if(newCol.split("-").length < 2){
                throw new KingDbException("[king db error] : 升级表["+newT.getName()+"]时，新增字段" + newCol + "不合法,大概率是使用了基本数据类型，请使用封装数据类型");
            }
        }

        for(String newCol : newT.getFields()){
            Boolean isExit = false;//查看新表字段在旧表中是否存在
            for(String oldCol : oldT.getFields()){
                if(KingDbUtil.replaceAllBlank(newCol).equals(KingDbUtil.replaceAllBlank(oldCol))){
                    isExit = true;
                }
            }

            if(!isExit){
                //如果一个都没有找到，说明是新增的字段
                String[] col = newCol.split("-");

                StringBuilder sqlBuilder = new StringBuilder("ALTER TABLE ");
                sqlBuilder.append(newT.getName()).append(" ADD COLUMN");

                /*
                 * 注意!!!
                 * 因为这个地方是新增字段，所以至少目前不支持约束
                 */
                for(int i = 0;i <= 1;i++){
                    sqlBuilder.append(" ").append(col[i]);
                }

                try {
                    KingDbHelper.getInstance().getDatabase().execSQL(sqlBuilder.toString());
                    /*Boolean isFirst = true;
                    StringBuilder field = new StringBuilder();
                    for(String fi : newT.getFields()){
                        field.append(isFirst ? "" : "-").append(fi);
                        isFirst = false;
                    }
                    KingDbManager.newUpdateBuilder().updateValue("table_fields",field.toString()).setCondition(
                            KingDbManager.newQueryBuilder().addEqual("table_name",newT.getName()).buildQueryCondition()
                    ).update(KingTableInfo.class);*/
                }catch (Exception e){
                    throw new KingDbException("[king db error] : 升级表["+newT.getName()+"]时，新增字段" + newCol + "发生异常，详细信息 : " + e.getMessage());
                }
            }
        }
        KingDbManager.newDeleteBuilder().setCondition(
                KingDbManager.newQueryBuilder().addEqual("table_name",newT.getName()).buildQueryCondition()
        ).delete(KingTableInfo.class);
        KingDbManager.newInsertBuilder().insert(newT);
    }



}
