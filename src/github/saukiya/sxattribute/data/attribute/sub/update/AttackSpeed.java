package github.saukiya.sxattribute.data.attribute.sub.update;

import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.UpdateEventData;
import github.saukiya.sxattribute.util.Config;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

public class AttackSpeed extends SubAttribute {

    /**
     * double[0] 攻击速度
     */
    public AttackSpeed(JavaPlugin plugin) {
        super(plugin, 1, SXAttributeType.UPDATE);
    }


    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof UpdateEventData && ((UpdateEventData) eventData).getEntity() instanceof Player) {
            Player player = (Player) ((UpdateEventData) eventData).getEntity();
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(Config.getConfig().getDouble(Config.DEFAULT_ATTACK_SPEED) * (100 + values[0]) / 100);
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
        if (lore.contains(Config.getConfig().getString(Config.NAME_ATTACK_SPEED))) {
            values[0] += getNumber(lore);
        }
    }

    @Override
    public void correct(double[] values) {
        values[0] = Math.max(values[0], -100);
    }


    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_ATTACK_SPEED);
    }
}
