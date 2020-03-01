package com.chinaoly.util;


import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 字符工具类
 *
 * @author
 * @since 2017-02-24
 */
public final class StringUtil {

    /**
     * <h3>判断所给的字符串是否为<code>null</code>或者空字符串</h3>
     *
     * @param str 欲进行判断的字符串
     * @return true if the {@link String} is null or empty, otherwise false
     */
    public static boolean isNullOrEmpty(String str) {

        return str == null || str.trim().length() == 0 || "null".equalsIgnoreCase(str) || "".equals(str);

    }

    /**
     * <h3>获取任意给定的参数的字符串</h3>
     *
     * @param t 获取该值
     * @return 返回其字符串值，如果为{@code null}则返回{@code ""}
     */
    public static <T> String getString(T t) {

        return t == null ? "" : t.toString();

    }

    /**
     * <h3>将数组的各个元素以指定的间隔符拼接起来</h3>
     *
     * @param array  数组
     * @param splice 间隔符
     * @return 将数组的各个元素以指定的间隔符拼接后的字符串
     */
    public static String spliceArray(Object[] array, Object splice) {

        if (array == null || array.length == 0) {
            return "";
        }

        StringBuilder str = new StringBuilder();
        if (splice == null) {
            splice = "";
        }
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                str.append(splice);
            }
            str.append(array[i]);
        }

        return str.toString();

    }

    public static int asciiLength(String str) {
        int length = 0;
        for (int i = 0; i < str.length(); i++) {
            int ascii = Character.codePointAt(str, i);
            if (ascii >= 0 && ascii <= 255)
                length++;
            else
                length += 2;
        }
        return length;
    }

    public static long parseLong(String str, long defaultValue) {
        if (str == null || "".equals(str)) {
            return defaultValue;
        }
        try {
            return Long.valueOf(str);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 剪切utf8字符
     *
     * @param source
     * @param maxLength
     * @return
     */
    public static String cutUtf8(String source, int maxLength) {
        byte[] bs = source.getBytes(Charset.forName("utf-8"));
        if (bs.length > maxLength) {
            int index = maxLength;
            for (; index > 0; index--) {
                if (((bs[index] & 0xff) >>> 6) != 2) {// 10xxxxxx
                    break;
                }
            }
            String ret = new String(bs, 0, index);
            index = ret.indexOf('[', ret.length() - 4);
            if (index > 0) {
                ret = ret.substring(0, index);
            }
            return ret;
        }
        return source;
    }

    /**
     * 字符串转换为BigDecimal金额  数组类型
     * monies  1,2,3....
     */
    public static BigDecimal[] getBigDecimal(String monies) {
        List<BigDecimal> list = new ArrayList<>();
        List<String> str = Arrays.asList(monies.split(","));
        for (String money : str) { // ^\\d+\\.??\\d+$  不可单个正数
            if (Pattern.matches("^[+]?([0-9]+(?:[\\.][0-9]*)?|\\.[0-9]+)$", money)) {
                try {
                    list.add(new BigDecimal(money));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list.toArray(new BigDecimal[list.size()]);
    }

    /**
     * 字符串转换为int类型
     */
    public static int parseInt(String s) {
        if (s != null && Pattern.matches("^\\d*$", s)) {
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * 字符串转换为转换Double型
     */
    public static Double parseDouble(String s) {
        if (s != null && Pattern.matches(
                "^[-+]?(/d+(/./d*)?|/./d+)([eE]([-+]?([012]?/d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?[dD]?$", s)) {
            try {
                return Double.parseDouble(s);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}