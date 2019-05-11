package github.saukiya.sxattribute.data.attribute.sub.other;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.api.Sx;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.util.Message;
import github.saukiya.sxlevel.event.ChangeType;
import github.saukiya.sxlevel.event.SXExpChangeEvent;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.List;

/**
 * 经验加成
 *
 * @author Saukiya
 */
public class ExpAddition extends SubAttribute {

    /**
     * double[] 经验加成
     */
    public ExpAddition(JavaPlugin plugin) {
        super(plugin, 1, SXAttributeType.OTHER);
    }

    @Override
    protected YamlConfiguration defaultConfig(YamlConfiguration config) {
        config.set("Message", Message.getMessagePrefix() + "&7你的经验增加了 &6{0}&7! [&a+{1}%&7]");
        config.set("ExpAddition.DiscernName", "经验增幅");
        config.set("ExpAddition.CombatPower", 1);
        return config;
    }

    @Override
    public Listener getListener() {
        return SXAttribute.isSxLevel() ? new OnSXExpChangeListener() : new OnExpChangeListener();
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        // no event
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
        if (lore.contains(getString("ExpAddition.DiscernName"))) {
            values[0] += getNumber(lore);
        }
    }

    @Override
    public void correct(double[] values) {
        super.correct(values);
        values[0] = Math.min(values[0], config().getInt("ExpAddition.UpperLimit", Integer.MAX_VALUE));
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return values[0] * config().getInt("ExpAddition.CombatPower");
    }

    /**
     * 默认经验监听器
     */
    private class OnExpChangeListener implements Listener {

        @SuppressWarnings("Duplicates")// 滚你妈的idea重复警告
        @EventHandler
        private void onExpChangeEvent(PlayerExpChangeEvent event) {
            Player player = event.getPlayer();
            double expAddition = Sx.getEntityData(player).getValues(getName())[0];
            if (event.getAmount() > 0 && expAddition > 0) {
                event.setAmount((int) (event.getAmount() * (100 + expAddition) / 100));
                send(player, "Message", event.getAmount(), expAddition);
            }
        }
    }

    /**
     * SX-level 经验监听器
     */
    private class OnSXExpChangeListener implements Listener {
        @EventHandler
        private void onExpChangeEvent(SXExpChangeEvent event) {
            if (event.isCancelled() || !event.getType().equals(ChangeType.ADD)) {
                return;
            }
            Player player = event.getPlayer();
            Double expAddition = Sx.getEntityData(player).getValues(getName())[0];
            if (event.getAmount() > 0 && expAddition > 0) {
                int exp = (int) (event.getAmount() * expAddition / 100);
                event.setAmount(exp + event.getAmount());
                send(player, "Message", event.getAmount(), expAddition);
            }
        }
    }
}
