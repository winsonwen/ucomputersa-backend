/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.ucomputersa.common.utils;

import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 返回数据
 *
 * @author Mark sunlightcs@gmail.com
 */
public class R extends HashMap<String, Object> {


    public R setData(Object data) {
        put("data", data);
        return this;
    }

    public R() {
        put("code", 0);
    }


    public static R error(String msg) {
        R r = new R();
        r.put("msg", msg);
        r.put("code", 400);

        return r;
    }

    public static R error(Object data) {
        R r = new R();
        r.put("data", data);
        r.put("code", 400);
        return r;
    }


    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok(Object object) {
        R r = new R();
        r.put("data",object);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }


}
