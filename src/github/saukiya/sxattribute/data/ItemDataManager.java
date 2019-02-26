package github.saukiya.sxattribute.data;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.condition.SubCondition;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import me.skymc.taboolib.json.tellraw.TellrawJson;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Saukiya
 */
public class ItemDataManager {
    private static final List<String> COLOR_LIST = Arrays.asList("§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9");
    private static final List<String> COLOR_REPLACE_LIST = Arrays.asList("%零%", "%一%", "%二%", "%三%", "%四%", "%五%", "%六%", "%七%", "%八%", "%九%");

    private final File itemFiles = new File(SXAttribute.getPluginFile(), "Item");
    private final File itemDefaultFile = new File(itemFiles, "Default" + File.separator + "Default.yml");
    private final Map<String, ItemData> itemMap = new HashMap<>();
    private final SXAttribute plugin;

    public ItemDataManager(SXAttribute plugin) throws IOException {
        this.plugin = plugin;
        loadItemData();
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
     * @throws IOException                   IOException
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
     * @throws IOException                   IOException
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
                for (String name : itemYml.getKeys(false)) {
                    if (itemMap.containsKey(name)) {
                        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cDon't Repeat Item Name: §4" + file.getName() + File.separator + name + " §c!");
                        continue;
                    }
                    if (itemYml.isItemStack(name)) {
                        itemMap.put(name, new ItemData(name, itemYml.getConfigurationSection(name), itemYml.getItemStack(name)));
                    } else {
                        itemMap.put(name, new ItemData(name, itemYml.getConfigurationSection(name)));
                    }
                }
            }
        }
    }

    /**
     * 创建物品默认数据
     *
     * @throws IOException IOException
     */
    private void createDefaultItemData() throws IOException {
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cCreate Item/Default.yml");
        YamlConfiguration itemData = new YamlConfiguration();
        itemData.set("默认一.Name", "<s:DefaultPrefix>&c炎之洗礼<s:DefaultSuffix> <s:<l:品质>Color><l:品质>");
        itemData.set("默认一.ID", Collections.singletonList("<s:<l:职业>ID>"));
        itemData.set("默认一.Lore", Arrays.asList(
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
        itemData.set("默认一.EnchantList", Collections.singletonList(
                "<s:<l:职业>附魔>"
        ));
        itemData.set("默认三.ItemFlagList", Collections.singletonList(
                "HIDE_ATTRIBUTES"
        ));
        itemData.set("默认一.Unbreakable", false);
        itemData.set("默认二.Name", "&c机械轻羽之靴");
        itemData.set("默认二.ID", 301);
        itemData.set("默认二.Lore", Arrays.asList(
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
        itemData.set("默认二.EnchantList", Collections.singletonList(
                "DURABILITY:1"
        ));
        itemData.set("默认二.ItemFlagList", Arrays.asList(
                "HIDE_ENCHANTS",
                "HIDE_UNBREAKABLE",
                "HIDE_ATTRIBUTES"
        ));
        itemData.set("默认二.Unbreakable", true);
        itemData.set("默认二.Color", "128,128,128");
        itemData.set("默认三.Name", "&b雷霆领主项链");
        itemData.set("默认三.ID", 287);
        itemData.set("默认三.Lore", Arrays.asList(
                "&6物品类型: 项链",
                "&c生命上限: +200",
                "&d移动速度: +50%",
                "&d雷霆几率: +20%",
                "&6限制等级: <r:20_30>级",
                "&r",
                "&e出售价格: 500"
        ));
        itemData.set("默认三.EnchantList", Collections.singletonList(
                "DURABILITY:1"
        ));
        itemData.save(itemDefaultFile);
    }

    /**
     * 判断物品是否存在
     *
     * @param itemName String
     * @return boolean
     */
    public boolean isItem(String itemName) {
        return itemMap.containsKey(itemName);
    }

    /**
     * 获取物品
     *
     * @param itemName String
     * @param player   Player
     * @return ItemStack
     */
    public ItemStack getItem(String itemName, Player player) {
        ItemData itemData = itemMap.get(itemName);
        if (itemData != null) {
            return itemData.getItem(player);
        } else {
            return null;
        }
    }

    /**
     * 返回是否存在物品
     * @param itemName String
     * @return boolean
     */
    public boolean hasItem(String itemName) {
        return itemMap.containsKey(itemName);
    }

    /**
     * 更新物品
     * 适用双方物品都无lore的时候更新
     *
     * @param item   ItemStack
     * @param player Player
     */
    public void updateItem(ItemStack item, Player player) {
        String dataName;
        if (item != null && (dataName = plugin.getItemUtil().getNBT(item, "Name")) != null) {
            ItemData itemData = itemMap.get(dataName);
            if (itemData != null && itemData.isUpdate() && !itemData.isImport()) {

                int hashCode = plugin.getItemUtil().isNBT(item, "HashCode") ? Integer.valueOf(plugin.getItemUtil().getNBT(item, "HashCode")) : 0;
                if (itemData.getHashCode() != hashCode) {
                    ItemStack newItem = itemData.getItem(player);
                    ItemMeta newItemMeta = newItem.getItemMeta();
                    ItemMeta itemMeta = item.getItemMeta();
                    List<String> itemList = itemMeta.hasLore() ? itemMeta.getLore() : null;
                    List<String> newItemList = newItemMeta.hasLore() ? newItemMeta.getLore() : null;
                    Object[] tempData = new Object[3];
                    String detect = Config.getConfig().getString(Config.NAME_DURABILITY);
                    if (itemList != null && newItemList != null) {
                        for (int i = newItemList.size() - 1; i >= 0; i--) {
                            if (newItemList.get(i).contains(detect)) {
                                for (int i1 = itemList.size() - 1; i1 >= 0; i1--) {
                                    String str = itemList.get(i);
                                    if (str.contains(detect)) {
                                        double maxDurability = SubCondition.getMaxDurability(str);
                                        double durability = SubCondition.getDurability(str);
                                        String lore = newItemList.get(i);
                                        double newMaxDurability = SubCondition.getMaxDurability(lore);
                                        newItemList.set(i, replaceColor(clearColor(newItemList.get(i)).replaceFirst(String.valueOf(durability), String.valueOf((int) (durability  / maxDurability * newMaxDurability)))));
                                        newItemMeta.setLore(newItemList);
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    item.setItemMeta(newItemMeta);
                    item.setType(newItem.getType());
                    if (newItem.getType().getMaxDurability() == 0) {
                        item.setDurability(newItem.getDurability());
                    }
                }
            }
        }
    }


    /**
     * 完全保存物品
     *
     * @param name String
     * @param item ItemStack
     * @throws IOException
     */
    public void importItem(String name, ItemStack item) throws IOException {
        YamlConfiguration yaml = itemDefaultFile.exists() ? YamlConfiguration.loadConfiguration(itemDefaultFile) : new YamlConfiguration();
        yaml.set(name, item);
        yaml.save(itemDefaultFile);
        itemMap.put(name, new ItemData(name, yaml.getConfigurationSection(name), item));
    }

    /**
     * 保存物品
     *
     * @param name  String
     * @param item ItemStack
     * @throws IOException                   IOException
     */
    @SuppressWarnings("deprecation")
    public void saveItem(String name, ItemStack item) throws IOException {
        YamlConfiguration yaml = itemDefaultFile.exists() ? YamlConfiguration.loadConfiguration(itemDefaultFile) : new YamlConfiguration();
        ItemMeta itemMeta = item.getItemMeta();
        yaml.set(name + ".Name", itemMeta.hasDisplayName() ? itemMeta.getDisplayName().replace("§", "&") : null);
        yaml.set(name + ".ID", item.getType().getMaxDurability() != 0 && item.getDurability() != 0 ? item.getTypeId() + ":" + item.getDurability() : String.valueOf(item.getTypeId()));
        if (itemMeta.hasLore()) {
            yaml.set(name + ".Lore", itemMeta.getLore().stream().map(s -> s.replace("§", "&")).collect(Collectors.toList()));
        }
        if (itemMeta.hasEnchants()) {
            yaml.set(name + ".EnchantList", itemMeta.getEnchants().entrySet().stream().map(entry -> entry.getKey().getName() + ":" + entry.getValue()).collect(Collectors.toList()));
        }
        if (itemMeta.getItemFlags().size() > 0) {
            yaml.set(name + ".ItemFlagList", itemMeta.getItemFlags().stream().map(Enum::name).collect(Collectors.toList()));
        }
        yaml.set(name + ".Unbreakable", SubCondition.getUnbreakable(itemMeta));
        if (itemMeta instanceof LeatherArmorMeta) {
            yaml.set(name + ".Color", ((LeatherArmorMeta) itemMeta).getColor());
        }
        if (itemMeta instanceof SkullMeta) {
            yaml.set(name + ".SkullName", ((SkullMeta) itemMeta).getOwner());
        }
        yaml.save(itemDefaultFile);
        itemMap.put(name, new ItemData(name, yaml.getConfigurationSection(name)));
    }

    /**
     * 发送物品列表给指令者
     *
     * @param sender  CommandSender
     * @param search String
     */
    public void sendItemMapToPlayer(CommandSender sender, String... search) {
        sender.sendMessage("§eItemList" + (sender instanceof Player ? "§b - §eClickGet" : ""));

        int filterSize = 0;
        int z = 1;
        for (String key : itemMap.keySet()) {
            ItemData itemData = itemMap.get(key);
            String itemName = itemData.isImport() ? itemData.getItem().getItemMeta().hasDisplayName() ? itemData.getItem().getItemMeta().getDisplayName() : itemData.getItem().getType().name() : itemData.getConfig().getString("Name");
            String str = "§b" + z + " - §a" + key + " §8[§7" + itemName + "§8]" + (itemData.isImport() ? "§7 - §8[§cImportItem§8]" : "");
            if (search.length > 0 && !str.contains(search[0])) {
                filterSize++;
                continue;
            }
            z++;
            if (sender instanceof Player) {
                if (!itemData.isImport()) {
                    YamlConfiguration yaml = new YamlConfiguration();
                    itemData.getConfig().getValues(false).forEach(yaml::set);
                    TellrawJson.create().append(str).hoverText(yaml.saveToString()).clickCommand("/sxAttribute give " + key).send(sender);
                } else {
                    TellrawJson.create().append(str).hoverItem(itemData.getItem()).clickCommand("/sxAttribute give " + key).send(sender);
                }
            } else {
                sender.sendMessage(str);
            }
        }
        if (search.length > 0) {
            sender.sendMessage("§7> Filter§c " + filterSize + " §7Items, Left §c" + z + "§7 Items.");
        }
    }
}
