package github.saukiya.sxattribute.data.attribute.sub.defence;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

/**
 * 闪避
 *
 * @author Saukiya
 */
public class Dodge extends SubAttribute {

    /**
     * double[0] 闪避几率
     */
    public Dodge(JavaPlugin plugin) {
        super(plugin, 1, SXAttributeType.DEFENCE);
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            DamageData damageData = (DamageData) eventData;
            if (values[0] > 0 && probability(values[0] - damageData.getAttackerData().getValues("HitRate")[0])) {
                damageData.setCancelled(true);
                Location loc = damageData.getAttacker().getLocation().clone();
                loc.setYaw(loc.getYaw() + SXAttribute.getRandom().nextInt(80) - 40);
                damageData.getDefender().setVelocity(loc.getDirection().setY(0.1).multiply(0.7));
                damageData.sendHolo(Message.getMsg(Message.PLAYER__HOLOGRAPHIC__DODGE));
                Message.send(damageData.getAttacker(), Message.PLAYER__BATTLE__DODGE, damageData.getDefenderName(), getFirstPerson());
                Message.send(damageData.getDefender(), Message.PLAYER__BATTLE__DODGE, getFirstPerson(), damageData.getAttackerName());
            }
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
        if (lore.contains(Config.getConfig().getString(Config.NAME_DODGE))) {
            values[0] += getNumber(lore);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_DODGE);
    }
}
