package github.saukiya.sxattribute.data.itemdata;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.event.SXItemSpawnEvent;
import github.saukiya.sxattribute.event.SXItemUpdateEvent;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import me.skymc.taboolib.json.tellraw.TellrawJson;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author Saukiya
 */
public class ItemDataManager {
    private static final List<ItemGenerator> generators = new ArrayList<>();

    private static final List<String> COLOR_LIST = Arrays.asList("§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9");

    private static final List<String> COLOR_REPLACE_LIST = Arrays.asList("%零%", "%一%", "%二%", "%三%", "%四%", "%五%", "%六%", "%七%", "%八%", "%九%");

    private final File itemFiles = new File(SXAttribute.getPluginFile(), "Item");
    private final File itemDefaultFile = new File(itemFiles, "Save\\Save.yml");
    private final SXAttribute plugin;

    private final Map<String, ItemGenerator> itemMap = new HashMap<>();

    public ItemDataManager(SXAttribute plugin) throws IOException {
        this.plugin = plugin;
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Load §c" + generators.size() + "§r ItemGenerators");
        loadItemData();
    }

    /**
     * 注册物品生成器
     *
     * @param generator ItemGenerator
     */
    public static void registerGenerator(ItemGenerator generator) {
        if (generator.getPlugin() == null) {
            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cItemGenerator >>  §7[§8NULL§4|§c" + generator.getClass().getSimpleName() + "§7] §cNull Plugin!");
            return;
        }
        if (generator.getType() == null || generators.stream().anyMatch((ig)-> ig.getType().equals(generator.getType()))) {
            Bukkit.getConsoleSender().sendMessage("[" + generator.getPlugin().getName() + "] §cItemGenerator >>  §7[§c" + generator.getPlugin().getName() + "§4|§c" + generator.getClass().getSimpleName() + "§7] §cType Error!");
            return;
        }
        generators.add(generator);
        Bukkit.getConsoleSender().sendMessage("[" + generator.getPlugin().getName() + "] ItemGenerator >> Register §7[§c" + generator.getPlugin().getName() + "§4|§c" + generator.getClass().getSimpleName() + "§7] §rTo Type §c" + generator.getType() + " §r!");
    }

    /**
     * 清除物品颜色
     *
     * @param lore String
     * @return String
     */
    public static String clearColor(String lore) {
        for (int i = 0; i < 10; i++) {
            lore = lore.replace(COLOR_LIST.get(i), COLOR_REPLACE_LIST.get(i));
        }
        return lore;
    }

    /**
     * 恢复物品颜色
     *
     * @param lore String
     * @return String
     */
    public static String replaceColor(String lore) {
        for (int i = 0; i < 10; i++) {
            lore = lore.replace(COLOR_REPLACE_LIST.get(i), COLOR_LIST.get(i));
        }
        return lore;
    }

    /**
     * 获取商品价格
     *
     * @param item ItemStack
     * @return double
     */
    public static double getSellValue(ItemStack item) {
        double sell = -0D;
        List<String> loreList = item.getItemMeta().getLore();
        for (String lore : loreList) {
            if (lore.contains(Config.getConfig().getString(Config.NAME_SELL))) {
                sell = SubAttribute.getNumber(lore);
            }
        }
        return sell;
    }

    /**
     * 读取物品数据
     *
     * @throws IOException IOException
     */
    public void loadItemData() throws IOException {
        loadItemMap();
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Load §c" + itemMap.size() + " §rItems");
    }

    /**
     * 获取物品编号列表
     *
     * @return Set
     */
    public Set<String> getItemList() {
        return itemMap.keySet();
    }

    /**
     * 读取物品数据
     *
     * @throws IOException IOException
     */
    private void loadItemMap() throws IOException {
        itemMap.clear();
        if (!itemFiles.exists() || Objects.requireNonNull(itemFiles.listFiles()).length == 0) {
            createDefaultItemData();
        }
        loadItem(itemFiles);
    }

    /**
     * 遍历读取物品数据
     *
     * @param files File
     */
    private void loadItem(File files) {
        for (File file : Objects.requireNonNull(files.listFiles())) {
            if (file.isDirectory()) {
                if (!file.getName().equals("NoLoad")) {
                    loadItem(file);
                }
            } else {
                YamlConfiguration itemYml = YamlConfiguration.loadConfiguration(file);
                do_1:for (String key : itemYml.getKeys(false)) {
                    if (itemMap.containsKey(key)) {
                        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cDon't Repeat Item Name: §4" + file.getName() + File.separator + key + " §c!");
                        continue;
                    }
                    String pathName = getPathName(files);
                    String type = itemYml.getString(key + ".Type","SX");
                    for (ItemGenerator generator : generators) {
                        if (generator.getType().equals(type)) {
                            itemMap.put(key, generator.newGenerator(pathName, key, itemYml.getConfigurationSection(key)));
                            continue do_1;
                        }
                    }
                    Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cDon't Item Type: §4" + file.getName() + File.separator + key + "§7 - §4" + type + "§c !");
                }
            }
        }
    }

    /**
     * 获取路径简称
     *
     * @param file File
     * @return PathName
     */
    private String getPathName(File file) {
        return file.toString().replace("plugins\\SX-Attribute\\Item\\", "").replace("\\",">");
    }

    /**
     * 创建物品默认数据
     *
     * @throws IOException IOException
     */
    private void createDefaultItemData() throws IOException {
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cCreate Item/Default/Default.yml");
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cCreate Item/NoLoad/Default.yml");
        YamlConfiguration yaml = new YamlConfiguration();
        yaml.set("Default-1.Name", "<s:DefaultPrefix>&c炎之洗礼<s:DefaultSuffix> <s:<l:品质>Color><l:品质>");
        yaml.set("Default-1.ID", Collections.singletonList("<s:<l:职业>ID>"));
        yaml.save(new File(itemFiles, "NoLoad\\Default.yml"));
        yaml.set("Default-1.Lore", Arrays.asList(
                "&6品质等级: <s:<l:品质>Color><l:品质>",
                "<s:<l:品质>职业>",
                "&6物品类型: 主武器",
                "&6限制等级: <s:<l:品质>等级-10>级",
                "&c攻击力: +<s:<l:品质>攻击-10>",
                "<s:<l:品质>攻一-10>",
                "<s:<l:品质>攻二-10>",
                "<s:<l:品质>攻三-10>",
                "<l:材质>",
                "<s:<l:品质>宝石孔>",
                "&7耐久度: <r:100_<s:<l:品质>耐久最低>>/<s:<l:品质>耐久>",
                "<s:<l:品质>绑定>",
                "&a获得时间: <t:0>",
                "&a到期时间: <t:600>"
        ));
        yaml.set("Default-1.EnchantList", Collections.singletonList(
                "<s:<l:职业>附魔>"
        ));
        yaml.set("Default-1.ItemFlagList", Collections.singletonList(
                "HIDE_ATTRIBUTES"
        ));
        yaml.set("Default-1.Unbreakable", false);
        yaml.set("Default-2.Name", "&c机械轻羽之靴");
        yaml.set("Default-2.ID", 301);
        yaml.set("Default-2.Lore", Arrays.asList(
                "&6物品类型: 靴子",
                "&b防御力: +15",
                "&c生命上限: +2000",
                "&d移动速度: +100%",
                "&d闪避几率: +20%",
                "&2生命恢复: +10",
                "&e经验加成: +20%",
                "&6限制等级: <r:50_100>级",
                "&r",
                "<s:DefaultLore>",
                "&r",
                "&e出售价格: 250"
        ));
        yaml.set("Default-2.EnchantList", Collections.singletonList(
                "DURABILITY:1"
        ));
        yaml.set("Default-2.ItemFlagList", Arrays.asList(
                "HIDE_ENCHANTS",
                "HIDE_UNBREAKABLE",
                "HIDE_ATTRIBUTES"
        ));
        yaml.set("Default-2.Unbreakable", true);
        yaml.set("Default-2.Color", Color.WHITE);
        yaml.set("Default-2.Update", true);
        yaml.set("Default-3.Name", "&b雷霆领主项链");
        yaml.set("Default-3.ID", 287);
        yaml.set("Default-3.Lore", Arrays.asList(
                "&6物品类型: 项链",
                "&c生命上限: +200",
                "&d移动速度: +50%",
                "&d雷霆几率: +20%",
                "&6限制等级: <r:20_30>级",
                "&r",
                "&e出售价格: 500"
        ));
        yaml.set("Default-3.EnchantList", Collections.singletonList(
                "DURABILITY:1"
        ));
        yaml.set("Default-4.Item", new ItemStack(Material.APPLE));
        yaml.set("Default-4.Type", "Import");
        yaml.save(new File(itemFiles, "Default\\Default.yml"));
    }

    /**
     * 获取物品
     *
     * @param itemName String
     * @param player   Player
     * @return ItemStack / null
     */
    public ItemStack getItem(String itemName, Player player) {
        ItemGenerator ig = itemMap.get(itemName);
        if (ig != null) {
            ItemStack item = ig.getItem(player);
            if (ig instanceof ItemUpdate) {
                SXAttribute.getApi().getNbtUtil().setNBT(item, SXAttribute.getPluginName() + "-Name", ig.getKey());
                SXAttribute.getApi().getNbtUtil().setNBT(item, SXAttribute.getPluginName() + "-HashCode", ((ItemUpdate) ig).getHashCode());
            }
            SXItemSpawnEvent event = new SXItemSpawnEvent(player, ig, item);
            Bukkit.getPluginManager().callEvent(event);
            return event.getItem();
        }
        return null;
    }

    /**
     * 返回是否存在物品
     *
     * @param itemName String
     * @return boolean
     */
    public boolean hasItem(String itemName) {
        return itemMap.containsKey(itemName);
    }

    /**
     * 更新物品
     *
     * @param oldItem ItemStack
     * @param player Player
     */
    public void updateItem(ItemStack oldItem, Player player) {
        String dataName;
        if (oldItem != null && (dataName = plugin.getNbtUtil().getNBT(oldItem, SXAttribute.getPluginName() + "-Name")) != null) {
            ItemGenerator ig = itemMap.get(dataName);
            if (ig instanceof ItemUpdate && ((ItemUpdate) ig).isUpdate() && plugin.getNbtUtil().hasNBT(oldItem, SXAttribute.getPluginName() + "-HashCode")) {
                int hashCode = Integer.valueOf(plugin.getNbtUtil().getNBT(oldItem, SXAttribute.getPluginName() + "-HashCode"));
                if (((ItemUpdate) ig).getHashCode() != hashCode) {
                    ItemStack item = ig.getItem(player);
                    SXAttribute.getApi().getNbtUtil().setNBT(item, SXAttribute.getPluginName() + "-Name", ig.getKey());
                    SXAttribute.getApi().getNbtUtil().setNBT(item, SXAttribute.getPluginName() + "-HashCode", ((ItemUpdate) ig).getHashCode());
                    SXItemUpdateEvent event = new SXItemUpdateEvent(player, ig, oldItem, item);
                    Bukkit.getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        oldItem.setType(event.getItem().getType());
                        if (event.getItem().getType().getMaxDurability() == 0) {
                            oldItem.setDurability(event.getItem().getDurability());
                        }
                        oldItem.setItemMeta(event.getItem().getItemMeta());
                    }
                }
            }
        }
    }

    /**
     * 保存物品
     *
     * @param key 编号
     * @param item 物品
     * @param type 类型
     * @return boolean
     * @throws IOException IOException
     */
    public boolean saveItem(String key, ItemStack item, String type) throws IOException {
        for (ItemGenerator ig : generators) {
            if (ig.getType().equals(type)) {
                ConfigurationSection mConfig = ig.saveItem(item, new MemoryConfiguration());
                if (mConfig != null) {
                    mConfig.set("Type", ig.getType());
                    File file = new File(itemFiles, "Type-" + ig.getType() +  "\\Item.yml");
                    YamlConfiguration yaml = file.exists() ? YamlConfiguration.loadConfiguration(file) : new YamlConfiguration();
                    yaml.set(key, mConfig);
                    yaml.save(file);
                    itemMap.put(key, ig.newGenerator(getPathName(file.getParentFile()), key, mConfig));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 发送物品列表给指令者
     *
     * @param sender CommandSender
     * @param search String
     */
    public void sendItemMapToPlayer(CommandSender sender, String... search) {
        sender.sendMessage("");
        int filterSize = 0, size = 0;
        if (search.length > 0 && search[0].equals("")) {
            TellrawJson.create().append("§eDirectoryList§8 - §7ClickOpen").hoverText("§8§o§lGo To ItemList").clickCommand("/sxAttribute give |").send(sender);
            Map<String, String> map = new HashMap<>();
            for (ItemGenerator ig : itemMap.values()) {
                String str = map.computeIfAbsent(ig.getPathName(), k -> "");
                map.put(ig.getPathName(), str + "§b" + (str.replaceAll("[^\n]","").length() + 1) + " - §a" + ig.getKey() + " §8[§7" + ig.getName() + "§8]§7 - §8[§cType:" + ig.getType()+ "§8]\n");
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey(), value = entry.getValue(), command = "/sxAttribute give |" + key;
                TellrawJson.create().append(" §8[§c" + key + "§8]").clickCommand(command).append("§7 - Has §c" + value.split("\n").length + "§7 Item").hoverText(value.substring(0, value.length() - 1)).clickCommand(command).send(sender);
            }
        } else {
            TellrawJson.create().append("§eItemList§8 - " + (sender instanceof Player ? "§7ClickGet" : "")).hoverText("§8§o§lTo DirectoryList").clickCommand("/sxAttribute give").send(sender);
            for (ItemGenerator ig : itemMap.values()) {
                String itemName = ig.getName();
                String str = " §b" + (size + 1) + " - §a" + ig.getKey() + " §8[§7" + itemName + "§8]§7 - §8[§cType:" + ig.getType()+ "§8]";
                if (search.length > 0 && !(str + "|" + ig.getPathName()).contains(search[0])) {
                    filterSize++;
                    continue;
                }
                size++;
                if (sender instanceof Player) {
                    YamlConfiguration yaml = new YamlConfiguration();
                    ig.getConfig().getValues(false).forEach(yaml::set);
                    TellrawJson.create().append(str).hoverText("§7" + yaml.saveToString() + "§8§o§lPath: " + ig.getPathName()).clickCommand("/sxAttribute give " + ig.getKey()).send(sender);
                } else {
                    sender.sendMessage(str);
                }
            }
            if (search.length > 0 && filterSize != 0) {
                sender.sendMessage("§7> Filter§c " + filterSize + " §7Items, Left §c" + size + "§7 Items.");
            }
        }
        sender.sendMessage("");
    }
}
