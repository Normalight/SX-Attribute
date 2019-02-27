package github.saukiya.sxattribute.data.attribute.sub.damage;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Saukiya
 */
public class EventMessage extends SubAttribute/** implements Listener **/
{

    @Getter
    private List<MessageData> messageDataList = new ArrayList<>();

//    @Getter
//    private List<MessageMetaData> messageList = new ArrayList<>();

    public EventMessage(JavaPlugin plugin) {
        super(plugin, 0, SXAttributeType.DAMAGE);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        if (SXAttribute.isHolographic()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = messageDataList.size() - 1; i >= 0; i--) {
                        if (messageDataList.get(i).clearTime < System.currentTimeMillis()) {
                            messageDataList.remove(i).getHologram().delete();
                        }
                    }
                }
            }.runTaskTimer(getPlugin(), 20, 20);
        }
//        Bukkit.getPluginManager().registerEvents(this,getPlugin());

//        new BukkitRunnable(){
//            @Override
//            public void run() {
//                for (int i = messageList.size() - 1; i >= 0; i--) {
//                    if (messageList.get(i).clearTime < System.currentTimeMillis()) {
//                        messageList.get(i).invalidate();
//                    }
//                }
//            }
//        }.runTaskTimer(getPlugin(), 20, 20);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (SXAttribute.isHolographic()) {
            for (MessageData data : getMessageDataList()) {
                data.getHologram().delete();
            }
        }
//        for (MessageMetaData data : messageList) {
//            data.invalidate();
//        }
    }
//
//    @EventHandler
//    void onItemMergeEvent(ItemMergeEvent event) {
//        if (event.isCancelled()) return;
//        if (event.getTarget().hasMetadata(SXAttribute.getPluginName() + getKey()) || event.getDefender().hasMetadata(SXAttribute.getPluginName() + getKey())) {
//            event.setCancelled(true);
//        }
//    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof DamageData) {
            DamageData damageData = (DamageData) eventData;
            EntityDamageByEntityEvent event = damageData.getEvent();

            if (!event.isCancelled()) {
                damageData.sendHolo(Message.getMsg(damageData.isCrit() && damageData.getDamage() > 0 ? Message.PLAYER__HOLOGRAPHIC__CRIT : Message.PLAYER__HOLOGRAPHIC__DAMAGE, SXAttribute.getDf().format(event.getFinalDamage())));
            }

            if (Config.isHolographic() && SXAttribute.isHolographic() && !Config.getHolographicBlackList().contains(event.getCause().name())) {
                Location loc = damageData.getDefender().getEyeLocation().clone().add(0, 0.6 - SXAttribute.getRandom().nextDouble() / 2, 0);
                loc.setYaw(damageData.getAttacker().getLocation().getYaw() + 90);
                loc.add(loc.getDirection().multiply(0.8D));
                Hologram hologram = HologramsAPI.createHologram(getPlugin(), loc);
                for (String message : damageData.getHoloList()) {
                    hologram.appendTextLine(message);
                }
                getMessageDataList().add(new MessageData(hologram));
            }

//            for (String message : damageData.getHoloList()) {
//                createMessage(loc, message);
//            }
        }
    }

//    private ArmorStand createMessage(Location loc, String message) {
//        ArmorStand armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
//        armorStand.setVisible(false); //可见
//        armorStand.setGravity(false); // 重力
//        armorStand.setSmall(true); //小
//        armorStand.setInvulnerable(true); //无敌
//        armorStand.setSilent(true); //静默
//        armorStand.setArms(false);
//        armorStand.setAI(false); // 作用??
//        armorStand.setBasePlate(false); //消失底盘
//        armorStand.setMarker(false); //碰撞箱子
//        armorStand.setRemoveWhenFarAway(true); //远离消失
//        armorStand.setMetadata(SXAttribute.getPluginName() + getKey(), new MessageMetaData(armorStand));
////        armorStand.setVelocity(loc.getDirection().multiply(0.5D));
//        armorStand.setVelocity(new Vector(0,0.5,0));
//        armorStand.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1D);
//        armorStand.setCustomName(message);
//        armorStand.setCustomNameVisible(true);
//        return armorStand;
//    }

    @Override
    public Object getPlaceholder(double[] values, Player player, String string) {
        return null;
    }

    @Override
    public List<String> getPlaceholders() {
        return null;
    }

    @Override
    public void loadAttribute(double[] values, String lore) {
    }

    @Override
    public double calculationCombatPower(double[] values) {
        return 0;
    }

    public class MessageData {

        @Getter
        private long clearTime = System.currentTimeMillis() + (Config.getConfig().getInt(Config.HOLOGRAPHIC_DISPLAY_TIME) * 1000);

        @Getter
        private Hologram hologram;

        MessageData(Hologram hologram) {
            this.hologram = hologram;
            EventMessage.this.messageDataList.add(this);
        }

    }

//    public class MessageMetaData extends MetadataValueAdapter {
//
//        @Getter
//        private long clearTime = System.currentTimeMillis() + (Config.getConfig().getInt(Config.HOLOGRAPHIC_DISPLAY_TIME) * 1000);
//
//        @Getter
//        private ArmorStand entity;
//
//        MessageMetaData(ArmorStand entity) {
//            super(EventMessage.this.getPlugin());
//            this.entity = entity;
//            EventMessage.this.messageList.add(this);
//        }
//
//        @Override
//        public Object value() {
//            return clearTime;
//        }
//
//        @Override
//        public void invalidate() {
//            System.out.println("invalidate: " + entity.getCustomName());
//            EventMessage.this.messageList.remove(this);
//            entity.remove();
//        }
//    }
}
