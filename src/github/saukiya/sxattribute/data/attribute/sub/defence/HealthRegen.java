package github.saukiya.sxattribute.data.attribute.sub.defence;

import github.saukiya.sxattribute.api.Sx;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 生命恢复
 *
 * @author Saukiya
 */
public class HealthRegen extends SubAttribute {

    // TODO 可能要修复某bug
    private BukkitRunnable runnable = new BukkitRunnable() {
        @Override
        public void run() {
            try {
                for (Player player : new ArrayList<>(Bukkit.getOnlinePlayers())) {
                    if (player != null && !player.isDead() && player.isOnline()) {
                        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
                        if (player.getHealth() < maxHealth) {
                            double healthRegen = Sx.getEntityData(player).getValues(getName())[0];
                            if (healthRegen > 0) {
                                EntityRegainHealthEvent event = new EntityRegainHealthEvent(player, healthRegen, EntityRegainHealthEvent.RegainReason.CUSTOM);
                                Bukkit.getPluginManager().callEvent(event);
                                if (!event.isCancelled()) {
                                    healthRegen = (event.getAmount() + player.getHealth()) > maxHealth ? (maxHealth - player.getHealth()) : event.getAmount();
                                    player.setHealth(healthRegen + player.getHealth());
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§4生命恢复系统崩溃 正在重新启动!");
                this.cancel();
                HealthRegen.this.onEnable();
                Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§c启动完毕!");
                Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§c如果此消息连续刷屏，请反馈作者QQ: 1940208750");
            }
        }
    };



    /**
     * double[0] 生命回复
     */
    public HealthRegen(JavaPlugin plugin) {
        super(plugin, 1, SXAttributeType.OTHER);
    }

    @Override
    protected YamlConfiguration defaultConfig(YamlConfiguration config) {
        config.set("Health.DiscernName", "生命恢复");
        config.set("Health.CombatPower", 1);
        config.set("HealthScaled.Enabled", true);
        config.set("HealthScaled.Value", 40);
        return config;
    }

    @Override
    public void onEnable() {
        runnable.runTaskTimer(getPlugin(), 19, 20);
    }

    @Override
    public void onDisable() {
        runnable.cancel();
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
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
        if (lore.contains(getString("Health.DiscernName"))) {
            values[0] += getNumber(lore);
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * config().getInt("Health.CombatPower");
    }
}
