package github.saukiya.sxattribute.command.sub;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.command.SenderType;
import github.saukiya.sxattribute.command.SubCommand;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.attribute.SXAttributeManager;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.util.Message;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 属性标签查询指令
 *
 * @author Saukiya
 */
public class AttributeListCommand extends SubCommand {

    public AttributeListCommand() {
        super("attributeList", " <search>", SenderType.ALL);
    }

    @Override
    public void onCommand(SXAttribute plugin, CommandSender sender, String[] args) {
        String search = args.length < 2 ? "" : args[1];
        int filterSize = 0;

        int size = SXAttributeManager.getSubAttributes().length;
        SXAttributeData attributeData = sender instanceof Player ? plugin.getAttributeManager().getEntityData((LivingEntity) sender) : null;
        for (int i = 0; i < size; i++) {
            SubAttribute attribute = SXAttributeManager.getSubAttributes()[i];
            String message = "§b" + attribute.getPriority() + " §7- §c" + attribute.getName() + " §8[§7Plugin: §c" + attribute.getPlugin().getName() + "§7,Length: §c" + attribute.getLength() + "§8]";
            if (message.contains(search)) {
                List<String> list = new ArrayList<>();
                list.add("&bName: " + attribute.getName());
                list.add("&bAttributeType: ");
                for (SXAttributeType type : attribute.getType()) {
                    list.add("&7- " + type.getType() + " &8(&7" + type.getName() + "&8)");
                }
                list.add("&bPlaceholders: ");
                if (attribute.getPlaceholders() != null) {
                    for (String placeName : attribute.getPlaceholders()) {
                        list.add("&7- %sx_" + placeName + "% : " + attribute.getPlaceholder(attributeData.getValues(attribute), (Player) sender, placeName));
                    }
                }
                list.add("&bValue: " + attribute.calculationCombatPower(attributeData.getValues(attribute)));
                TextComponent tc = Message.getTextComponent(message, null, list);
                if (attribute.getYaml() != null) {
                    tc.addExtra(" ");
                    tc.addExtra(Message.getTextComponent("§8[§cYaml§8]", null, Collections.singletonList(attribute.getYaml().saveToString())));
                }
                if (sender instanceof Player) {
                    ((Player) sender).spigot().sendMessage(tc);
                } else {
                    sender.sendMessage(tc.getText());
                }
            } else {
                filterSize++;
            }
        }
        if (search.length() > 0) {
            sender.sendMessage("§7> Filter§c " + filterSize + " §7Conditions");
        }
    }

    @Override
    public List<String> onTabComplete(SXAttribute plugin, CommandSender sender, String[] args) {
        if (args.length == 2) {
            List<String> list = new ArrayList<>();
            for (SubAttribute subAttribute : SXAttributeManager.getSubAttributes()) {
                String name = subAttribute.getName();
                list.add(name);
            }
            return list;
        }
        return null;
    }
}
