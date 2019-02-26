package github.saukiya.sxattribute.data;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.condition.SubCondition;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.skymc.taboolib.inventory.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/**
 * @author Saukiya
 */
@Getter
public class ItemData {

    private String name;

    private ItemStack item;

    private String displayName;

    private List<String> ids;

    private List<String> loreList;

    private List<String> enchantList;

    private List<String> itemFlagList;

    private boolean unbreakable;

    private Color color;

    private String skullName;

    private ConfigurationSection config;

    private int hashCode;

    private boolean update;

    public ItemData(String name, ConfigurationSection config) {
        this.name = name;
        this.config = config;
        this.hashCode = config.hashCode();
        this.displayName = config.getString("Name");
        this.ids = config.isList("ID") ? config.getStringList("ID") : Collections.singletonList(config.getString(".ID"));
        this.loreList = config.getStringList("Lore");
        this.enchantList = config.getStringList("EnchantList");
        this.itemFlagList = config.getStringList( "ItemFlagList");
        this.unbreakable = config.getBoolean( "Unbreakable");
        this.color = config.getColor("Color");
        this.skullName = config.getString( "SkullName");
        this.update = config.getBoolean("Update");
    }

    public ItemData(String name, ConfigurationSection config, ItemStack item) {
        this.name = name;
        this.config = config;
        this.item = item;
    }

    public boolean isImport() {
        return item != null;
    }

    public ItemStack getItem(Player player) {
        if (isImport()) {
            return getItem().clone();
        }
        Map<String, String> lockRandomMap = new HashMap<>();
        String displayName = SXAttribute.getApi().getRandomStringManager().processRandomString(this.displayName, lockRandomMap);
        String id = SXAttribute.getApi().getRandomStringManager().processRandomString(getIds().get(SXAttribute.getRandom().nextInt(getIds().size())), lockRandomMap);
        List<String> loreList = new ArrayList<>();
        for (String lore : this.loreList) {
            lore = SXAttribute.getApi().getRandomStringManager().processRandomString(lore, lockRandomMap);
            if (!lore.contains("%DeleteLore%")) {
                loreList.addAll(Arrays.asList(lore.replace("\n","/n").split("/n")));
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
                enchantList.addAll(Arrays.asList(enchant.replace("\n","/n").split("/n")));
            }
        }
        ItemStack item = getItemStack(displayName, id, loreList, enchantList, itemFlagList, unbreakable, color, skullName);
        if (lockRandomMap.size() > 0) {
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : lockRandomMap.entrySet()) {
                list.add(entry.getKey() + "§e§k|§e§r" + entry.getValue());
            }
            SXAttribute.getApi().getItemUtil().setNBTList(item, "Lock", list);
        }

        // TODO 尝试刷新物品损害值
        if (player != null) {
            Bukkit.getPluginManager().callEvent(new PlayerItemDamageEvent(player, item, 0));
        }
        SXAttribute.getApi().getItemUtil().setNBT(item, "HashCode", String.valueOf(hashCode));
        if (item.getItemMeta().hasLore()) {
            if (Config.isDamageGauges() && SXAttribute.getApi().getItemUtil().getAttackSpeed(item) != null) {
                return SXAttribute.getApi().getItemUtil().setAttackSpeed(item);
                // 判断需要被清除标签的武器 防具
            } else if (Config.isClearDefaultAttributePlugin() && SXAttribute.getApi().getItemUtil().isEquipment(item)) {
                return SXAttribute.getApi().getItemUtil().clearAttribute(item);
            }
        }
        return item;
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
            //1.11.2方法
            meta.setUnbreakable(unbreakable);
        } else {
            //1.9.0方法
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
}
