package github.saukiya.sxattribute.data.attribute.sub.attack;

import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.listener.OnHealthChangeDisplayListener;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * 吸血
 *
 * @author Saukiya
 */
public class LifeSteal extends SubAttribute {

    /**
     * double[0] 吸血几率
     * double[1] 吸血倍率
     */
    public LifeSteal(JavaPlugin plugin) {
        super(plugin, 2, SXAttributeType.ATTACK);
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            if (probability(values[0])) {
                DamageData damageData = (DamageData) eventData;
                LivingEntity damager = damageData.getAttacker();
                double maxHealth = OnHealthChangeDisplayListener.getMaxHealth(damager);
                double lifeHealth = damageData.getDamage() * values[1] / 100;
                EntityRegainHealthEvent event = new EntityRegainHealthEvent(damager, lifeHealth, EntityRegainHealthEvent.RegainReason.CUSTOM);
                Bukkit.getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    return;
                }
                lifeHealth = (maxHealth < damager.getHealth() + event.getAmount()) ? (maxHealth - damager.getHealth()) : event.getAmount();
                damager.setHealth(damager.getHealth() + lifeHealth);
                damageData.sendHolo(Message.getMsg(Message.PLAYER__HOLOGRAPHIC__LIFE_STEAL, getDf().format(lifeHealth)));
                Message.send(damager, Message.PLAYER__BATTLE__LIFE_STEAL, damageData.getDefenderName(), getFirstPerson(), getDf().format(lifeHealth));
                Message.send(damageData.getDefender(), Message.PLAYER__BATTLE__LIFE_STEAL, getFirstPerson(), damageData.getAttackerName(), getDf().format(lifeHealth));
            }
        }
    }

    @Override
    public Object getPlaceholder(double[] values, Player player, String string) {
        switch (string) {
            case "LifeStealRate":
                return values[0];
            case "LifeSteal":
                return values[1];
        }
        return null;
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.asList(
                "LifeStealRate",
                "LifeSteal"
        );
    }

    @Override
    public void loadAttribute(double[] values, String lore) {
        if (lore.contains(Config.getConfig().getString(Config.NAME_LIFE_STEAL_RATE))) {
            values[0] += getNumber(lore);
        }
        if (lore.contains(Config.getConfig().getString(Config.NAME_LIFE_STEAL))) {
            values[1] += getNumber(lore);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_LIFE_STEAL_RATE) +
                values[1] * Config.getConfig().getInt(Config.VALUE_LIFE_STEAL);
    }
}
