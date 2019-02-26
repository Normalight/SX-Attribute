package github.saukiya.sxattribute.data.attribute.sub.attack;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.data.eventdata.sub.UpdateEventData;
import github.saukiya.sxattribute.util.Config;
import lombok.Getter;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

import java.util.Arrays;
import java.util.List;

/**
 * 攻击力
 *
 * @author Saukiya 我随便打介绍注释啦
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
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            DamageData damageData = (DamageData) eventData;
            EntityDamageByEntityEvent event = damageData.getEvent();

            damageData.addDamage(((!Config.isDamageGauges() || event.getDamager() instanceof Projectile) || !(event.getDamager() instanceof Player)) ? getAttribute(values, TYPE_DEFAULT) : getAttribute(values, TYPE_DEFAULT) - values[0]);

            // DamageGauges 1.9攻击冷却机制
            // [if]如果不是1.9机制，或者为箭，那么为最大最小伤害的平均值。
            // [else if]如果为玩家 那么 平均值-最小伤害。(更新中自带最小伤害)
            // [else]非玩家，那么为平均值(更新不处理非玩家)
//            if (!Config.isDamageGauges() || event.getAttacker() instanceof Projectile) {
//                damageData.addDamage(getAttribute(values, 0));
//            } else if (event.getAttacker() instanceof Player) {
//                damageData.addDamage(getAttribute(values, 0) - values[0]);
//            } else {
//                damageData.addDamage(getAttribute(values, 0));
//            }
            // PVP/PVE伤害
            damageData.addDamage(getAttribute(values, event.getEntity() instanceof Player ? TYPE_PVE : TYPE_PVP));
            // 如果该事件更新事件，并且更新目标为玩家
        } else if (eventData instanceof UpdateEventData && ((UpdateEventData) eventData).getEntity() instanceof Player) {
            ((UpdateEventData) eventData).getEntity().getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(Config.isDamageGauges() ? values[0] : values[1] == 0D ? 1 : 0.01);
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
        }
        return null;
    }

    // 显示papi变量...
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

    // 一个获取最大最小伤害值的东西，虽然可以把这三个方法通过getAttribute(int 类型)集合在一块，不过还没有弄
    private double getAttribute(double[] values, int type) {
        return values[type * 2] + SXAttribute.getRandom().nextDouble() * (values[type * 2 + 1] - values[type * 2]);
    }

    // 读取属性数据，略为麻烦 无法优化
    @Override
    public void loadAttribute(double[] values, String lore) {
        String[] loreSplit = lore.split("-");
        if (lore.contains(Config.getConfig().getString(Config.NAME_PVE_DAMAGE))) {
            values[4] += getNumber(loreSplit[0]);
            values[5] += getNumber(loreSplit[loreSplit.length > 1 ? 1 : 0]);
        } else if (lore.contains(Config.getConfig().getString(Config.NAME_PVP_DAMAGE))) {
            values[2] += getNumber(loreSplit[0]);
            values[3] += getNumber(loreSplit[loreSplit.length > 1 ? 1 : 0]);
        } else if (lore.contains(Config.getConfig().getString(Config.NAME_DAMAGE))) {
            values[0] += getNumber(loreSplit[0]);
            values[1] += getNumber(loreSplit[loreSplit.length > 1 ? 1 : 0]);
        }
    }

    // 数据纠正 防止错误的上lore导致最小值大于最大值 还没用Math.max
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

    // 换算成战斗力数值 略为麻烦 无法优化
    @Override
    public double calculationCombatPower(double[] values) {
        double value = (values[0] + values[1]) / 2 * Config.getConfig().getInt(Config.VALUE_DAMAGE);
        value += (values[2] + values[3]) / 2 * Config.getConfig().getInt(Config.VALUE_PVP_DAMAGE);
        value += (values[4] + values[5]) / 2 * Config.getConfig().getInt(Config.VALUE_PVE_DAMAGE);
        return value;
    }
}
