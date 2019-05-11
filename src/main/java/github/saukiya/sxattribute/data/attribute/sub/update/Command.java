package github.saukiya.sxattribute.data.attribute.sub.update;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.UpdateData;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Saukiya
 */
public class Command extends SubAttribute implements Listener {

    private CommandRunnable[] commandRunnables;

    private CommandSender sxSender = Bukkit.getConsoleSender();
//    private CommandSender sxSender = new SXSender();

    public Command(JavaPlugin plugin) {
        super(plugin, 0, SXAttributeType.UPDATE);
    }

    @Override
    public Listener getListener() {
        return this;
    }

    @EventHandler
    void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        for (CommandRunnable runnable : commandRunnables) {
            if (runnable.players.contains(player.getName())) {
                runnable.disable(player);
            }
        }
    }

    @Override
    protected YamlConfiguration defaultConfig(YamlConfiguration config) {
        config.set("List.Tick1.DiscernName", "驾驭飞行");
        config.set("List.Tick1.Enabled", Arrays.asList("fly %player% on"));
        config.set("List.Tick1.Disable", Arrays.asList("fly %player% off"));
        config.set("List.Tick1.Continued", Arrays.asList("particle smoke %player_x% %player_y% %player_z% 0.5 0.5 0.5 0 20 normal %player%"));
        config.set("List.Tick2.DiscernName", "速度 II");
        config.set("List.Tick2.Enabled", Arrays.asList("effect %player% minecraft:speed 5 1"));
        config.set("List.Tick2.Disable", Arrays.asList("effect %player% minecraft:speed 0 0"));
        config.set("List.Tick2.Continued", Arrays.asList("delay 40", "effect %player% minecraft:speed 5 1"));
        config.set("List.Tick2.CombatPower", 20);
        return config;
    }

    @Override
    public void onEnable() {
        super.onEnable();
        List<CommandRunnable> list = new ArrayList<>();
        for (String key : config().getConfigurationSection("List").getKeys(false)) {
            list.add(new CommandRunnable(key));
        }
        commandRunnables = list.toArray(new CommandRunnable[0]);
        setLength(commandRunnables.length);
    }

    @Override
    public void onReLoad() {
        super.onReLoad();
        for (CommandRunnable commandRunnable : commandRunnables) {
            commandRunnable.load();
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @Override
    public void eventMethod(double[] values, EventData eventData) {
        if (eventData instanceof UpdateData && ((UpdateData) eventData).getEntity() instanceof Player) {
            Player player = (Player) ((UpdateData) eventData).getEntity();
            for (int i = 0; i < commandRunnables.length; i++) {
                CommandRunnable runnable = commandRunnables[i];
                if (runnable.players.contains(player.getName())) {
                    if (values[i] == 0) {
                        runnable.disable(player);
                    }
                } else {
                    if (values[i] > 0) {
                        runnable.enabled(player);
                    }
                }
            }
        }
    }

    @Override
    public Object getPlaceholder(double[] values, Player player, String string) {
        for (int i = 0; i < commandRunnables.length; i++) {
            if (string.equals(commandRunnables[i].name)) {
                return values[i];
            }
        }
        return null;
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.stream(commandRunnables).map(runnable -> runnable.name).collect(Collectors.toList());
    }

    @Override
    public void loadAttribute(double[] values, String lore) {
        for (int i = 0; i < commandRunnables.length; i++) {
            if (lore.contains(commandRunnables[i].discernName)) {
                values[i] += 1;
            }
        }
    }

    @Override
    public double calculationCombatPower(double[] values) {
        int value = 0;
        for (int i = 0; i < commandRunnables.length; i++) {
            if (values[i] > 0) {
                value += commandRunnables[i].combatPower;
            }
        }
        return value;
    }

    public class CommandRunnable {

        String name;

        String discernName;

        List<String> enabledCommands;

        List<String> disableCommands;

        List<String> continuedCommands;

        // TODO 预设
//        List<String> disableWorldList;

        int combatPower;

        List<String> players = new ArrayList<>();

        int i = 0;

        public CommandRunnable(String name) {
            this.name = name;
            load();
            run();
        }

        public void load() {
            this.discernName = getString("List." + name + ".DiscernName");
            this.enabledCommands = config().getStringList("List." + name + ".Enabled");
            this.disableCommands = config().getStringList("List." + name + ".Disable");
            this.continuedCommands = config().getStringList("List." + name + ".Continued");
//            this.disableWorldList = config().getStringList("List." + name + ".DisableWorldList");
            this.combatPower = config().getInt("List." + name + ".CombatPower");
        }

        public void enabled(Player player) {
            players.add(player.getName());
            runCommand(player, enabledCommands);
        }

        public void disable(Player player) {
            players.remove(player.getName());
            runCommand(player, disableCommands);
        }

        public void runCommand(Player player, List<String> commands) {
            int delay = 0;
            for (String command : commands) {
                if (command.startsWith("delay")) {
                    delay = Integer.parseInt(command.substring(5));
                } else {
                    Bukkit.getScheduler().runTaskLater(getPlugin(), () ->Bukkit.dispatchCommand(sxSender, (SXAttribute.isPlaceholder() ? PlaceholderAPI.setPlaceholders(player, command) : command).replace("%player%", player.getName())), delay);
                }
            }
        }

        public void run() {
            int delay = 0;
            for (String command : continuedCommands) {
                if (command.startsWith("delay ")) {
                    delay = Integer.parseInt(command.substring(6));
                } else {
                    Bukkit.getScheduler().runTaskLater(getPlugin(), () ->{
                        for (String playerName : players) {
                            Player player = Bukkit.getPlayerExact(playerName);
                            if (player != null) {
                                Bukkit.dispatchCommand(sxSender, (SXAttribute.isPlaceholder() ? PlaceholderAPI.setPlaceholders(player, command) : command).replace("%player%", player.getName()));
                            }
                        }
                    }, delay);
                }
            }
            Bukkit.getScheduler().runTaskLater(getPlugin(), this::run, delay == 0 ? 20 : delay);
        }
    }

    public class SXSender implements CommandSender {

        private final PermissibleBase perm = new PermissibleBase(this);
        private final Spigot spigot = new Spigot() {
            public void sendMessage(BaseComponent component) {
                Bukkit.getConsoleSender().sendMessage(TextComponent.toLegacyText(component));
            }

            public void sendMessage(BaseComponent... components) {
                Bukkit.getConsoleSender().sendMessage(TextComponent.toLegacyText(components));
            }
        };

        @Override
        public void sendMessage(String s) {
            System.out.println(ChatColor.stripColor(s));
        }

        @Override
        public void sendMessage(String[] strings) {
            for (String string : strings) {
                sendMessage(string);
            }
        }

        @Override
        public Server getServer() {
            return Bukkit.getServer();
        }

        @Override
        public String getName() {
            return "SXSender";
        }

        @Override
        public Spigot spigot() {
            return new Spigot();
        }

        @Override
        public boolean isPermissionSet(String name) {
            return this.perm.isPermissionSet(name);
        }

        @Override
        public boolean isPermissionSet(Permission perm) {
            return this.perm.isPermissionSet(perm);
        }

        @Override
        public boolean hasPermission(String name) {
            return this.perm.hasPermission(name);
        }

        @Override
        public boolean hasPermission(Permission perm) {
            return this.perm.hasPermission(perm);
        }

        @Override
        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
            return this.perm.addAttachment(plugin, name, value);
        }

        @Override
        public PermissionAttachment addAttachment(Plugin plugin) {
            return this.perm.addAttachment(plugin);
        }


        @Override
        public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
            return this.perm.addAttachment(plugin, name, value, ticks);
        }

        @Override
        public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
            return this.perm.addAttachment(plugin, ticks);
        }

        @Override
        public void removeAttachment(PermissionAttachment attachment) {
            this.perm.removeAttachment(attachment);
        }

        @Override
        public void recalculatePermissions() {
            this.perm.recalculatePermissions();
        }

        @Override
        public Set<PermissionAttachmentInfo> getEffectivePermissions() {
            return this.perm.getEffectivePermissions();
        }

        @Override
        public boolean isOp() {
            return true;
        }

        @Override
        public void setOp(boolean b) {
            throw new UnsupportedOperationException("Cannot change operator status of server console");
        }
    }

}
