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
        //读取配置
        reload();
        //McMenuImpl
        ((McMenuImpl)McMenuApi.mcMenu).initOnLoad();
        //注册服务
        getServer().getServiceManager().register(CommandService.class, new McCommandService(), this, ServicePriority.NORMAL);
    }

    @Override
    public void onEnable() {
        //McMenuImpl
        ((McMenuImpl)McMenuApi.mcMenu).initOnEnable();
        //注册事件
        getServer().getPluginManager().registerEvents(this, this);
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
            if (args[0].equalsIgnoreCase("reload")) {
                if (p != null && !p.isOp()) {
                    Util.send(sender, 5000);
                    return false;
                }
                McMenuApi.reload();
                //提示
                Util.send(sender, 5010);
                return true;
            }else if (args[0].equalsIgnoreCase("get")) {
                if (args.length >= 2) {
                    if (p == null) {
                        Util.send(sender, 5020);
                        return false;
                    }
                    String menuName = args[1];
                    cmdGet(p, menuName);
                    return true;
                }
            }else if (args[0].equalsIgnoreCase("join")) {
                if (args.length >= 2) {
                    if (p == null) {
                        Util.send(sender, 5020);
                        return false;
                    }
                    String menuName = args[1];
                    int slot;
                    if (args.length >= 3) {
                        slot = Integer.parseInt(args[2]);
                    }else {
                        Menu menu = McMenuApi.getMenuManager().getMenu(p);
                        slot = menu != null?menu.getSlot():p.getInventory().getHeldItemSlot();
                    }
                    cmdJoin(p, menuName, slot);
                    return true;
                }
            }else if (args[0].equalsIgnoreCase("left")) {
                if (p == null) {
                    Util.send(sender, 5020);
                    return false;
                }
                cmdLeft(p);
                return true;
            }else if (args[0].equalsIgnoreCase("right")) {
                if (p == null) {
                    Util.send(sender, 5020);
                    return false;
                }
                cmdRight(p);
                return true;
            }else if (args[0].equalsIgnoreCase("confirm")) {
                if (p == null) {
                    Util.send(sender, 5020);
                    return false;
                }
                cmdConfirm(p);
                return true;
            }else if (args[0].equalsIgnoreCase("back")) {
                if (p == null) {
                    Util.send(sender, 5020);
                    return false;
                }
                cmdBack(p);
                return true;
            }else if (args[0].equalsIgnoreCase("exit")) {
                if (p == null) {
                    Util.send(sender, 5020);
                    return false;
                }
                cmdExit(p);
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
        if (!p.isOp() && !config.isGetCmd()) {
            Util.send(p, 5030);
            return;
        }
        //请将要设置菜单的物品放在手上
        Item item = p.getInventory().getItemInHand();
        if (item == null || item.getId() == 0) {
            Util.send(p, 5040);
            return;
        }
        //非普通物品
        if (!isNormalItem(item)) {
            Util.send(p, 5050);
            return;
        }
        //菜单不存在
        MenuFactory menuFactory = McMenuApi.getMenuManager().getMenuFactory(menuName);
        if (menuFactory == null) {
            Util.send(p, 5060, menuName);
            return;
        }
        //获取
        try {
            item = McMenuApi.getItemManager().saveMenuInfo(item, menuName);
            p.getInventory().setItemInHand(item);
            //提示
            Util.send(p, 5070, menuName);
        } catch (Exception e) {
            e.printStackTrace();
            Util.send(p, 5080, e.getMessage());
        }
    }

    /**
     * 是否是普通物品
     */
    private boolean isNormalItem(Item item) {
        return !item.hasCompoundTag();
    }

    private void cmdJoin(Player p, String menuName, int slot) {
        McMenuApi.getActionManager().join(p, slot, menuName);
    }

    private void cmdLeft(Player p) {
        McMenuApi.getActionManager().left(p);
    }

    private void cmdRight(Player p) {
        McMenuApi.getActionManager().right(p);
    }

    private void cmdConfirm(Player p) {
        McMenuApi.getActionManager().confirm(p);
    }

    private void cmdBack(Player p) {
        McMenuApi.getActionManager().back(p);
    }

    private void cmdExit(Player p) {
        McMenuApi.getActionManager().exit(p);
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
            Util.info(1, "插件配置目录存在,不生成文件.");
            return;
        }
        Util.info(1, "生成文件.");

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
