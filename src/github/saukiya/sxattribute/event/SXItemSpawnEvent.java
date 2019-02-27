package github.saukiya.sxattribute.event;

import github.saukiya.sxattribute.data.itemdata.ItemGenerator;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * 物品生成事件
 *
 * @author Saukiya
 */

public class SXItemSpawnEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private Player player;

    @Getter
    private ItemGenerator ig;

    @Getter
    @Setter
    private ItemStack item;

    @Getter
    @Setter
    private boolean cancelled = false;

    public SXItemSpawnEvent(Player player, ItemGenerator ig, ItemStack item) {
        this.player = player;
        this.ig = ig;
        this.item = item;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
