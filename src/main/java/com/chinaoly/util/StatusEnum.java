package com.chinaoly.util;

public class StatusEnum {

    /**
     * 是否有效
     */
    public static Integer NORMAL = 1;

    public static Integer DELETE = 0;

    /**
     * 正常
     */
    public static Integer VISITOR_NORMAL = 0;
    /**
     * 邀请人取消
     */
    public static Integer VISITOR_CANCEL = 1;

    /**
     * 申请人取消
     */
    public static Integer VISITOR_ACANCEL = 2;

    /**
     * 管理员删除
     */
    public static Integer VISITOR_DELETE = 3;
    /**
     * 过期
     */
    public static Integer VISITOR_DEPRECATED = 4;

}
