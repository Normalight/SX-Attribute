package github.saukiya.sxattribute.data.attribute;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.api.Sx;
import github.saukiya.sxattribute.data.SlotData;
import github.saukiya.sxattribute.data.condition.SXConditionManager;
import github.saukiya.sxattribute.data.condition.SXConditionReturnType;
import github.saukiya.sxattribute.data.condition.SXConditionType;
import github.saukiya.sxattribute.data.condition.SubCondition;
import github.saukiya.sxattribute.data.eventdata.sub.UpdateData;
import github.saukiya.sxattribute.event.SXGetDataEvent;
import github.saukiya.sxattribute.event.SXLoadItemDataEvent;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import ru.endlesscode.rpginventory.inventory.InventoryManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 属性管理器
 *
 * @author Saukiya
 */
public class SXAttributeManager implements Listener {

    @Getter
    private static final SubAttribute[] subAttributes = SubAttribute.registerAttributeMap.values().toArray(new SubAttribute[0]);

    static {
        SubAttribute.registerAttributeMap = null;
        SubAttribute.priorityList = null;
    }

    @Getter
    private final Map<UUID, SXAttributeData> entityDataMap = new ConcurrentHashMap<>();

    private SXAttributeData defaultAttributeData;

    private final SXAttribute plugin;

    public SXAttributeManager(SXAttribute plugin) {
        this.plugin = plugin;
        int size = getSubAttributes().length;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        for (int i = 0; i < size; i++) {
            getSubAttributes()[i].setPriority(i).loadConfig().onEnable();
        }
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Load §c" + getSubAttributes().length + "§r Attributes");
    }

    @EventHandler
    public void onSubAttributePluginEnableEvent(PluginEnableEvent event) {
        for (SubAttribute subAttribute : subAttributes) {
            if (subAttribute.getPlugin().equals(event.getPlugin())) {
                if (subAttribute.getListener() != null) {
                    Bukkit.getPluginManager().registerEvents(subAttribute.getListener(), subAttribute.getPlugin());
                }
            }
        }
        if (Arrays.stream(subAttributes).allMatch(subAttribute -> subAttribute.getPlugin().isEnabled())) {
            loadDefaultAttributeData();
        }
    }

    public void onAttributeDisable() {
        for (SubAttribute attribute : getSubAttributes()) {
            attribute.onDisable();
        }
    }

    public void onAttributeReload() {
        for (SubAttribute attribute : getSubAttributes()) {
            attribute.loadConfig().onReLoad();
        }
    }

    /**
     * 获取属性
     *
     * @param attributeName String
     * @return SubAttribute / null
     */
    public static SubAttribute getSubAttribute(String attributeName) {
        for (SubAttribute subAttribute : getSubAttributes()) {
            if (subAttribute.getName().equals(attributeName)) {
                return subAttribute;
            }
        }
        return null;
    }

    /**
     * 获取物品的属性
     *
     * @param entity    LivingEntity 可以为Null 则不进行条件判断
     * @param type      SXConditionType 可以为Null 则不进行条件判断
     * @param itemArray ItemStack[] 物品列表 不满足条件的物品修改为null
     * @return SXAttributeData
     */
    public SXAttributeData getItemData(LivingEntity entity, SXConditionType type, ItemStack... itemArray) {
        SXAttributeData sxAttributeDataList = new SXAttributeData();

        for (int i = 0; i < itemArray.length; i++) {
            ItemStack item = itemArray[i];
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
                String itemName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name();
                if (itemName.contains("§X")) {
                    itemArray[i] = null;
                    continue;
                }
                SXAttributeData sxAttributeData = new SXAttributeData();
                for (String lore : item.getItemMeta().getLore()) {
                    lore = lore.split("\u00a7X")[0];
                    if (lore.length() > 0) {
                        for (SubCondition subCondition : SXConditionManager.getSubConditions()) {
                            if (subCondition.containsType(type, true)) {
                                SXConditionReturnType returnType = subCondition.determine(entity, item, lore);
                                if (returnType.equals(SXConditionReturnType.ITEM)) {
                                    sxAttributeData = null;
                                    break;
                                } else if (returnType.equals(SXConditionReturnType.LORE)) {
                                    break;
                                }
                            }
                        }
                        if (sxAttributeData == null) {
                            itemArray[i] = null;
                            break;
                        }

                        for (SubAttribute subAttribute : getSubAttributes()) {
                            subAttribute.loadAttribute(sxAttributeData.getValues()[subAttribute.getPriority()], lore);
                        }
                    }
                }
                sxAttributeDataList.add(sxAttributeData);
            }
        }
        return sxAttributeDataList.isValid() ? sxAttributeDataList : null;
    }

    /**
     * 获取Lore的属性
     * 带有§X 的一行不被识别属性
     *
     * @param entity     如果有玩家 那么判断玩家是否满足条件才可使用该物品
     * @param type       更新位置类型 可以为Null 则不进行条件判断
     * @param stringList 物品lore，也可以是其他存有属性的list
     * @return 满足条件返回 SXAttributeData 不满足返回null
     */
    public SXAttributeData getListStats(LivingEntity entity, SXConditionType type, List<String> stringList) {
        SXAttributeData sxAttributeData = new SXAttributeData();
        for (String lore : stringList) {
            lore = lore.split("§X")[0];
            if (lore.length() > 0) {
                for (SubCondition subCondition : SXConditionManager.getSubConditions()) {
                    if (subCondition.containsType(type, true)) {
                        SXConditionReturnType returnType = subCondition.determine(entity, null, lore);
                        if (returnType.equals(SXConditionReturnType.ITEM)) {
                            return null;
                        } else if (returnType.equals(SXConditionReturnType.LORE)) {
                            break;
                        }
                    }
                }
                for (SubAttribute subAttribute : getSubAttributes()) {
                    subAttribute.loadAttribute(sxAttributeData.getValues()[subAttribute.getPriority()], lore);
                }
            }
        }
        return sxAttributeData.isValid() ? sxAttributeData : null;
    }

    /**
     * 判断物品是否符合使用条件
     *
     * @param entity 实体
     * @param item   物品
     * @param type   物品所处位置
     * @return boolean
     */
    public Boolean isUse(LivingEntity entity, SXConditionType type, ItemStack item) {
        if (type != null && item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
            List<String> loreList = item.getItemMeta().getLore();
            for (String lore : loreList) {
                for (SubCondition subCondition : SXConditionManager.getSubConditions()) {
                    if (subCondition.containsType(type, true)) {
                        if (subCondition.determine(entity, item, lore).equals(SXConditionReturnType.ITEM)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 更新实体UPDATE类属性
     *
     * @param entity Player
     */
    public void attributeUpdateEvent(LivingEntity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                UpdateData updateData = new UpdateData(entity);
                SXAttributeData attributeData = getEntityData(entity);
                for (SubAttribute subAttribute : getSubAttributes()) {
                    if (subAttribute.containsType(SXAttributeType.UPDATE)) {
                        subAttribute.eventMethod(attributeData.getValues()[subAttribute.getPriority()], updateData);
                    }
                }
            }
        }.runTask(plugin);
    }

    public void loadDefaultAttributeData() {
        defaultAttributeData = getListStats(null, null, Config.getConfig().getStringList(Config.DEFAULT_STATS));
    }

    /**
     * 获取生物总数据
     *
     * @param entity LivingEntity
     * @return SXAttributeData
     */
    public SXAttributeData getEntityData(LivingEntity entity) {
        SXAttributeData data = new SXAttributeData();
        data.add(getEntityDataMap().get(entity.getUniqueId()));
        data.add(Sx.getAPIAttribute(entity.getUniqueId()));
        data.calculationCombatPower();
        data.add(defaultAttributeData);
        // TODO s
        SXGetDataEvent event = new SXGetDataEvent(entity, data);
        Bukkit.getPluginManager().callEvent(event);
        data.correct();
        return data;
    }

    /**
     * 清除生物数据
     *
     * @param uuid EntityUUID
     */
    public void clearEntityData(UUID uuid) {
        getEntityDataMap().remove(uuid);
        Sx.removeEntityAllPluginData(uuid);
    }

    /**
     * 加载生物数据
     *
     * @param entity LivingEntity
     */
    public void updateData(LivingEntity entity) {
        Player player = entity instanceof Player ? (Player) entity : null;
        Map<SXConditionType, SXLoadItemDataEvent.Data> dataMap = new HashMap<>();
        SXAttributeData attributeData;

        if (SXAttribute.isRpgInventory() && player != null) {
            // RPGInv Load
            Inventory inv = InventoryManager.get(player).getInventory();
            if (inv != null) {
                List<ItemStack> itemList = new ArrayList<>();
                for (Integer i : Config.getRpgInvSlotList()) {
                    ItemStack item = inv.getItem(i);
                    plugin.getItemDataManager().updateItem(item, (Player) entity);
                    if (item != null && item.getItemMeta().hasLore()) {
                        itemList.add(item);
                    }
                }

                ItemStack[] items = itemList.toArray(new ItemStack[0]);
                attributeData = getItemData(player, SXConditionType.RPG_INVENTORY, items);
                dataMap.put(SXConditionType.RPG_INVENTORY, new SXLoadItemDataEvent.Data(attributeData, items));
            }
        } else {
            // Armor Update
            if (entity instanceof Player) {
                for (ItemStack item : entity.getEquipment().getArmorContents()) {
                    plugin.getItemDataManager().updateItem(item, (Player) entity);
                }
            }

            // Slot Load
            if (player != null) {
                List<ItemStack> itemList = new ArrayList<>();
                Inventory inv = player.getInventory();
                for (SlotData slotData : plugin.getSlotDataManager().getSlotList()) {
                    ItemStack item = inv.getItem(slotData.getSlot());
                    plugin.getItemDataManager().updateItem(item, (Player) entity);
                    if (item != null && !item.getType().equals(Material.AIR) && item.getItemMeta().hasLore() && item.getItemMeta().getLore().stream().anyMatch(lore -> lore.contains(slotData.getName()))) {
                        itemList.add(item);
                    }
                }

                ItemStack[] items = itemList.toArray(new ItemStack[0]);
                attributeData = getItemData(player, SXConditionType.SLOT, items);
                dataMap.put(SXConditionType.SLOT, new SXLoadItemDataEvent.Data(attributeData, items));
            }

            // Equipment Load
            ItemStack[] items = entity.getEquipment().getArmorContents();
            attributeData = getItemData(entity, SXConditionType.EQUIPMENT, items);
            dataMap.put(SXConditionType.EQUIPMENT, new SXLoadItemDataEvent.Data(attributeData, items));

        }

        if (player != null) {
            plugin.getItemDataManager().updateItem(entity.getEquipment().getItemInMainHand(), (Player) entity);
            plugin.getItemDataManager().updateItem(entity.getEquipment().getItemInOffHand(), (Player) entity);
        }

        // Hand Load
        ItemStack[] mainItem = new ItemStack[]{entity.getEquipment().getItemInMainHand()};
        ItemStack[] offItem = new ItemStack[]{entity.getEquipment().getItemInOffHand()};
        SXAttributeData mainHandData = getItemData(entity, SXConditionType.MAIN_HAND, mainItem);
        SXAttributeData offHandData = getItemData(entity, SXConditionType.OFF_HAND, offItem);
        dataMap.put(SXConditionType.MAIN_HAND, new SXLoadItemDataEvent.Data(mainHandData, mainItem));
        dataMap.put(SXConditionType.OFF_HAND, new SXLoadItemDataEvent.Data(offHandData, offItem));

        if ((attributeData = new SXLoadItemDataEvent(entity, dataMap).getAllData()).isValid()) {
            entityDataMap.put(entity.getUniqueId(), attributeData);
        } else {
            entityDataMap.remove(entity.getUniqueId());
        }
    }

    /**
     * 读取主手物品数据
     *
     * @param entity Entity
     * @return Attribute
     */
    public SXAttributeData getMainHandData(LivingEntity entity) {
        ItemStack[] mainItem = new ItemStack[]{entity.getEquipment().getItemInMainHand()};
        SXAttributeData mainHandData = getItemData(entity, SXConditionType.MAIN_HAND, mainItem);
        Map<SXConditionType, SXLoadItemDataEvent.Data> dataMap = new HashMap<>();
        dataMap.put(SXConditionType.MAIN_HAND, new SXLoadItemDataEvent.Data(mainHandData, mainItem));
        return new SXLoadItemDataEvent(entity, dataMap).getAllData();
    }
}
