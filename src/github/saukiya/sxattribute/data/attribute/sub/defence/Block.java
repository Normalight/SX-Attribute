package github.saukiya.sxattribute.data.attribute.sub.defence;

import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

/**
 * 格挡
 *
 * @author Saukiya
 */
public class Block extends SubAttribute {

    /**
     * double[0] 格挡几率
     * double[1] 格挡伤害
     */
    public Block(JavaPlugin plugin) {
        super(plugin, 2, SXAttributeType.DEFENCE);
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            if (probability(values[0])) {
                DamageData damageData = (DamageData) eventData;
                if (!(damageData.getEffectiveAttributeList().contains("Real") || damageData.getEffectiveAttributeList().contains("Reflection"))) {
                    damageData.getEffectiveAttributeList().add(this.getName());
                    double blockDamage = damageData.getDamage() * values[1] / 100;
                    damageData.setDamage(damageData.getDamage() - blockDamage);
                    damageData.sendHolo(Message.getMsg(Message.PLAYER__HOLOGRAPHIC__BLOCK, getDf().format(blockDamage)));
                    Message.send(damageData.getAttacker(), Message.PLAYER__BATTLE__BLOCK, damageData.getDefenderName(), getFirstPerson(), getDf().format(blockDamage));
                    Message.send(damageData.getDefender(), Message.PLAYER__BATTLE__BLOCK, getFirstPerson(), damageData.getAttackerName(), getDf().format(blockDamage));
                }
            }
        }
    }

    @Override
    public Object getPlaceholder(double[] values, Player player, String string) {
        switch (string) {
            case "BlockRate":
                return values[0];
            case "Block":
                return values[1];
        }
        return null;
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.asList(
                "BlockRate",
                "Block"
        );
    }

    @Override
    public void loadAttribute(double[] values, String lore) {
        if (lore.contains(Config.getConfig().getString(Config.NAME_BLOCK_RATE))) {
            values[0] += getNumber(lore);
        }
        if (lore.contains(Config.getConfig().getString(Config.NAME_BLOCK))) {
            values[1] += getNumber(lore);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * Config.getConfig().getInt(Config.VALUE_BLOCK_RATE) + values[1] * Config.getConfig().getInt(Config.VALUE_BLOCK);
    }
}
