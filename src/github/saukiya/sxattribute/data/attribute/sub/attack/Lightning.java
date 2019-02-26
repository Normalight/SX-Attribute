package github.saukiya.sxattribute.data.attribute.sub.attack;

import github.saukiya.sxattribute.SXAttribute;
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
 * 雷霆
 *
 * @author Saukiya
 */
public class Lightning extends SubAttribute {

    /**
     * double[0] 雷霆几率
     */
    public Lightning(JavaPlugin plugin) {
        super(plugin, 1, SXAttributeType.ATTACK);
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            DamageData damageData = (DamageData) eventData;
            if (values[0] > 0 && probability(values[0] - damageData.getDefenderData().getValues("Toughness")[0])) {
                damageData.getDefender().getWorld().strikeLightningEffect(damageData.getDefender().getLocation());
                double lightningDamage = damageData.getDefender().getHealth() * SXAttribute.getRandom().nextDouble() / 10;
                damageData.getDefender().setHealth(damageData.getDefender().getHealth() - lightningDamage);
                damageData.sendHolo(Message.getMsg(Message.PLAYER__HOLOGRAPHIC__LIGHTNING, getDf().format(lightningDamage)));
                Message.send(damageData.getAttacker(), Message.PLAYER__BATTLE__LIGHTNING, damageData.getDefenderName(), getFirstPerson(), getDf().format(lightningDamage));
                Message.send(damageData.getDefender(), Message.PLAYER__BATTLE__LIGHTNING, getFirstPerson(), damageData.getAttackerName(), getDf().format(lightningDamage));
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
        if (lore.contains(Config.getConfig().getString(Config.NAME_LIGHTNING))) {
            values[0] += getNumber(lore);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_LIGHTNING);
    }
}
