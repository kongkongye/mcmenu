package com.kongkongye.np.mcmenu.util;

import com.google.common.base.Preconditions;
import com.kongkongye.np.mcmenu.common.annotation.Nullable;
import com.kongkongye.np.mcmenu.util.color.Reducer;
import com.kongkongye.np.mcmenu.util.color.Separator;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {
    public static final String COLOR_CHARS = "0123456789abcdef";
    public static final String FORMAT_CHARS = "lmnor";
    public static final String COLOR_FORMAT_CHARS = "0123456789abcdeflmnor";

    public static final String MC_COLOR_CHAR = "\u00a7";
    public static final String COLOR_CHAR = "&";
    public static final Pattern COLOR_PATTERN = Pattern.compile(COLOR_CHAR+"["+COLOR_FORMAT_CHARS+"]");
    //颜色,使用真实颜色字符
    public static final Pattern REAL_COLOR_PATTERN = Pattern.compile(MC_COLOR_CHAR+"["+ COLOR_CHARS +"]");
    //格式,使用真实颜色字符
    public static final Pattern REAL_FORMATS_PATTERN = Pattern.compile(MC_COLOR_CHAR+"["+ FORMAT_CHARS +"]");
    //颜色与格式,使用真实颜色字符
    public static final Pattern REAL_COLOR_FORMATS_PATTERN = Pattern.compile(MC_COLOR_CHAR+"["+COLOR_FORMAT_CHARS+"]");

    /**
     * 颜色字符&转换<br>
     * 如'&a'或'&l'会转换,但'&z'不会转换
     * @param msg 要转换的信息(null时会返回null)
     * @return 转换后的字符串
     */
    public static String convertColor(@Nullable String msg){
        return convertColor(COLOR_PATTERN, COLOR_CHAR, msg);
    }

    /**
     * 颜色字符转换
     * @param colorPattern 颜色匹配式
     * @param colorChar 颜色字符
     * @param msg 要转换的信息(null时会返回null)
     * @return 转换后的字符串
     */
    public static String convertColor(Pattern colorPattern, String colorChar, @Nullable String msg){
        if (msg == null) return null;

        Matcher m = colorPattern.matcher(msg);
        StringBuffer sb = new StringBuffer();
        while (m.find()) m.appendReplacement(sb, m.group().replace(colorChar, MC_COLOR_CHAR+""));
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 按最大长度分割行:
     *   1. 不会分割掉两个完整的颜色格式字符
     *   2. 换行颜色格式字符会继承下来
     * @param content 内容
     * @param maxLength 最大长度,>=2
     * @return 不为null
     */
    public static List<String> separateLines(String content, int maxLength) {
        Preconditions.checkArgument(maxLength >= 2);

        return new Separator(content, maxLength).separate();
    }

    /**
     * 去除所有颜色格式字符(单个颜色字符不会去除,必须是正常的两个颜色字符才会去除)
     * 比如:
     *   'a§l§a§bb'会变成'ab'
     *   'a§tb'不会变
     */
    public static String trimColors(@Nullable String content) {
        if (content == null) return null;

        Matcher m = REAL_COLOR_FORMATS_PATTERN.matcher(content);
        StringBuffer sb = new StringBuffer();
        while (m.find()) m.appendReplacement(sb, "");
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 去除所有额外的颜色格式字符
     * 会去除所有不影响显示的颜色格式字符
     * 注意!此方法不会转换颜色字符!即最后面的颜色字符直接是mc的那个颜色字符\u00A7
     */
    public static String trimExtraColors(@Nullable String content) {
        if (content == null) return null;

        //先去除末尾的所有颜色格式字符
        content = trimEndColors(content);

        //再去除前面的
        content = new Reducer(content).reduce();

        //再去除相邻的两个相同的
        content = trimSameColors(content);

        //返回
        return content;
    }

    /**
     * 去除末尾所有的颜色格式字符
     * 注意!此方法不会转换颜色字符!即最后面的颜色字符直接是mc的那个颜色字符\u00A7
     */
    public static String trimEndColors(@Nullable String content) {
        if (content == null) return null;

        while (true) {
            String result = trimEndColor(content);
            if (content.length() == result.length()) break;//长度一样,没有删减
            else content = result;
        }

        //返回
        return content;
    }

    /**
     * 去除最末尾的颜色格式字符(单个或两个颜色字符)
     * 注意!此方法不会转换颜色字符!即最后面的颜色字符直接是mc的那个颜色字符\u00A7
     */
    public static String trimEndColor(@Nullable String content) {
        if (content == null) return null;

        //最末尾直接是单个颜色字符 --> 去除
        if (content.length() >= 1 && content.charAt(content.length()-1) == MC_COLOR_CHAR.charAt(0)) {
            return content.substring(0, content.length()-1);
        }

        //最末尾是正常的颜色格式字符 --> 去除
        if (content.length() >= 2 && REAL_COLOR_FORMATS_PATTERN.matcher(content.substring(content.length()-2)).matches()) {
            return content.substring(0, content.length()-2);
        }
        return content;
    }

    /**
     * 删除相邻的相同的颜色格式字符
     */
    private static String trimSameColors(@Nullable String content) {
        if (content == null) return null;

        Matcher m = REAL_COLOR_FORMATS_PATTERN.matcher(content);
        StringBuffer sb = new StringBuffer();
        String pre = null;
        while (m.find()) {
            String now = m.group();
            if (Objects.equals(pre, now)) m.appendReplacement(sb, "");
            else m.appendReplacement(sb, now);
            pre = now;
        }
        m.appendTail(sb);
        return sb.toString();
    }
}
