package github.saukiya.sxattribute.data.attribute.sub.attack;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.util.Message;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 失明
 *
 * @author Saukiya
 */
public class Potion extends SubAttribute {

    @Getter
    private PotionData[] dataList;

    /**
     * 药水属性
     * double[28] PotionEffectType最大数量28个
     */
    public Potion(JavaPlugin plugin) {
        super(plugin, 28, SXAttributeType.ATTACK);
    }

    @Override
    protected YamlConfiguration defaultYaml(YamlConfiguration yaml) {
        yaml.set("NetworkLink", "https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html");
        yaml.set("List.BLINDNESS.MessageName", "&7致盲&7");
        yaml.set("List.BLINDNESS.DiscernName", "致盲几率");
        yaml.set("List.BLINDNESS.CombatPower", 1);
        yaml.set("List.BLINDNESS.MinLevel", 1);
        yaml.set("List.BLINDNESS.MaxLevel", 2);
        yaml.set("List.BLINDNESS.MinTime", 2);
        yaml.set("List.BLINDNESS.MaxTime", 5);
        yaml.set("List.WITHER.MessageName", "&9诅咒&7");
        yaml.set("List.WITHER.DiscernName", "诅咒几率");
        yaml.set("List.WITHER.CombatPower", 1);
        yaml.set("List.WITHER.MinLevel", 1);
        yaml.set("List.WITHER.MaxLevel", 2);
        yaml.set("List.WITHER.MinTime", 2);
        yaml.set("List.WITHER.MaxTime", 5);
        yaml.set("List.SLOW.MessageName", "&3减速&7");
        yaml.set("List.SLOW.DiscernName", "减速几率");
        yaml.set("List.SLOW.CombatPower", 1);
        yaml.set("List.SLOW.MinLevel", 1);
        yaml.set("List.SLOW.MaxLevel", 2);
        yaml.set("List.SLOW.MinTime", 2);
        yaml.set("List.SLOW.MaxTime", 5);
        yaml.set("List.POISON.MessageName", "&d中毒&7");
        yaml.set("List.POISON.DiscernName", "中毒几率");
        yaml.set("List.POISON.CombatPower", 1);
        yaml.set("List.POISON.MinLevel", 1);
        yaml.set("List.POISON.MaxLevel", 2);
        yaml.set("List.POISON.MinTime", 2);
        yaml.set("List.POISON.MaxTime", 5);
        yaml.set("List.HUNGER.MessageName", "&c饥饿&7");
        yaml.set("List.HUNGER.DiscernName", "夺食几率");
        yaml.set("List.HUNGER.CombatPower", 1);
        yaml.set("List.HUNGER.MinLevel", 4);
        yaml.set("List.HUNGER.MaxLevel", 5);
        yaml.set("List.HUNGER.MinTime", 2);
        yaml.set("List.HUNGER.MaxTime", 5);
        yaml.set("List.HUNGER.UpperLimit", 50);
        return yaml;
    }

    @Override
    public void onEnable() {
        List<PotionData> potionDataList = new ArrayList<>();
        Set<String> nameList = getYaml().getConfigurationSection("List").getKeys(false);
        for (PotionEffectType type : PotionEffectType.values()) {
            if (type != null && nameList.contains(type.getName()) && getYaml().isString("List." + type.getName() + ".MessageName") && getYaml().isString("List." + type.getName() + ".DiscernName")) {
                potionDataList.add(new PotionData(type, getYaml().getConfigurationSection("List." + type.getName())));
            }
        }
        dataList = potionDataList.toArray(new PotionData[0]);
        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + getName() + " - Load " + dataList.length + " PotionAttributes");
    }

    @Override
    public void onReLoad() {
        for (String typeName : getYaml().getConfigurationSection("List").getKeys(false)) {
            for (PotionData potionData : dataList) {
                if (typeName.equals(potionData.type.getName())) {
                    potionData.load(getYaml().getConfigurationSection("List." + potionData.type.getName()));
                    break;
                }
            }
        }
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            DamageData damageData = (DamageData) eventData;
            double defenseToughness = damageData.getDefenderData().getValues("Toughness")[0];
            for (int i = 0; i < dataList.length; i++) {
                if (probability(values[i] - defenseToughness)) {
                    double time = dataList[i].getTime();
                    damageData.getDefender().addPotionEffect(new PotionEffect(dataList[i].type, (int) (time * 20), dataList[i].getLevel()));
                    damageData.sendHolo(Message.getMsg(Message.PLAYER__HOLOGRAPHIC__POTION, dataList[i].messageName, getDf().format(time)));
                    Message.send(damageData.getAttacker(), Message.PLAYER__BATTLE__POTION, damageData.getDefenderName(), getFirstPerson(), getDf().format(time), dataList[i].messageName);
                    Message.send(damageData.getDefender(), Message.PLAYER__BATTLE__POTION, getFirstPerson(), damageData.getAttackerName(), getDf().format(time), dataList[i].messageName);
                }
            }
        }
    }

    @Override
    public Object getPlaceholder(double[] values, Player player, String string) {
        for (int i = 0; i < dataList.length; i++) {
            if (string.equals(dataList[i].type.getName())) {
                return values[i];
            }
        }
        return null;
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.stream(dataList).map(data -> data.type.getName()).collect(Collectors.toList());
    }


    @Override
    public void loadAttribute(double[] values, String lore) {
        for (int i = 0; i < dataList.length; i++) {
            if (lore.contains(dataList[i].discernName)) {
                values[i] += getNumber(lore);
                return;
            }
        }
    }

    @Override
    public void correct(double[] values) {
        for (int i = 0; i < dataList.length; i++) {
            values[i] = Math.min(Math.max(values[i], 0), dataList[i].upperLimit);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        double combatPower = 0D;
        for (int i = 0; i < dataList.length; i++) {
            combatPower += values[i] * dataList[i].combatPower;
        }
        return combatPower;
    }

    public class PotionData {

        private PotionEffectType type;
        private String messageName;
        private String discernName;
        private int combatPower;
        private int minLevel;
        private int maxLevel;
        private int minTime;
        private int maxTime;
        private int upperLimit;

        public PotionData(PotionEffectType type, ConfigurationSection config) {
            this.type = type;
            load(config);
        }

        public void load(ConfigurationSection config) {
            this.messageName = config.getString("MessageName").replace("&", "§");
            this.discernName = config.getString("DiscernName").replace("&", "§");
            this.combatPower = config.getInt("CombatPower", 1);
            this.minLevel = config.getInt("MinLevel", 1);
            this.maxLevel = config.getInt("MaxLevel", 2);
            this.minTime = config.getInt("MinTime", 2);
            this.maxTime = config.getInt("MaxTime", 5);
            this.upperLimit = config.getInt("UpperLimit", 100);
        }

        public int getLevel() {
            return SXAttribute.getRandom().nextInt(maxLevel - minLevel + 1) + minLevel;
        }

        public double getTime() {
            return SXAttribute.getRandom().nextDouble() * (maxTime - minTime) + minTime;
        }
    }
}
