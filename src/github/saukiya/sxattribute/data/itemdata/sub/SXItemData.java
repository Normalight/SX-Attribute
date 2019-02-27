package github.saukiya.sxattribute.data.itemdata.sub;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.condition.SubCondition;
import github.saukiya.sxattribute.data.itemdata.ItemGenerator;
import github.saukiya.sxattribute.data.itemdata.ItemUpdate;
import github.saukiya.sxattribute.util.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Saukiya
 * @Date 2019/2/27 17:48
 */
public class SXItemData implements ItemGenerator, ItemUpdate {

    JavaPlugin plugin;

    String pathName;

    String key;

    ConfigurationSection config;

    String displayName;

    List<String> ids;

    List<String> loreList;

    List<String> enchantList;

    List<String> itemFlagList;

    boolean unbreakable;

    Color color;

    String skullName;

    int hashCode;

    boolean update;

    public SXItemData(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private SXItemData(String pathName, String key, ConfigurationSection config) {
        this.pathName = pathName;
        this.key = key;
        this.config = config;
        this.displayName = config.getString("Name");
        this.ids = config.isList("ID") ? config.getStringList("ID") : Collections.singletonList(config.getString(".ID"));
        this.loreList = config.getStringList("Lore");
        this.enchantList = config.getStringList("EnchantList");
        this.itemFlagList = config.getStringList("ItemFlagList");
        this.unbreakable = config.getBoolean("Unbreakable");
        this.color = config.getColor("Color");
        this.skullName = config.getString("SkullName");
        this.hashCode = config.getValues(true).hashCode();
        this.update = config.getBoolean("Update");
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public String getType() {
        return "SX";
    }

    @Override
    public ItemGenerator newGenerator(String pathName, String key, ConfigurationSection config) {
        return new SXItemData(pathName, key, config);
    }

    @Override
    public String getPathName() {
        return pathName;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return SXAttribute.getApi().getRandomStringManager().processRandomString(displayName, new HashMap<>());
    }

    @Override
    public ConfigurationSection getConfig() {
        return config;
    }

    @Override
    public ItemStack getItem(Player player) {
        Map<String, String> lockRandomMap = new HashMap<>();
        String displayName = SXAttribute.getApi().getRandomStringManager().processRandomString(this.displayName, lockRandomMap);
        String id = SXAttribute.getApi().getRandomStringManager().processRandomString(ids.get(SXAttribute.getRandom().nextInt(ids.size())), lockRandomMap);
        List<String> loreList = new ArrayList<>();
        for (String lore : this.loreList) {
            lore = SXAttribute.getApi().getRandomStringManager().processRandomString(lore, lockRandomMap);
            if (!lore.contains("%DeleteLore%")) {
                loreList.addAll(Arrays.asList(lore.replace("\n", "/n").split("/n")));
            }
        }
        if (SXAttribute.isPlaceholder() && player != null) {
            displayName = PlaceholderAPI.setPlaceholders(player, displayName);
            loreList = PlaceholderAPI.setPlaceholders(player, loreList);
        }

        List<String> enchantList = new ArrayList<>();
        for (String enchant : this.enchantList) {
            enchant = SXAttribute.getApi().getRandomStringManager().processRandomString(enchant, lockRandomMap);
            if (!enchant.contains("%DeleteLore%")) {
                enchantList.addAll(Arrays.asList(enchant.replace("\n", "/n").split("/n")));
            }
        }

        ItemStack item = getItemStack(displayName, id, loreList, enchantList, itemFlagList, unbreakable, color, skullName);
        if (lockRandomMap.size() > 0) {
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : lockRandomMap.entrySet()) {
                list.add(entry.getKey() + "§e§k|§e§r" + entry.getValue());
            }
            SXAttribute.getApi().getNbtUtil().setNBTList(item, SXAttribute.getPluginName() + "-Lock", list);
        }
        if (item.getItemMeta().hasLore()) {
            if (Config.isDamageGauges() && SXAttribute.getApi().getNbtUtil().getAttackSpeed(item) != null) {
                SXAttribute.getApi().getNbtUtil().setAttackSpeed(item);
            } else if (Config.isClearDefaultAttributePlugin() && SXAttribute.getApi().getNbtUtil().isEquipment(item)) {
                SXAttribute.getApi().getNbtUtil().clearAttribute(item);
            }
        }
        return item;
    }

    @Override
    public ConfigurationSection saveItem(ItemStack saveItem, ConfigurationSection config) {
        ItemMeta itemMeta = saveItem.getItemMeta();
        config.set(key + ".Name", itemMeta.hasDisplayName() ? itemMeta.getDisplayName().replace("§", "&") : null);
        config.set(key + ".ID", saveItem.getType().getMaxDurability() != 0 && saveItem.getDurability() != 0 ? saveItem.getTypeId() + ":" + saveItem.getDurability() : String.valueOf(saveItem.getTypeId()));
        if (itemMeta.hasLore()) {
            config.set(key + ".Lore", itemMeta.getLore().stream().map(s -> s.replace("§", "&")).collect(Collectors.toList()));
        }
        if (itemMeta.hasEnchants()) {
            config.set(key + ".EnchantList", itemMeta.getEnchants().entrySet().stream().map(entry -> entry.getKey().getName() + ":" + entry.getValue()).collect(Collectors.toList()));
        }
        if (itemMeta.getItemFlags().size() > 0) {
            config.set(key + ".ItemFlagList", itemMeta.getItemFlags().stream().map(Enum::name).collect(Collectors.toList()));
        }
        config.set(key + ".Unbreakable", SubCondition.getUnbreakable(itemMeta));
        if (itemMeta instanceof LeatherArmorMeta) {
            config.set(key + ".Color", ((LeatherArmorMeta) itemMeta).getColor());
        }
        if (itemMeta instanceof SkullMeta) {
            config.set(key + ".SkullName", ((SkullMeta) itemMeta).getOwner());
        }
        return config;
    }



    /**
     * 快速生成物品
     *
     * @param itemName     String
     * @param id           String
     * @param loreList     List
     * @param itemFlagList List
     * @param unbreakable  Boolean
     * @param color        String
     * @param skullName    String
     * @return ItemStack
     */
    public ItemStack getItemStack(String itemName, String id, List<String> loreList, List<String> enchantList, List<String> itemFlagList, boolean unbreakable, Color color, String skullName) {
        String[] idSplit = id.split(":");
        Material itemMaterial = Material.getMaterial(Integer.valueOf(idSplit[0]));
        ItemStack item = new ItemStack(itemMaterial == null ? Material.BARRIER : itemMaterial, 1, idSplit.length > 1 ? Short.valueOf(idSplit[1]) : 0);
        ItemMeta meta = item.getItemMeta();

        if (itemName != null) {
            meta.setDisplayName(itemName.replace("&", "§"));
        }
        for (int i = 0; i < loreList.size(); i++) {
            loreList.set(i, loreList.get(i).replace("&", "§"));
        }
        meta.setLore(loreList);

        for (String enchant : enchantList) {
            String[] enchantSplit = enchant.split(":");
            Enchantment enchantment = Enchantment.getByName(enchantSplit[0]);
            int level = Integer.valueOf(enchantSplit[1]);
            if (enchantment != null && level != 0) {
                meta.addEnchant(enchantment, level, true);
            }
        }

        for (ItemFlag itemFlag : ItemFlag.values()) {
            if (itemFlagList.contains(itemFlag.name())) {
                meta.addItemFlags(itemFlag);
            }
        }

        if (SXAttribute.getVersionSplit()[1] >= 11) {
            meta.setUnbreakable(unbreakable);
        } else {
            meta.spigot().setUnbreakable(unbreakable);
        }

        if (color != null && meta instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) meta).setColor(color);
        }

        if (skullName != null && meta instanceof SkullMeta) {
            ((SkullMeta) meta).setOwner(skullName);
        }

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getHashCode() {
        return hashCode;
    }

    @Override
    public boolean isUpdate() {
        return update;
    }
}
