package github.saukiya.sxattribute.data.attribute.sub.attack;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.data.eventdata.sub.UpdateData;
import github.saukiya.sxattribute.event.SXDamageEvent;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import lombok.Getter;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

import java.util.Arrays;
import java.util.List;

/**
 * 伤害
 *
 * @author Saukiya
 */
public class Damage extends SubAttribute {

    @Getter
    private static int TYPE_DEFAULT = 0;
    @Getter
    private static int TYPE_PVP = 1;
    @Getter
    private static int TYPE_PVE = 2;

    /**
     * double[0] 伤害最小值
     * double[1] 伤害最大值
     * double[2] 伤害最小值 - PVP
     * double[3] 伤害最大值 - PVP
     * double[4] 伤害最小值 - PVE
     * double[5] 伤害最大值 - PVE
     */
    public Damage(JavaPlugin plugin) {
        super(plugin, 6, SXAttributeType.ATTACK, SXAttributeType.UPDATE);
    }

    @Override
    protected YamlConfiguration defaultConfig(YamlConfiguration config) {
        config.set("Damage.DiscernName", "攻击力");
        config.set("Damage.CombatPower", 1);
        config.set("PVPDamage.DiscernName", "PVP攻击力");
        config.set("PVPDamage.CombatPower", 1);
        config.set("PVEDamage.DiscernName", "PVE攻击力");
        config.set("PVEDamage.CombatPower", 1);
        return config;
    }

    @EventHandler
    public void onSXDamageEvent(SXDamageEvent event) {
        DamageData damageData = event.getData();
        if (!damageData.isCancelled() && !damageData.isCrit()) {
            damageData.sendHolo(Message.getMsg(Message.PLAYER__HOLOGRAPHIC__DAMAGE, SXAttribute.getDf().format(damageData.getEvent().getFinalDamage())));
        }
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            DamageData damageData = (DamageData) eventData;
            EntityDamageByEntityEvent event = damageData.getEvent();

            damageData.addDamage(((!Config.isDamageGauges() || event.getDamager() instanceof Projectile) || !(event.getDamager() instanceof Player)) ? getAttribute(values, TYPE_DEFAULT) : getAttribute(values, TYPE_DEFAULT) - values[0]);

            damageData.addDamage(getAttribute(values, event.getEntity() instanceof Player ? TYPE_PVE : TYPE_PVP));
            // 如果该事件更新事件，并且更新目标为玩家
        } else if (eventData instanceof UpdateData && ((UpdateData) eventData).getEntity() instanceof Player) {
            ((UpdateData) eventData).getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(Config.isDamageGauges() ? values[0] : values[1] == 0D ? 1 : 0.01);
        }
    }

    @Override
    public Object getPlaceholder(double[] values, Player player, String string) {
        switch (string) {
            case "MinDamage":
                return values[0];
            case "MaxDamage":
                return values[1];
            case "Damage":
                return values[0] == values[1] ? values[0] : (getDf().format(values[0]) + " - " + getDf().format(values[1]));
            case "PvpMinDamage":
                return values[2];
            case "PvpMaxDamage":
                return values[3];
            case "PvpDamage":
                return values[2] == values[3] ? values[2] : (getDf().format(values[2]) + " - " + getDf().format(values[3]));
            case "PveMinDamage":
                return values[4];
            case "PveMaxDamage":
                return values[5];
            case "PveDamage":
                return values[4] == values[5] ? values[4] : (getDf().format(values[4]) + " - " + getDf().format(values[5]));
            default :
                return null;
        }
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.asList(
                "MinDamage",
                "MaxDamage",
                "Damage",
                "PvpMinDamage",
                "PvpMaxDamage",
                "PvpDamage",
                "PveMinDamage",
                "PveMaxDamage",
                "PveDamage"
        );
    }

    private double getAttribute(double[] values, int type) {
        return values[type * 2] + SXAttribute.getRandom().nextDouble() * (values[type * 2 + 1] - values[type * 2]);
    }

    @Override
    public void loadAttribute(double[] values, String lore) {
        String[] loreSplit = lore.split("-");
        if (lore.contains(getString("PVEDamage.DiscernName"))) {
            values[4] += getNumber(loreSplit[0]);
            values[5] += getNumber(loreSplit[loreSplit.length > 1 ? 1 : 0]);
        } else if (lore.contains(getString("PVPDamage.DiscernName"))) {
            values[2] += getNumber(loreSplit[0]);
            values[3] += getNumber(loreSplit[loreSplit.length > 1 ? 1 : 0]);
        } else if (lore.contains(getString("Damage.DiscernName"))) {
            values[0] += getNumber(loreSplit[0]);
            values[1] += getNumber(loreSplit[loreSplit.length > 1 ? 1 : 0]);
        }
    }

    @Override
    public void correct(double[] values) {
        values[0] = Math.min(Math.max(values[0], Config.isDamageGauges() ? 1 : 0), SpigotConfig.attackDamage);
        values[1] = Math.min(values[1], SpigotConfig.attackDamage);
        values[1] = Math.max(values[1], values[0]);
        values[2] = Math.max(values[2], 0);
        values[3] = Math.max(values[3], values[2]);
        values[4] = Math.max(values[4], 0);
        values[5] = Math.max(values[5], values[4]);
    }

    @Override
    public double calculationCombatPower(double[] values) {
        double value = (values[0] + values[1]) / 2 * config().getInt("Damage.CombatPower");
        value += (values[2] + values[3]) / 2 * config().getInt("PVPDamage.CombatPower");
        value += (values[4] + values[5]) / 2 * config().getInt("PVEDamage.CombatPower");
        return value;
    }
}
