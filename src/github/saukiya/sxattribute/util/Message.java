package github.saukiya.sxattribute.util;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import lombok.Getter;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Message {
    MESSAGE_VERSION,
    PLAYER__NO_REGISTER_SLOTS,
    PLAYER__NO_LEVEL_USE,
    PLAYER__NO_ROLE,
    PLAYER__NO_USE_SLOT,
    PLAYER__OVERDUE_ITEM,
    PLAYER__EXP_ADDITION,
    PLAYER__NO_VAULT,
    PLAYER__NO_DURABILITY,
    PLAYER__SELL,
    PLAYER__BATTLE__FIRST_PERSON,
    PLAYER__BATTLE__CRIT,
    PLAYER__BATTLE__IGNITION,
    PLAYER__BATTLE__WITHER,
    PLAYER__BATTLE__POISON,
    PLAYER__BATTLE__POTION,
    PLAYER__BATTLE__SLOWNESS,
    PLAYER__BATTLE__LIGHTNING,
    PLAYER__BATTLE__REAL,
    PLAYER__BATTLE__TEARING,
    PLAYER__BATTLE__REFLECTION,
    PLAYER__BATTLE__BLOCK,
    PLAYER__BATTLE__DODGE,
    PLAYER__BATTLE__LIFE_STEAL,
    PLAYER__HOLOGRAPHIC__CRIT,
    PLAYER__HOLOGRAPHIC__IGNITION,
    PLAYER__HOLOGRAPHIC__WITHER,
    PLAYER__HOLOGRAPHIC__POISON,
    PLAYER__HOLOGRAPHIC__POTION,
    PLAYER__HOLOGRAPHIC__SLOWNESS,
    PLAYER__HOLOGRAPHIC__LIGHTNING,
    PLAYER__HOLOGRAPHIC__REAL,
    PLAYER__HOLOGRAPHIC__TEARING,
    PLAYER__HOLOGRAPHIC__REFLECTION,
    PLAYER__HOLOGRAPHIC__BLOCK,
    PLAYER__HOLOGRAPHIC__DODGE,
    PLAYER__HOLOGRAPHIC__LIFE_STEAL,
    PLAYER__HOLOGRAPHIC__DAMAGE,
    PLAYER__HOLOGRAPHIC__HEALTH,
    PLAYER__HOLOGRAPHIC__TAKE,
    INVENTORY__STATS__NAME,
    INVENTORY__STATS__HIDE_ON,
    INVENTORY__STATS__HIDE_OFF,
    INVENTORY__STATS__SKULL_NAME,
    INVENTORY__STATS__SKULL_LORE,
    INVENTORY__STATS__ATTACK,
    INVENTORY__STATS__ATTACK_LORE,
    INVENTORY__STATS__DEFENSE,
    INVENTORY__STATS__DEFENSE_LORE,
    INVENTORY__STATS__BASE,
    INVENTORY__STATS__BASE_LORE,
    INVENTORY__SELL__NAME,
    INVENTORY__SELL__SELL,
    INVENTORY__SELL__ENTER,
    INVENTORY__SELL__OUT,
    INVENTORY__SELL__NO_SELL,
    INVENTORY__SELL__LORE__DEFAULT,
    INVENTORY__SELL__LORE__FORMAT,
    INVENTORY__SELL__LORE__NO_SELL,
    INVENTORY__SELL__LORE__ALL_SELL,
    INVENTORY__REPAIR__NAME,
    INVENTORY__REPAIR__GUIDE,
    INVENTORY__REPAIR__ENTER,
    INVENTORY__REPAIR__MONEY,
    INVENTORY__REPAIR__NO_MONEY,
    INVENTORY__REPAIR__UNSUITED,
    INVENTORY__REPAIR__REPAIR,
    INVENTORY__REPAIR__LORE__ENTER,
    INVENTORY__REPAIR__LORE__MONEY,
    INVENTORY__DISPLAY_SLOTS_NAME,
    ADMIN__CLEAR_ENTITY_DATA,
    ADMIN__NO_ITEM,
    ADMIN__HAS_ITEM,
    ADMIN__GIVE_ITEM,
    ADMIN__SAVE_ITEM,
    ADMIN__SAVE_ITEM_ERROR,
    ADMIN__NO_PERMISSION_CMD,
    ADMIN__NO_CMD,
    ADMIN__NO_FORMAT,
    ADMIN__NO_CONSOLE,
    ADMIN__PLUGIN_RELOAD,
    ADMIN__NO_ONLINE,
    COMMAND__STATS,
    COMMAND__SELL,
    COMMAND__REPAIR,
    COMMAND__GIVE,
    COMMAND__SAVE,
    COMMAND__NBT,
    COMMAND__DISPLAYSLOT,
    COMMAND__ATTRIBUTELIST,
    COMMAND__CONDITIONLIST,
    COMMAND__RELOAD,
    REPLACE_LIST;


    private static final File FILE = new File(SXAttribute.getPluginFile(), "Message.yml");

    @Getter
    private static final String messagePrefix = "[" + SXAttribute.getPluginName() + "] ";

    @Getter
    private static YamlConfiguration messages;

    private static void createDefaultMessage() {
        messages.set(MESSAGE_VERSION.toString(), SXAttribute.getPluginVersion());

        messages.set(PLAYER__NO_REGISTER_SLOTS.toString(), getMessagePrefix() + "&c服务器没有开启额外的槽位识别");
        messages.set(PLAYER__NO_LEVEL_USE.toString(), getMessagePrefix() + "&c你没有达到使用 &a{0} &c的等级要求!");
        messages.set(PLAYER__NO_ROLE.toString(), getMessagePrefix() + "&c你没有达到使用 &a{0} &c的职业要求!");
        messages.set(PLAYER__NO_USE_SLOT.toString(), getMessagePrefix() + "&7物品 &a{0} &7属于 &a{1}&7 类型!");
        messages.set(PLAYER__OVERDUE_ITEM.toString(), getMessagePrefix() + "&c物品 &a{0}&c 已经过期了:&a{1}");
        messages.set(PLAYER__EXP_ADDITION.toString(), getMessagePrefix() + "&7你的经验增加了 &6{0}&7! [&a+{1}%&7]");
        messages.set(PLAYER__NO_VAULT.toString(), getMessagePrefix() + "&c服务器没有启用经济系统: Vault-Economy null");
        messages.set(PLAYER__NO_DURABILITY.toString(), getMessagePrefix() + "&c物品 &a{0}&c 耐久度已经为零了!");
        messages.set(PLAYER__SELL.toString(), getMessagePrefix() + "&7出售成功! 一共出售了 &6{0}&7 个物品，总价 &6{1}&7 金币!");

        messages.set(INVENTORY__STATS__NAME.toString(), "&d&l&oSX-Attribute");
        messages.set(INVENTORY__STATS__HIDE_ON.toString(), "&a点击显示更多属性");
        messages.set(INVENTORY__STATS__HIDE_OFF.toString(), "&c点击隐藏更多属性");
        messages.set(INVENTORY__STATS__SKULL_NAME.toString(), "&6&l&o{0} 的属性");
        messages.set(INVENTORY__STATS__SKULL_LORE.toString(), Collections.singletonList("&d战斗力:&b %sx_CombatPower%"));
        messages.set(INVENTORY__STATS__ATTACK.toString(), "&a&l&o攻击属性");
        messages.set(INVENTORY__STATS__ATTACK_LORE.toString(), Arrays.asList(
                "&c攻击力:&b %sx_Damage%",
                "&cPVP攻击力:&b %sx_PvpDamage%",
                "&cPVE攻击力:&b %sx_PveDamage%",
                "&a命中几率:&b %sx_HitRate%%",
                "&6破甲几率:&b %sx_Real%%",
                "&c暴击几率:&b %sx_CritRate%%",
                "&4暴伤增幅:&b %sx_Crit%%",
                "&6吸血几率:&b %sx_LifeStealRate%%",
                "&6吸血倍率:&b %sx_LifeSteal%%",
                "&c点燃几率:&b %sx_Ignition%%",
                "&9凋零几率:&b %sx_Wither%%",
                "&d中毒几率:&b %sx_Poison%%",
                "&7致盲几率:&b %sx_Blindness%%",
                "&3减速几率:&b %sx_Slowness%%",
                "&e雷霆几率:&b %sx_Lightning%%",
                "&c撕裂几率:&b %sx_Tearing%%"
        ));
        messages.set(INVENTORY__STATS__DEFENSE.toString(), "&9&l&o防御属性");
        messages.set(INVENTORY__STATS__DEFENSE_LORE.toString(), Arrays.asList(
                "&6防御力:&b %sx_Defense%",
                "&6PVP防御力:&b %sx_PvpDefense%",
                "&6PVE防御力:&b %sx_PveDefense%",
                "&a生命上限:&b %sx_Health%/%sx_MaxHealth%",
                "&a生命恢复:&b %sx_HealthRegen%",
                "&d闪避几率:&b %sx_Dodge%%",
                "&9韧性:&b %sx_Toughness%%",
                "&c反射几率:&b %sx_ReflectionRate%%",
                "&c反射伤害:&b %sx_Reflection%%",
                "&2格挡几率:&b %sx_BlockRate%%",
                "&2格挡伤害:&b %sx_Block%%"
        ));
        messages.set(INVENTORY__STATS__BASE.toString(), "&9&l&o其他属性");
        messages.set(INVENTORY__STATS__BASE_LORE.toString(), Arrays.asList(
                "&e经验加成:&b %sx_ExpAddition%%",
                "&b移速增幅:&b %sx_WalkSpeed%%",
                "&b攻速增幅:&b %sx_AttackSpeed%%"
        ));
        messages.set(INVENTORY__SELL__NAME.toString(), "&6&l出售物品");
        messages.set(INVENTORY__SELL__SELL.toString(), "&e&l点击出售");
        messages.set(INVENTORY__SELL__ENTER.toString(), "&c&l确认出售");
        messages.set(INVENTORY__SELL__OUT.toString(), "&6出售完毕:&e {0} 金币");
        messages.set(INVENTORY__SELL__NO_SELL.toString(), "&c&l不可出售");
        messages.set(INVENTORY__SELL__LORE__DEFAULT.toString(), Collections.singletonList("&7&o请放入你要出售的物品"));
        messages.set(INVENTORY__SELL__LORE__FORMAT.toString(), "&b[{0}] &a{1}&7 - &7{2}&e 金币");
        messages.set(INVENTORY__SELL__LORE__NO_SELL.toString(), "&b[{0}] &4不可出售");
        messages.set(INVENTORY__SELL__LORE__ALL_SELL.toString(), "&e总金额: {0}");

        messages.set(INVENTORY__REPAIR__NAME.toString(), "&9&l修理物品");
        messages.set(INVENTORY__REPAIR__GUIDE.toString(), "&7&o待修理物品放入凹槽");
        messages.set(INVENTORY__REPAIR__ENTER.toString(), "&e&l点击修理");
        messages.set(INVENTORY__REPAIR__MONEY.toString(), "&c&l确认修理");
        messages.set(INVENTORY__REPAIR__NO_MONEY.toString(), "&c&l金额不足");
        messages.set(INVENTORY__REPAIR__UNSUITED.toString(), "&4&l不可修理");
        messages.set(INVENTORY__REPAIR__REPAIR.toString(), "&6修理成功:&e {0} 金币");
        messages.set(INVENTORY__REPAIR__LORE__ENTER.toString(), Collections.singletonList("&7&o价格: {0}/破损值"));
        messages.set(INVENTORY__REPAIR__LORE__MONEY.toString(), Arrays.asList("&c破损值: {0} 耐久", "&e价格: {1} 金币", "&7&o价格: {2}/破损值"));

        messages.set(INVENTORY__DISPLAY_SLOTS_NAME.toString(), "&9&l槽位展示");

        messages.set(PLAYER__BATTLE__FIRST_PERSON.toString(), "你");
        messages.set(PLAYER__BATTLE__CRIT.toString(), "[ACTIONBAR]&c{0}&6 对 &c{1}&6 造成了暴击!");
        messages.set(PLAYER__BATTLE__IGNITION.toString(), "[ACTIONBAR]&c{0}&6 被 &c{1}&6 点燃了!");
        messages.set(PLAYER__BATTLE__WITHER.toString(), "[ACTIONBAR]&c{0}&6 被 &c{1}&6 凋零了!");
        messages.set(PLAYER__BATTLE__POISON.toString(), "[ACTIONBAR]&c{0}&6 被 &c{1}&6 中毒了!");
        messages.set(PLAYER__BATTLE__POTION.toString(), "[ACTIONBAR]&c{0}&6 对 &c{1}&6 造成了§r{2}&6效果!");
        messages.set(PLAYER__BATTLE__SLOWNESS.toString(), "[ACTIONBAR]&c{0}&6 被 &c{1}&6 减速了!");
        messages.set(PLAYER__BATTLE__LIGHTNING.toString(), "[ACTIONBAR]&c{0}&6 被 &c{1}&6 用雷电击中了!");
        messages.set(PLAYER__BATTLE__REAL.toString(), "[ACTIONBAR]&c{0}&6 被 &c{1}&6 破甲了!");
        messages.set(PLAYER__BATTLE__TEARING.toString(), "[ACTIONBAR]&c{0}&6 被 &c{1}&6 撕裂了!");
        messages.set(PLAYER__BATTLE__REFLECTION.toString(), "[ACTIONBAR]&c{0}&6 被 &c{1}&6 反弹伤害了!");
        messages.set(PLAYER__BATTLE__BLOCK.toString(), "[ACTIONBAR]&c{0}&6 格挡了 &c{1}&6 的部分伤害!");
        messages.set(PLAYER__BATTLE__DODGE.toString(), "[ACTIONBAR]&c{0}&6 躲开了 &c{1}&6 的攻击!");
        messages.set(PLAYER__BATTLE__LIFE_STEAL.toString(), "[ACTIONBAR]&c{0}&6 被 &c{1}&6 偷取生命了!");

        messages.set(PLAYER__HOLOGRAPHIC__CRIT.toString(), "&a&o暴击: &b&o+{0}");
        messages.set(PLAYER__HOLOGRAPHIC__IGNITION.toString(), "&c&o点燃: &b&o{0}s");
        messages.set(PLAYER__HOLOGRAPHIC__WITHER.toString(), "&7&o凋零: &b&o{0}s");
        messages.set(PLAYER__HOLOGRAPHIC__POISON.toString(), "&5&o中毒: &b&o{0}s");
        messages.set(PLAYER__HOLOGRAPHIC__POTION.toString(), "&o{0}: &b&o{1}s");
        messages.set(PLAYER__HOLOGRAPHIC__SLOWNESS.toString(), "&b&o减速: &b&o{0}s");
        messages.set(PLAYER__HOLOGRAPHIC__LIGHTNING.toString(), "&e&o雷霆: &b&o{0}");
        messages.set(PLAYER__HOLOGRAPHIC__REAL.toString(), "&c&o破甲");
        messages.set(PLAYER__HOLOGRAPHIC__TEARING.toString(), "&c&o撕裂: &b{0}");
        messages.set(PLAYER__HOLOGRAPHIC__REFLECTION.toString(), "&6&o反伤: &b&o{0}");
        messages.set(PLAYER__HOLOGRAPHIC__BLOCK.toString(), "&2&o格挡: &b&o{0}");
        messages.set(PLAYER__HOLOGRAPHIC__DODGE.toString(), "&a&o闪避");
        messages.set(PLAYER__HOLOGRAPHIC__LIFE_STEAL.toString(), "&c&o吸取: &b&o{0}");
        messages.set(PLAYER__HOLOGRAPHIC__DAMAGE.toString(), "&c&o伤害: &b&o{0}");
        messages.set(PLAYER__HOLOGRAPHIC__TAKE.toString(), "&c&o- {0}");
        messages.set(PLAYER__HOLOGRAPHIC__HEALTH.toString(), "&e&o+ {0}");

        messages.set(ADMIN__CLEAR_ENTITY_DATA.toString(), getMessagePrefix() + "&c清理了 &6{0}&c 个多余的生物属性数据!");
        messages.set(ADMIN__NO_ITEM.toString(), getMessagePrefix() + "&c物品不存在!");
        messages.set(ADMIN__HAS_ITEM.toString(), getMessagePrefix() + "&c已经存在名字为  &6{0}&c的物品!");
        messages.set(ADMIN__GIVE_ITEM.toString(), getMessagePrefix() + "&c给予 &6{0} &a{1}&c个 &6{2}&c 物品!");
        messages.set(ADMIN__SAVE_ITEM.toString(), getMessagePrefix() + "&a物品 &6{0} &a成功保存! 编号为: &6{1}&a!");
        messages.set(ADMIN__SAVE_ITEM_ERROR.toString(), getMessagePrefix() + "&c物品 &4{0} &c保存出现不可预知的错误 [&4{1}&c]");
        messages.set(ADMIN__NO_PERMISSION_CMD.toString(), getMessagePrefix() + "&c你没有权限执行此指令");
        messages.set(ADMIN__NO_CMD.toString(), getMessagePrefix() + "&c未找到此子指令:{0}");
        messages.set(ADMIN__NO_FORMAT.toString(), getMessagePrefix() + "&c格式错误!");
        messages.set(ADMIN__NO_ONLINE.toString(), getMessagePrefix() + "&c玩家不在线或玩家不存在!");
        messages.set(ADMIN__NO_CONSOLE.toString(), getMessagePrefix() + "&c控制台不允许执行此指令!");
        messages.set(ADMIN__PLUGIN_RELOAD.toString(), getMessagePrefix() + "§c插件已重载");

        messages.set(COMMAND__STATS.toString(), "查看属性");
        messages.set(COMMAND__SELL.toString(), "打开出售界面");
        messages.set(COMMAND__REPAIR.toString(), "打开修理界面");
        messages.set(COMMAND__GIVE.toString(), "给予玩家RPG物品");
        messages.set(COMMAND__SAVE.toString(), "保存当前的物品到配置文件 加[-a]完全保存");
        messages.set(COMMAND__NBT.toString(), "查看当前手持物品的NBT数据");
        messages.set(COMMAND__DISPLAYSLOT.toString(), "显示可装载物品的槽位");
        messages.set(COMMAND__ATTRIBUTELIST.toString(), "查看当前属性列表");
        messages.set(COMMAND__CONDITIONLIST.toString(), "查看当前条件列表");
        messages.set(COMMAND__RELOAD.toString(), "重新加载这个插件的配置");

        messages.set(REPLACE_LIST.toString() + ".Pig", "猪猪");
        messages.set(REPLACE_LIST.toString() + ".Sheep", "羊羊");
        messages.set(REPLACE_LIST.toString() + ".Rabbit", "兔兔");
        messages.set(REPLACE_LIST.toString() + ".Mule", "骡骡");
        messages.set(REPLACE_LIST.toString() + ".Skeleton", "骷髅");
        messages.set(REPLACE_LIST.toString() + ".Zombie", "僵尸");
        messages.set(REPLACE_LIST.toString() + ".Silverfish", "蠢虫");
        messages.set(REPLACE_LIST.toString() + ".Horse", "马马");
        messages.set(REPLACE_LIST.toString() + ".Cow", "牛牛");
        messages.set(REPLACE_LIST.toString() + ".Chicken", "鸡鸡");
        messages.set(REPLACE_LIST.toString() + ".Polar Bear", "熊熊");
    }

    /**
     * 检查版本更新
     *
     * @return boolean
     * @throws IOException IOException
     */
    private static boolean detectionVersion() throws IOException {
        if (!messages.getString(Message.MESSAGE_VERSION.toString(), "").equals(SXAttribute.getPluginVersion())) {
            messages.save(new File(FILE.toString().replace(".yml", "_" + messages.getString(Message.MESSAGE_VERSION.toString()) + ".yml")));
            messages = new YamlConfiguration();
            createDefaultMessage();
            return true;
        }
        return false;
    }

    /**
     * 加载Message类
     *
     * @throws IOException                   IOException
     * @throws InvalidConfigurationException InvalidConfigurationException
     */
    public static void loadMessage() throws IOException, InvalidConfigurationException {
        messages = new YamlConfiguration();
        if (!FILE.exists()) {
            Bukkit.getConsoleSender().sendMessage(messagePrefix + "§cCreate Message.yml");
            createDefaultMessage();
            messages.save(FILE);
        } else {
            messages.load(FILE);
            if (detectionVersion()) {
                Bukkit.getConsoleSender().sendMessage(messagePrefix + "§eUpdate Message.yml");
                messages.save(FILE);
            } else {
                Bukkit.getConsoleSender().sendMessage(messagePrefix + "Find Message.yml");
            }
        }
        SubAttribute.setFirstPerson(Message.getMsg(Message.PLAYER__BATTLE__FIRST_PERSON));
    }

    /**
     * 替换名字
     *
     * @param str String
     * @return String
     */
    public static String replaceName(String str) {
        if (str != null && messages.contains(REPLACE_LIST.toString())) {
            for (String replaceName : messages.getConfigurationSection(REPLACE_LIST.toString()).getKeys(false)) {
                if (str.equals(replaceName)) {
                    return ChatColor.translateAlternateColorCodes('&', messages.getString(REPLACE_LIST.toString() + "." + replaceName));
                }
            }
        }
        return str;
    }

    /**
     * 获取String
     *
     * @param loc  Message
     * @param args Object...
     * @return String
     */
    public static String getMsg(Message loc, Object... args) {
        return ChatColor.translateAlternateColorCodes('&', MessageFormat.format(messages.getString(loc.toString(), "Null Message: " + loc), args));
    }

    /**
     * 获取List
     *
     * @param loc  Message
     * @param args Object...
     * @return List
     */
    public static List<String> getStringList(Message loc, Object... args) {
        List<String> list = messages.getStringList(loc.toString());
        if (list.size() == 0) return Collections.singletonList("Null Message: " + loc);
        int bound = list.size();
        for (int i = 0; i < bound; i++) {
            list.set(i, ChatColor.translateAlternateColorCodes('&', MessageFormat.format(list.get(i), args)));
        }
        return list;
    }

    /**
     * 转换消息为TextComponent
     *
     * @param message    String
     * @param command    String
     * @param stringList List
     * @return TextComponent
     */
    public static TextComponent getTextComponent(String message, String command, List<String> stringList) {
        TextComponent tcMessage = new TextComponent(message);
        if (stringList != null && stringList.size() > 0) {
            ComponentBuilder bc = new ComponentBuilder("§7" + stringList.get(0).replace("&", "§"));
            int bound = stringList.size();
            for (int i = 1; i < bound; i++) {
                String s = "\n§7" + stringList.get(i).replace("&", "§");
                bc.append(s);
            }
            tcMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, bc.create()));
        }
        if (command != null) {
            tcMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        }
        return tcMessage;
    }

    /**
     * 发送消息给玩家
     *
     * @param entity LivingEntity
     * @param loc    Message
     * @param args   Object...
     */
    public static void send(LivingEntity entity, Message loc, Object... args) {
        if (entity instanceof Player) {
            send((Player) entity, Message.getMsg(loc, args));
        }
    }
    
    /**
     * 发送消息给玩家
     *
     * @param player  Player
     * @param message String
     */
    public static void send(Player player, String message) {
        if (message.contains("Null Message")) return;
        if (message.contains("[ACTIONBAR]")) {
            message = message.replace("[ACTIONBAR]", "");
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        } else if (message.contains("[TITLE]")) {
            message = message.replace("[TITLE]", "");
            if (message.contains(":")) {
                String title = message.split(":")[0];
                String subTitle = message.split(":")[1];
                player.sendTitle(title, subTitle, 5, 20, 5);
            } else {
                player.sendTitle(message, null, 5, 20, 5);
            }
        } else {
            player.sendMessage(message);
        }
    }

    @Override
    public String toString() {
        return name().replace("__", ".");
    }
}
