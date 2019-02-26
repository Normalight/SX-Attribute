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
    public static final String QAQ = "QAQ";
    public static final String HOLOGRAPHIC_DISPLAY_TIME = "Holographic.DisplayTime";
    public static final String COMMAND_STATS_DISPLAY_SKULL_SKIN = "CommandStatsDisplaySkullSkin";
    public static final String HEALTH_NAME_VISIBLE_SIZE = "Health.NameVisible.Size";
    public static final String HEALTH_NAME_VISIBLE_CURRENT = "Health.NameVisible.Current";
    public static final String HEALTH_NAME_VISIBLE_LOSS = "Health.NameVisible.Loss";
    public static final String HEALTH_NAME_VISIBLE_PREFIX = "Health.NameVisible.Prefix";
    public static final String HEALTH_NAME_VISIBLE_SUFFIX = "Health.NameVisible.Suffix";
    public static final String HEALTH_NAME_VISIBLE_DISPLAY_TIME = "Health.NameVisible.DisplayTime";
    public static final String HEALTH_BOSS_BAR_FORMAT = "Health.BossBar.Format";
    public static final String HEALTH_BOSS_BAR_DISPLAY_TIME = "Health.BossBar.DisplayTime";
    public static final String HEALTH_SCALED_VALUE = "HealthScaled.Value";
    public static final String PRG_INVENTORY_SLOT = "RPGInventorySlot.List";
    public static final String REPAIR_ITEM_VALUE = "RepairItemValue";
    public static final String REGISTER_SLOTS_LIST = "RegisterSlots.List";
    public static final String REGISTER_SLOTS_LOCK_NAME = "RegisterSlots.Lock.Name";
    public static final String DEFAULT_STATS = "DefaultAttribute";
    public static final String NAME_HAND_MAIN = "Condition.Hand.InMain.Name";
    public static final String NAME_HAND_OFF = "Condition.Hand.InOff.Name";
    public static final String NAME_ARMOR = "Condition.Armor";
    public static final String NAME_ROLE = "Condition.Role.Name";
    public static final String NAME_LIMIT_LEVEL = "Condition.LimitLevel.Name";
    public static final String NAME_DURABILITY = "Condition.Durability.Name";
    public static final String CLEAR_ITEM_DURABILITY = "Condition.Durability.ClearItem";
    public static final String NAME_SELL = "Condition.Sell.Name";
    public static final String NAME_EXPIRY_TIME = "Condition.ExpiryTime.Name";
    public static final String FORMAT_EXPIRY_TIME = "Condition.ExpiryTime.Format";

    public static final String NAME_EXP_ADDITION = "Attribute.ExpAddition.Name";
    public static final String NAME_WALK_SPEED = "Attribute.WalkSpeed.Name";
    public static final String NAME_ATTACK_SPEED = "Attribute.AttackSpeed.Name";
    public static final String DEFAULT_ATTACK_SPEED = "Attribute.AttackSpeed.Default";
    public static final String NAME_HEALTH = "Attribute.Health.Name";
    public static final String NAME_HEALTH_REGEN = "Attribute.HealthRegen.Name";
    public static final String NAME_DODGE = "Attribute.Dodge.Name";
    public static final String NAME_DEFENSE = "Attribute.Defense.Name";
    public static final String NAME_PVP_DEFENSE = "Attribute.PVPDefense.Name";
    public static final String NAME_PVE_DEFENSE = "Attribute.PVEDefense.Name";
    public static final String NAME_TOUGHNESS = "Attribute.Toughness.Name";
    public static final String NAME_REFLECTION_RATE = "Attribute.ReflectionRate.Name";
    public static final String NAME_REFLECTION = "Attribute.Reflection.Name";
    public static final String NAME_BLOCK_RATE = "Attribute.BlockRate.Name";
    public static final String NAME_BLOCK = "Attribute.Block.Name";
    public static final String NAME_DAMAGE = "Attribute.Damage.Name";
    public static final String NAME_PVP_DAMAGE = "Attribute.PVPDamage.Name";
    public static final String NAME_PVE_DAMAGE = "Attribute.PVEDamage.Name";
    public static final String NAME_HIT_RATE = "Attribute.HitRate.Name";
    public static final String NAME_REAL = "Attribute.Real.Name";
    public static final String NAME_CRIT_RATE = "Attribute.Crit.Name";
    public static final String NAME_CRIT = "Attribute.CritDamage.Name";
    public static final String NAME_LIFE_STEAL = "Attribute.LifeSteal.Name";
    public static final String NAME_LIFE_STEAL_RATE = "Attribute.LifeStealRate.Name";
    public static final String NAME_IGNITION = "Attribute.Ignition.Name";
    public static final String NAME_WITHER = "Attribute.Wither.Name";
    public static final String NAME_POISON = "Attribute.Poison.Name";
    public static final String NAME_BLINDNESS = "Attribute.Potion.Name";
    public static final String NAME_SLOWNESS = "Attribute.Slowness.Name";
    public static final String NAME_LIGHTNING = "Attribute.Lightning.Name";
    public static final String NAME_TEARING = "Attribute.Tearing.Name";
    public static final String VALUE_EXP_ADDITION = "Attribute.ExpAddition.Value";
    public static final String VALUE_WALK_SPEED = "Attribute.WalkSpeed.Value";
    public static final String VALUE_ATTACK_SPEED = "Attribute.AttackSpeed.Value";
    public static final String VALUE_HEALTH = "Attribute.Health.Value";
    public static final String VALUE_HEALTH_REGEN = "Attribute.HealthRegen.Value";
    public static final String VALUE_DODGE = "Attribute.Dodge.Value";
    public static final String VALUE_DEFENSE = "Attribute.Defense.Value";
    public static final String VALUE_PVP_DEFENSE = "Attribute.PVPDefense.Value";
    public static final String VALUE_PVE_DEFENSE = "Attribute.PVEDefense.Value";
    public static final String VALUE_TOUGHNESS = "Attribute.Toughness.Value";
    public static final String VALUE_REFLECTION_RATE = "Attribute.ReflectionRate.Value";
    public static final String VALUE_REFLECTION = "Attribute.Reflection.Value";
    public static final String VALUE_BLOCK_RATE = "Attribute.BlockRate.Value";
    public static final String VALUE_BLOCK = "Attribute.Block.Value";
    public static final String VALUE_DAMAGE = "Attribute.Damage.Value";
    public static final String VALUE_PVE_DAMAGE = "Attribute.PVPDamage.Value";
    public static final String VALUE_PVP_DAMAGE = "Attribute.PVEDamage.Value";
    public static final String VALUE_HIT_RATE = "Attribute.HitRate.Value";
    public static final String VALUE_REAL = "Attribute.Real.Value";
    public static final String VALUE_CRIT_RATE = "Attribute.Crit.Value";
    public static final String VALUE_CRIT = "Attribute.CritDamage.Value";
    public static final String VALUE_LIFE_STEAL = "Attribute.LifeSteal.Value";
    public static final String VALUE_LIFE_STEAL_RATE = "Attribute.LifeStealRate.Value";
    public static final String VALUE_IGNITION = "Attribute.Ignition.Value";
    public static final String VALUE_WITHER = "Attribute.Wither.Value";
    public static final String VALUE_POISON = "Attribute.Poison.Value";
    public static final String VALUE_BLINDNESS = "Attribute.Potion.Value";
    public static final String VALUE_SLOWNESS = "Attribute.Slowness.Value";
    public static final String VALUE_LIGHTNING = "Attribute.Lightning.Value";
    public static final String VALUE_TEARING = "Attribute.Tearing.Value";
    public static final String ATTRIBUTE_PRIORITY = "AttributePriority";
    public static final String CONDITION_PRIORITY = "ConditionPriority";

    public static final String CONFIG_VERSION = "ConfigVersion";
    public static final String DECIMAL_FORMAT = "DecimalFormat";


    public static final String REGISTER_SLOTS_ENABLED = "RegisterSlots.Enabled";
    public static final String REGISTER_SLOTS_LOCK_ENABLED = "RegisterSlots.Lock.Enabled";
    public static final String HOLOGRAPHIC_ENABLED = "Holographic.Enabled";
    public static final String HOLOGRAPHIC_BLACK_CAUSE_LIST = "Holographic.BlackCauseList";
    public static final String HOLOGRAPHIC_HEALTH_TAKE_ENABLED = "Holographic.HealthOrTake.Enabled";
    public static final String HEALTH_NAME_VISIBLE_ENABLED = "Health.NameVisible.Enabled";
    public static final String HEALTH_BOSS_BAR_ENABLED = "Health.BossBar.Enabled";
    public static final String HEALTH_BOSS_BAR_BLACK_CAUSE_LIST = "Health.BossBar.BlackCauseList";
    public static final String DAMAGE_EVENT_BLACK_CAUSE_LIST = "DamageEvent.BlackCauseList";
    public static final String HEALTH_SCALED_ENABLED = "HealthScaled.Enabled";
    public static final String ITEM_DISPLAY_NAME = "ItemDisplayName";
    public static final String DAMAGE_CALCULATION_TO_EVE = "DamageCalculationToEVE";
    public static final String DAMAGE_GAUGES = "DamageGauges";
    public static final String BAN_SHIELD_DEFENSE = "BanShieldDefense";
    public static final String CLEAR_DEFAULT_ATTRIBUTE_THIS_PLUGIN = "ClearDefaultAttribute.ThisPlugin";

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
    private static boolean healthScaled;
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
    private static boolean clearDefaultAttributePlugin;
    @Getter
    private static boolean registerSlot;
    @Getter
    private static boolean registerSlotsLock;
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
        config.set(HOLOGRAPHIC_BLACK_CAUSE_LIST, Arrays.asList("ENTITY_SWEEP_ATTACK", "POISON"));
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
        config.set(HEALTH_BOSS_BAR_BLACK_CAUSE_LIST, Arrays.asList("ENTITY_SWEEP_ATTACK", "POISON"));
        // 血条压缩
        config.set(HEALTH_SCALED_ENABLED, true);
        config.set(HEALTH_SCALED_VALUE, 40);
        config.set(DAMAGE_EVENT_BLACK_CAUSE_LIST, Collections.singletonList("CUSTOM"));
        // 展示物品名称
        config.set(ITEM_DISPLAY_NAME, true);
        // 怪V怪的属性计算
        config.set(DAMAGE_CALCULATION_TO_EVE, false);
        // 伤害计量器
        config.set(DAMAGE_GAUGES, true);
        // 伤害计量器
        config.set(DAMAGE_GAUGES, true);
        // 禁止盾牌右键
        config.set(BAN_SHIELD_DEFENSE, false);
        // 清除默认属性标签
        config.set(CLEAR_DEFAULT_ATTRIBUTE_THIS_PLUGIN, true);
        // 读取的槽位
        config.set(PRG_INVENTORY_SLOT, Arrays.asList(4, 10, 13, 14, 19, 22, 28, 31, 47, 48, 49, 50, 51));
        // 修复价格
        config.set(REPAIR_ITEM_VALUE, 3.5);
        // 注册槽位
        config.set(REGISTER_SLOTS_ENABLED, false);
        config.set(REGISTER_SLOTS_LIST, Arrays.asList("17#戒指#物品锁槽编号", "26#项链#物品锁槽编号", "35#项链#物品锁槽编号"));
        // 是否锁槽
        config.set(REGISTER_SLOTS_LOCK_ENABLED, false);
        config.set(REGISTER_SLOTS_LOCK_NAME, "&7&o%SlotName%槽");
        // 默认属性
        config.set(DEFAULT_STATS, Collections.singletonList("生命上限: 20"));

        config.set(NAME_HAND_MAIN, "主武器");
        config.set(NAME_HAND_OFF, "副武器");
        config.set(NAME_ARMOR, Arrays.asList("头盔", "盔甲", "护腿", "靴子"));
        config.set(NAME_ROLE, "限制职业");
        config.set(NAME_LIMIT_LEVEL, "限制等级");
        config.set(NAME_EXP_ADDITION, "经验加成");
        config.set(NAME_DURABILITY, "耐久度");
        config.set(CLEAR_ITEM_DURABILITY, true);
        config.set(NAME_SELL, "出售价格");
        config.set(NAME_EXPIRY_TIME, "到期时间");
        config.set(FORMAT_EXPIRY_TIME, "yyyy/MM/dd HH:mm");
        config.set(NAME_WALK_SPEED, "移速增幅");
        config.set(NAME_ATTACK_SPEED, "攻速增幅");
        config.set(DEFAULT_ATTACK_SPEED, 3.5);

        config.set(NAME_HEALTH, "生命上限");
        config.set(NAME_HEALTH_REGEN, "生命恢复");
        config.set(NAME_DODGE, "闪避几率");
        config.set(NAME_DEFENSE, "防御力");
        config.set(NAME_PVP_DEFENSE, "PVP防御力");
        config.set(NAME_PVE_DEFENSE, "PVE防御力");
        config.set(NAME_TOUGHNESS, "韧性");
        config.set(NAME_REFLECTION_RATE, "反射几率");
        config.set(NAME_REFLECTION, "反射伤害");
        config.set(NAME_BLOCK_RATE, "格挡几率");
        config.set(NAME_BLOCK, "格挡伤害");

        config.set(NAME_DAMAGE, "攻击力");
        config.set(NAME_PVP_DAMAGE, "PVP攻击力");
        config.set(NAME_PVE_DAMAGE, "PVE攻击力");
        config.set(NAME_HIT_RATE, "命中几率");
        config.set(NAME_REAL, "破甲几率");
        config.set(NAME_CRIT_RATE, "暴击几率");
        config.set(NAME_CRIT, "暴伤增幅");
        config.set(NAME_LIFE_STEAL_RATE, "吸血几率");
        config.set(NAME_LIFE_STEAL, "吸血倍率");
        config.set(NAME_IGNITION, "点燃几率");
        config.set(NAME_WITHER, "凋零几率");
        config.set(NAME_POISON, "中毒几率");
        config.set(NAME_BLINDNESS, "失明几率");
        config.set(NAME_SLOWNESS, "缓慢几率");
        config.set(NAME_LIGHTNING, "雷霆几率");
        config.set(NAME_TEARING, "撕裂几率");

        config.set(VALUE_EXP_ADDITION, 1);
        config.set(VALUE_WALK_SPEED, 1);

        config.set(VALUE_HEALTH, 1);
        config.set(VALUE_HEALTH_REGEN, 1);
        config.set(VALUE_DODGE, 1);
        config.set(VALUE_DEFENSE, 1);
        config.set(VALUE_PVP_DEFENSE, 1);
        config.set(VALUE_PVE_DEFENSE, 1);
        config.set(VALUE_TOUGHNESS, 1);
        config.set(VALUE_REFLECTION_RATE, 1);
        config.set(VALUE_REFLECTION, 1);
        config.set(VALUE_BLOCK_RATE, 1);
        config.set(VALUE_BLOCK, 1);

        config.set(VALUE_DAMAGE, 1);
        config.set(VALUE_PVP_DAMAGE, 1);
        config.set(VALUE_PVE_DAMAGE, 1);
        config.set(VALUE_HIT_RATE, 1);
        config.set(VALUE_REAL, 1);
        config.set(VALUE_CRIT_RATE, 1);
        config.set(VALUE_CRIT, 1);
        config.set(VALUE_LIFE_STEAL, 1);
        config.set(VALUE_LIFE_STEAL_RATE, 1);
        config.set(VALUE_IGNITION, 1);
        config.set(VALUE_WITHER, 1);
        config.set(VALUE_POISON, 1);
        config.set(VALUE_BLINDNESS, 1);
        config.set(VALUE_SLOWNESS, 1);
        config.set(VALUE_LIGHTNING, 1);
        config.set(VALUE_TEARING, 1);

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
//                "Wither",
//                "Poison",
                "Potion",
//                "Slowness",
                "Lightning",
                "Tearing",
                "Toughness",
                "Health",
                "HealthRegen",
                "HitRate",
                "ExpAddition",
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
        healthBossBar = config.getBoolean(HEALTH_BOSS_BAR_ENABLED);
        bossBarBlackCauseList = config.getStringList(HEALTH_BOSS_BAR_BLACK_CAUSE_LIST);
        healthScaled = config.getBoolean(HEALTH_SCALED_ENABLED);
        holographic = config.getBoolean(HOLOGRAPHIC_ENABLED);
        holographicBlackList = config.getStringList(HOLOGRAPHIC_BLACK_CAUSE_LIST);
        damageEventBlackList = config.getStringList(DAMAGE_EVENT_BLACK_CAUSE_LIST);
        holographicHealthTake = config.getBoolean(HOLOGRAPHIC_HEALTH_TAKE_ENABLED);
        itemDisplayName = config.getBoolean(ITEM_DISPLAY_NAME);
        damageCalculationToEVE = config.getBoolean(DAMAGE_CALCULATION_TO_EVE);
        damageGauges = config.getBoolean(DAMAGE_GAUGES);
        clearDefaultAttributePlugin = config.getBoolean(CLEAR_DEFAULT_ATTRIBUTE_THIS_PLUGIN);
        banShieldDefense = config.getBoolean(BAN_SHIELD_DEFENSE);
        registerSlot = config.getBoolean(REGISTER_SLOTS_ENABLED);
        registerSlotsLock = registerSlot && config.getBoolean(REGISTER_SLOTS_LOCK_ENABLED);
        clearItemDurability = config.getBoolean(CLEAR_ITEM_DURABILITY, true);
    }
}
