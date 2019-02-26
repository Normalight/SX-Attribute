package github.saukiya.sxattribute.data.attribute.sub.attack;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import org.bukkit.EntityEffect;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collections;
import java.util.List;

/**
 * 撕裂
 *
 * @author Saukiya
 */
public class Tearing extends SubAttribute {

    /**
     * double[0] 撕裂几率
     */
    public Tearing(JavaPlugin plugin) {
        super(plugin, 1, SXAttributeType.ATTACK);
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            DamageData damageData = (DamageData) eventData;
            if (values[0] > 0 && probability(values[0] - damageData.getDefenderData().getValues("Toughness")[0])) {
                int size = SXAttribute.getRandom().nextInt(3) + 1;
                double tearingDamage = damageData.getDefender().getHealth() / 100;
                new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        i++;
                        if (i >= 12 / size || damageData.getDefender().isDead() || damageData.getEvent().isCancelled())
                            cancel();
                        damageData.getDefender().playEffect(EntityEffect.HURT);
                        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damageData.getAttacker(), damageData.getDefender(), EntityDamageEvent.DamageCause.CUSTOM, tearingDamage);
                        if (!event.isCancelled()) {
                            double damage = damageData.getDefender().getHealth() < event.getDamage() ? damageData.getDefender().getHealth() : event.getDamage();
                            damageData.getDefender().setHealth(damageData.getDefender().getHealth() - damage);
                            if (SXAttribute.getVersionSplit()[1] >= 9) {
                                damageData.getDefender().getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, damageData.getDefender().getEyeLocation().add(0, -1, 0), 2, 0.2D, 0.2D, 0.2D, 0.1f);
                            }
                            if (damageData.getAttacker() instanceof Player) {
                                ((Player) damageData.getAttacker()).playSound(damageData.getDefender().getEyeLocation(), "ENTITY_" + damageData.getDefender().getType().toString() + "_HURT", 1, 1);
                            }
                        }
                    }
                }.runTaskTimer(getPlugin(), 5, size);
                damageData.sendHolo(Message.getMsg(Message.PLAYER__HOLOGRAPHIC__TEARING, getDf().format(tearingDamage * 12 / size)));
                Message.send(damageData.getAttacker(), Message.PLAYER__BATTLE__TEARING, damageData.getDefenderName(), getFirstPerson(), getDf().format(tearingDamage * 12 / size));
                Message.send(damageData.getDefender(), Message.PLAYER__BATTLE__TEARING, getFirstPerson(), damageData.getAttackerName(), getDf().format(tearingDamage * 12 / size));
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
        if (lore.contains(Config.getConfig().getString(Config.NAME_TEARING))) {
            values[0] += getNumber(lore);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_TEARING);
    }
}
