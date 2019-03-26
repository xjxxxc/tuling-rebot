package com.xjxxxc.utils;

import com.xjxxxc.model.ConvertInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * @author Jason Xu
 * @DESCRIPTION 模拟数据库（实际应用请替换其他DB）
 * @create 2019/3/21
 */
public class DatabaseUtils {

    public static Map<String, ConvertInfo> map = new ConcurrentHashMap<String, ConvertInfo>();

    public static  ConvertInfo getKey(String id){
       return map.get(id);
    }

    public static ConvertInfo setKey(String id, ConvertInfo convertInfo){
        return map.put(id,convertInfo);
    }
}
