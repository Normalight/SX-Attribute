package github.saukiya.sxattribute.event;

import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.condition.SXConditionType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * 加载实体物品数据事件
 *
 * @author Saukiya
 */

public class SXLoadItemDataEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final LivingEntity entity;

    @Getter
    private Map<SXConditionType, Data> dataMap;

    public SXLoadItemDataEvent(LivingEntity entity, Map<SXConditionType, Data> dataMap) {
        this.entity = entity;
        this.dataMap = dataMap;
        Bukkit.getPluginManager().callEvent(this);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public SXAttributeData getAllData() {
        SXAttributeData sxAttributeData = new SXAttributeData();
        for (Data value : dataMap.values()) {
            sxAttributeData.add(value.getAttributeData());
        }
        return sxAttributeData;
    }

    public static class Data {

        @Getter
        @Setter
        private SXAttributeData attributeData;

        @Getter
        private ItemStack[] items;

        public Data(SXAttributeData sxAttributeData, ItemStack... items) {
            this.attributeData = sxAttributeData;
            this.items = items;
        }
    }
}
