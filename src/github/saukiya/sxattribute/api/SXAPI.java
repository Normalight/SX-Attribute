package github.saukiya.sxattribute.api;

import github.saukiya.sxattribute.data.RandomStringManager;
import github.saukiya.sxattribute.data.RegisterSlot;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.condition.SXConditionType;
import github.saukiya.sxattribute.util.ItemUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @Author Saukiya
 * @Date 2019/2/20 11:06
 */
public interface SXAPI {

    /**
     * 获取 ItemUtils(NBT反射类)
     * key值结构为:SX-Attribute-{key}
     *
     * @return ItemUtils
     */
    ItemUtil getItemUtil();

    /**
     * 获取 RandomStringManager(随机字符管理)
     *
     * @return RandomStringManager
     */
    RandomStringManager getRandomStringManager();

    /**
     * 为抛射物设定数据，例如箭、雪球、烈焰球。
     * 本插件只会在玩家射箭的时候附加属性
     * 如需添加其他请自行添加抛射物
     *
     * @param uuid          实体UUID
     * @param attributeData / null
     */
    void setProjectileData(UUID uuid, SXAttributeData attributeData);

    /**
     * 获取抛射物数据，例如箭、雪球、烈焰球。
     *
     *
     * @param uuid 实体UUID
     * @return SXAttributeData / null
     */
    SXAttributeData getProjectileData(UUID uuid, boolean remove);

    /**
     * 获取实体属性数据 更改无效
     *
     * @param livingEntity LivingEntity
     * @return SXAttributeData / null
     */
    SXAttributeData getEntityAllData(LivingEntity livingEntity);

    /**
     * 获取实体与插件关联的属性数据
     *
     * @param c    Class
     * @param uuid UUID
     * @return SXAttributeData / null
     */
    SXAttributeData getEntityAPIData(Class<?> c, UUID uuid);

    /**
     * 判断插件是否有注册该实体的属性
     *
     * @param c    Class
     * @param uuid UUID
     * @return boolean
     */
    boolean isEntityAPIData(Class<?> c, UUID uuid);

    /**
     * 设置插件关联的实体属性数据
     *
     * @param c             Class
     * @param uuid          UUID
     * @param attributeData SXAttributeData
     */
    void setEntityAPIData(Class<?> c, UUID uuid, SXAttributeData attributeData);

    /**
     * 清除插件关联的实体属性数据
     * 会返回清除前的数据
     *
     * @param c    插件Class
     * @param uuid 实体UUID
     * @return SXAttributeData / null
     */
    SXAttributeData removeEntityAPIData(Class<?> c, UUID uuid);

    /**
     * 清除插件关联的所有实体属性数据
     *
     * @param c Class
     */
    void removePluginAllEntityData(Class<?> c);

    /**
     * 清除插件所有关联的实体属性数据
     *
     * @param uuid 实体UUID
     */
    void removeEntityAllPluginData(UUID uuid);

    /**
     * 获取List的SXAttributeData数据
     * (entity/type 为null 时不进行条件判断)
     * 如果不满足条件则返回null
     *
     * @param entity   LivingEntity
     * @param type     SXConditionType
     * @param loreList List
     * @return SXAttributeData
     */
    SXAttributeData getLoreData(LivingEntity entity, SXConditionType type, List<String> loreList);

    /**
     * 获取物品的SXAttributeData数据，可以是多个
     * (entity/type 为null 时不进行条件判断)
     * 不满足条件的ItemStack将会在数组内设置为null
     * 如果全部物品都无法识别到属性，那么返回null
     *
     * @param livingEntity LivingEntity
     * @param type         SXConditionType
     * @param item         ItemStack[]
     * @return SXAttributeData
     */
    SXAttributeData getItemData(LivingEntity livingEntity, SXConditionType type, ItemStack... item);

    /**
     * 判断玩家是否达到使用物品要求
     * SXConditionType 为判断位置 一般情况为ALL
     *
     * @param entity LivingEntity
     * @param type   SXConditionType
     * @param item   ItemStack
     * @return boolean
     */
    boolean isUse(LivingEntity entity, SXConditionType type, ItemStack item);

    /**
     * 获取被本插件修改过的原名
     *
     * @param livingEntity LivingEntity
     * @return String
     */
    String getEntityName(LivingEntity livingEntity);

    /**
     * 获取物品的限制等级
     *
     * @param item ItemStack
     * @return int / -1
     */
    int getItemLevel(ItemStack item);

    /**
     * 获取实体等级(如果SX-Level工作时 那将会获取SL等级)
     * 怪物目前为10000
     *
     * @param entity LivingEntity
     * @return level
     */
    int getEntityLevel(LivingEntity entity);

    /**
     * 更新玩家装备属性
     * RPGInventory运行的情况下，不更新装备属性(特殊情况)
     *
     * @param entity LivingEntity
     */
    void updateData(LivingEntity entity);

    /**
     * UPDATE类属性更新
     *
     * @param entity LivingEntity
     */
    void updateStats(LivingEntity entity);

    /**
     * 获取物品
     * 代入Player 则支持Placeholder变量
     *
     * @param itemKey String
     * @param player  Player
     * @return ItemStack
     */
    ItemStack getItem(String itemKey, Player player);

    /**
     * 获取物品编号列表
     *
     * @return Set
     */
    Set<String> getItemList();
}
