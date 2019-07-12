package com.sqy.kingdb.db;

import android.database.Cursor;
import android.util.Log;

import com.sqy.kingdb.KingDbHelper;
import com.sqy.kingdb.annotion.KingField;
import com.sqy.kingdb.bean.QueryCondition;
import com.sqy.kingdb.sql.QuerySql;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QueryManager {

    public <T>  List<T> query(Class<T> clazz,QueryCondition condition){
        List<T> result = new ArrayList<>();

        QuerySql querySql = new QuerySql();
        String sql = querySql.querySql(clazz,condition);
        Log.i("[king db]",sql);
        Cursor cursor = null;
        try {
            cursor = KingDbHelper.getInstance().getDatabase().rawQuery(sql,null);
            if(cursor != null && cursor.getCount() > 0){

                while (cursor.moveToNext()){
                    T entity = clazz.newInstance();
                    Field[] fields = entity.getClass().getDeclaredFields();
                    for(Field field : fields){
                        field.setAccessible(true);
                        String fieldName = field.getAnnotation(KingField.class) == null ?
                                field.getName() : field.getAnnotation(KingField.class).value();
                        int columnNum = cursor.getColumnIndex(fieldName);
                        if(columnNum == -1){
                            continue;
                        }
                        switch (field.getType().getSimpleName()){
                            case "Byte":
                                field.set(entity,(byte)cursor.getInt(columnNum));
                                break;
                            case "Short":
                                field.set(entity,cursor.getShort(columnNum));
                                break;
                            case "Integer":
                                field.set(entity,cursor.getInt(columnNum));
                                break;
                            case "Long":
                                field.set(entity,cursor.getLong(columnNum));
                                break;
                            case "Float":
                                field.set(entity,cursor.getFloat(columnNum));
                                break;
                            case "Double":
                                field.set(entity,cursor.getDouble(columnNum));
                                break;
                            case "Boolean":
                                field.set(entity, "1".equals(cursor.getString(columnNum)));
                                break;
                            case "String":
                                field.set(entity,cursor.getString(columnNum));
                                break;
                            case "Date":
                                try {
                                    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    java.util.Date utilD = dataFormat.parse(cursor.getString(columnNum));
                                    field.set(entity,new Date(utilD.getTime()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "Time":
                                try {
                                    SimpleDateFormat dataFormat = new SimpleDateFormat("HH:mm:ss");
                                    java.util.Date utilD = dataFormat.parse(cursor.getString(columnNum));
                                    field.set(entity,new Time(utilD.getTime()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "Timestamp":
                                try {
                                    SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    java.util.Date utilD = dataFormat.parse(cursor.getString(columnNum));
                                    field.set(entity,new Timestamp(utilD.getTime()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            case "List":
                                Type type = field.getGenericType();
                                if(type instanceof ParameterizedType) {// 如果是泛型参数的类型
                                    ParameterizedType parameterizedType = (ParameterizedType) type;
                                    try {
                                        Class genericClazz = (Class)parameterizedType.getActualTypeArguments()[0]; //得到泛型里的class类型对象。
                                        String listData = cursor.getString(columnNum);
                                        if(listData.contains("[")){
                                            listData = listData.substring(1,listData.length() - 1);
                                        }
                                        switch (genericClazz.getSimpleName()){
                                            case "Byte":
                                                List<Byte> byteList = new ArrayList<>();
                                                if(listData.contains(",")){
                                                    for(String item : listData.split(",")){
                                                        byteList.add(Byte.valueOf(item));
                                                    }
                                                }else{
                                                    byteList.add(Byte.valueOf(listData));
                                                }
                                                field.set(entity,byteList);
                                                break;
                                            case "Short":
                                                List<Short> shortList = new ArrayList<>();
                                                if(listData.contains(",")){
                                                    for(String item : listData.split(",")){
                                                        shortList.add(Short.valueOf(item));
                                                    }
                                                }else{
                                                    shortList.add(Short.valueOf(listData));
                                                }
                                                field.set(entity,shortList);
                                                break;
                                            case "Integer":
                                                List<Integer> integerList = new ArrayList<>();
                                                if(listData.contains(",")){
                                                    for(String item : listData.split(",")){
                                                        integerList.add(Integer.valueOf(item));
                                                    }
                                                }else{
                                                    integerList.add(Integer.valueOf(listData));
                                                }
                                                field.set(entity,integerList);
                                                break;
                                            case "Long":
                                                List<Long> longList = new ArrayList<>();
                                                if(listData.contains(",")){
                                                    for(String item : listData.split(",")){
                                                        longList.add(Long.valueOf(item));
                                                    }
                                                }else{
                                                    longList.add(Long.valueOf(listData));
                                                }
                                                field.set(entity,longList);
                                                break;
                                            case "Float":
                                                List<Float> floatList = new ArrayList<>();
                                                if(listData.contains(",")){
                                                    for(String item : listData.split(",")){
                                                        floatList.add(Float.valueOf(item));
                                                    }
                                                }else{
                                                    floatList.add(Float.valueOf(listData));
                                                }
                                                field.set(entity,floatList);
                                                break;
                                            case "Double":
                                                List<Double> doubleList = new ArrayList<>();
                                                if(listData.contains(",")){
                                                    for(String item : listData.split(",")){
                                                        doubleList.add(Double.valueOf(item));
                                                    }
                                                }else{
                                                    doubleList.add(Double.valueOf(listData));
                                                }
                                                field.set(entity,doubleList);
                                                break;
                                            case "Boolean":
                                                List<Boolean> booleanList = new ArrayList<>();
                                                if(listData.contains(",")){
                                                    for(String item : listData.split(",")){
                                                        booleanList.add(Boolean.valueOf(item));
                                                    }
                                                }else{
                                                    booleanList.add(Boolean.valueOf(listData));
                                                }
                                                field.set(entity,booleanList);
                                                break;
                                            case "String":
                                                List<String> stringList = new ArrayList<>();
                                                if(listData.contains(",")){
                                                    stringList.addAll(Arrays.asList(listData.split(",")));
                                                }else{
                                                    stringList.add(listData);
                                                }
                                                field.set(entity,stringList);
                                                break;
                                        }
                                    }catch (Exception e){
                                        continue;
                                    }
                                }else{
                                    continue;
                                }
                                break;
                        }
                    }
                    result.add(entity);
                }
            }
        }catch (Exception ignored){}
        finally {
            if(cursor != null){
                cursor.close();
            }
        }

        return result;
    }
}
