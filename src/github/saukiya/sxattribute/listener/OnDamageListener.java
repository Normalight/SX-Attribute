package github.saukiya.sxattribute.listener;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.api.Sx;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.attribute.SXAttributeManager;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.attribute.sub.attack.Damage;
import github.saukiya.sxattribute.data.condition.SubCondition;
import github.saukiya.sxattribute.data.eventdata.sub.DamageData;
import github.saukiya.sxattribute.event.SXDamageEvent;
import github.saukiya.sxattribute.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * @author Saukiya
 */

public class OnDamageListener implements Listener {

    private final SXAttribute plugin;

    public OnDamageListener(SXAttribute plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onProjectileHitEvent(EntityShootBowEvent event) {
        if (event.isCancelled()) return;
        Entity projectile = event.getProjectile();
        LivingEntity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            Sx.setProjectileData(projectile.getUniqueId(), plugin.getAttributeManager().getEntityData(entity));
            ItemStack item = event.getBow();
            if (item != null && SubCondition.getUnbreakable(item.getItemMeta())) {
                Bukkit.getPluginManager().callEvent(new PlayerItemDamageEvent((Player) entity, item, 1));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.isCancelled() || Config.getDamageEventBlackList().contains(event.getCause().name())) {
            return;
        }
        LivingEntity defenseEntity = (event.getEntity() instanceof LivingEntity && !(event.getEntity() instanceof ArmorStand)) ? (LivingEntity) event.getEntity() : null;
        LivingEntity attackEntity = null;
        SXAttributeData defenseData;
        SXAttributeData attackData = null;
        // 当攻击者为投抛物时
        if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof LivingEntity) {
            attackEntity = (LivingEntity) ((Projectile) event.getDamager()).getShooter();
            attackData = Sx.getProjectileData(event.getDamager().getUniqueId(), true);
        } else if (event.getDamager() instanceof LivingEntity) {
            attackEntity = (LivingEntity) event.getDamager();
        }

        // 若有一方为null 或 怪v怪的属性计算 则取消
        if (defenseEntity == null || attackEntity == null || (!Config.isDamageCalculationToEVE() && !(defenseEntity instanceof Player || attackEntity instanceof Player))) {
            return;
        }

        defenseData = plugin.getAttributeManager().getEntityData(defenseEntity);
        attackData = attackData != null ? attackData : plugin.getAttributeManager().getEntityData(attackEntity);

        if (event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
            EntityEquipment eq = attackEntity.getEquipment();
            ItemStack mainHand = eq.getItemInMainHand();
            if (mainHand != null) {
                // 主手持弓左键判断
                if (Material.BOW.equals(mainHand.getType()) && !Config.isBowCloseRangeAttack()) {
                    SXAttributeData sxAttributeData = plugin.getAttributeManager().getMainHandData(attackEntity);
                    event.setDamage(event.getDamage() - (attackEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() / event.getDamage() * sxAttributeData.getValues(Damage.class.getSimpleName())[0]));
                    attackData = plugin.getAttributeManager().getEntityData(attackEntity).take(sxAttributeData);
                }
                if (!Material.AIR.equals(mainHand.getType()) && mainHand.getItemMeta().hasLore()) {
                    if (attackEntity instanceof Player && !((HumanEntity) attackEntity).getGameMode().equals(GameMode.CREATIVE)) {
                        if (mainHand.getType().getMaxDurability() == 0 || SubCondition.getUnbreakable(mainHand.getItemMeta())) {
                            Bukkit.getPluginManager().callEvent(new PlayerItemDamageEvent((Player) attackEntity, mainHand, 1));
                        }
                    }
                }
            }
        }

        String entityName = plugin.getOnHealthChangeListener().getEntityName(defenseEntity);
        String damagerName = plugin.getOnHealthChangeListener().getEntityName(attackEntity);

        DamageData damageData = new DamageData(defenseEntity, attackEntity, entityName, damagerName, defenseData, attackData, event);

        for (SubAttribute attribute : SXAttributeManager.getSubAttributes()) {
            if (attribute.containsType(SXAttributeType.ATTACK) && attackData.isValid(attribute)) {
                attribute.eventMethod(attackData.getValues(attribute), damageData);
            } else if (attribute.containsType(SXAttributeType.DEFENCE) && defenseData.isValid(attribute)) {
                attribute.eventMethod(defenseData.getValues(attribute), damageData);
            }

            if (damageData.isCancelled() || damageData.getDamage() <= Config.getMinimumDamage()) {
                damageData.setDamage(Config.getMinimumDamage());
                break;
            }
        }
        Bukkit.getPluginManager().callEvent(new SXDamageEvent(damageData));
    }
}
