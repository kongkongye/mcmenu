package com.kongkongye.np.mcmenu.util;

import com.google.common.base.Preconditions;
import com.kongkongye.np.mcmenu.common.annotation.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static class SeparateResult {
        //结果
        private List<String> result;
        //原来的某一行对应结果的哪一行(如果结果有多行,取第一行)
        //原来的行 结果的行
        private Map<Integer, Integer> originToResultMap;
        //结果的每一行对应原来的哪一行
        //结果的行 原来的行
        private Map<Integer, Integer> resultToOriginMap;

        public SeparateResult(List<String> result, Map<Integer, Integer> originToResultMap, Map<Integer, Integer> resultToOriginMap) {
            this.result = result;
            this.originToResultMap = originToResultMap;
            this.resultToOriginMap = resultToOriginMap;
        }

        public List<String> getResult() {
            return result;
        }

        public Map<Integer, Integer> getOriginToResultMap() {
            return originToResultMap;
        }

        public Map<Integer, Integer> getResultToOriginMap() {
            return resultToOriginMap;
        }
    }

    /**
     * 通用的允许的字符(包含大小写字母与数字)
     * (适合随机生成字符串使用)
     */
    public static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    /**
     * 通用的允许的字符除去容易看混淆的一些字符(包含大小写字母与数字)
     * (适合验证码使用)
     */
    public static final String CHARS_SEE = "ABCDEFGHJKMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";

    /**
     * 删除前面的所有空白字符
     * @param str 字符串
     * @param all 是否删除全部空白字符,false时如果有空白字符,则会压缩成一个空格(无论之前有多少个)
     */
    public static String trimStart(String str, boolean all) {
        String result = str.replaceAll("^\\s+", "");
        if (!all && str.length() > result.length()) {
            result = " " + result;
        }
        return result;
    }

    /**
     * 删除后面的所有空白字符
     * @param str 字符串
     * @param all 是否删除全部空白字符,false时如果有空白字符,则会压缩成一个空格(无论之前有多少个)
     */
    public static String trimEnd(String str, boolean all) {
        String result = str.replaceAll("\\s+$", "");
        if (!all && str.length() > result.length()) {
            result = result + " ";
        }
        return result;
    }

    /**
     * 获取随机字符串<br>
     * 比如可以当作随机不会重复的id来使用
     * @return 32位随机字符串,实际上为随机UUID去除-后的
     */
    public static String getRandomUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 随机生成字符串
     * @param length 长度,>=1
     */
    public static String generateStr(int length) {
        return generateStr(CHARS, length);
    }

    /**
     * 随机生成字符串(不包含容易产生视觉混淆的字符如i与1)
     * @param length 长度,>=1
     */
    public static String generateStrSee(int length) {
        return generateStr(CHARS_SEE, length);
    }

    /**
     * 随机生成字符串(不包含容易产生视觉混淆的字符如i与1)
     * @param chars 允许使用的字符
     * @param length 长度,>=1
     */
    public static String generateStr(@NotNull String chars, int length) {
        Preconditions.checkNotNull(chars);
        Preconditions.checkArgument(length >= 1);

        StringBuilder sb = new StringBuilder(length);
        int size = chars.length();
        for (int count=0;count<length;count++) {
            sb.append(chars.charAt(MathUtil.nextInt(0, size - 1)));
        }
        return sb.toString();
    }

    /**
     * 把变量重新组合成字符串
     * (包含开始与结束位置)
     * @param args 变量
     * @param separator 变量之间用这个分隔
     * @param start 开始位置,0-(args.length-1)
     * @param end 结束位置,0-(args.length-1),大于最大时不会出异常
     * @return 组合后的字符串
     */
    public static String combine(@NotNull String[] args, @NotNull String separator, int start, int end) {
        Preconditions.checkNotNull(args);
        Preconditions.checkNotNull(separator);

        String result = "";
        for (int i=0;i<args.length;i++) {
            if (i < start) {
                continue;
            }
            if (i > end) {
                break;
            }
            if (i > start) {
                result += separator;
            }
            result += args[i];
        }
        return result;
    }

    /**
     * 检测字符串是否合法,合法表示只包含英文,数字,下划线
     * @param s 字符串
     * @return 是否合法
     */
    public static boolean isValid(String s) {
        return s.matches("^[\\da-zA-Z_]*$");
    }

    /**
     * 分割字符串
     * @param str 字符串
     * @param maxLength 每行最大长度
     * @return 不为null可为空列表
     */
    public static List<String> separateLines(String str, int maxLength) {
        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < str.length()) {
            result.add(str.substring(index, Math.min(str.length(), index+maxLength)));

            index += maxLength;
        }
        return result;
    }

    /**
     * 分割字符串
     * @param strs 字符串列表
     * @param maxLength 每行最大长度
     */
    public static void separateLines(List<String> strs, int maxLength) {
        int index = 0;
        while (index < strs.size()) {
            List<String> list = separateLines(strs.get(index), maxLength);
            if (list.size() > 1) {
                strs.set(index, list.get(0));
                for (int i=1;i<list.size();i++) {
                    strs.add(index + i, list.get(i));
                }
                index += list.size();
                continue;
            }

            index ++;
        }
    }

    /**
     * 分割字符串
     * @param strs 字符串列表(不会被改变)
     * @param maxLength 每行最大长度
     * @return ''的映射
     */
    public static SeparateResult separateLinesDetail(List<String> strs, int maxLength) {
        List<String> result = new ArrayList<>();
        Map<Integer, Integer> originToResultMap = new HashMap<>();
        Map<Integer, Integer> resultToOriginMap = new HashMap<>();

        //处理
        for (int index=0;index < strs.size();index++) {
            //原来行 -> 结果行
            originToResultMap.put(index, result.size());

            //分割
            List<String> list = separateLines(strs.get(index), maxLength);

            //结果行 -> 原来行
            for (int i=0;i<list.size();i++) {
                resultToOriginMap.put(result.size()+i, index);
            }

            //添加
            result.addAll(list);
        }

        //返回
        return new SeparateResult(result, originToResultMap, resultToOriginMap);
    }

    /**
     * 获取字符串中指定内容出现的次数
     * @param content 在此字符串中找
     * @param search 要找的字符串(正则形式)
     * @return 出现的次数,>=0
     */
    public static int count(String content, String search) {
        Pattern pattern = Pattern.compile(search);
        Matcher matcher = pattern.matcher(content);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
