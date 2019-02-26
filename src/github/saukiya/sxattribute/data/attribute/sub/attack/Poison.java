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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

/**
 * 中毒
 *
 * @author Saukiya
 */
@Deprecated
public class Poison extends SubAttribute {

    /**
     * double[0] 中毒几率
     */
    public Poison(JavaPlugin plugin) {
        super(plugin, 1, SXAttributeType.ATTACK);
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            DamageData damageData = (DamageData) eventData;
            if (values[0] > 0 && probability(values[0] - damageData.getDefenderData().getValues("Toughness")[0])) {
                int tick = 40 + SXAttribute.getRandom().nextInt(60);
                damageData.getDefender().addPotionEffect(new PotionEffect(PotionEffectType.POISON, tick, SXAttribute.getRandom().nextInt(2) + 1));
                damageData.sendHolo(Message.getMsg(Message.PLAYER__HOLOGRAPHIC__POISON, getDf().format(tick / 20D)));
                Message.send(damageData.getAttacker(), Message.PLAYER__BATTLE__POISON, damageData.getDefenderName(), getFirstPerson(), getDf().format(tick / 20D));
                Message.send(damageData.getDefender(), Message.PLAYER__BATTLE__POISON, getFirstPerson(), damageData.getAttackerName(), getDf().format(tick / 20D));
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
        if (lore.contains(Config.getConfig().getString(Config.NAME_POISON))) {
            values[0] += getNumber(lore);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_POISON);
    }
}
