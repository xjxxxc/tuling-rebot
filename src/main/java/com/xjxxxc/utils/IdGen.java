package com.xjxxxc.utils;

import java.util.UUID;

/*
 * @author Jason Xu
 * @DESCRIPTION uuid
 * @create 2019/3/20
 */
public class IdGen {
    /**
     *  获取去除“-”及大写的uuid
     * @return String uuid
     */
    public static String genId() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
