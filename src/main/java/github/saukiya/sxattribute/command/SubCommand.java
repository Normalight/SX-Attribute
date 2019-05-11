package github.saukiya.sxattribute.command;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.util.Message;
import lombok.Getter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * 子指令抽象类
 *
 * @author Saukiya
 */
public abstract class SubCommand {

    static final CommandList subCommands = new CommandList();

    @Getter
    private JavaPlugin plugin;

    private String cmd, arg = "";

    private boolean hide = false;

    private SenderType[] senderTypes = new SenderType[]{SenderType.ALL};

    /**
     * @param cmd         String
     * @param arg         String
     * @param senderTypes SenderType[]
     */
    public SubCommand(String cmd, String arg, SenderType... senderTypes) {
        this.cmd = cmd;
        this.arg = arg;
        this.senderTypes = senderTypes;
    }

    /**
     * @param cmd         String
     * @param hide        Boolean
     * @param senderTypes SenderType[]
     */
    public SubCommand(String cmd, Boolean hide, SenderType... senderTypes) {
        this.cmd = cmd;
        this.hide = hide;
        this.senderTypes = senderTypes;
    }

    /**
     * @param cmd         String
     * @param senderTypes SenderType[]
     */
    public SubCommand(String cmd, SenderType... senderTypes) {
        this.cmd = cmd;
        this.senderTypes = senderTypes;
    }

    /**
     * @param cmd String
     */
    public SubCommand(String cmd) {
        this.cmd = cmd;
    }

    /**
     * 注册指令方法
     *
     * @param plugin JavaPlugin
     */
    public final void registerCommand(JavaPlugin plugin) {
        if (plugin == null) {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cCommand >> §4" + this.cmd() + " §cNull Plugin!");
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("SX-Attribute").isEnabled()) {
            Bukkit.getConsoleSender().sendMessage("[" + plugin.getName() + "] §cCommand >> §cSXAttribute is Enabled §4" + this.cmd() + "§r !");
            return;
        }
        this.plugin = plugin;
        subCommands.add(this);
    }

    public String cmd() {
        return cmd;
    }

    public String arg() {
        return arg;
    }

    public boolean hide() {
        return hide;
    }

    private String permission() {
        return plugin.getName() + "." + cmd;
    }

    /**
     * 执行指令抽象方法
     *
     * @param plugin SXAttribute
     * @param sender CommandSender
     * @param args   String[]
     */
    public abstract void onCommand(SXAttribute plugin, CommandSender sender, String[] args);


    /**
     * 指令注册成功后启动时执行的方法
     */
    public void onEnable() {

    }

    /**
     * 指令关闭时执行的方法
     */
    public void onDisable() {

    }

    /**
     * TAB执行方法
     *
     * @param plugin SXAttribute
     * @param sender CommandSender
     * @param args   String[]
     * @return List
     */
    public List<String> onTabComplete(SXAttribute plugin, CommandSender sender, String[] args) {
        return null;
    }

    /**
     * 判断是否可用
     *
     * @param sender CommandSender
     * @param type   SenderType
     * @return boolean
     */
    public boolean isUse(CommandSender sender, SenderType type) {
        return sender.hasPermission(permission()) && Arrays.stream(this.senderTypes).anyMatch(senderType -> senderType.equals(SenderType.ALL) || senderType.equals(type));
    }

    /**
     * 输出介绍
     *
     * @param sender CommandSender
     * @param color  String
     * @param label  String
     */
    public void sendIntroduction(CommandSender sender, String color, String label) {
        String clickCommand = MessageFormat.format("/{0} {1}", label, cmd());
        TextComponent tc = Message.Tool.getTextComponent(color + MessageFormat.format("/{0} {1}{2}§7 - §c" + Message.getMsg(Message.valueOf("COMMAND__" + cmd().toUpperCase())), label, cmd(), arg()), clickCommand, sender.isOp() ? "§8§oPermission: " + permission() : null);
        sender.spigot().sendMessage(tc);
    }

    protected Player getPlayer(CommandSender sender, Player player) {
        if (player == null) {
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(Message.getMsg(Message.ADMIN__NO_CONSOLE));
                return null;
            }
        }
        return player;
    }
}
