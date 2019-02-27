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
 * 获取实体数据事件
 *
 * @author Saukiya
 */

public class SXGetDataEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final LivingEntity entity;

    @Getter
    @Setter
    private SXAttributeData data;

    public SXGetDataEvent(LivingEntity entity, SXAttributeData data) {
        this.entity = entity;
        this.data = data;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
