package com.kongkongye.np.mcmenu;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.event.player.PlayerItemHeldEvent;
import cn.nukkit.event.player.PlayerMoveEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.service.ServicePriority;
import com.kongkongye.np.mcmenu.api.McMenuApi;
import com.kongkongye.np.mcmenu.api.menu.Menu;
import com.kongkongye.np.mcmenu.api.menu.MenuFactory;
import com.kongkongye.np.mcmenu.api.service.CommandService;
import com.kongkongye.np.mcmenu.config.Config;
import com.kongkongye.np.mcmenu.menu.AbstractMenu;
import com.kongkongye.np.mcmenu.service.McCommandService;
import com.kongkongye.np.mcmenu.util.LogUtil;
import com.kongkongye.np.mcmenu.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class McMenuPlugin extends PluginBase implements Listener {
    public static McMenuPlugin instance;
    public static Config config;

    public McMenuPlugin() {
        instance = this;
        McMenuApi.mcMenu = new McMenuImpl();
    }

    @Override
    public void onLoad() {
        //McMenuImpl
        ((McMenuImpl)McMenuApi.mcMenu).initOnLoad();
        //注册服务
        getServer().getServiceManager().register(CommandService.class, new McCommandService(), this, ServicePriority.NORMAL);
    }

    @Override
    public void onEnable() {
        //读取配置
        reload();
        //注册事件
        getServer().getPluginManager().registerEvents(this, this);
        //McMenuImpl
        ((McMenuImpl)McMenuApi.mcMenu).initOnEnable();
    }

    @Override
    public void onDisable() {
        //McMenuImpl
        ((McMenuImpl)McMenuApi.mcMenu).initOnDisable();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        //退出
        McMenuApi.getActionManager().exit(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (config.isExitMoveEnable()) {
            Menu menu = McMenuApi.getMenuManager().getMenu(event.getPlayer());
            if (menu instanceof AbstractMenu && menu.getMenuFactory().isUseDefaultExitMethod()) {
                AbstractMenu abstractMenu = (AbstractMenu) menu;
                double distance = abstractMenu.getLocation().distance(event.getPlayer().getLocation());
                if (distance >= config.getExitMoveDistance()) {
                    //退出
                    McMenuApi.getActionManager().exit(event.getPlayer());
                }

            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (config.isExitOpenInventoryEnable()) {
            Menu menu = McMenuApi.getMenuManager().getMenu(event.getPlayer());
            if (menu != null && menu.getMenuFactory().isUseDefaultExitMethod()) {
                McMenuApi.getActionManager().exit(event.getPlayer());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        Config config = McMenuPlugin.config;
        Menu menu = McMenuApi.getMenuManager().getMenu(player);
        if (menu != null) {//在菜单内
            int slot = event.getSlot();
            if (slot == 0) {//左移
                McMenuApi.getActionManager().left(player);
                //取消事件
//                event.setCancelled();
                stick(player);
                //声音
                if (config.getSoundLeft() != null) {
                    player.getLevel().addSound(location, config.getSoundLeft(), config.getSoundVolume(), config.getSoundPitch(), player);
                }
            }else if (slot == 8) {//右移
                McMenuApi.getActionManager().right(player);
                //取消事件
//                event.setCancelled();
                stick(player);
                //声音
                if (config.getSoundRight() != null) {
                    player.getLevel().addSound(location, config.getSoundRight(), config.getSoundVolume(), config.getSoundPitch(), player);
                }
            }else if (slot == menu.getSlot()) {//确认
                McMenuApi.getActionManager().confirm(player);
                //取消事件
//                event.setCancelled();
                stick(player);
                //声音
                if (config.getSoundConfirm() != null) {
                    player.getLevel().addSound(location, config.getSoundConfirm(), config.getSoundVolume(), config.getSoundPitch(), player);
                }
            }else if (slot == 7) {//返回
                McMenuApi.getActionManager().back(player);
                //取消事件
//                event.setCancelled();
                stick(player);
                //声音
                if (config.getSoundBack() != null) {
                    player.getLevel().addSound(location, config.getSoundBack(), config.getSoundVolume(), config.getSoundPitch(), player);
                }
            }
        }else {//不在菜单内
            Item item = event.getItem();
            String menuName = McMenuApi.getItemManager().getMenuName(item);
            if (menuName != null) {
                //加入菜单
                McMenuApi.getActionManager().join(player, event.getSlot(), menuName);
                //取消事件
//                event.setCancelled();
                stick(player);
                //声音
                if (config.getSoundJoin() != null) {
                    player.getLevel().addSound(location, config.getSoundJoin(), config.getSoundVolume(), config.getSoundPitch(), player);
                }
            }
        }
    }

    private void stick(Player player) {
        getServer().getScheduler().scheduleDelayedTask(this, () -> player.getInventory().setHeldItemSlot(1), 0);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }

        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("get")) {
                if (args.length >= 2) {
                    if (p == null) {
                        Util.send(sender, "此命令只能玩家发出!");
                        return false;
                    }
                    String menuName = args[1];
                    cmdGet(p, menuName);
                    return true;
                }
            }else if (args[0].equalsIgnoreCase("reload")) {
                McMenuApi.reload();
                //提示
                Util.send(sender, "重载配置.");
                return true;
            }
        }
        return false;
    }

    /**
     * 命令: 获取菜单
     */
    private void cmdGet(Player p, String menuName) {
        //权限
        if (!p.isOp()) {
            Util.send(p, "&c没有权限获取菜单!");
            return;
        }
        //请将要设置菜单的物品放在手上
        Item item = p.getInventory().getItemInHand();
        if (item == null || item.getId() == 0) {
            Util.send(p, "&c请将要设置菜单的物品放在手上!");
            return;
        }
        //菜单不存在
        MenuFactory menuFactory = McMenuApi.getMenuManager().getMenuFactory(menuName);
        if (menuFactory == null) {
            Util.send(p, "&c菜单不存在: &e{0}", menuName);
            return;
        }
        //获取
        try {
            item = McMenuApi.getItemManager().saveMenuInfo(item, menuName);
            p.getInventory().setItemInHand(item);
            //提示
            Util.send(p, "&a成功设置手上的物品为菜单: &e{0}", menuName);
        } catch (Exception e) {
            e.printStackTrace();
            Util.send(p, "&c获取菜单出错: &e{0}", e.getMessage());
        }
    }

    /**
     * 重新读取配置
     */
    public void reload() {
        //生成文件
        generate();
        //读取
        Config config = new Config();
        config.load(new cn.nukkit.utils.Config(new File(getDataFolder(), Constants.FILE_CONFIG)));
        //读取成功,替换
        McMenuPlugin.config = config;
    }

    /**
     * 生成文件(只有菜单配置目录不存在时才会生成)
     */
    private void generate() {
        //配置目录已经存在
        if (getDataFolder().exists()) {
            LogUtil.info(1, "插件配置目录存在,不生成文件.");
            return;
        }
        LogUtil.info(1, "生成文件.");

        ZipInputStream jis = null;
        FileOutputStream fos = null;
        try {
            jis = new ZipInputStream(new FileInputStream(getFile()));
            ZipEntry entry;
            byte[] buff = new byte[1024];
            int read;
            File dir = getDataFolder();
            while ((entry = jis.getNextEntry()) != null) {
                try {
                    if (entry.getName().startsWith(Constants.SOURCE_DIR)) {
                        String suffix = entry.getName().substring(Constants.SOURCE_DIR.length());
                        if (entry.isDirectory()) {
                            new File(dir, suffix).mkdirs();
                        }else {
                            File outFile = new File(dir, suffix);
                            if (outFile.exists()) {
                                continue;
                            }else {
                                outFile.getParentFile().mkdirs();
                                outFile.createNewFile();
                            }
                            fos = new FileOutputStream(outFile);
                            while((read = jis.read(buff)) > 0) {
                                fos.write(buff, 0, read);
                            }
                            fos.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (jis != null) {
                    jis.close();
                }
            } catch (IOException e) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
