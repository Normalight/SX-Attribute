package github.saukiya.sxattribute.data.attribute.sub.attack;

import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * 暴击
 *
 * @author Saukiya
 */
public class Crit extends SubAttribute {

//    private String

    /**
     * double[0] 暴击几率
     * double[1] 暴击伤害
     */
    public Crit(JavaPlugin plugin) {
        super(plugin, 2, SXAttributeType.ATTACK);
    }

    // TODO 这里
    @Override
    protected YamlConfiguration defaultYaml(YamlConfiguration yaml) {
        yaml.set("Crit.DiscernName", "暴伤增幅");
        yaml.set("Crit.CombatPower", 1);
        yaml.set("Crit.UpperLimit", 500);
        yaml.set("CritRate.DiscernName", "暴击几率");
        yaml.set("CritRate.CombatPower", 1);
        return yaml;
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            if (probability(values[0])) {
                DamageData damageData = (DamageData) eventData;
                damageData.setCrit(true);
                damageData.setDamage(damageData.getDamage() * (100 + values[1]) / 100);
                Message.send(damageData.getAttacker(), Message.PLAYER__BATTLE__CRIT, getFirstPerson(), damageData.getDefenderName());
                Message.send(damageData.getDefender(), Message.PLAYER__BATTLE__CRIT, damageData.getAttackerName(), getFirstPerson());
            }
        }
    }

    @Override
    public Object getPlaceholder(double[] values, Player player, String string) {
        switch (string) {
            case "CritRate":
                return values[0];
            case "Crit":
                return values[1];
            case "Crit_100":
                return values[1] + 100;
        }
        return null;
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.asList(
                "CritRate",
                "Crit",
                "Crit_100"
        );
    }

    @Override
    public void loadAttribute(double[] values, String lore) {
        if (lore.contains(Config.getConfig().getString(Config.NAME_CRIT_RATE))) {
            values[0] += getNumber(lore);
        } else if (lore.contains(Config.getConfig().getString(Config.NAME_CRIT))) {
            values[1] += getNumber(lore);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_CRIT_RATE) +
                values[1] * Config.getConfig().getInt(Config.VALUE_CRIT);
    }
}
