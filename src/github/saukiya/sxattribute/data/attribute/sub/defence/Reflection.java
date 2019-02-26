package github.saukiya.sxattribute.data.attribute.sub.defence;

import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.EntityEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * 反射
 *
 * @author Saukiya
 */
public class Reflection extends SubAttribute {

    /**
     * double[0] 反射几率
     * double[1] 反射伤害
     */
    public Reflection(JavaPlugin plugin) {
        super(plugin, 2, SXAttributeType.DEFENCE);
    }

    // TODO 更正EntityDamageByEntityEvent 过老问题
    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            if (probability(values[0])) {
                DamageData damageData = (DamageData) eventData;
                if (!(damageData.getEffectiveAttributeList().contains("Real") || damageData.getEffectiveAttributeList().contains("Block"))) {
                    damageData.getEffectiveAttributeList().add(this.getName());
                    double damage = damageData.getDamage() * values[1] / 100;
                    LivingEntity damager = damageData.getAttacker();
                    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damageData.getDefender(), damager, EntityDamageEvent.DamageCause.CUSTOM, damage);
                    Bukkit.getPluginManager().callEvent(event);
                    if (event.isCancelled()) {
                        return;
                    }
                    damager.playEffect(EntityEffect.HURT);
                    damager.setHealth(damager.getHealth() < event.getFinalDamage() ? 0 : (damager.getHealth() - event.getFinalDamage()));
                    damageData.sendHolo(Message.getMsg(Message.PLAYER__HOLOGRAPHIC__REFLECTION, getDf().format(damage)));
                    Message.send(damager, Message.PLAYER__BATTLE__REFLECTION, getFirstPerson(), damageData.getDefenderName(), getDf().format(damage));
                    Message.send(damageData.getDefender(), Message.PLAYER__BATTLE__REFLECTION, damageData.getAttackerName(), getFirstPerson(), getDf().format(damage));
                }
            }
        }
    }

    @Override
    public Object getPlaceholder(double[] values, Player player, String string) {
        switch (string) {
            case "ReflectionRate":
                return values[0];
            case "Reflection":
                return values[1];
        }
        return null;
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.asList(
                "ReflectionRate",
                "Reflection"
        );
    }

    @Override
    public void loadAttribute(double[] values, String lore) {
        if (lore.contains(Config.getConfig().getString(Config.NAME_REFLECTION_RATE))) {
            values[0] += getNumber(lore);
        }
        if (lore.contains(Config.getConfig().getString(Config.NAME_REFLECTION))) {
            values[1] += getNumber(lore);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_REFLECTION_RATE) + values[1] * Config.getConfig().getInt(Config.VALUE_REFLECTION);
    }
}
