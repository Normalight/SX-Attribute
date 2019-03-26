package github.saukiya.sxattribute.data.attribute;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 属性抽象类
 *
 * @author Saukiya
 */
public abstract class SubAttribute extends Message.Tool {

    static Map<Integer, SubAttribute> registerAttributeMap = new TreeMap<>();

    static String[] priorityList = Config.getConfig().getStringList(Config.ATTRIBUTE_PRIORITY).toArray(new String[0]);

    @Setter
    @Getter
    private static String firstPerson;

    @Getter
    private static DecimalFormat df = SXAttribute.getDf();

    @Getter
    private final String name;

    @Getter
    private final JavaPlugin plugin;

    @NotNull
    private final SXAttributeType[] attributeTypes;

    @Getter
    @Setter
    private int length;

    @Getter
    private int priority = -1;

    @Getter
    private File file;

    /**
     * 实现一个属性类
     *
     * @param valuesLength int 数组长度
     * @param types        SXAttributeType 属性类型
     */
    public SubAttribute(@NotNull JavaPlugin plugin, int valuesLength, @NotNull SXAttributeType... types) {
        this.name = getClass().getSimpleName();
        this.plugin = plugin;
        this.length = valuesLength;
        this.attributeTypes = types.length == 0 ? new SXAttributeType[]{SXAttributeType.OTHER} : types;
        this.file = new File(SXAttribute.getPluginFile(), "Attribute" + File.separator + getPlugin().getName() + File.separator + getName() + ".yml");
        for (int i = 0; i < priorityList.length; i++) {
            String[] args = priorityList[i].split("#");
            if (args[0].equals(getName())) {
                this.priority = args.length > 1 && !args[1].equals(getPlugin().getName()) ? -1 : i;
                break;
            }
        }
        registerAttribute();
    }

    /**
     * 属性正常启动后
     * 加载配置文件
     */
    public final SubAttribute loadConfig() {
        if (!file.exists()) {
            YamlConfiguration yaml = defaultConfig(new YamlConfiguration());
            if (yaml != null) {
                setConfig(yaml);
                saveConfig();
            }
        } else {
            setConfig(YamlConfiguration.loadConfiguration(file));
        }
        return this;
    }

    /**
     * 编写默认配置文件
     * 返回null则不需要配置文件
     *
     * @return Yaml
     */
    protected YamlConfiguration defaultConfig(YamlConfiguration config) {
        return null;
    }

    /**
     * 保存配置信息
     */
    public void saveConfig() {
        try {
            config().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置优先级
     *
     * @param priority int
     */
    SubAttribute setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    /**
     * 获取类型
     *
     * @return SXAttributeType[]
     */
    public final SXAttributeType[] getType() {
        return attributeTypes.clone();
    }

    /**
     * 判断属性类型
     *
     * @param attributeType SXAttributeType
     * @return boolean
     */
    public final boolean containsType(SXAttributeType attributeType) {
        for (SXAttributeType type : attributeTypes) {
            if (type.equals(attributeType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 注册属性方法-实例化就注册【脏话】
     * <p>
     * 优先级需在SX-Attribute/Config.yml 设定
     */
    private void registerAttribute() {
        if (getPlugin() == null) {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cAttribute >>  §7[§8NULL§4|§c" + getName() + "§7] §cNull Plugin!");
        } else if (getPriority() < 0) {
            Bukkit.getConsoleSender().sendMessage("[" + getPlugin().getName() + "] §8Attribute >> Disable §7[§8" + getPlugin().getName() + "§4|§8" + getName() + "§7] §8!");
        } else if (Bukkit.getPluginManager().getPlugin("SX-Attribute").isEnabled()) {
            Bukkit.getConsoleSender().sendMessage("[" + getPlugin().getName() + "] §cAttribute >> §cSXAttribute is Enabled , Unable to register §7[§c" + getPlugin().getName() + "§4|§c" + getName() + "§7]§c !");
        } else {
            SubAttribute attribute = registerAttributeMap.put(getPriority(), this);
            if (attribute == null) {
                Bukkit.getConsoleSender().sendMessage("[" + getPlugin().getName() + "] Attribute >> Register §7[§c" + getPlugin().getName() + "§4|§c" + getName() + "§7] §rTo Priority §c" + this.getPriority() + " §r!");
            } else {
                Bukkit.getConsoleSender().sendMessage("[" + getPlugin().getName() + "] Attribute >> The §7[§c" + getPlugin().getName() + "§4|§c" + getName() + "§7] §rCover To §7[§c" + attribute.getPlugin().getName() + "§4|§c" + attribute.getName() + "§7]§r !");
            }
        }
    }

    public Listener getListener() {
        return null;
    }

    /**
     * 启动时执行的方法
     */
    public void onEnable() {

    }

    /**
     * 关闭时执行的方法
     */
    public void onDisable() {

    }

    /**
     * 重载时执行的方法
     */
    public void onReLoad() {

    }

    /**
     * 根据属性枚举执行相应方法
     * 伤害事件
     *
     * @param eventData 事件数据
     */
    public abstract void eventMethod(double[] values, EventData eventData);

    /**
     * 获取placeholder变量
     *
     * @param string String
     * @param player Player
     * @return Object / null
     */
    public abstract Object getPlaceholder(double[] values, Player player, String string);

    /**
     * 获取placeholder变量列表
     *
     * @return List
     */
    public abstract List<String> getPlaceholders();

    /**
     * 读取属性方法
     *
     * @param lore 物品lore
     */
    public abstract void loadAttribute(double[] values, String lore);

    /**
     * 纠正属性值
     */
    public void correct(double[] values) {
        int bound = values.length;
        for (int i = 0; i < bound; i++) {
            values[i] = Math.max(values[i], 0D);
        }
    }

    /**
     * 计算战力值
     *
     * @return double
     */
    public abstract double calculationCombatPower(double[] values);

    /**
     * 判断几率
     *
     * @param value double
     * @return boolean
     */
    public static boolean probability(double value) {
        return value > 0 && value / 100D > SXAttribute.getRandom().nextDouble();
    }

    /**
     * 获取属性值
     *
     * @param lore String
     * @return String
     */
    public static double getNumber(String lore) {
        String str = lore.replaceAll("\u00a7+[a-z0-9]", "").replaceAll("[^-0-9.]", "");
        return str.length() == 0 || str.replaceAll("[^.]", "").length() > 1 ? 0D : Double.valueOf(str);
    }
}
