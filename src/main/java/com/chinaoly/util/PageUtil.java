package com.chinaoly.util;

import java.util.ArrayList;
import java.util.List;

public class PageUtil<T> {
    /**
     * layui接受的数据格式如下：
     * response: { // 定义后端 json 格式
     * code: 0,
     * msg: "",
     * count: 1000,
     * data: []
     * }
     */

    private int code;

    private String msg;

    private Integer count; // 总条数

    private List<T> data = new ArrayList(); // 装前台当前页的数据

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public static PageUtil backPage(int a, String b, Integer c, List d) {
        PageUtil page = new PageUtil<>();
        page.setCode(a);
        page.setMsg(b);
        page.setCount(c);
        page.setData(d);
        return page;
    }
}
