package github.saukiya.sxattribute.util;

import github.saukiya.sxattribute.SXAttribute;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Config {
    public static final String CONFIG_VERSION = "ConfigVersion";
    public static final String QAQ = "装逼启动信息";
    public static final String COMMAND_STATS_DISPLAY_SKULL_SKIN = "CommandStatsDisplaySkullSkin";
    public static final String DECIMAL_FORMAT = "DecimalFormat";

    public static final String HOLOGRAPHIC_ENABLED = "Holographic.Enabled";
    public static final String HOLOGRAPHIC_DISPLAY_TIME = "Holographic.DisplayTime";
    public static final String HOLOGRAPHIC_BLACK_CAUSE_LIST = "Holographic.BlackCauseList";
    public static final String HOLOGRAPHIC_HEALTH_TAKE_ENABLED = "Holographic.HealthOrTake.Enabled";

    public static final String HEALTH_NAME_VISIBLE_ENABLED = "Health.NameVisible.Enabled";
    public static final String HEALTH_NAME_VISIBLE_SIZE = "Health.NameVisible.Size";
    public static final String HEALTH_NAME_VISIBLE_CURRENT = "Health.NameVisible.Current";
    public static final String HEALTH_NAME_VISIBLE_LOSS = "Health.NameVisible.Loss";
    public static final String HEALTH_NAME_VISIBLE_PREFIX = "Health.NameVisible.Prefix";
    public static final String HEALTH_NAME_VISIBLE_SUFFIX = "Health.NameVisible.Suffix";
    public static final String HEALTH_NAME_VISIBLE_DISPLAY_TIME = "Health.NameVisible.DisplayTime";

    public static final String HEALTH_BOSS_BAR_ENABLED = "Health.BossBar.Enabled";
    public static final String HEALTH_BOSS_BAR_FORMAT = "Health.BossBar.Format";
    public static final String HEALTH_BOSS_BAR_DISPLAY_TIME = "Health.BossBar.DisplayTime";
    public static final String HEALTH_BOSS_BAR_BLACK_CAUSE_LIST = "Health.BossBar.BlackCauseList";

    public static final String DAMAGE_EVENT_BLACK_CAUSE_LIST = "DamageEvent.BlackCauseList";
    public static final String ITEM_DISPLAY_NAME = "ItemDisplayName";
    public static final String DAMAGE_CALCULATION_TO_EVE = "DamageCalculationToEVE";
    public static final String DAMAGE_GAUGES = "DamageGauges";
    public static final String BAN_SHIELD_DEFENSE = "BanShieldDefense";
    public static final String BOW_CLOSE_RANGE_ATTACK = "BanShieldDefense";
    public static final String CLEAR_DEFAULT_ATTRIBUTE = "ClearDefaultAttribute";

    public static final String PRG_INVENTORY_SLOT = "RPGInventorySlot";
    public static final String REPAIR_ITEM_VALUE = "RepairItemValue";
    public static final String REGISTER_SLOTS_ENABLED = "RegisterSlots.Enabled";
    public static final String REGISTER_SLOTS_LIST = "RegisterSlots.List";
    public static final String MINIMUM_DAMAGE = "MinimumDamage";
    public static final String DEFAULT_STATS = "DefaultAttribute";

    public static final String NAME_HAND_MAIN = "Condition.Hand.MainName";
    public static final String NAME_HAND_OFF = "Condition.Hand.OffName";
    public static final String NAME_ARMOR = "Condition.Armor";
    public static final String NAME_ROLE = "Condition.Role.Name";
    public static final String NAME_LIMIT_LEVEL = "Condition.LimitLevel.Name";
    public static final String NAME_DURABILITY = "Condition.Durability.Name";
    public static final String CLEAR_ITEM_DURABILITY = "Condition.Durability.ClearItem";
    public static final String NAME_SELL = "Condition.Sell.Name";
    public static final String NAME_EXPIRY_TIME = "Condition.ExpiryTime.Name";
    public static final String FORMAT_EXPIRY_TIME = "Condition.ExpiryTime.Format";

    public static final String ATTRIBUTE_PRIORITY = "AttributePriority";
    public static final String CONDITION_PRIORITY = "ConditionPriority";


    private static final File FILE = new File(SXAttribute.getPluginFile(), "Config.yml");
    @Getter
    private static YamlConfiguration config;
    @Getter
    private static boolean commandStatsDisplaySkullSkin;
    @Getter
    private static List<String> damageEventBlackList;
    @Getter
    private static boolean healthNameVisible;
    @Getter
    private static boolean healthBossBar;
    @Getter
    private static boolean holographic;
    @Getter
    private static List<String> holographicBlackList;
    @Getter
    private static boolean holographicHealthTake;
    @Getter
    private static boolean itemDisplayName;
    @Getter
    private static boolean damageCalculationToEVE;
    @Getter
    private static boolean damageGauges;
    @Getter
    private static boolean banShieldDefense;
    @Getter
    private static boolean bowCloseRangeAttack;
    @Getter
    private static List<Integer> rpgInvSlotList;
    @Getter
    private static boolean clearDefaultAttributePlugin;
    @Getter
    private static boolean registerSlot;
    @Getter
    private static double minimumDamage;
    @Getter
    private static List<String> bossBarBlackCauseList;
    @Getter
    private static boolean clearItemDurability;

    /**
     * 创建默认Config文件
     */
    private static void createDefaultConfig() {
        config.set(CONFIG_VERSION, SXAttribute.getPluginVersion());
        config.set(QAQ, true);
        config.set(COMMAND_STATS_DISPLAY_SKULL_SKIN, false);
        config.set(DECIMAL_FORMAT, "#.##");
        // 全息显示
        config.set(HOLOGRAPHIC_ENABLED, true);
        config.set(HOLOGRAPHIC_DISPLAY_TIME, 2);
        config.set(HOLOGRAPHIC_HEALTH_TAKE_ENABLED, false);
        // 血量头顶显示
        config.set(HEALTH_NAME_VISIBLE_ENABLED, true);
        config.set(HEALTH_NAME_VISIBLE_SIZE, 10);
        config.set(HEALTH_NAME_VISIBLE_CURRENT, "❤");
        config.set(HEALTH_NAME_VISIBLE_LOSS, "&7❤");
        config.set(HEALTH_NAME_VISIBLE_PREFIX, "&8[&c");
        config.set(HEALTH_NAME_VISIBLE_SUFFIX, "&8] &7- &8[&c{0}&8]");
        config.set(HEALTH_NAME_VISIBLE_DISPLAY_TIME, 4);
        // 血量显示
        config.set(HEALTH_BOSS_BAR_ENABLED, true);
        config.set(HEALTH_BOSS_BAR_FORMAT, "&a&l{0}:&8&l[&a&l{1}&7&l/&c&l{2}&8&l]");
        config.set(HEALTH_BOSS_BAR_DISPLAY_TIME, 4);
        config.set(HEALTH_BOSS_BAR_BLACK_CAUSE_LIST, Collections.singletonList("CUSTOM"));
        // 血条压缩
        config.set(DAMAGE_EVENT_BLACK_CAUSE_LIST, Collections.singletonList("CUSTOM"));
        // 展示物品名称
        config.set(ITEM_DISPLAY_NAME, true);
        // 怪V怪的属性计算
        config.set(DAMAGE_CALCULATION_TO_EVE, false);
        // 伤害计量器
        config.set(DAMAGE_GAUGES, true);
        // 禁止盾牌右键
        config.set(BAN_SHIELD_DEFENSE, false);
        // 允许弓近战
        config.set(BOW_CLOSE_RANGE_ATTACK, false);
        // 清除默认属性标签
        config.set(CLEAR_DEFAULT_ATTRIBUTE, true);
        // 读取的槽位
        config.set(PRG_INVENTORY_SLOT, Arrays.asList(4, 10, 13, 14, 19, 22, 28, 31, 47, 48, 49, 50, 51));
        // 修复价格
        config.set(REPAIR_ITEM_VALUE, 3.5);
        // 注册槽位
        config.set(REGISTER_SLOTS_ENABLED, true);
        config.set(REGISTER_SLOTS_LIST, Arrays.asList("17#戒指", "26#项链", "35#项链"));
        config.set(MINIMUM_DAMAGE, 1D);
        // 默认属性
        config.set(DEFAULT_STATS, Collections.singletonList("生命上限: 20"));

        config.set(NAME_HAND_MAIN, "主武器");
        config.set(NAME_HAND_OFF, "副武器");
        config.set(NAME_ARMOR, Arrays.asList("头盔", "盔甲", "护腿", "靴子"));
        config.set(NAME_ROLE, "限制职业");
        config.set(NAME_LIMIT_LEVEL, "限制等级");
        config.set(NAME_DURABILITY, "耐久度");
        config.set(CLEAR_ITEM_DURABILITY, false);
        config.set(NAME_SELL, "出售价格");
        config.set(NAME_EXPIRY_TIME, "到期时间");
        config.set(FORMAT_EXPIRY_TIME, "yyyy/MM/dd HH:mm");

        config.set(ATTRIBUTE_PRIORITY, Arrays.asList(
                "Dodge#SX-Attribute",
                "Damage",
                "Crit",
                "Real",
                "Defense",
                "Reflection",
                "Block",
                "LifeSteal",
                "Ignition",
                "AttackPotion",
                "Lightning",
                "Tearing",
                "Toughness",
                "Health",
                "HealthRegen",
                "HitRate",
                "ExpAddition",
                "Command",
                "WalkSpeed",
                "AttackSpeed",
                "MythicMobsDrop",
                "EventMessage"
        ));

        config.set(CONDITION_PRIORITY, Arrays.asList(
                "Hand",
                "MainHand",
                "OffHand",
                "LimitLevel",
                "Role",
                "ExpiryTime",
                "Durability"
        ));
    }

    /**
     * 检查版本更新
     *
     * @return boolean
     * @throws IOException IOException
     */
    private static boolean detectionVersion() throws IOException {
        if (!config.getString(CONFIG_VERSION, "").equals(SXAttribute.getPluginVersion())) {
            config.save(new File(FILE.toString().replace(".yml", "_" + config.getString(CONFIG_VERSION) + ".yml")));
            config = new YamlConfiguration();
            createDefaultConfig();
            return true;
        }
        return false;
    }

    /**
     * 加载Config类
     *
     * @throws IOException                   IOException
     * @throws InvalidConfigurationException InvalidConfigurationException
     */
    public static void loadConfig() throws IOException, InvalidConfigurationException {
        config = new YamlConfiguration();
        if (!FILE.exists()) {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cCreate Config.yml");
            createDefaultConfig();
            config.save(FILE);
        } else {
            config.load(FILE);
            if (detectionVersion()) {
                config.save(FILE);
                Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§eUpdate Config.yml");
            } else {
                Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Find Config.yml");
            }
        }
        SXAttribute.setDf(new DecimalFormat(config.getString(DECIMAL_FORMAT)));
        commandStatsDisplaySkullSkin = config.getBoolean(COMMAND_STATS_DISPLAY_SKULL_SKIN);
        healthNameVisible = config.getBoolean(HEALTH_NAME_VISIBLE_ENABLED);
        healthBossBar = config.getBoolean(HEALTH_BOSS_BAR_ENABLED) && SXAttribute.getVersionSplit()[1] >= 9;
        bossBarBlackCauseList = config.getStringList(HEALTH_BOSS_BAR_BLACK_CAUSE_LIST);
        holographic = config.getBoolean(HOLOGRAPHIC_ENABLED);
        holographicBlackList = config.getStringList(HOLOGRAPHIC_BLACK_CAUSE_LIST);
        damageEventBlackList = config.getStringList(DAMAGE_EVENT_BLACK_CAUSE_LIST);
        holographicHealthTake = config.getBoolean(HOLOGRAPHIC_HEALTH_TAKE_ENABLED);
        itemDisplayName = config.getBoolean(ITEM_DISPLAY_NAME);
        damageCalculationToEVE = config.getBoolean(DAMAGE_CALCULATION_TO_EVE);
        damageGauges = config.getBoolean(DAMAGE_GAUGES);
        clearDefaultAttributePlugin = config.getBoolean(CLEAR_DEFAULT_ATTRIBUTE);
        banShieldDefense = config.getBoolean(BAN_SHIELD_DEFENSE);
        bowCloseRangeAttack = config.getBoolean(BOW_CLOSE_RANGE_ATTACK);
        rpgInvSlotList = config.getIntegerList(PRG_INVENTORY_SLOT);
        registerSlot = config.getBoolean(REGISTER_SLOTS_ENABLED);
        minimumDamage = config.getDouble(MINIMUM_DAMAGE);
        clearItemDurability = config.getBoolean(CLEAR_ITEM_DURABILITY, true);
    }
}
