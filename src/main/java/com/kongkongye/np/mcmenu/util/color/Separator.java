package com.kongkongye.np.mcmenu.util.color;

import com.google.common.base.Preconditions;
import com.kongkongye.np.mcmenu.common.annotation.NotNull;
import com.kongkongye.np.mcmenu.util.ColorUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

/**
 * 分割器
 * (换行保留颜色格式)
 * 这里的颜色字符直接使用MC的颜色字符
 */
public class Separator {
    //原内容(不变),不为null可为空字符串
    private String content;
    //一行最大长度,>=2
    private int maxLength;

    //指针位置,初始为-1
    private int pointer = -1;

    //最近的颜色
    private String color;
    //最近的格式
    private Set<String> formats = new HashSet<>();

    //结果
    private List<String> result = new ArrayList<>();

    /**
     * @param maxLength >=2
     */
    public Separator(@NotNull String content, int maxLength) {
        Preconditions.checkNotNull(content);
        Preconditions.checkArgument(maxLength >= 2);

        this.content = content;
        this.maxLength = maxLength;
    }

    /**
     * 执行分割
     * @return 返回分割后的结果
     */
    public List<String> separate() {
        while (true) {
            //指针已经到最后了
            if (pointer >= content.length()-1) return result;

            //后面还有内容

            //前面的颜色格式
            String preColor = "";
            if (color != null) preColor += ColorUtil.MC_COLOR_CHAR+color;
            for (String format:formats) {
                preColor += ColorUtil.MC_COLOR_CHAR + format;
            }
            //是否后面马上是颜色: 是的话前面的颜色格式就不顶用了
            if (!preColor.isEmpty() && content.length()-1 - pointer >= 2) {
                if (ColorUtil.REAL_COLOR_PATTERN.matcher(content.substring(pointer+1, pointer+3)).matches()) {
                    preColor = "";
                }
            }

            //指针往前移
            //移到行尾那个字符上
            int nextPointer = pointer + maxLength - preColor.length();

            //指针位置修复: 已经移到超出范围了
            if (nextPointer >= content.length()) {
                nextPointer = content.length()-1;
            }

            //检测是否刚好将一个完整的颜色格式字符分两半
            if (content.length()-1 - nextPointer >= 1 && ColorUtil.REAL_COLOR_FORMATS_PATTERN.matcher(content.substring(nextPointer, nextPointer+2)).matches()) {
                //往回退一格
                nextPointer--;
            }

            //这种情况如果发生了,说明再难分割行了,直接返回结果吧(真的会发生?)
            if (nextPointer <= pointer) return result;

            //获取行内容
            String line = content.substring(pointer+1, nextPointer+1);
            result.add(preColor+line);

            //检测这行内的颜色格式,更新一下
            Matcher m = ColorUtil.REAL_COLOR_FORMATS_PATTERN.matcher(line);
            while (m.find()) {
                String s = m.group().substring(1);
                if (ColorUtil.COLOR_CHARS.contains(s)) {
                    //颜色
                    color = s;
                    formats.clear();
                }else {
                    //格式
                    formats.add(s);
                }
            }

            //更新指针
            pointer = nextPointer;
        }
    }
}
