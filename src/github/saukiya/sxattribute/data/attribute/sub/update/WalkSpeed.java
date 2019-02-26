package github.saukiya.sxattribute.data.attribute.sub.update;

import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.UpdateEventData;
import github.saukiya.sxattribute.util.Config;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

/**
 * 速度
 *
 * @author Saukiya
 */
public class WalkSpeed extends SubAttribute {

    /**
     * double[0] 移动速度
     */
    public WalkSpeed(JavaPlugin plugin) {
        super(plugin, 1, SXAttributeType.UPDATE);
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof UpdateEventData && ((UpdateEventData) eventData).getEntity() instanceof Player) {
            Player player = (Player) ((UpdateEventData) eventData).getEntity();
            player.setWalkSpeed((float) ((100 + values[0]) / 500D));
        }
    }

    @Override
    public Object getPlaceholder(double[] values, Player player, String string) {
        return string.equals(getName()) ? values[0] : null;
    }

    @Override
    public List<String> getPlaceholders() {
        return Collections.singletonList(getName());
    }

    @Override
    public void loadAttribute(double[] values, String lore) {
        if (lore.contains(Config.getConfig().getString(Config.NAME_WALK_SPEED))) {
            values[0] += getNumber(lore);
        }
    }

    @Override
    public void correct(double[] values) {
        values[0] = Math.min(Math.max(values[0], -99), 400);
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_WALK_SPEED);
    }
}
