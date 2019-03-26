package github.saukiya.sxattribute.data.condition;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import github.saukiya.sxlevel.SXLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * 条件抽象类
 *
 * @author Saukiya
 */
public abstract class SubCondition {

    private static Map<Integer, SubCondition> registerConditionMap = new TreeMap<>();

    @Getter
    private static final String[] conditionPriority = Config.getConfig().getStringList(Config.CONDITION_PRIORITY).toArray(new String[0]);

    @Getter
    private final String name;

    @Getter
    private final JavaPlugin plugin;

    @NotNull
    private final SXConditionType[] conditionTypes;

    @Getter
    private int priority = -1;

    /**
     * 实现一个条件类
     *
     * @param plugin JavaPlugin 注册插件
     * @param type   SXConditionType[] 条件类型
     */
    public SubCondition(JavaPlugin plugin, SXConditionType... type) {
        this.name = getClass().getSimpleName();
        this.plugin = plugin;
        this.conditionTypes = type.length == 0 ? new SXConditionType[]{SXConditionType.ALL} : type;
        int length = conditionPriority.length;
        for (int i = 0; i < length; i++) {
            String[] args = conditionPriority[i].split("#");
            if (args[0].equals(getName())) {
                this.priority = args.length > 1 && !args[1].equals(getPlugin().getName()) ? -1 : i;
                break;
            }
        }
        registerCondition();
    }


    /**
     * 莫得介绍
     *
     * @return SubAttribute[]
     */
    static SubCondition[] getConditions() {
        return registerConditionMap.values().toArray(new SubCondition[0]);
    }


    /**
     * 设置优先级
     *
     * @param priority int
     */
    SubCondition setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * 获取物品名称
     *
     * @param item ItemStack
     * @return String
     */
    public static String getItemName(ItemStack item) {
        return item == null ? "N/A" : item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name();
    }

    /**
     * 获取物品等级
     *
     * @param item ItemStack
     * @return int 没有则为-1
     */
    public static int getItemLevel(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            return item.getItemMeta().getLore().stream().filter(lore -> lore.contains(Config.getConfig().getString(Config.NAME_LIMIT_LEVEL))).findFirst().map(lore -> Integer.valueOf(getNumber(lore).replace(".", ""))).orElse(-1);
        }
        return -1;
    }

    /**
     * 获取生物等级
     *
     * @param entity LivingEntity
     * @return int 非玩家则为-1
     */
    public static int getLevel(LivingEntity entity) {
        return entity instanceof Player ? SXAttribute.isSxLevel() ? SXLevel.getApi().getPlayerData((Player) entity).getLevel() : ((Player) entity).getLevel() : -1;
    }

    /**
     * 获取属性值
     *
     * @param lore String
     * @return String
     */
    public static String getNumber(String lore) {
        String str = lore.replaceAll("§+[a-z0-9]", "").replaceAll("[^-0-9.]", "");
        return str.length() == 0 ? "0" : str;
    }


    /**
     * 获取当前耐久值
     *
     * @param lore String
     * @return int
     */
    public static int getDurability(String lore) {
        return lore.contains("/") && lore.split("/").length > 1 ? Integer.valueOf(lore.replaceAll("§+[0-9]", "").split("/")[0].replaceAll("[^0-9]", "")) : 0;
    }

    /**
     * 获取最大耐久值
     *
     * @param lore String
     * @return int
     */
    public static int getMaxDurability(String lore) {
        return lore.contains("/") && lore.split("/").length > 1 ? Integer.valueOf(lore.replaceAll("§+[0-9]", "").split("/")[1].replaceAll("[^0-9]", "")) : 0;
    }

    /**
     * 获取物品是否为无限耐久
     *
     * @param meta ItemMeta
     * @return boolean
     */
    public static boolean getUnbreakable(ItemMeta meta) {
        return SXAttribute.getVersionSplit()[1] >= 11 ? meta.isUnbreakable() : meta.spigot().isUnbreakable();
    }

    /**
     * 获取类型
     *
     * @return SXConditionType[]
     */
    public final SXConditionType[] getType() {
        return conditionTypes.clone();
    }

    /**
     * 判断物品是否符合条件
     *
     * @param entity LivingEntity
     * @param item   ItemStack
     * @param lore   String
     * @return SXConditionReturnType
     */
    public abstract SXConditionReturnType determine(LivingEntity entity, ItemStack item, String lore);

    /**
     * 判断条件类型
     *
     * @param type        目标类型
     * @param strContains 是否判断字符串
     * @return boolean
     */
    public final boolean containsType(SXConditionType type, boolean strContains) {
        return type != null && Arrays.stream(this.conditionTypes).anyMatch(updateType -> updateType.equals(SXConditionType.ALL) || (strContains ? type.getName().contains(updateType.getName()) : updateType.equals(type)));
    }

    /**
     * 注册条件方法
     * 优先级需在SX-Attribute/Config.yml 设定
     */
    private void registerCondition() {
        if (getPlugin() == null) {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cCondition >> §7[§8NULL§4|§c" + getName() + "§7] §cNull Plugin!");
        } else if (this.getPriority() < 0) {
            Bukkit.getConsoleSender().sendMessage("[" + getPlugin().getName() + "] §8Condition >> Disable §7[§8" + getPlugin().getName() + "§4|§8" + getName() + "§7] §8!");
        } else if (Bukkit.getPluginManager().getPlugin("SX-Attribute").isEnabled()) {
            Bukkit.getConsoleSender().sendMessage("[" + getPlugin().getName() + "] §cCondition >> §cSXAttribute is Enabled , Unable to register §7[§c" + getPlugin().getName() + "§4|§c" + getName() + "§7]§c !");
        } else {
            SubCondition condition = registerConditionMap.put(getPriority(), this);
            if (condition == null) {
                Bukkit.getConsoleSender().sendMessage("[" + getPlugin().getName() + "] Condition >> Register §7[§c" + getPlugin().getName() + "§4|§c" + getName() + "§7] §rTo Priority §c" + getPriority() + " §r!");
            } else {
                Bukkit.getConsoleSender().sendMessage("[" + getPlugin().getName() + "] Condition >> §cThe §7[§c" + getPlugin().getName() + "§4|§c" + getName() + "§7] §cCover To §7[§c" + condition.getPlugin().getName() + "§4|§c" + condition.getName() + "§7]§r !");
            }
        }
    }

    /**
     * 条件注册成功后启动时执行的方法
     */
    public void onEnable() {

    }

    /**
     * 条件注册成功后关闭时执行的方法
     */
    public void onDisable() {

    }

}
