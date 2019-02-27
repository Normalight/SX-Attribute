package github.saukiya.sxattribute.data.itemdata.sub;

import github.saukiya.sxattribute.data.itemdata.ItemGenerator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @Author Saukiya
 * @Date 2019/2/27 17:48
 */
public class ImportItemData implements ItemGenerator {

    JavaPlugin plugin;

    String pathName;

    String key;

    ConfigurationSection config;

    ItemStack item;

    public ImportItemData(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private ImportItemData(String pathName, String key, ConfigurationSection config) {
        this.pathName = pathName;
        this.key = key;
        this.config = config;
        this.item = config.getItemStack("Item");
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public String getType() {
        return "Import";
    }

    @Override
    public ItemGenerator newGenerator(String pathName, String key, ConfigurationSection config) {
        return new ImportItemData(pathName, key, config);
    }

    @Override
    public String getPathName() {
        return pathName;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getName() {
        return item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name();
    }

    @Override
    public ConfigurationSection getConfig() {
        return config;
    }

    @Override
    public ItemStack getItem(Player player) {
        return item.clone();
    }

    @Override
    public ConfigurationSection saveItem(ItemStack saveItem, ConfigurationSection config) {
        config.set("Item", saveItem);
        return config;
    }
}
