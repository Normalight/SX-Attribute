package github.saukiya.sxattribute;

import github.saukiya.sxattribute.api.Sx;
import github.saukiya.sxattribute.bstats.Metrics;
import github.saukiya.sxattribute.command.MainCommand;
import github.saukiya.sxattribute.data.RandomStringManager;
import github.saukiya.sxattribute.data.SlotDataManager;
import github.saukiya.sxattribute.data.attribute.SXAttributeManager;
import github.saukiya.sxattribute.data.attribute.sub.attack.*;
import github.saukiya.sxattribute.data.attribute.sub.other.EventMessage;
import github.saukiya.sxattribute.data.attribute.sub.defence.*;
import github.saukiya.sxattribute.data.attribute.sub.other.ExpAddition;
import github.saukiya.sxattribute.data.attribute.sub.other.MythicMobsDrop;
import github.saukiya.sxattribute.data.attribute.sub.update.AttackSpeed;
import github.saukiya.sxattribute.data.attribute.sub.update.Command;
import github.saukiya.sxattribute.data.attribute.sub.update.WalkSpeed;
import github.saukiya.sxattribute.data.condition.SXConditionManager;
import github.saukiya.sxattribute.data.condition.sub.*;
import github.saukiya.sxattribute.data.itemdata.ItemDataManager;
import github.saukiya.sxattribute.data.itemdata.sub.ImportItemData;
import github.saukiya.sxattribute.data.itemdata.sub.SXItemData;
import github.saukiya.sxattribute.listener.*;
import github.saukiya.sxattribute.util.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * SX-Attribute
 *
 * @author Saukiya
 * <p>
 * 该插件只发布与MCBBS。
 */


@Getter
public class SXAttribute extends JavaPlugin {

    @Getter
    private static int[] versionSplit = new int[3];

    @Getter
    private static Random random = new Random();

    @Getter
    private static String pluginName;
    @Getter
    private static String pluginVersion;
    @Getter
    private static File pluginFile;

    @Getter
    @Setter
    private static DecimalFormat df = new DecimalFormat("#.##");
    @Getter
    private static boolean tabooLib = false;
    @Getter
    private static boolean placeholder = false;
    @Getter
    private static boolean holographic = false;
    @Getter
    private static boolean vault = false;
    @Getter
    private static boolean rpgInventory = false;
    @Getter
    private static boolean sxLevel = false;
    @Getter
    private static boolean mythicMobs = false;


    private NbtUtil nbtUtil;

    private MainCommand mainCommand;

    private SXAttributeManager attributeManager;

    private SXConditionManager conditionManager;

    private RandomStringManager randomStringManager;

    private ItemDataManager itemDataManager;

    private SlotDataManager slotDataManager;

    private OnUpdateStatsListener onUpdateStatsListener;

    private OnDamageListener onDamageListener;

    private OnHealthChangeListener onHealthChangeListener;

    @Override
    public void onLoad() {
        super.onLoad();
        pluginFile = getDataFolder();
        pluginName = getName();
        pluginVersion = this.getDescription().getVersion();
        String version = Bukkit.getBukkitVersion().split("-")[0].replace(" ", "");
        String[] strSplit = version.split("[.]");
        IntStream.range(0, strSplit.length).forEach(i -> versionSplit[i] = Integer.valueOf(strSplit[i]));
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "ServerVersion: " + version);
        new Sx(this);
        try {
            Config.loadConfig();
            Message.loadMessage();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cIO Error!");
        }
        mainCommand = new MainCommand(this);

        new Crit(this);
        new Damage(this);
        new HitRate(this);
        new Ignition(this);
        new LifeSteal(this);
        new Lightning(this);
        new AttackPotion(this);
        new Real(this);
        new Tearing(this);

        new Block(this);
        new Defense(this);
        new Dodge(this);
        new Reflection(this);
        new Toughness(this);

        new EventMessage(this);
        new ExpAddition(this);
        new MythicMobsDrop(this);
        new HealthRegen(this);

        new Health(this);
        new WalkSpeed(this);
        new AttackSpeed(this);
        new Command(this);

        new MainHand(this);
        new OffHand(this);
        new Hand(this);
        new LimitLevel(this);
        new Role(this);
        new ExpiryTime(this);
        new Durability(this);

        ItemDataManager.registerGenerator(new ImportItemData(this));
        ItemDataManager.registerGenerator(new SXItemData(this));
    }

    @Override
    public void onEnable() {
        long oldTimes = System.currentTimeMillis();
        if (Bukkit.getPluginManager().isPluginEnabled("TabooLib")) {
            tabooLib = true;
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Find TabooLib");
        } else {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cNo Find TabooLib!");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            placeholder = true;
            new Placeholders(this);
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Find Placeholders");
        } else {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cNo Find PlaceholderAPI!");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
            try {
                MoneyUtil.setup();
                vault = true;
                Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Find Vault");
            } catch (NullPointerException e) {
                Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cNo Find Vault-Economy!");
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cNo Find Vault!");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays")) {
            holographic = true;
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Find HolographicDisplays");
        } else {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cNo Find HolographicDisplays!");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            mythicMobs = true;
            Bukkit.getPluginManager().registerEvents(new OnMythicmobsSpawnListener(this), this);
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Find MythicMobs");
        } else {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cNo Find MythicMobs!");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("RPGInventory")) {
            rpgInventory = true;
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Find RPGInventory");
        } else {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cNo Find RPGInventory!");
        }

        if (Bukkit.getPluginManager().isPluginEnabled("SX-Level")) {
            sxLevel = true;
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Find SX-Level");
        } else {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cNo Find SX-Level!");
        }

        new Metrics(this);
        try {
            nbtUtil = new NbtUtil();
            randomStringManager = new RandomStringManager(this);
            itemDataManager = new ItemDataManager(this);
        } catch (IOException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cIO Error!");
            this.setEnabled(false);
            return;
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cReflection Error!");
            this.setEnabled(false);
            return;
        }

        attributeManager = new SXAttributeManager(this);
        conditionManager = new SXConditionManager();
        slotDataManager = new SlotDataManager();
        onUpdateStatsListener = new OnUpdateStatsListener(this);
        onDamageListener = new OnDamageListener(this);
        onHealthChangeListener = new OnHealthChangeListener(this);
        Bukkit.getPluginManager().registerEvents(new OnBanShieldInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(onUpdateStatsListener, this);
        Bukkit.getPluginManager().registerEvents(onDamageListener, this);
        Bukkit.getPluginManager().registerEvents(onHealthChangeListener, this);
        Bukkit.getPluginManager().registerEvents(new OnItemSpawnListener(), this);
        mainCommand.setUp("sxAttribute");
        mainCommand.onCommandEnable();
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Load Time: §c" + (System.currentTimeMillis() - oldTimes) + "§r ms");
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cAuthor: Saukiya QQ: 1940208750");
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cThis plugin was first launched on www.mcbbs.net!");
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§4Reprint is prohibited without permission!");
        if (Config.getConfig().getBoolean(Config.QAQ)) {
            Bukkit.getConsoleSender().sendMessage("§c");
            Bukkit.getConsoleSender().sendMessage("§c   ______  __             ___   __  __       _ __          __");
            Bukkit.getConsoleSender().sendMessage("§c  / ___/ |/ /            /   | / /_/ /______(_) /_  __  __/ /____");
            Bukkit.getConsoleSender().sendMessage("§c  \\__ \\|   /   ______   / /| |/ __/ __/ ___/ / __ \\/ / / / __/ _ \\");
            Bukkit.getConsoleSender().sendMessage("§c ___/ /   |   /_____/  / ___ / /_/ /_/ /  / / /_/ / /_/ / /_/  __/");
            Bukkit.getConsoleSender().sendMessage("§c/____/_/|_|           /_/  |_\\__/\\__/_/  /_/_.___/\\__,_/\\__/\\___/");
            Bukkit.getConsoleSender().sendMessage("§c");
        }
    }

    @Override
    public void onDisable() {
        attributeManager.onAttributeDisable();
        conditionManager.onConditionDisable();
        mainCommand.onCommandDisable();
        onHealthChangeListener.cancel();
    }
}
