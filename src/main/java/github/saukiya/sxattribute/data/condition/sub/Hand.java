package github.saukiya.sxattribute.data.condition.sub;

import github.saukiya.sxattribute.api.Sx;
import github.saukiya.sxattribute.data.condition.SXConditionReturnType;
import github.saukiya.sxattribute.data.condition.SXConditionType;
import github.saukiya.sxattribute.data.condition.SubCondition;
import github.saukiya.sxattribute.util.Config;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * 手持免疫
 *
 * @author Saukiya
 */
public class Hand extends SubCondition {

    public Hand(JavaPlugin plugin) {
        super(plugin, SXConditionType.HAND);
    }

    @Override
    public SXConditionReturnType determine(LivingEntity entity, ItemStack item, String lore) {
        return (Config.getConfig().getStringList(Config.NAME_ARMOR).stream().anyMatch(lore::contains) || Sx.getRegisterSlotList().stream().anyMatch(slot -> lore.contains(slot.getName()))) ? SXConditionReturnType.ITEM : SXConditionReturnType.NULL;
    }
}
