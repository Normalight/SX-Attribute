package github.saukiya.sxattribute.event;

import github.saukiya.sxattribute.data.itemdata.ItemGenerator;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

/**
 * 物品更新事件
 *
 * @author Saukiya
 */

public class SXItemUpdateEvent extends SXItemSpawnEvent {

    private static final HandlerList handlers = new HandlerList();

    @Getter
    private Player player;

    @Getter
    private ItemGenerator ig;

    @Getter
    private ItemStack oldItem;

    @Getter
    @Setter
    private ItemStack item;

    @Getter
    @Setter
    private boolean cancelled = false;

    public SXItemUpdateEvent(Player player, ItemGenerator ig, ItemStack oldItem, ItemStack item) {
        super(player, ig, item);
        this.player = player;
        this.ig = ig;
        this.oldItem = oldItem;
        this.item = item;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

}
