package com.kongkongye.np.mcmenu.util.color;

import com.google.common.base.Preconditions;
import com.kongkongye.np.mcmenu.common.annotation.NotNull;
import com.kongkongye.np.mcmenu.util.ColorUtil;

import java.util.LinkedHashSet;
import java.util.Objects;

public class Reducer {
    private static final String COLOR_CHARS = "0123456789abcdef";
    private static final String FORMAT_CHARS = "lmnor";

    //总内容(最后面非颜色格式字符,而是普通字符)
    private final String content;
    //普通文本开始的位置,-1表示未开始
    private int textIndex = -1;
    //指针的位置,从0开始
    private int index = -1;//初始为-1

    //固化信息(颜色&格式)

    //颜色字符
    private Character color;
    //格式字符集合
    private LinkedHashSet<Character> formats = new LinkedHashSet<>();

    private final StringBuilder sb = new StringBuilder();

    public Reducer(@NotNull String content) {
        Preconditions.checkNotNull(content);

        this.content = content;
    }

    /**
     * 移动
     */
    public String reduce() {
        while (true) {
            //后面已经没内容
            if (index >= content.length()-1) {
                addPreText(index);
                return sb.toString();
            }

            //移动
            index++;

            //固化
            char c = content.charAt(index);
            if (Objects.equals(c, ColorUtil.MC_COLOR_CHAR)) {//颜色字符开头,可能是颜色或格式
                //下个字符(必然存在,因为传入的content就有这个特点了)
                char c2 = content.charAt(++index);
                //是颜色
                if (COLOR_CHARS.indexOf(c2) != -1) {//特点: 颜色不能累积,且会清除格式
                    //先检测前面的普通文本加上
                    addPreText(index-2);
                    //再更新
                    color = c2;
                    formats.clear();
                    continue;
                }
                //是格式
                if (FORMAT_CHARS.indexOf(c2) != -1) {//特点: 不影响颜色,且不同格式可以累积
                    //先检测前面的普通文本加上
                    addPreText(index-2);
                    //再更新
                    formats.add(c2);
                    continue;
                }
                //是普通字符
                //如果之前都是颜色字符,这时设置一下
                if (textIndex == -1) {
                    textIndex = index-1;//注意减1
                }
            }else {
                //普通字符

                //如果之前都是颜色字符,这时设置一下
                if (textIndex == -1) {
                    textIndex = index;
                }
            }
        }
    }

    /**
     * 检测加上前面的普通文本
     * @param end 最后位置(包含)
     */
    private void addPreText(int end) {
        if (textIndex != -1) {
            //把前面的颜色格式加上
            //先加颜色
            if (color != null) {
                //添加
                sb.append(ColorUtil.MC_COLOR_CHAR).append(color);
                //清空
                color = null;
            }
            //再加格式
            if (!formats.isEmpty()) {
                //添加
                formats.forEach(format-> sb.append(ColorUtil.MC_COLOR_CHAR).append(format));
                //清空
                formats.clear();
            }

            //再加上普通字符
            sb.append(content.substring(textIndex, end+1));

            //更新值
            textIndex = -1;
        }
    }
}
