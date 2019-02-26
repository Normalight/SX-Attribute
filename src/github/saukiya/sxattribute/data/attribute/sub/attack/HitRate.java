package github.saukiya.sxattribute.data.attribute.sub.attack;

import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.util.Config;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

/**
 * 命中
 *
 * @author Saukiya
 */
public class HitRate extends SubAttribute {

    /**
     * double[0] 命中几率
     */
    public HitRate(JavaPlugin plugin) {
        super(plugin, 1, SXAttributeType.OTHER);
    }


    @Override
    public void eventMethod(double[] values, EventData eventData) {
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
        if (lore.contains(Config.getConfig().getString(Config.NAME_HIT_RATE))) {
            values[0] += getNumber(lore);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_HIT_RATE);
    }
}
