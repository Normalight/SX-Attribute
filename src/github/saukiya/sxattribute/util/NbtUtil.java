package github.saukiya.sxattribute.util;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Saukiya
 */

public class NbtUtil {

    private Class<?> xCraftItemStack;
    private Class<?> xNBTTagCompound;
    private Class<?> xNBTTagList;
    private Constructor<?> xNewNBTTagString;
    private Constructor<?> xNewNBTTagDouble;
    private Constructor<?> xNewNBTTagInt;

    private Method xAsNMSCopay;
    private Method xGetTag;
    private Method xHasTag;
    private Method xSet;
    private Method xAdd;
    private Method xRemove;
    private Method xSetTag;
    private Method xAsBukkitCopy;

    private Method xHasKey;
    private Method xGet;
    private Method xGetString;
    private Method xGetListString;
    private Method xSize;

    /**
     * 加载NBT反射类
     * 此类所附加的nbt均带有插件名
     *
     * @throws NoSuchMethodException  NoSuchMethodException
     * @throws ClassNotFoundException ClassNotFoundException
     */
    public NbtUtil() throws NoSuchMethodException, ClassNotFoundException {
        String packet = Bukkit.getServer().getClass().getPackage().getName();
        String nmsName = "net.minecraft.server." + packet.substring(packet.lastIndexOf('.') + 1);
        xCraftItemStack = Class.forName(packet + ".inventory.CraftItemStack");
        Class<?> xNMSItemStack = Class.forName(nmsName + ".ItemStack");
        xNBTTagCompound = Class.forName(nmsName + ".NBTTagCompound");
        Class<?> xNBTTagString = Class.forName(nmsName + ".NBTTagString");
        Class<?> xNBTTagDouble = Class.forName(nmsName + ".NBTTagDouble");
        Class<?> xNBTTagInt = Class.forName(nmsName + ".NBTTagInt");
        xNBTTagList = Class.forName(nmsName + ".NBTTagList");
        Class<?> xNBTBase = Class.forName(nmsName + ".NBTBase");
        xNewNBTTagString = xNBTTagString.getConstructor(String.class);
        xNewNBTTagDouble = xNBTTagDouble.getConstructor(double.class);
        xNewNBTTagInt = xNBTTagInt.getConstructor(int.class);

        xAsNMSCopay = xCraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
        xGetTag = xNMSItemStack.getDeclaredMethod("getTag");
        xHasTag = xNMSItemStack.getDeclaredMethod("hasTag");
        xSet = xNBTTagCompound.getDeclaredMethod("set", String.class, xNBTBase);
        xAdd = xNBTTagList.getDeclaredMethod("add", xNBTBase);
        xRemove = xNBTTagCompound.getDeclaredMethod("remove", String.class);
        xSetTag = xNMSItemStack.getDeclaredMethod("setTag", xNBTTagCompound);
        xAsBukkitCopy = xCraftItemStack.getDeclaredMethod("asBukkitCopy", xNMSItemStack);

        xHasKey = xNBTTagCompound.getDeclaredMethod("hasKey", String.class);
        xGet = xNBTTagCompound.getDeclaredMethod("get", String.class);
        xGetString = xNBTTagCompound.getDeclaredMethod("getString", String.class);
        xGetListString = xNBTTagList.getDeclaredMethod("getString", int.class);
        xSize = xNBTTagList.getDeclaredMethod("size");
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "Load NbtUtil!");
    }

    /**
     * 获取物品攻击速度
     *
     * @param item ItemStack
     * @return double
     */
    public Double getAttackSpeed(ItemStack item) {
        switch (item.getType().name()) {
            case "DIAMOND_HOE":
                return 0D;
            case "IRON_HOE":
                return -1D;
            case "STONE_HOE":
                return -2D;
            case "DIAMOND_SWORD":
            case "IRON_SWORD":
            case "GOLD_SWORD":
            case "STONE_SWORD":
            case "WOOD_SWORD":
                return -2.4D;
            case "DIAMOND_PICKAXE":
            case "IRON_PICKAXE":
            case "GOLD_PICKAXE":
            case "STONE_PICKAXE":
            case "WOOD_PICKAXE":
                return -2.8D;
            case "DIAMOND_SPADE":
            case "IRON_SPADE":
            case "GOLD_SPADE":
            case "STONE_SPADE":
            case "WOOD_SPADE":
            case "DIAMOND_AXE":
            case "GOLD_AXE":
            case "WOOD_HOE":
            case "GOLD_HOE":
                return -3D;
            case "IRON_AXE":
                return -3.1D;
            case "STONE_AXE":
            case "WOOD_AXE":
                return -3.2D;
        }
        return null;
    }

    public boolean isEquipment(ItemStack item) {
        switch (item.getType().name()) {
            case "DIAMOND_HELMET":
            case "DIAMOND_CHESTPLATE":
            case "DIAMOND_LEGGINGS":
            case "DIAMOND_BOOTS":
            case "GOLD_HELMET":
            case "GOLD_CHESTPLATE":
            case "GOLD_LEGGINGS":
            case "GOLD_BOOTS":
            case "IRON_HELMET":
            case "IRON_CHESTPLATE":
            case "IRON_LEGGINGS":
            case "IRON_BOOTS":
            case "LEATHER_HELMET":
            case "LEATHER_CHESTPLATE":
            case "LEATHER_LEGGINGS":
            case "LEATHER_BOOTS":
            case "CHAINMAIL_HELMET":
            case "CHAINMAIL_CHESTPLATE":
            case "CHAINMAIL_LEGGINGS":
            case "CHAINMAIL_BOOTS":
            case "DIAMOND_AXE":
            case "DIAMOND_HOE":
            case "DIAMOND_SWORD":
            case "DIAMOND_SPADE":
            case "DIAMOND_PICKAXE":
            case "GOLD_AXE":
            case "GOLD_HOE":
            case "GOLD_SWORD":
            case "GOLD_SPADE":
            case "GOLD_PICKAXE":
            case "IRON_AXE":
            case "IRON_HOE":
            case "IRON_SWORD":
            case "IRON_SPADE":
            case "IRON_PICKAXE":
            case "STONE_AXE":
            case "STONE_HOE":
            case "STONE_SWORD":
            case "STONE_SPADE":
            case "STONE_PICKAXE":
            case "WOOD_AXE":
            case "WOOD_HOE":
            case "WOOD_SWORD":
            case "WOOD_SPADE":
            case "WOOD_PICKAXE":
                return true;
        }
        return false;
    }

    /**
     * 设置物品攻击速度
     *
     * @param item ItemStack
     * @return ItemStack
     */
    public ItemStack setAttackSpeed(ItemStack item) {
        if (item != null && !item.getType().name().equals("AIR")) {
            try {
                Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack, item);
                Object compound = ((Boolean) xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem) : xNBTTagCompound.newInstance();
                Object modifiers = xNBTTagList.newInstance();
                Object attackSpeed = xNBTTagCompound.newInstance();
                xSet.invoke(attackSpeed, "AttributeName", xNewNBTTagString.newInstance("generic.attackSpeed"));
                xSet.invoke(attackSpeed, "Name", xNewNBTTagString.newInstance("AttackSpeed"));
                xSet.invoke(attackSpeed, "Amount", xNewNBTTagDouble.newInstance(getAttackSpeed(item)));
                xSet.invoke(attackSpeed, "Operation", xNewNBTTagInt.newInstance(0));
                xSet.invoke(attackSpeed, "UUIDLeast", xNewNBTTagInt.newInstance(20000));
                xSet.invoke(attackSpeed, "UUIDMost", xNewNBTTagInt.newInstance(1000));
                xSet.invoke(attackSpeed, "Slot", xNewNBTTagString.newInstance("mainhand"));
                xAdd.invoke(modifiers, attackSpeed);
                xSet.invoke(compound, "AttributeModifiers", modifiers);
                xSetTag.invoke(nmsItem, compound);
                item.setItemMeta(((ItemStack) xAsBukkitCopy.invoke(xCraftItemStack, nmsItem)).getItemMeta());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    /**
     * 清除物品默认属性标签
     *
     * @param item ItemStack
     */
    public ItemStack clearAttribute(ItemStack item) {
        if (item != null) {
            try {
                Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack, item);
                Object compound = ((Boolean) xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem) : xNBTTagCompound.newInstance();
                Object modifiers = xNBTTagList.newInstance();
                xSet.invoke(compound, "AttributeModifiers", modifiers);
                xSetTag.invoke(nmsItem, compound);
                item.setItemMeta(((ItemStack) xAsBukkitCopy.invoke(xCraftItemStack, nmsItem)).getItemMeta());
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        }
        return item;
    }

    /**
     * 获取全部NBT数据
     *
     * @param item ItemStack
     * @return String
     */
    public String getAllNBT(ItemStack item) {
        try {
            Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack, item);
            Object itemTag = ((Boolean) xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem) : xNBTTagCompound.newInstance();
            return "[" + item.getTypeId() + ":" + item.getDurability() + "-" + item.hashCode() + "]>" + itemTag.toString();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            return null;
        }
    }

    /**
     * 设置物品NBT数据
     *
     * @param item  ItemStack
     * @param key   String
     * @param value String
     * @return ItemStack
     */
    public ItemStack setNBT(ItemStack item, String key, Object value) {
        try {
            Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack, item);
            Object itemTag = ((Boolean) xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem) : xNBTTagCompound.newInstance();
            Object tagString = xNewNBTTagString.newInstance(value.toString());
            xSet.invoke(itemTag, key, tagString);
            xSetTag.invoke(nmsItem, itemTag);
            item.setItemMeta(((ItemStack) xAsBukkitCopy.invoke(xCraftItemStack, nmsItem)).getItemMeta());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * 设置物品NBT数据 List 会被设置ItemMeta
     *
     * @param item ItemStack
     * @param key  String
     * @param list List
     * @return ItemStack
     */
    public ItemStack setNBTList(ItemStack item, String key, List<String> list) {
        try {
            Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack, item);
            Object itemTag = ((Boolean) xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem) : xNBTTagCompound.newInstance();
            Object tagList = xNBTTagList.newInstance();
            for (String str : list) {
                xAdd.invoke(tagList, xNewNBTTagString.newInstance(str));
            }
            xSet.invoke(itemTag, key, tagList);
            xSetTag.invoke(nmsItem, itemTag);
            item.setItemMeta(((ItemStack) xAsBukkitCopy.invoke(xCraftItemStack, nmsItem)).getItemMeta());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return item;
    }

    /**
     * 获取物品NBT数据
     *
     * @param item ItemStack
     * @param key  String
     * @return String
     */
    public String getNBT(ItemStack item, String key) {
        try {
            Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack, item);
            Object itemTag = ((Boolean) xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem) : xNBTTagCompound.newInstance();
            if ((boolean) xHasKey.invoke(itemTag, key))
                return (String) xGetString.invoke(itemTag, key);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 设置物品NBT数据 List
     *
     * @param item ItemStack
     * @param key  String
     * @return List
     */
    public List<String> getNBTList(ItemStack item, String key) {
        List<String> list = new ArrayList<>();
        try {
            Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack, item);
            Object itemTag = ((Boolean) xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem) : xNBTTagCompound.newInstance();
            Object tagList = (Boolean) xHasKey.invoke(itemTag, key) ? xGet.invoke(itemTag, key) : xNBTTagList.newInstance();
            for (int i = 0; i < (Integer) xSize.invoke(tagList); i++) {
                list.add((String) xGetListString.invoke(tagList, i));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 判断是否有物品NBT数据
     *
     * @param item ItemStack
     * @param key  String
     * @return Boolean
     */
    public boolean hasNBT(ItemStack item, String key) {
        try {
            Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack, item);
            Object itemTag = ((Boolean) xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem) : xNBTTagCompound.newInstance();
            if ((boolean) xHasKey.invoke(itemTag, key)) return true;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 清除指定 SX设置的nbt
     *
     * @param item ItemStack
     * @param key  String
     * @return boolean
     */
    public boolean removeNBT(ItemStack item, String key) {
        try {
            Object nmsItem = xAsNMSCopay.invoke(xCraftItemStack, item);
            Object itemTag = ((Boolean) xHasTag.invoke(nmsItem)) ? xGetTag.invoke(nmsItem) : xNBTTagCompound.newInstance();
            if ((boolean) xHasKey.invoke(itemTag, key)) {
                xRemove.invoke(itemTag, key);
                xSetTag.invoke(nmsItem, itemTag);
                item.setItemMeta(((ItemStack) xAsBukkitCopy.invoke(xCraftItemStack, nmsItem)).getItemMeta());
            }
            return true;
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            return false;
        }
    }
}
