package github.saukiya.sxattribute.data.attribute.sub.defence;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.manager.AttributeManager;
import github.saukiya.sxattribute.api.Sx;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.UpdateData;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.SpigotConfig;

import java.util.Arrays;
import java.util.List;

/**
 * 生命 - 当前不更新怪物生命
 *
 * @author Saukiya
 */
public class Health extends SubAttribute {

    private boolean skillAPI = false;

    private boolean healthScaled = false;

    private int healthScaledValue = 40;

    /**
     * 生命
     * double[0] 生命值
     */
    public Health(JavaPlugin plugin) {
        super(plugin, 1, SXAttributeType.UPDATE);
    }

    @Override
    protected YamlConfiguration defaultConfig(YamlConfiguration config) {
        config.set("Health.DiscernName", "生命上限");
        config.set("Health.CombatPower", 1);
        config.set("HealthScaled.Enabled", true);
        config.set("HealthScaled.Value", 40);
        return config;
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof UpdateData && ((UpdateData) eventData).getEntity() instanceof Player) {
            Player player = (Player) ((UpdateData) eventData).getEntity();
            if (skillAPI) {
                SkillAPI.getPlayerData(player).getAttribute(AttributeManager.HEALTH);
            }
            double maxHealth = values[0] + getSkillAPIHealth(player);
            if (player.getHealth() > maxHealth) player.setHealth(maxHealth);
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
            if (healthScaled && healthScaledValue < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
                player.setHealthScaled(true);
                player.setHealthScale(healthScaledValue);
            } else {
                player.setHealthScaled(false);
            }
        }
    }

    private int getSkillAPIHealth(Player player) {
        return skillAPI ? SkillAPI.getPlayerData(player).getClasses().stream().mapToInt(aClass -> (int) aClass.getHealth()).sum() : 0;
    }

    @Override
    public void onEnable() {
        healthScaled = config().getBoolean("HealthScaled.Enabled");
        skillAPI = Bukkit.getPluginManager().getPlugin("SkillAPI") != null;
    }

    @Override
    public void onReLoad() {
        super.onReLoad();
        healthScaled = config().getBoolean("HealthScaled.Enabled");
        healthScaledValue = config().getInt("HealthScaled.Value", 40);
    }

    @Override
    public Object getPlaceholder(double[] values, Player player, String string) {
        switch (string) {
            case "MaxHealth":
                return Sx.getMaxHealth(player);
            case "Health":
                return player.getHealth();
            case "HealthValue":
                return values[0];
            default:
                return null;
        }
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.asList(
                "MaxHealth",
                "Health",
                "HealthValue"
        );
    }

    @Override
    public void loadAttribute(double[] values, String lore) {
        if (lore.contains(getString("Health.DiscernName"))) {
            values[0] += getNumber(lore);
        }
    }

    @Override
    public void correct(double[] values) {
        values[0] = Math.max(values[0], 1D);
        values[0] = Math.min(values[0], SpigotConfig.maxHealth);
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * config().getInt("Health.CombatPower");
    }
}
