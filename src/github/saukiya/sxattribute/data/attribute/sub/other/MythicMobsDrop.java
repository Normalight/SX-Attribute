package github.saukiya.sxattribute.data.attribute.sub.other;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.util.Message;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

//import github.saukiya.sxseal.SXSeal;

/**
 * @author Saukiya
 */
public class MythicMobsDrop extends SubAttribute implements Listener {

    public MythicMobsDrop(JavaPlugin plugin) {
        super(plugin, 0, new SXAttributeType(SXAttributeType.Type.OTHER, "MythicMobsDrop"));
    }

    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("MythicMobs") != null) {
//            sxSeal = Bukkit.getPluginManager().getPlugin("SX-Seal") != null;
            Bukkit.getPluginManager().registerEvents(this, getPlugin());
        }
    }

    @EventHandler
    private void onMythicMobDeathEvent(MythicMobDeathEvent event) {
        if (event.getKiller() instanceof Player) {
            MythicMob mm = event.getMobType();
            List<ItemStack> drops = event.getDrops();
            //    private static boolean sxSeal = false;
            for (String str : mm.getConfig().getStringList("SX-Drop")) {
                if (str.contains(" ")) {
                    String[] args = str.split(" ");
                    int amount = 1;
                    if (args.length > 2 && args[2].length() > 0 && SXAttribute.getRandom().nextDouble() > Double.valueOf(args[2].replaceAll("[^0-9.]", ""))) {
                        continue;
                    }
                    if (args.length > 1 && args[1].length() > 0) {// 数量判断
                        if (args[1].contains("-") && args[1].split("-").length > 1) {
                            int i1 = Integer.valueOf(args[1].split("-")[0].replaceAll("[^0-9]", ""));
                            int i2 = Integer.valueOf(args[1].split("-")[1].replaceAll("[^0-9]", ""));
                            if (i1 > i2) {
                                Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cMythicMobs - Drop Random Error: §4" + mm.getDisplayName() + "§c - §4" + str);
                            } else {
                                amount = SXAttribute.getRandom().nextInt(i2 - i1 + 1) + i1;
                            }
                        } else {
                            amount = Integer.valueOf(args[1].replaceAll("[^0-9]", ""));
                        }
                    }
                    ItemStack item = SXAttribute.getApi().getItem(args[0], (Player) event.getKiller());
                    if (item != null) {
//                        if (str.contains("seal") && sxSeal) {
//                            SXSeal.getApi().sealItem(itemdata);
//                        }
                        item.setAmount(amount);
                        drops.add(item.clone());
                    } else {
                        Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cMythicMobs - Drop No Item: §4" + mm.getDisplayName() + "§c - §4" + args[0]);
                    }
                }
            }
        }
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
    }

    @Override
    public Object getPlaceholder(double[] values, Player player, String string) {
        return null;
    }

    @Override
    public List<String> getPlaceholders() {
        return new ArrayList<>();
    }

    @Override
    public void loadAttribute(double[] values, String lore) {
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return 0;
    }
}
