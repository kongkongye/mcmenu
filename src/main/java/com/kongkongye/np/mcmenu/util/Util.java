package com.kongkongye.np.mcmenu.util;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.ConfigSection;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Util {
    /**
     * 给玩家发送信息
     * @param msg 会转换颜色字符
     */
    public static void send(CommandSender sender, String msg, Object... args) {
        //转换变量
        msg = ParamUtil.convert(msg, false, args);
        //转换颜色
        msg = ColorUtil.convertColor(msg);
        //发送
        sender.sendMessage(msg);
    }

    /**
     * 解码
     */
    public static ConfigSection decode(String content) {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(dumperOptions);
        return new ConfigSection(yaml.loadAs(content, LinkedHashMap.class));
    }

    /**
     * 编码
     */
    public static String encode(ConfigSection config) {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(dumperOptions);
        return yaml.dump(config);
    }

    public static byte[] toBase64(byte[] bytes) {
        return Base64.getEncoder().encode(bytes);
    }

    public static byte[] fromBase64(byte[] bytes) {
        return Base64.getDecoder().decode(bytes);
    }

    public static byte[] compress(byte[] bytes) throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(bytes);
        }
        return out.toByteArray();
    }

    public static byte[] uncompress(byte[] bytes) throws Exception{
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        GZIPInputStream ungzip = new GZIPInputStream(in);
        byte[] buffer = new byte[256];
        int n;
        while ((n = ungzip.read(buffer)) >= 0) {
            out.write(buffer, 0, n);
        }

        return out.toByteArray();
    }
}
