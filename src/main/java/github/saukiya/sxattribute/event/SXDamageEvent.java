package github.saukiya.sxattribute.event;

import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * 插件重载事件
 *
 * @author Saukiya
 */
@Getter
public class SXDamageEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private DamageData data;

    public SXDamageEvent(DamageData data) {
        this.data = data;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
