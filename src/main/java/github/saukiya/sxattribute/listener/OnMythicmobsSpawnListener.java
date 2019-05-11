package github.saukiya.sxattribute.listener;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.util.Message;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * @author Saukiya
 */

public class OnMythicmobsSpawnListener implements Listener {

    private final String CONFIG_EQUIPMENT = "SX-Equipment";

    private final SXAttribute plugin;

    public OnMythicmobsSpawnListener(SXAttribute plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onMythicMobSpawnEvent(MythicMobSpawnEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            MythicMob mm = event.getMobType();
            LivingEntity entity = (LivingEntity) event.getEntity();
            EntityEquipment eq;
            eq = entity.getEquipment();
            for (String str : mm.getConfig().getStringList(CONFIG_EQUIPMENT)) {
                // - sx 默认一:0 0.5
                if (str.contains(":")) {
                    String[] args = str.split(":");// 物品:物品位置穿戴位置 几率
                    if (args.length > 1) {
                        int position = 0;
                        //几率判断 args[1] = 0 0.05
                        if (args[1].contains(" ")) {
                            String[] argsSplit = args[1].split(" ");
                            if (argsSplit.length > 1 && SXAttribute.getRandom().nextDouble() > Double.valueOf(argsSplit[1])) {
                                continue;
                            } else {
                                position = Integer.valueOf(argsSplit[0]);
                            }
                        } else {
                            position = Integer.valueOf(args[1]);
                        }

                        ItemStack item = plugin.getItemDataManager().getItem(args[0], null);
                        if (item != null) {
                            switch (position) {
                                case -1:
                                    eq.setItemInOffHand(item);
                                    break;
                                case 0:
                                    eq.setItemInMainHand(item);
                                    break;
                                case 1:
                                    eq.setBoots(item);
                                    break;
                                case 2:
                                    eq.setLeggings(item);
                                    break;
                                case 3:
                                    eq.setChestplate(item);
                                    break;
                                case 4:
                                    eq.setHelmet(item);
                                    break;
                                default:
                                    Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cMythicMobs - Equipment Error: §4" + mm.getDisplayName() + "§c - §4" + str);
                            }
                        } else {
                            Bukkit.getConsoleSender().sendMessage(Message.getMessagePrefix() + "§cMythicMobs - Equipment No Item: §4" + mm.getDisplayName() + "§c - §4" + args[0]);
                        }
                    }
                }
            }
        }
    }
}
