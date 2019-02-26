package github.saukiya.sxattribute.util;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.attribute.SXAttributeManager;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import org.bukkit.entity.Player;

public class Placeholders extends EZPlaceholderHook {

    private final SXAttribute plugin;

    public Placeholders(SXAttribute plugin) {
        super(plugin, "sx");
        this.plugin = plugin;
        this.hook();
    }

    @Override
    public String onPlaceholderRequest(Player player, String string) {
        return Placeholders.onPlaceholderRequest(player, string, plugin.getAttributeManager().getEntityData(player));
    }

    public static String onPlaceholderRequest(Player player, String string, SXAttributeData attributeData) {
        if (string.equals("Money") && SXAttribute.isVault())
            return SXAttribute.getDf().format(MoneyUtil.get(player));
        if (string.equals("CombatPower")) return SXAttribute.getDf().format(attributeData.getCombatPower());
        for (SubAttribute attribute : SXAttributeManager.getSubAttributes()) {
            Object obj = attribute.getPlaceholder(attributeData.getValues(attribute), player, string);
            if (obj != null) {
                if (obj instanceof Double) {
                    return SXAttribute.getDf().format(obj);
                } else {
                    return obj.toString();
                }
            }
        }
        return "§cN/A - " + string;
    }

}
