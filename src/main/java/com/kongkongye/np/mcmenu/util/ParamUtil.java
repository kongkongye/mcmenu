package com.kongkongye.np.mcmenu.util;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.kongkongye.np.mcmenu.common.annotation.NotNull;
import com.kongkongye.np.mcmenu.common.annotation.Nullable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParamUtil {
    public static final Pattern PARAMS_PATTERN = Pattern.compile("\\{([\\w]+)\\}");
    public static final Pattern ARG_PARAMS_PATTERN = Pattern.compile("\\{([\\d]*\\,[\\d]*|[\\d]+)\\}");
    public static final Pattern PARAMS_DETAIL_PATTERN = Pattern.compile("\\{([\\w-]+):([\\w-]+(\\|[\\w-]+)*)\\}");
    public static final ValueGetter EMPTY_GETTER = new ValueGetter() {
        @Override
        public String apply(@Nullable String input) {
            return null;
        }
    };

    /**
     * param value getter
     */
    public interface ValueGetter extends Function<String, String> {
    }

    public static Collection<String> getParams(String s) {
        return getParams(s, PARAMS_PATTERN);
    }

    public static Collection<String> getParams(String s, Pattern pattern) {
        Set<String> result = new HashSet();
        if (s != null) {
            Matcher matcher = pattern.matcher(s);

            while(matcher.find()) {
                result.add(matcher.group(1));
            }
        }

        return result;
    }

    /**
     * 转换完整的字符串
     * @param s 完整的可能包含变量的字符串,如'hello {1}, you say: {2,}'
     * @see #convertArg(String[], String)
     */
    public static String convertArgFull(String[] args, String s, boolean stay) {
        Collection<String> params = getParams(s, ARG_PARAMS_PATTERN);
        Map<String, Object> values = new HashMap<>();
        params.forEach(e -> {
            String value = convertArg(args, "{"+e+"}");
            if (!Strings.isNullOrEmpty(value) || !stay) {
                values.put(e, value);
            }
        });
        return convert(s, params, values, stay);
    }

    /**
     * 变量转换,包括:
     * {x}
     * {x,}
     * {,y}
     * {x,y}
     * {,}
     * 如args为['set','name','Jim','Kate'],s为'{4}',则转换后的值为'Kate'
     * @param args 变量来源
     * @param s 不为null
     * @return 转换后的值,可为空字符串不为null
     */
    public static String convertArg(String[] args, String s) {
        if (args == null) {
            return "";
        }
        try {
            if (s.length() >= 3 && s.charAt(0) == '{' && s.charAt(s.length()-1) == '}') {//{...}
                String content = s.substring(1, s.length()-1).toLowerCase();
                if (content.indexOf(',') == -1) {//{1}
                    int pos = Integer.parseInt(content);
                    if (pos < 1 || pos > args.length) {
                        return "";
                    }
                    return args[pos-1];
                }else if (s.length() == 3){//{,}
                    return StringUtil.combine(args, " ", 0, args.length);
                }else {
                    int index = content.indexOf(",");
                    if (index == 0) {//{,1}
                        int endPos = Integer.parseInt(content.substring(1));
                        if (endPos < 1) {
                            return "";
                        }
                        return StringUtil.combine(args, " ", 0, endPos-1);
                    }else if (index == content.length()-1) {//{1,}
                        int startPos = Integer.parseInt(content.substring(0, index));
                        if (startPos < 1 || startPos > args.length) {
                            return "";
                        }
                        return StringUtil.combine(args, " ", startPos-1, args.length);
                    }else {//{1,2}
                        int startPos = Integer.parseInt(content.substring(0, index));
                        int endPos = Integer.parseInt(content.substring(index+1));
                        if (startPos < 1 || endPos < 1 || startPos > endPos || startPos > args.length) {
                            return "";
                        }
                        return StringUtil.combine(args, " ", startPos-1, endPos-1);
                    }
                }
            }
        } catch (Exception e) {
        }
        return s;
    }

    /**
     * 转换变量,会将{0},{1}...这些转换为对应的变量
     * @param s 原值
     * @param stay 是否保留变量名,即如果值映射列表内无变量名的值映射,是否保留原变量名.true时保留,false时替换为空字符串
     * @param args 变量值列表
     * @return 转换后的值
     */
    public static String convert(@NotNull String s, boolean stay, Object... args) {
        Preconditions.checkNotNull(s);

        Map<String, Object> params = new HashMap<>();
        for (int index=0;index<args.length;index++) {
            params.put(index + "", args[index]);
        }

        return convert(s, params, stay);
    }

    /**
     * 转换变量,会将{0},{1}...这些转换为对应的变量
     * @param s 原值
     * @param stay 是否保留变量名,即如果值映射列表内无变量名的值映射,是否保留原变量名.true时保留,false时替换为空字符串
     * @param params 变量值列表
     * @return 转换后的值
     */
    public static String convert(@NotNull String s, Map<String, Object> params, boolean stay) {
        Preconditions.checkNotNull(s);

        Collection<String> keys = getParams(s);
        return convert(s, keys, params, stay);
    }

    /**
     * 转换变量
     * 会将格式为'{param}'的变量替换为值
     * @param s 原值
     * @param params 变量名集合,变量名不包含{}
     * @param values 值映射列表
     * @param stay 是否保留变量名,即如果值映射列表内无变量名的值映射,是否保留原变量名.true时保留,false时替换为空字符串
     * @return 转换后的值
     */
    public static String convert(@NotNull String s, @NotNull Collection<String> params, @NotNull Map<String, Object> values, boolean stay) {
        Preconditions.checkNotNull(s);
        Preconditions.checkNotNull(params);
        Preconditions.checkNotNull(values);

        Object value;
        for (String param:params) {
            value = values.get(param);
            if (value == null) {
                if (!stay) {
                    s = s.replace("{" + param + "}", "");
                }
            }else {
                s = s.replace("{" + param + "}", value.toString());
            }
        }
        return s;
    }

    /**
     * 转换变量
     * 会将格式为'param'的变量替换为值
     * @param s 原值
     * @param params 变量名集合(完整的替换键)
     * @param values 值映射列表
     * @param stay 是否保留变量名,即如果值映射列表内无变量名的值映射,是否保留原变量名.true时保留,false时替换为空字符串
     * @return 转换后的值
     */
    public static String convertFull(@NotNull String s, @NotNull Collection<String> params, @NotNull Map<String, Object> values, boolean stay) {
        Preconditions.checkNotNull(s);
        Preconditions.checkNotNull(params);
        Preconditions.checkNotNull(values);

        Object value;
        for (String param:params) {
            value = values.get(param);
            if (value == null) {
                if (!stay) {
                    s = s.replace(param, "");
                }
            }else {
                s = s.replace(param, value.toString());
            }
        }
        return s;
    }

    /**
     * stay为false
     * @see #convert(String, Collection, ValueGetter, boolean)
     */
    public static String convert(@NotNull String s, @NotNull Collection<String> params, @NotNull ValueGetter valueGetter) {
        Preconditions.checkNotNull(s);
        Preconditions.checkNotNull(params);
        Preconditions.checkNotNull(valueGetter);

        for (String param:params) {
            s = s.replace("{" + param + "}", String.valueOf(valueGetter.apply(param)));
        }
        return s;
    }

    /**
     * 转换变量
     * 会将格式为'{param}'的变量替换为值
     * @param s 原值
     * @param params 变量名集合,变量名不包含{}
     * @param valueGetter 变量值获取器(返回null表示指定的变量名不存在)
     * @param stay 是否保留变量名,即如果值映射列表内无变量名的值映射,是否保留原变量名.true时保留,false时替换为空字符串
     * @return 转换后的值
     */
    public static String convert(@NotNull String s, @NotNull Collection<String> params, @NotNull ValueGetter valueGetter, boolean stay) {
        Preconditions.checkNotNull(s);
        Preconditions.checkNotNull(params);
        Preconditions.checkNotNull(valueGetter);

        for (String param:params) {
            Object value = valueGetter.apply(param);
            String replace;
            if (value != null) {
                replace = String.valueOf(value);
            } else if (stay) {
                continue;
            } else {
                replace = "";
            }
            s = s.replace("{"+param+"}", replace);
        }
        return s;
    }
}
