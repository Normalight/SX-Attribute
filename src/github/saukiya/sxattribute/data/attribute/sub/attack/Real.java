package github.saukiya.sxattribute.data.attribute.sub.attack;

import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

/**
 * 破甲
 *
 * @author Saukiya
 */
public class Real extends SubAttribute {

    /**
     * double[0] 破甲几率
     */
    public Real(JavaPlugin plugin) {
        super(plugin, 1, SXAttributeType.ATTACK);
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            if (probability(values[0])) {
                DamageData damageData = (DamageData) eventData;
                damageData.getEffectiveAttributeList().add(getName());
                damageData.sendHolo(Message.getMsg(Message.PLAYER__HOLOGRAPHIC__REAL));
                Message.send(damageData.getAttacker(), Message.PLAYER__BATTLE__REAL, damageData.getDefenderName(), getFirstPerson());
                Message.send(damageData.getDefender(), Message.PLAYER__BATTLE__REAL, getFirstPerson(), damageData.getAttackerName());
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
        if (lore.contains(Config.getConfig().getString(Config.NAME_REAL))) {
            values[0] += getNumber(lore);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_REAL);
    }
}
